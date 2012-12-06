package dk.itu.mario.level.generator.bio;

import java.io.Serializable;

import dk.itu.mario.engine.sprites.SpriteTemplate;

public class Chunk implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6143312258785923056L;

	private int id;
	private byte[][] map;
	private SpriteTemplate[][] sprites;
	
	/**
	 * value - the estimated arousal level of this chunk.
	 */
	private float value = 0f;
	private int type = 0;
	
	public Chunk(int id, int width, int height) {
		this.id = id;
		map = new byte[width][height];
		sprites = new SpriteTemplate[width][height];
	}
	
	public byte[][] getMap() {
		return map;
	}
	
	public SpriteTemplate[][] getSprites() {
		return sprites;
	}
	
	public int getId() {
		return id;
	}
	
	public float getValue() {
		return value;
	}
	
	public void setValue(float value) {
		if (value < 0f)
			value = 0f;
		if (value > 1f)
			value = 1f;
		this.value = value;
	}
	
	public void draw(BioLevel level, int x, int y) {
		for (int i = 0; i < map.length; ++i)
			for (int o = 0; o < map[0].length; ++o) {
				level.setBlock(x+i, y+o, map[i][o]);
				if (sprites[i][o] != null)
					level.setSpriteTemplate(x+i, y+o, sprites[i][o].clone());
			}
	}
	
	public int getWidth() {
		return map.length;
	}
	
	public int getHeight() {
		return map[0].length;
	}
}
