package dk.itu.mario.level.generator.bio;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import dk.itu.mario.engine.sprites.SpriteTemplate;

public class ScreenChunk implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7329714787742763936L;
	private static ChunkLibrary chunkLibrary = ChunkLibrary.getInstance();
	
	private int width;
	private static byte height = 15;
	
	private List<ChunkWrapper> chunks; 
	
	private float value = 0f;
	
	private List<byte[]> windowsIn;
	private List<byte[]> windowsOut;
	
	private int id;
	
	public ScreenChunk(int id, int width) {
		this.id = id;
		this.width = width;
		chunks = new ArrayList<ChunkWrapper>();
		windowsIn = new ArrayList<byte[]>();
		windowsOut = new ArrayList<byte[]>();
		
		//if (chunkLibrary == null)
		//	chunkLibrary = ChunkLibrary.getInstance();
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
	
	public float getTotalValue() {
		float v = 0f;
		int count = 0;
		
		for (ChunkWrapper cw : chunks) {
			Chunk c = chunkLibrary.getChunk(cw.id);
			if (c != null) {
				v += c.getValue();
				++count;
			}
		}
		
		v /= count;
		v += value;
		v /= 2f; // normalize to [0f,1f] 
		return v;
	}
	
	public void addChunk(int id, int x, int y) {
		chunks.add(new ChunkWrapper(id, x, y));
	}
	
	public void removeChunk(int id, int x, int y) {
		int i;
		
		for (i = 0; i < chunks.size(); ++i) {
			ChunkWrapper cw = chunks.get(i);
			if ((cw.id == id) && (cw.x == x) && (cw.y == y))
				break;
		}
		
		chunks.remove(i);
	}
	
	public int addInWindow(byte startY, byte endY) {
		byte[] window = new byte[2];
		window[0] = startY;
		window[1] = endY;
		windowsIn.add(window);
		return windowsIn.indexOf(window);
	}
	
	public void removeInWindow(int window) {
		windowsIn.remove(window);
	}
	
	public int addOutWindow(byte startY, byte endY) {
		byte[] window = new byte[2];
		window[0] = startY;
		window[1] = endY;
		windowsOut.add(window);
		return windowsOut.indexOf(window);
	}
	
	public void removeOutWindow(int window) {
		windowsOut.remove(window);
	}
	
	public List<byte[]> getInWindows() {
		return windowsIn;
	}
	
	public List<byte[]> getOutWindows() {
		return windowsOut;
	}
	
	public int getId() {
		return id;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void draw(BioLevel level, int x) {
		for (ChunkWrapper cw : chunks) {
			Chunk c = chunkLibrary.getChunk(cw.id);
			c.draw(level, x + cw.x, cw.y);
		}
	}
	
	public void draw(byte[][] map, SpriteTemplate[][] sprites) {
		for (ChunkWrapper cw : chunks) {
			Chunk c = chunkLibrary.getChunk(cw.id);
			c.draw(map, sprites, cw.x, cw.y);
		}
	}
	
	// Let screen chunk update chunks
	// TODO: Use float precision for overlap check
	public void updateChunksValue(int x, int y, float v) {
		for (ChunkWrapper cw : chunks) {
			if (chunkOverlap(x, y, cw))
				;//TODO: Update chunk value
		}
	}
	
	private boolean chunkOverlap(int x, int y, ChunkWrapper cw) {
		return false;
	}
	
	public List<Chunk> getChunks(List<Point> coords) {
		ArrayList<Chunk> c = new ArrayList<Chunk>();
		
		if (coords != null)
			coords.clear();
		
		for (ChunkWrapper cw : chunks) {
			c.add(chunkLibrary.getChunk(cw.id));
			if (coords != null)
				coords.add(new Point(cw.x, cw.y));
		}
		
		return c;
	}
}

class ChunkWrapper implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3077570803319396699L;
	
	int x;
	int y;
	int id;
	
	public ChunkWrapper(int id, int x, int y) {
		this.id = id;
		this.x = x;
		this.y = y;
	}
}