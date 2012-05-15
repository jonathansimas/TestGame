package org.queops.game.timers;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.extension.customTimer.Timer;
import org.andengine.util.color.Color;
import org.queops.MainActivity;

public class BulletsTimer extends Timer {

	public BulletsTimer(float pInterval, final MainActivity main) {		
		super(pInterval, new ITimerCallback() {
			
			@Override
			public void onTick() {
				main.detectCollisions();
				if(main.isFiring())
				{ 
					final Rectangle rect = new Rectangle(main.getHero().getX(), main.getHero().getY(), 8, 2,	main.getVertexBufferObjectManager());
					rect.setX(main.getHero().getX()+10);
					rect.setY(main.getHero().getY());
					rect.setZIndex(main.ENEMY_LAYER);
					rect.setColor(Color.RED);
					final Rectangle rect2 = new Rectangle(main.getHero().getX(), main.getHero().getY() + 16, 8, 2,	main.getVertexBufferObjectManager());
					rect2.setX(main.getHero().getX()+10);
					rect2.setY(main.getHero().getY()+32);
					rect2.setZIndex(main.ENEMY_LAYER);
					rect2.setColor(Color.RED);
					
					main.getBullets().add(rect);				
					main.getBullets().add(rect2);
					main.getScene().attachChild(rect);
					main.getScene().attachChild(rect2);
					
					main.getScene().registerUpdateHandler(new IUpdateHandler() {
						@Override
						public void reset(){} 

						@Override
						public void onUpdate(final float pSecondsElapsed)
						{
							rect.setX(rect.getX() + 7.5f);	
							rect2.setX(rect2.getX() + 7.5f);	
						}
					});
					main.playMachineGun();
				}			
				for (Rectangle spriteB : main.getBullets()) {
					if (spriteB.getX() > main.CAMERA_WIDTH) {
						main.getBullets().remove(spriteB);
						spriteB.detachSelf();
						spriteB = null;
						return;
					}				
				}
			
			}
		});
		
	}
	


}
