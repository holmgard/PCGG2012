package dk.itu.mario.level.generator.bio.editors;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.Scrollable;

import dk.itu.mario.engine.Art;
import dk.itu.mario.engine.sprites.SpriteTemplate;

public class LevelDrawer extends JComponent implements Scrollable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7556305882239741186L;
	
	
	public static final byte BLOCK_EMPTY	= (byte) (0 + 1 * 16);
	public static final byte BLOCK_POWERUP	= (byte) (4 + 2 + 1 * 16);
	public static final byte BLOCK_COIN	= (byte) (4 + 1 + 1 * 16);
	public static final byte GROUND		= (byte) (1 + 9 * 16);
	public static final byte ROCK			= (byte) (9 + 0 * 16);
	public static final byte COIN			= (byte) (2 + 2 * 16);


	public static final byte LEFT_GRASS_EDGE = (byte) (0+9*16);
	public static final byte RIGHT_GRASS_EDGE = (byte) (2+9*16);
	public static final byte RIGHT_UP_GRASS_EDGE = (byte) (2+8*16);
	public static final byte LEFT_UP_GRASS_EDGE = (byte) (0+8*16);
	public static final byte LEFT_POCKET_GRASS = (byte) (3+9*16);
	public static final byte RIGHT_POCKET_GRASS = (byte) (3+8*16);

	public static final byte HILL_FILL = (byte) (5 + 9 * 16);
	public static final byte HILL_LEFT = (byte) (4 + 9 * 16);
	public static final byte HILL_RIGHT = (byte) (6 + 9 * 16);
	public static final byte HILL_TOP = (byte) (5 + 8 * 16);
	public static final byte HILL_TOP_LEFT = (byte) (4 + 8 * 16);
	public static final byte HILL_TOP_RIGHT = (byte) (6 + 8 * 16);

	public static final byte HILL_TOP_LEFT_IN = (byte) (4 + 11 * 16);
	public static final byte HILL_TOP_RIGHT_IN = (byte) (6 + 11 * 16);

	public static final byte TUBE_TOP_LEFT = (byte) (10 + 0 * 16);
	public static final byte TUBE_TOP_RIGHT = (byte) (11 + 0 * 16);

	public static final byte TUBE_SIDE_LEFT = (byte) (10 + 1 * 16);
	public static final byte TUBE_SIDE_RIGHT = (byte) (11 + 1 * 16);
	
	private byte[][] map = null;
	private SpriteTemplate[][] sprites = null;
	
	public LevelDrawer() {
		super();
		
		this.setPreferredSize(new Dimension(24*16, 15*16));
		
		//Art.init(getGraphicsConfiguration(), null);
	}
	
	public void init() {
		Art.init(getGraphicsConfiguration(), null);
	}

	protected void paintComponent(Graphics g) {
		// Clear component
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		/*// Draw chunk
		if (Art.level != null) { // Test to allow Window Builder to work in design mode
			int b = ((byte) (5 + 8 * 16)) & 0xff;
			g.drawImage(Art.level[b % 16][b / 16], 0, 0, null);
		}*/
		
		if (Art.level != null) { // Test to allow Window Builder to work in design mode
			// Draw map
			if (map != null) {
				for (int x = 0; x < map.length; ++x) {
					for (int y = 0; y < map[0].length; ++y) {
						int b = map[x][y] & 0xff;
						g.drawImage(Art.level[b % 16][b / 16], x * 16, y * 16, null);
						
						if (map[x][y] == BLOCK_POWERUP) {
							g.setColor(Color.BLUE);
							g.drawString("P", x * 16 + 4, y * 16 + 12);
						}
						
						if (map[x][y] == BLOCK_COIN) {
							g.setColor(Color.RED);
							g.drawString("C", x * 16 + 4, y * 16 + 12);
						}
					}
				}
			}

			// Draw sprites
			if (sprites != null) {
				for (int x = 0; x < sprites.length; ++x) {
					for (int y = 0; y < sprites[0].length; ++y) {
						SpriteTemplate sprite = sprites[x][y];
						if (sprite != null) {
							g.drawImage(Art.enemies[0][getSpriteIndex(sprite.type)], x * 16, y * 16 - 16, null);
							if (sprite.winged)
								g.drawImage(Art.enemies[0][4], x * 16, y * 16 - 16, null);
						}
					}
				}
			}

		}
		
		// Draw grid
		g.setColor(Color.BLACK);
		for (int y = 0; y < this.getHeight(); y += 16)
			g.drawLine(0, y, this.getWidth(), y);
		for (int x = 0; x < this.getWidth(); x += 16)
			g.drawLine(x, 0, x, this.getHeight());
	}
	
	private int getSpriteIndex(int type) {
		switch (type) {
		case 0: return 0;
		case 1: return 1;
		case 2: return 2;
		case 3: return 3;
		case 4: return 6;
		case 5: return 5;
		case 6: return 6;
		default: return 0;
		}
	}
	
	public void setMap(byte[][] map) {
		this.map = map;
	}
	
	public void setSprites(SpriteTemplate[][] sprites) {
		this.sprites = sprites;
	}

	public Dimension getPreferredScrollableViewportSize() {
		return this.getPreferredSize();
		//return new Dimension(24*16, 15*16);
	}

	public int getScrollableBlockIncrement(Rectangle arg0, int arg1, int arg2) {
		return 16;
	}

	public boolean getScrollableTracksViewportHeight() {
		return false;
	}

	public boolean getScrollableTracksViewportWidth() {
		return false;
	}

	public int getScrollableUnitIncrement(Rectangle arg0, int arg1, int arg2) {
		return 8;
	}
}
