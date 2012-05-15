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
				if(main.firing)
				{ 
					final Rectangle rect = new Rectangle(main.hero.getX(), main.hero.getX()	- main.diferencialY, 8, 2,	main.getVertexBufferObjectManager());
					rect.setX(main.hero.getX()+10);
					rect.setY(main.hero.getY());
					rect.setZIndex(main.ENEMY_LAYER);
					rect.setColor(Color.RED);
					final Rectangle rect2 = new Rectangle(main.hero.getX(), main.hero.getX()	- main.diferencialY, 8, 2,	main.getVertexBufferObjectManager());
					rect2.setX(main.hero.getX()+10);
					rect2.setY(main.hero.getY()+32);
					rect2.setZIndex(main.ENEMY_LAYER);
					rect2.setColor(Color.RED);
					
					main.bullets.add(rect);				
					main.bullets.add(rect2);
					main.scene.attachChild(rect);
					main.scene.attachChild(rect2);
					
					main.scene.registerUpdateHandler(new IUpdateHandler() {
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
				for (Rectangle spriteB : main.bullets) {
					if (spriteB.getX() > main.CAMERA_WIDTH) {
						main.bullets.remove(spriteB);
						spriteB.detachSelf();
						spriteB = null;
						return;
					}				
				}
			
			}
		});
		
	}
	


}
