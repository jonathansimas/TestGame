package org.queops;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl.IAnalogOnScreenControlListener;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.particle.SpriteParticleSystem;
import org.andengine.entity.particle.emitter.CircleParticleEmitter;
import org.andengine.entity.particle.initializer.AccelerationParticleInitializer;
import org.andengine.entity.particle.initializer.BlendFunctionParticleInitializer;
import org.andengine.entity.particle.initializer.ColorParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.entity.particle.modifier.ExpireParticleInitializer;
import org.andengine.entity.particle.modifier.ScaleParticleModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.customTimer.Timer;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.controller.MultiTouch;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;
import org.andengine.util.math.MathUtils;
import org.queops.game.timers.BulletsTimer;
import org.queops.game.timers.EnemiesCreationTimer;
import org.queops.game.timers.HeroMovingTimer;

import android.graphics.Typeface;
import android.opengl.GLES10;
import android.opengl.GLES20;
import android.view.MotionEvent;
import android.widget.Toast;

public class MainActivity extends SimpleBaseGameActivity  implements IOnMenuItemClickListener{
	// ===========================================================
	// Constants
	// ===========================================================
	public static int CAMERA_WIDTH = 640;//480;
	public static int CAMERA_HEIGHT = 384;//288;
	public static final int BG_LAYER = 1;
	public static final int ENEMY_LAYER = 2;
	public static final int HERO_LAYER = 3;
	public static final int CONTROLS_LAYER = 1;
	public static final float RATE_MIN = 8;
	public static final float RATE_MAX = 12;
	public static final int PARTICLES_MAX = 50;
	// ===========================================================
	// Fields
	// ===========================================================
	private MainActivity mainAc;
	private Scene scene;	

	private Camera camera;
	private Sprite hero;
	private Sprite fireBtn;
	private Sprite pauseBtn;
	protected List<Sprite> enemies = new ArrayList<Sprite>();
	protected List<Rectangle> bullets = new ArrayList<Rectangle>();
	
	//Textures
	private BuildableBitmapTextureAtlas mBitmapTextureAtlas;
	private ITextureRegion heroTextureRegion;
	private ITiledTextureRegion fireButtonTextureRegion;
	private TiledTextureRegion fumacaTextureRegion;
	private TiledTextureRegion bgTextureRegion;
	//Enemies
	private ITextureRegion enemy1TextureRegion;
	private ITextureRegion enemy2TextureRegion;
	private ITextureRegion enemy3TextureRegion;
	//Controls
	private BitmapTextureAtlas mOnScreenControlTexture;
	private ITextureRegion mOnScreenControlBaseTextureRegion;
	private ITextureRegion mOnScreenControlKnobTextureRegion;
	//Other Buttons
	private TiledTextureRegion gameOverTextureRegion;
	private TiledTextureRegion exitButtonTextureRegion;
	private TiledTextureRegion newGameButtonTextureRegion;
	private TiledTextureRegion pauseButtonTextureRegion;
	
	//particles
	private SpriteParticleSystem starsParticleSystem;
	private CircleParticleEmitter emitter;
	private CircleParticleEmitter bulletsEmitter;
	private SpriteParticleSystem bulletsParticleSystem;
	//Sounds
	private Sound machnineGunSound;
	private Sound canonSound;
	//Hero Moving Vars
	private float[] bounds;
	protected float diferencialX;
	protected float diferencialY;
	protected float deFaultSpeed = 0.41f;
	protected float rotationSpeed = 0.1f;
	protected float defaultRotationSpeed = 0.5f;
	protected float speed = 0.11f;
	protected float newY = 0;
	protected float newX = 0;
	private float antigoTouchX = -1f;
	private float antigoTouchY = -1f;
	protected float destinationX;
	protected float destinationY;
	private boolean pitchSpeed = false;
	private float newRotation;
	private float[] inicialTouchPosition;
	//Analog Controls
	private boolean analogControlsEnabled = true;
	private PhysicsHandler physicsHandler;
	private boolean firing;
	//Placar e LifeBar
	private IFont mFont;
	private Integer score = new Integer(1);
	private Text placarText;
	private Rectangle lifeBarRectangle;
	//Timers
	public Timer bulletsTimer;
	public Timer enemiesCreationTimer;
	public Timer heroMovingTimer;
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		
		mainAc = this;
		camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		final EngineOptions engineOptions = new EngineOptions(true,	ScreenOrientation.LANDSCAPE_SENSOR, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
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
		bounds = new float[] { 1, 1, CAMERA_WIDTH - 32, CAMERA_HEIGHT - 32 };
		return engineOptions;
	}

	@Override
	public void onCreateResources() {

		SoundFactory.setAssetBasePath("mfx/");
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		this.mFont = FontFactory.create(this.getFontManager(), this.getTextureManager(), 256, 256, Typeface.create(Typeface.DEFAULT, Typeface.BOLD),32, Color.WHITE_ABGR_PACKED_INT);
		this.mFont.load();
		try {
			this.machnineGunSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "MachineGun2.ogg");
			this.canonSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "Canon.ogg");
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
		this.gameOverTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this,"gameOverPanel.png", 1, 1);
		this.exitButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this,"ExitButton.png", 1, 1);
		this.newGameButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this,"newGameButton.png", 1, 1);
		this.pauseButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this,"playerPause.png", 1, 1);
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
		scene = new Scene();
		createHero();
		createBg();		
		createPropulsionSmoke();
		createBullets();
		createBg();
		createFireButton();		
		createCannonButton();
		createPlacar();
		createLifeBar();
		createPauseButton();
		heroMovingTimer = new HeroMovingTimer(1, this);
		enemiesCreationTimer = new EnemiesCreationTimer(1, this);
		bulletsTimer = new BulletsTimer(1, this);
		mEngine.registerUpdateHandler(heroMovingTimer);
		mEngine.registerUpdateHandler(enemiesCreationTimer);
		mEngine.registerUpdateHandler(bulletsTimer);
		
		scene.setTouchAreaBindingOnActionDownEnabled(true);
		
		return scene;
	}
	
	private void createFireButton(){
		//Fire Button
				fireBtn = new Sprite(CAMERA_WIDTH - 130,CAMERA_HEIGHT - 70, this.fireButtonTextureRegion, this.getVertexBufferObjectManager()){

					@Override
					public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
						if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN)
						{
							firing = true;
							bulletsParticleSystem.setParticlesSpawnEnabled(true);
						}
						else if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP ){
							firing = false;
							bulletsParticleSystem.setParticlesSpawnEnabled(false);
						} 
						else if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_OUTSIDE ){
							firing = false;
							bulletsParticleSystem.setParticlesSpawnEnabled(false);
						}
						return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
					}
					
				};
				 
				fireBtn.setZIndex(CONTROLS_LAYER);  
				scene.registerTouchArea(fireBtn);
				scene.attachChild(fireBtn);
	}

	private void createCannonButton(){
		//Fire Button
				final Sprite fireBtn = new ButtonSprite(CAMERA_WIDTH - 90,CAMERA_HEIGHT - 120, this.fireButtonTextureRegion, this.getVertexBufferObjectManager(), cannonBtnHandler);
				 
				fireBtn.setZIndex(CONTROLS_LAYER);  
				scene.registerTouchArea(fireBtn);
				scene.attachChild(fireBtn);
	}

	private void createBg() {
		
		final AutoParallaxBackground autoParallaxBackground = new AutoParallaxBackground(0, 0, 0, 5);
		final VertexBufferObjectManager vertexBufferObjectManager = this.getVertexBufferObjectManager();
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-10f, new Sprite(0, 0,CAMERA_WIDTH, CAMERA_HEIGHT, bgTextureRegion, vertexBufferObjectManager)));
		
		scene.setBackground(autoParallaxBackground);
	}

	public void playMachineGun() {
		machnineGunSound.play();
	}

	public void playCanon() {
		canonSound.play();
	}

	public float getLookFowardRotation(float toX, float toY, float fromX, float fromY)
	{		
		    double angle = Math.toDegrees(Math.atan2(toY - fromY, toX - fromX))*180/Math.PI;
		   
		    return  (float) angle;
	 }
	
	public void createHero()
	{
		hero = new Sprite(CAMERA_WIDTH / 3, CAMERA_HEIGHT / 2, heroTextureRegion, getVertexBufferObjectManager());
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
		physicsHandler = new PhysicsHandler(hero);
		hero.registerUpdateHandler(physicsHandler);
		
		if(analogControlsEnabled)
		{
			createAnalogControl();
		}
		else
		{
			//createFullTouchControl();
		}
		scene.attachChild(hero);
		
	}
	
	// Canon Button Handler
	public 	OnClickListener cannonBtnHandler = new OnClickListener() {

		@Override
		public void onClick(final ButtonSprite pButtonSprite,	float pTouchAreaLocalX, float pTouchAreaLocalY) {
			
		
					if(pButtonSprite.getState() == ButtonSprite.State.PRESSED)
					{
						
					}
					if(scene.hasChildScene()){
						scene.clearChildScene();
						//TODO Implementar "unpause"
						//pauseBtn.setVisible(true);
					}
					final Rectangle rect = new Rectangle(newX, newY	- diferencialY, 20, 2,	mainAc.getVertexBufferObjectManager());
					rect.setX(hero.getX()+10);
					rect.setY(hero.getY()+16);
					rect.setZIndex(ENEMY_LAYER);
					rect.setColor(Color.CYAN);

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
					playCanon();
			

		}
	};

	public float angleBetween2Lines(float x1, float y1, float x2, float y2)
	{
	    double angle1 = Math.atan2(y1 - inicialTouchPosition[1], x1 - inicialTouchPosition[0]);
	    double angle2 = Math.atan2(y2 - inicialTouchPosition[1], x2 - inicialTouchPosition[0]);
	    return (float)(angle1 - angle2);
	}
	
	public void createPropulsionSmoke()
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
		bulletsParticleSystem = new SpriteParticleSystem(bulletsEmitter, RATE_MIN, RATE_MAX, PARTICLES_MAX, this.fumacaTextureRegion, this.getVertexBufferObjectManager());
		//bulletsParticleSystem.addParticleInitializer(new BlendFunctionParticleInitializer<Sprite>(GLES10.GL_SRC_ALPHA, GLES10.GL_ONE));//(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE));
		bulletsParticleSystem.addParticleInitializer(new VelocityParticleInitializer<Sprite>(175, 175, 0, 0));
		//bulletsParticleSystem.addParticleInitializer(new AccelerationParticleInitializer<Sprite>(5, 0));
		//bulletsParticleSystem.addParticleInitializer(new RotationParticleInitializer<Sprite>(0.0f, 360.0f));
		bulletsParticleSystem.addParticleInitializer(new ColorParticleInitializer<Sprite>(Color.RED));
		bulletsParticleSystem.addParticleInitializer(new ExpireParticleInitializer<Sprite>(4.5f));
		bulletsParticleSystem.setParticlesSpawnEnabled(false);
		//starsParticleSystem.addParticleModifier(new ScaleParticleModifier<Sprite>(0, 5, 0.5f, 2.0f));
		//particleSystem.addParticleModifier(new ColorParticleModifier<Sprite>(2.5f, 5.5f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f));
		//starsParticleSystem.addParticleModifier(new AlphaParticleModifier<Sprite>(0f, 1.5f, 1.0f, 0.0f));
		
		scene.attachChild(bulletsParticleSystem);
	}
	
	public void createAnalogControl()
	{
		final float x1 = 0;
		final float y1 = CAMERA_HEIGHT - this.mOnScreenControlBaseTextureRegion.getHeight();
		final AnalogOnScreenControl velocityOnScreenControl = new AnalogOnScreenControl(x1, y1, this.mEngine.getCamera(), this.mOnScreenControlBaseTextureRegion, this.mOnScreenControlKnobTextureRegion, 0.1f, this.getVertexBufferObjectManager(), new IAnalogOnScreenControlListener() {
			@Override
			public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl, final float pValueX, final float pValueY) {
				physicsHandler.setVelocity(pValueX * 100, pValueY * 100);
				hero.setRotation(MathUtils.radToDeg((float)Math.atan2(pValueX, -pValueY)));
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
		
		

		if (hero != null && pSceneTouchEvent.getX() < CAMERA_WIDTH * 2 / 3 ) 
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
			if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP || pSceneTouchEvent.getAction() == TouchEvent.ACTION_OUTSIDE) 
			{
				//firing = false;
				//bulletsParticleSystem.setParticlesSpawnEnabled(false);
			}
			else
			{
				//firing = true;
				//bulletsParticleSystem.setParticlesSpawnEnabled(true);
			}
			
		}

		return true;//super.onSceneTouchEvent(pSceneTouchEvent);

	}// END onSceneTouchEvent
	
	public void removeEnemies()
	{
		for (Sprite enemy : enemies) {
			if (enemy.getX() < -enemy.getWidth()) {
				enemy.detachSelf();
				enemies.remove(enemy);
				enemy = null;
				removeEnemies();
				break;
			}
		}
	}
	

	public void detectCollisions()
	{
		enemiesfor:
		for(int i = 0; i <enemies.size();i++)
		{
			if(enemies.get(i).hasParent())
			{
				bulletsfor:
				for(int j = 0; j < bullets.size();j++)
				{
					//Manual detection mode - não excluir, pois servirá como exemplo onde não for possivel o metodo "collidesWith()"
					//if(bullets.get(j).hasParent() & MathUtils.distance(bullets.get(j).getX(), bullets.get(j).getY(), enemies.get(i).getX(), enemies.get(i).getY()) <= enemies.get(i).getWidth() * 1.4)
					//{
						if(enemies.get(i).collidesWith(bullets.get(j)))
						{
							//Se atingiu, remove a bala
							bullets.get(j).detachSelf();
							bullets.remove(bullets.get(j));
							//e a nave inimiga
							enemies.get(i).detachSelf();
							enemies.remove(enemies.get(i));
							score++;
							placarText.setText(score.toString());
							return;							
						}
					//}
				}
			}
		}
	}
	public void createPlacar()
	{
		
		placarText = new Text(20, 10, this.mFont, "0", 30, new TextOptions(HorizontalAlign.CENTER), getVertexBufferObjectManager());
		//placarText.setColor(Color.WHITE);
		scene.attachChild(placarText);
	}
	
	public void createLifeBar()
	{
		lifeBarRectangle = new Rectangle(CAMERA_WIDTH - 60, 10, 50, 10, getVertexBufferObjectManager());
		lifeBarRectangle.setColor(Color.GREEN);
		scene.attachChild(lifeBarRectangle);
	}
	
	public void createGameOverPanel(){
		//GameOverPanel
		Sprite gameOverSprite = new Sprite(CAMERA_WIDTH/2 - 127,CAMERA_HEIGHT/2 - 127, this.gameOverTextureRegion, this.getVertexBufferObjectManager());
		scene.attachChild(gameOverSprite);
		
		//new Game Button
		ButtonSprite newGameBtn = new ButtonSprite(gameOverSprite.getX()+11,gameOverSprite.getY()+147, this.newGameButtonTextureRegion, this.getVertexBufferObjectManager()){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) 
			{
				scene.reset();
				mEngine.start();
				 //scene.setChildScene(pauseScene(this), false, true, true);
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		
		scene.registerTouchArea(newGameBtn);
		scene.attachChild(newGameBtn);
	}
	
	public void createPauseButton(){
		pauseBtn= new Sprite(CAMERA_WIDTH-100, 50, pauseButtonTextureRegion, this.getVertexBufferObjectManager()){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
	   			 if(pSceneTouchEvent.getAction()==MotionEvent.ACTION_UP){
	   				 this.setVisible(false);
	   				 scene.setChildScene(pauseScene(pauseBtn), false, true, true);
	   			 }
	   			 return true;
       	}};
       //	pauseBtn.setScale(2);
       	scene.attachChild(pauseBtn);
       	scene.registerTouchArea(pauseBtn);
	}
	
	private MenuScene pauseScene(Sprite buttonPause)
	{
		final MenuScene pauseGame= new MenuScene(camera);		
		final SpriteMenuItem btnPlay = new SpriteMenuItem(1, pauseButtonTextureRegion, this.getVertexBufferObjectManager());
		btnPlay.setPosition(CAMERA_WIDTH-100, 50);
		//btnPlay.setScale(2);
		pauseGame.addMenuItem(btnPlay);
		
		pauseGame.setBackgroundEnabled(false);
		pauseGame.setOnMenuItemClickListener(this);
		return  pauseGame;
	}
	
	@Override
	public boolean onMenuItemClicked(MenuScene arg0, IMenuItem arg1,
			float arg2, float arg3) {
		switch(arg1.getID()){
		case 1:
			if(scene.hasChildScene()){
				scene.clearChildScene();
				pauseBtn.setVisible(true);
				
			}
			return true;
		default:
			return false;
		}
	}

	public Sprite getHero()
	{
		return hero;
	}
	public Scene getScene() {
		return scene;
	}
	
	public List<Sprite> getEnemies() {
		return enemies;
	}

	public List<Rectangle> getBullets() {
		return bullets;
	}

	public boolean isFiring() {
		return firing;
	}

	public void setFiring(boolean firing) {
		this.firing = firing;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Rectangle getLifeBarRectangle() {
		return lifeBarRectangle;
	}

	public ITextureRegion getEnemy1TextureRegion() {
		return enemy1TextureRegion;
	}

	public ITextureRegion getEnemy2TextureRegion() {
		return enemy2TextureRegion;
	}

	public ITextureRegion getEnemy3TextureRegion() {
		return enemy3TextureRegion;
	}
}
