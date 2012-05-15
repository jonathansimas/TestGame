package org.queops.game.timers;

import org.andengine.extension.customTimer.Timer;
import org.queops.MainActivity;

public class HeroMovingTimer extends Timer {

	public HeroMovingTimer(float pInterval, final MainActivity main) {		
		super(pInterval, new ITimerCallback() {
			
			@Override
			public void onTick() {
				main.detectCollisions(); 
				//Movendo o heroi
//				if(main.pitchSpeed)
//				{
//					main.speed = main.deFaultSpeed;
//					main.rotationSpeed = main.defaultRotationSpeed;
//				}
//				else if (main.speed >0){
//					main.speed -= 0.02f;
//					main.rotationSpeed -= 0.02f;
//				}
//				else
//				{
//					main.speed = 0;
//					main.rotationSpeed = 0;
//				}
//				
//				if(main.speed != 0)
//				{
//					main.newX += main.diferencialX * main.speed;
//					main.newY += main.diferencialY * main.speed;
//				}
//
//				main.newX = Math.max(Math.min(main.newX, main.bounds[2]), main.bounds[0]);
//				main.newY = Math.max(Math.min(main.newY, main.bounds[3]), main.bounds[1]);
//				main.hero.setRotation(main.newRotation);
//				main.hero.setPosition(main.newX, main.newY);
//				main.propulsionEmitter.setCenter(main.newX, main.newY+8);
//				main.bulletsEmitter.setCenter(main.newX, main.newY+8);
			
			}
		});
		// TODO Auto-generated constructor stub
	}
	


}
