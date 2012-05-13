package org.queops;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl.IAnalogOnScreenControlListener;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.PathModifier;
import org.andengine.entity.particle.SpriteParticleSystem;
import org.andengine.entity.particle.emitter.BaseParticleEmitter;
import org.andengine.entity.particle.emitter.CircleParticleEmitter;
import org.andengine.entity.particle.emitter.PointParticleEmitter;
import org.andengine.entity.particle.initializer.AccelerationParticleInitializer;
import org.andengine.entity.particle.initializer.BlendFunctionParticleInitializer;
import org.andengine.entity.particle.initializer.ColorParticleInitializer;
import org.andengine.entity.particle.initializer.RotationParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.entity.particle.modifier.ColorParticleModifier;
import org.andengine.entity.particle.modifier.ExpireParticleInitializer;
import org.andengine.entity.particle.modifier.ScaleParticleModifier;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.examples.adt.card.Card;
import org.andengine.extension.customTimer.Timer;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.controller.MultiTouch;
import org.andengine.opengl.texture.Texture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.util.GLHelper;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;
import org.andengine.util.math.MathUtils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import android.opengl.GLES10;
import android.opengl.GLES20;
import android.view.MotionEvent;
import android.widget.Toast;

public class MainActivity extends SimpleBaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	private int cameraWidth = 640;//480;
	private int cameraHeight = 384;//288;
	private static final int BG_LAYER = 1;
	private static final int ENEMY_LAYER = 2;
	protected static final int HERO_LAYER = 3;
	protected static final int CONTROLS_LAYER = 1;
	private static final float RATE_MIN = 8;
	private static final float RATE_MAX = 12;
	private static final int PARTICLES_MAX = 50;
	// ===========================================================
	// Fields
	// ===========================================================

	private BuildableBitmapTextureAtlas mBitmapTextureAtlas;
	private ITextureRegion mFace1TextureRegion;
	private ITextureRegion heroTextureRegion;
	private MainActivity mainAc;
	private Sprite hero;
	protected float diferencialX;
	protected float diferencialY;
	protected List<Sprite> enemies = new ArrayList<Sprite>();
	protected List<Rectangle> bullets = new ArrayList<Rectangle>();
	private Sound mAttackSound;
	protected float deFaultSpeed = 0.41f;
	protected float rotationSpeed = 0.1f;
	protected float defaultRotationSpeed = 0.5f;
	protected float speed = 0.11f;
	private ITextureRegion enemy1TextureRegion;
	private ITextureRegion enemy2TextureRegion;
	private ITextureRegion enemy3TextureRegion;
	private ITiledTextureRegion fireButtonTextureRegion;
	protected float newY = 0;
	protected float newX = 0;
	private Sound mExplosionSound;
	private float[] bounds = new float[] { 1, 1, cameraWidth - 32, cameraHeight - 32 };
	private float antigoTouchX = -1f;
	private float antigoTouchY = -1f;
	private Scene scene;
	protected float destinationX;
	protected float destinationY;
	private boolean pitchSpeed = false;
	private float newRotation;
	private float[] inicialTouchPosition;
	private SpriteParticleSystem starsParticleSystem;
	private CircleParticleEmitter emitter;
	//rotation
	private float mCenterX, mCenterY;
	private float direction = 0;
	private float sX, sY;
	private float startDirection=0;
	private TiledTextureRegion fumacaTextureRegion;
	private TiledTextureRegion bgTextureRegion;
	private Sprite bg;
	private BitmapTextureAtlas mOnScreenControlTexture;
	private ITextureRegion mOnScreenControlBaseTextureRegion;
	private ITextureRegion mOnScreenControlKnobTextureRegion;
	private ITextureRegion nullPixelTexture;
	private boolean analogControlsEnabled;
	private boolean firing;
	private CircleParticleEmitter bulletsEmitter;
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		
		mainAc = this;
		final Camera camera = new Camera(0, 0, cameraWidth, cameraHeight);
		final EngineOptions engineOptions = new EngineOptions(true,	ScreenOrientation.LANDSCAPE_SENSOR, new RatioResolutionPolicy(cameraWidth, cameraHeight), camera);
		engineOptions.getAudioOptions().setNeedsSound(true);
		engineOptions.getTouchOptions().setNeedsMultiTouch(true);
		engineOptions.getTouchOptions().setTouchEventIntervalMilliseconds(41);
		engineOptions.getRenderOptions().setMultiSampling(true);	
		engineOptions.getRenderOptions().setDithering(true);
		
		if(MultiTouch.isSupported(this)) {
			if(MultiTouch.isSupportedDistinct(this)) {
				//Toast.makeText(this, "MultiTouch detected --> Both controls will work properly!", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "MultiTouch detected, but your device has problems distinguishing between fingers.\n\nControls are placed at different vertical locations.", Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(this, "Sorry your device does NOT support MultiTouch!\n\n(Falling back to SingleTouch.)\n\nControls are placed at different vertical locations.", Toast.LENGTH_LONG).show();
		}
		
		return engineOptions;
	}

	@Override
	public void onCreateResources() {

		SoundFactory.setAssetBasePath("mfx/");
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		try {
			this.mAttackSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "fast-attack.ogg");
			this.mExplosionSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "explosion.ogg");
		} 
		catch (final IOException e) 
		{
			Debug.e(e);
		}		

		this.mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(this.getTextureManager(), 512, 512);
		
		this.heroTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "ship.png");
		this.enemy1TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "enemy1.png");
		this.enemy2TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "enemy2.png");
		this.enemy3TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "enemy3.png");
		this.fireButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this,"fireBtn.png", 1, 1);
		this.fumacaTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this,"particle1.png", 1, 1);
		this.bgTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this,"bg.png", 1, 1);
		//Textura "transparente"
		this.nullPixelTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "nullpixel.png");
		
		//this.bulletTextureRegion = TextureRegionFactory.(this.mBitmapTextureAtlas, this,"bg.png", 1, 1);
		
		this.mOnScreenControlTexture = new BitmapTextureAtlas(this.getTextureManager(), 256, 128, TextureOptions.BILINEAR);
		this.mOnScreenControlBaseTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mOnScreenControlTexture, this, "onscreen_control_base.png", 0, 0);
		this.mOnScreenControlKnobTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mOnScreenControlTexture, this, "onscreen_control_knob.png", 128, 0);
		this.mOnScreenControlTexture.load();
		
		try {
			this.mBitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
			this.mBitmapTextureAtlas.load();
		} 
		catch (TextureAtlasBuilderException e) {
			Debug.e(e);
		}
	}

	@Override
	public Scene onCreateScene() {

		//this.mEngine.registerUpdateHandler(new FPSLogger());
		scene = getScene1();
		getHero();
		createBg();
		scene.attachChild(hero);
		createStars();
		createBullets();
		createBg();
		createFireButton();		
		
		mEngine.registerUpdateHandler(heroMovingTimer);
		mEngine.registerUpdateHandler(enemiesCreationTimer);
		scene.registerUpdateHandler(timerBullets);
		
		scene.setTouchAreaBindingOnActionDownEnabled(true);
		
		return scene;
	}
	
	private void createFireButton(){
		//Fire Button
				final Sprite fireBtn = new ButtonSprite(cameraWidth - 50,cameraHeight - 50, this.fireButtonTextureRegion, this.getVertexBufferObjectManager(), fireBtnHandler);
				 
				fireBtn.setZIndex(CONTROLS_LAYER);  
				scene.registerTouchArea(fireBtn);
				scene.attachChild(fireBtn);
	}


	private void createBg() {
		
		final AutoParallaxBackground autoParallaxBackground = new AutoParallaxBackground(0, 0, 0, 5);
		final VertexBufferObjectManager vertexBufferObjectManager = this.getVertexBufferObjectManager();
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-10f, new Sprite(0, 0,cameraWidth, cameraHeight, bgTextureRegion, vertexBufferObjectManager)));
		
		scene.setBackground(autoParallaxBackground);
	}

	public void playExplosion() {
		mAttackSound.play();
	}

	PathModifier.IPathModifierListener pathListener = new PathModifier.IPathModifierListener() {
		@Override
		public void onPathStarted(final PathModifier pPathModifier,
				final IEntity pEntity) {
			Debug.d("onPathStarted");
		}

		@Override
		public void onPathWaypointStarted(final PathModifier pPathModifier,
				final IEntity pEntity, final int pWaypointIndex) {
			Debug.d("onPathWaypointStarted:  " + pWaypointIndex);
		}

		@Override
		public void onPathWaypointFinished(final PathModifier pPathModifier,
				final IEntity pEntity, final int pWaypointIndex) {
			Debug.d("onPathWaypointFinished: " + pWaypointIndex);
		}

		@Override
		public void onPathFinished(final PathModifier pPathModifier,
				final IEntity pEntity) {
			Debug.d("onPathFinished");
		}
	};


	public float getLookFowardRotation(float toX, float toY, float fromX, float fromY)
	{		
		    double angle = Math.toDegrees(Math.atan2(toY - fromY, toX - fromX))*180/Math.PI;
		   
		    return  (float) angle;
	 }
	
	public Sprite getHero()
	{
		hero = new Sprite(cameraWidth / 3, cameraHeight / 2, heroTextureRegion, getVertexBufferObjectManager());
		hero.setRotation(-90);
		hero.setRotationCenter(8, 8);
		//Alternative hero
		
//	//Triangle
//		Line line1 = new Line(0,0,0,16, getVertexBufferObjectManager());
//		Line line2 = new Line(0,16,16,8, getVertexBufferObjectManager());
//		Line line3 = new Line(16,8,0,0, getVertexBufferObjectManager());
//		line1.setColor(Color.RED);
//		line2.setColor(Color.RED);
//		line3.setColor(Color.RED);
//		hero.attachChild(line1);
//		hero.attachChild(line2); 
//		hero.attachChild(line3);
//		
		if(analogControlsEnabled)
		{
			createAnalogControl();
		}
		else
		{
			//createFullTouchControl();
		}
		
		return hero;
	}
	
	private void createFullTouchControl() {
		
		Rectangle leftRect = new Rectangle(0, 0, cameraWidth/3*2, cameraHeight, getVertexBufferObjectManager());
		Rectangle rightRect = new Rectangle(0, 0, cameraWidth/3, cameraHeight, getVertexBufferObjectManager());
		
		
		Sprite leftSprite = new Sprite(0,0,nullPixelTexture,getVertexBufferObjectManager()){			
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				pitchSpeed =true;

				if (hero != null && pSceneTouchEvent.getX() < cameraWidth * 2 / 3 ) 
				{
					if (antigoTouchX == -1f) 
					{
						antigoTouchX = pSceneTouchEvent.getX();
						antigoTouchY = pSceneTouchEvent.getY();
					}
					
					newX = hero.getX();
					newY = hero.getY();
					
					if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP || pSceneTouchEvent.getAction() == TouchEvent.ACTION_OUTSIDE) 
					{
						pitchSpeed = false;
						antigoTouchX = -1f;
						antigoTouchY = -1f;
					
					}
					
					if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN && pSceneTouchEvent.getX() < cameraWidth * 2 / 3) 
					{
						
						inicialTouchPosition = new float[]{pSceneTouchEvent.getX(), pSceneTouchEvent.getY()};
						
						
							diferencialX = (float) (pSceneTouchEvent.getX() - antigoTouchX);
							diferencialY = (float) (pSceneTouchEvent.getY() - antigoTouchY);

							if (pSceneTouchEvent.getX() > newX && diferencialX < 0) {
								diferencialX = -(float) (pSceneTouchEvent.getX() - antigoTouchX);
							}
							if (pSceneTouchEvent.getY() > newY && diferencialX < 0) {
								diferencialY = -(float) (pSceneTouchEvent.getY() - antigoTouchY);
							}

					} 
					else if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_MOVE) 
					{
							diferencialX = (float) (pSceneTouchEvent.getX() - antigoTouchX);
							diferencialY = (float) (pSceneTouchEvent.getY() - antigoTouchY);
							newRotation = angleBetween2Lines(pSceneTouchEvent.getX(), pSceneTouchEvent.getY(), antigoTouchX, antigoTouchY);
							antigoTouchX = pSceneTouchEvent.getX();
							antigoTouchY = pSceneTouchEvent.getY();
							
							
					}

				}

				return true;//super.onSceneTouchEvent(pSceneTouchEvent);
				
			}
		};
		leftSprite.attachChild(leftRect);
		
		
		Sprite rightSprite = new Sprite(cameraWidth/3*2,0,nullPixelTexture,getVertexBufferObjectManager()){
			boolean mGrabbed = false;
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				
				return true;
			}
		};		
		rightSprite.attachChild(rightRect);
		
		rightSprite.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		leftSprite.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		
		scene.registerTouchArea(rightSprite);
		scene.registerTouchArea(leftSprite);
		scene.attachChild(leftSprite);
		scene.attachChild(rightSprite);
	}

	public Scene getScene1() 
	{
		final Scene scene = new Scene(){

			@Override
			public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
				onLeftTouchEvent(pSceneTouchEvent);
				return super.onSceneTouchEvent(pSceneTouchEvent);
			}
			
		};
		
		return scene;
	}

	// Fire Button Handler
	public 	OnClickListener fireBtnHandler = new OnClickListener() {

		@Override
		public void onClick(ButtonSprite pButtonSprite,	float pTouchAreaLocalX, float pTouchAreaLocalY) {
			
			runOnUiThread(new Runnable() {
				@Override
				public void run() {

					final Rectangle rect = new Rectangle(newX, newY	- diferencialY, 20, 2,	mainAc.getVertexBufferObjectManager());
					rect.setX(hero.getX());
					rect.setY(hero.getY());
					rect.setZIndex(ENEMY_LAYER);
					rect.setColor(Color.RED);

					bullets.add(rect);
					scene.attachChild(rect);
					scene.registerUpdateHandler(new IUpdateHandler() {
						@Override
						public void reset(){}

						@Override
						public void onUpdate(final float pSecondsElapsed)
						{
							rect.setX(rect.getX() + 7.5f);	
						}
					});
					playExplosion();
				}
			});

		}
	};


	
	public float angleBetween2Lines(float x1, float y1, float x2, float y2)
	{
	    double angle1 = Math.atan2(y1 - inicialTouchPosition[1], x1 - inicialTouchPosition[0]);
	    double angle2 = Math.atan2(y2 - inicialTouchPosition[1], x2 - inicialTouchPosition[0]);
	    return (float)(angle1 - angle2);
	}
	
	
	public void createStars()
	{
		
		emitter = new CircleParticleEmitter(0, 0, 2);
		starsParticleSystem = new SpriteParticleSystem(emitter, RATE_MIN, RATE_MAX, PARTICLES_MAX, this.fumacaTextureRegion, this.getVertexBufferObjectManager());
		starsParticleSystem.addParticleInitializer(new BlendFunctionParticleInitializer<Sprite>(GLES10.GL_SRC_ALPHA, GLES10.GL_ONE));//(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE));
		starsParticleSystem.addParticleInitializer(new VelocityParticleInitializer<Sprite>(-35, -35, 0, 0));
		starsParticleSystem.addParticleInitializer(new AccelerationParticleInitializer<Sprite>(5, 0));
		//particleSystem.addParticleInitializer(new RotationParticleInitializer<Sprite>(0.0f, 360.0f));
		starsParticleSystem.addParticleInitializer(new ColorParticleInitializer<Sprite>(Color.WHITE));
		starsParticleSystem.addParticleInitializer(new ExpireParticleInitializer<Sprite>(1.5f));

		starsParticleSystem.addParticleModifier(new ScaleParticleModifier<Sprite>(0, 5, 0.5f, 2.0f));
		//particleSystem.addParticleModifier(new ColorParticleModifier<Sprite>(2.5f, 5.5f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f));
		starsParticleSystem.addParticleModifier(new AlphaParticleModifier<Sprite>(0f, 1.5f, 1.0f, 0.0f));

		scene.attachChild(starsParticleSystem);
	}
	
	
	
	
public void createBullets()
	{
		
		bulletsEmitter = new CircleParticleEmitter(0, 0, 2);
		starsParticleSystem = new SpriteParticleSystem(bulletsEmitter, RATE_MIN, RATE_MAX, PARTICLES_MAX, this.fumacaTextureRegion, this.getVertexBufferObjectManager());
		starsParticleSystem.addParticleInitializer(new BlendFunctionParticleInitializer<Sprite>(GLES10.GL_SRC_ALPHA, GLES10.GL_ONE));//(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE));
		starsParticleSystem.addParticleInitializer(new VelocityParticleInitializer<Sprite>(75, 75, 0, 0));
		starsParticleSystem.addParticleInitializer(new AccelerationParticleInitializer<Sprite>(5, 0));
		//particleSystem.addParticleInitializer(new RotationParticleInitializer<Sprite>(0.0f, 360.0f));
		starsParticleSystem.addParticleInitializer(new ColorParticleInitializer<Sprite>(Color.RED));
		starsParticleSystem.addParticleInitializer(new ExpireParticleInitializer<Sprite>(6.5f));

		//starsParticleSystem.addParticleModifier(new ScaleParticleModifier<Sprite>(0, 5, 0.5f, 2.0f));
		//particleSystem.addParticleModifier(new ColorParticleModifier<Sprite>(2.5f, 5.5f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f));
		//starsParticleSystem.addParticleModifier(new AlphaParticleModifier<Sprite>(0f, 1.5f, 1.0f, 0.0f));
		
		scene.attachChild(starsParticleSystem);
	}
	
	
	public void createAnalogControl()
	{
		final float x1 = 0;
		final float y1 = cameraHeight - this.mOnScreenControlBaseTextureRegion.getHeight();
		final AnalogOnScreenControl velocityOnScreenControl = new AnalogOnScreenControl(x1, y1, this.mEngine.getCamera(), this.mOnScreenControlBaseTextureRegion, this.mOnScreenControlKnobTextureRegion, 0.1f, this.getVertexBufferObjectManager(), new IAnalogOnScreenControlListener() {
			@Override
			public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl, final float pValueX, final float pValueY) {
				//physicsHandler.setVelocity(pValueX * 100, pValueY * 100);
			}

			@Override
			public void onControlClick(final AnalogOnScreenControl pAnalogOnScreenControl) {
				/* Nothing. */
			}
		});
		velocityOnScreenControl.getControlBase().setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		velocityOnScreenControl.getControlBase().setAlpha(0.5f);

		scene.setChildScene(velocityOnScreenControl);
	}

	

	public boolean onLeftTouchEvent(TouchEvent pSceneTouchEvent) {
		
		

		if (hero != null && pSceneTouchEvent.getX() < cameraWidth * 2 / 3 ) 
		{
			pitchSpeed =true;
			if (antigoTouchX == -1f) 
			{
				antigoTouchX = pSceneTouchEvent.getX();
				antigoTouchY = pSceneTouchEvent.getY();
			}
			
			newX = hero.getX();
			newY = hero.getY();
			
			if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP || pSceneTouchEvent.getAction() == TouchEvent.ACTION_OUTSIDE) 
			{
				pitchSpeed = false;
				antigoTouchX = -1f;
				antigoTouchY = -1f;
			
			}
			
			if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN ) 
			{
				
				inicialTouchPosition = new float[]{pSceneTouchEvent.getX(), pSceneTouchEvent.getY()};
				
				
					diferencialX = (float) (pSceneTouchEvent.getX() - antigoTouchX);
					diferencialY = (float) (pSceneTouchEvent.getY() - antigoTouchY);

					if (pSceneTouchEvent.getX() > newX && diferencialX < 0) {
						diferencialX = -(float) (pSceneTouchEvent.getX() - antigoTouchX);
					}
					if (pSceneTouchEvent.getY() > newY && diferencialX < 0) {
						diferencialY = -(float) (pSceneTouchEvent.getY() - antigoTouchY);
					}

			} 
			else if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_MOVE) 
			{
				
	    
					
					diferencialX = (float) (pSceneTouchEvent.getX() - antigoTouchX);
					diferencialY = (float) (pSceneTouchEvent.getY() - antigoTouchY);
											
						
					newRotation = angleBetween2Lines(pSceneTouchEvent.getX(), pSceneTouchEvent.getY(), antigoTouchX, antigoTouchY);
					
					antigoTouchX = pSceneTouchEvent.getX();
					antigoTouchY = pSceneTouchEvent.getY();
					
					
			}

		}
		else
		{
			if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN ) 
			{
				firing = true;
			}
			else if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP || pSceneTouchEvent.getAction() == TouchEvent.ACTION_OUTSIDE) 
			{
				firing = false;
			}
			
		}

		return true;//super.onSceneTouchEvent(pSceneTouchEvent);

	}// END onSceneTouchEvent
	
	Timer enemiesCreationTimer = new Timer(2, new Timer.ITimerCallback() {

		@Override
		public void onTick() {
			// Inimigos comecam no fim da tela
			final float centerX = cameraWidth + 50;
			final float centerY = cameraHeight * MathUtils.RANDOM.nextFloat();
			ITextureRegion inimigo;
			switch (MathUtils.RANDOM.nextInt(2)) {
			case 0:
				inimigo = enemy1TextureRegion;
				break;
			case 1:
				inimigo = enemy2TextureRegion;
				break;
			case 2:
				inimigo = enemy3TextureRegion;
				break;
			default:
				inimigo = enemy1TextureRegion;
				break;
			}

			final Sprite newEnemy = new Sprite(centerX, centerY, inimigo,getVertexBufferObjectManager());				
			newEnemy.setZIndex(ENEMY_LAYER);
			newEnemy.setRotation(90);
			
			enemies.add(newEnemy);				

			scene.registerTouchArea(newEnemy);
			scene.attachChild(newEnemy);
			
			for (Sprite enemy : enemies) {
				if (enemy.getX() < -enemy.getWidth()) {
					enemy.detachSelf();
					enemies.remove(enemy);
					// spriteB = null;
					return;
				}
			}
			
			scene.registerUpdateHandler(new IUpdateHandler() {
				@Override
				public void reset() {
				}

				@Override
				public void onUpdate(final float pSecondsElapsed) {
					
					//Movendo Inimigos
					newEnemy.setX(newEnemy.getX() - 5.5f);

					for (Sprite spriteB : enemies) {
						
						if (spriteB.collidesWith(hero)) {

							spriteB.detachSelf();
							enemies.remove(spriteB);
							spriteB = null;
							return;
						}
					}
				}
			});
		}
	});

	
	//OnEnterFrame timed
	Timer heroMovingTimer = new Timer(0.023f, new Timer.ITimerCallback() {

		@Override
		public void onTick() {
			//Movendo o heroi
			if(pitchSpeed)
			{
				speed = deFaultSpeed;
				rotationSpeed = defaultRotationSpeed;
			}
			else if (speed >0){
				speed -= 0.02f;
				rotationSpeed -= 0.02f;
			}
			else
			{
				speed = 0;
				rotationSpeed = 0;
			}
			
			if(speed != 0)
			{
				newX += diferencialX * speed;
				newY += diferencialY * speed;
			}

			newX = Math.max(Math.min(newX, bounds[2]), bounds[0]);
			newY = Math.max(Math.min(newY, bounds[3]), bounds[1]);
			hero.setRotation(newRotation);
			hero.setPosition(newX, newY);
			emitter.setCenter(newX, newY+8);
			bulletsEmitter.setCenter(newX, newY+8);
		}
	});
	
	
	Timer timerBullets = new Timer(0.5f, new Timer.ITimerCallback() {
		@Override
		public void onTick() {						
			
			if(firing)
			{
				final Rectangle rect = new Rectangle(newX, newY	- diferencialY, 20, 2,	mainAc.getVertexBufferObjectManager());
				rect.setX(hero.getX());
				rect.setY(hero.getY());
				rect.setZIndex(ENEMY_LAYER);
				rect.setColor(Color.RED);

				bullets.add(rect);
				scene.attachChild(rect);
				scene.registerUpdateHandler(new IUpdateHandler() {
					@Override
					public void reset(){}

					@Override
					public void onUpdate(final float pSecondsElapsed)
					{
						rect.setX(rect.getX() + 7.5f);	
					}
				});
				playExplosion();
			}
			
			for (Rectangle spriteB : bullets) {
				if (spriteB.getX() > cameraWidth) {
					bullets.remove(spriteB);
					spriteB.detachSelf();
					spriteB = null;
					return;
				}
				// TODO
				// if (spriteB.collidesWith(hero)) {
				//
				// spriteB.detachSelf();
				// bullets.remove(spriteB);
				// spriteB = null;
				// return;
				// }
			}
		}
	});
	
}
