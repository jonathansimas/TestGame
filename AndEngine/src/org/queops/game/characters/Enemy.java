package org.queops.game.characters;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.math.MathUtils;

public class Enemy extends Sprite {
	
	private static float pY = 0;
	private static float pX;
	private static VertexBufferObjectManager pHeight;
	private static ITextureRegion pWidth;
	private static ShaderProgram pVertexBufferObjectManager;
	private static DrawType pTextureRegion;

	public Enemy()
	{
		super(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager);
	}

	public Enemy(float pX, float pY, float pWidth, float pHeight,
			ITextureRegion pTextureRegion,VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager);
		// TODO Auto-generated constructor stub
	}
	
	public void destroySelf()
	{
		
	}
	
	public Enemy createRandomEnemy(ITextureRegion[] texturas, VertexBufferObjectManager pVertexBufferObjectManager)
	{
		ITextureRegion inimigo;
		switch (MathUtils.RANDOM.nextInt(2)) {
		case 0:
			inimigo = texturas[0];
			break;
		case 1:
			inimigo = texturas[1];
			break;
		case 2:
			inimigo = texturas[2];
			break;
		default:
			inimigo = texturas[0];
			break;
		}
		
		final Sprite newEnemy = new Sprite(0, 0, inimigo, pVertexBufferObjectManager);	
		
		newEnemy.setRotation(90);
		return new Enemy();
	}

}
