package org.queops.game.entities;

import java.util.ArrayList;
import java.util.List;

import org.andengine.audio.sound.Sound;
import org.andengine.entity.particle.SpriteParticleSystem;
import org.andengine.entity.particle.emitter.CircleParticleEmitter;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.queops.MainActivity;

public class GameEntity {

	// ===========================================================
	// Constants
	// ===========================================================

	public int CAMERA_WIDTH = 640;//480;
	public int CAMERA_HEIGHT = 384;//288;
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
	private ShaderProgram pVertexBufferObjectManager;
	private Scene scene;
	//Textures
	private ITextureRegion enemy1TextureRegion;
	private ITextureRegion enemy2TextureRegion;
	private ITextureRegion enemy3TextureRegion;
	private ITiledTextureRegion fireButtonTextureRegion;
	private BitmapTextureAtlas mOnScreenControlTexture;
	private ITextureRegion mOnScreenControlBaseTextureRegion;
	private ITextureRegion mOnScreenControlKnobTextureRegion;
	private TiledTextureRegion gameOverTextureRegion;
	//Hero moving vars
	private float[] bounds;
	protected float newY = 0;
	protected float newX = 0;
	private float newRotation;
	private float antigoTouchX = -1f;
	private float antigoTouchY = -1f;

	protected float destinationX;
	protected float destinationY;
	private boolean pitchSpeed = false;

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
	private ITextureRegion nullPixelTexture;
	private boolean analogControlsEnabled;
	private boolean firing;
	private CircleParticleEmitter bulletsEmitter;
	private SpriteParticleSystem bulletsParticleSystem;
	private Sprite fireBtn;
	private IFont mFont;
	private Integer score = new Integer(1);
	private Text placarText;

	private Sound mExplosionSound;
	
	public GameEntity()
	{
		setBounds(new float[] { 1, 1, CAMERA_WIDTH - 32, CAMERA_HEIGHT - 32 });
	}
	public float[] getBounds() {
		return bounds;
	}
	public void setBounds(float[] bounds) {
		this.bounds  = new float[] { 1, 1, CAMERA_WIDTH - 32, CAMERA_HEIGHT - 32 };
	}

}
