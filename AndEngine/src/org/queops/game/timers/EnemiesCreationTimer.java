package org.queops.game.timers;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.customTimer.Timer;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.math.MathUtils;
import org.queops.MainActivity;

public class EnemiesCreationTimer extends Timer {

	public EnemiesCreationTimer(float pInterval, final MainActivity main) {		
		super(pInterval, new ITimerCallback() {
			
			@Override
			public void onTick() {
				// Inimigos comecam no fim da tela
				final float centerX = MainActivity.CAMERA_WIDTH + 50;
				final float centerY = MainActivity.CAMERA_HEIGHT * MathUtils.RANDOM.nextFloat();
				ITextureRegion inimigo;
				switch (MathUtils.RANDOM.nextInt(2)) {
				case 0:
					inimigo = main.getEnemy1TextureRegion();
					break;
				case 1:
					inimigo = main.getEnemy2TextureRegion();
					break;
				case 2:
					inimigo = main.getEnemy3TextureRegion();
					break;
				default:
					inimigo = main.getEnemy1TextureRegion();
					break;
				}
				
				final Sprite newEnemy = new Sprite(centerX, centerY, inimigo, main.getVertexBufferObjectManager());				
				newEnemy.setZIndex(main.ENEMY_LAYER);
				newEnemy.setRotation(90);
				
				main.getEnemies().add(newEnemy);				

				main.getScene().registerTouchArea(newEnemy);
				main.getScene().attachChild(newEnemy);
				
				main.getScene().registerUpdateHandler(new IUpdateHandler() {
					@Override
					public void reset() {
					}

					@Override
					public void onUpdate(final float pSecondsElapsed) {
						
						//Movendo Inimigos
						if(newEnemy != null)
						newEnemy.setX(newEnemy.getX() - 5.5f);
						if (newEnemy != null && newEnemy.collidesWith(main.getHero()) || newEnemy.getX() < -20) {
							
							if (newEnemy != null && newEnemy.collidesWith(main.getHero()) && newEnemy.hasParent() )
							{
								if(main.getScore() > 0)
								{
									//score = score-1;
									//placarText.setText(score.toString());
									main.getLifeBarRectangle().setWidth(main.getLifeBarRectangle().getWidth() - 10);
									if(main.getLifeBarRectangle().getWidth() == 0)
									{
										main.getEngine().stop();
										main.createGameOverPanel();
									}
								}
							}
							newEnemy.detachSelf();
							main.getEnemies().remove(newEnemy);						
						}
					}
				});
			
			
			}
		});
		// TODO Auto-generated constructor stub
	}
	


}
