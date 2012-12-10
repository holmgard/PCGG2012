package dk.itu.mario.level.generator.bio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import dk.itu.mario.engine.sprites.SpriteTemplate;
import dk.itu.mario.level.Level;

public class BioLevel extends Level {
	
	private List<ScreenChunkWrapper> chunkLevel;
	private ScreenChunkWrapper[] chunkPosition;
	public static final int HEIGHT = 15;

	public BioLevel(int width) {
		width = generateLevel(width);
		
		this.width = width;
        this.height = HEIGHT;

        //xExit = width-1; // fix
        //yExit = 10;  // fix
        map = new byte[width][HEIGHT];
        spriteTemplates = new SpriteTemplate[width][HEIGHT];
        
        drawLevel();
        
        findExit();
	}
	
	//TODO: constructor from file
	public BioLevel(File file) {
		ArrayList<Integer> chunkLevelId = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			chunkLevelId = (ArrayList<Integer>) ois.readObject();
			ois.close();
			fis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//throw e;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//throw e;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//throw e;
		}
		
		chunkLevel = new ArrayList<ScreenChunkWrapper>();
		int x = 0;
		for (Integer id : chunkLevelId) {
			ScreenChunk sc = ScreenChunkLibrary.getInstance().getChunk(id);
			chunkLevel.add(new ScreenChunkWrapper(x, sc));
			x += sc.getWidth();
		}
		
		this.width = x;
		this.height = HEIGHT;
		
		/*try {
			FileReader fr = new FileReader(file);
			BufferedReader in = new BufferedReader(fr);
			
			chunkLevel = new ArrayList<ScreenChunkWrapper>();
			
			int c = in.read();
			int x = 0;
			while (c != -1) {
				ScreenChunk sc = ScreenChunkLibrary.getInstance().getChunk(c);
				chunkLevel.add(new ScreenChunkWrapper(x, sc));
				x += sc.getWidth();
			}
			
			this.width = x;
			this.height = HEIGHT;
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		map = new byte[width][HEIGHT];
        spriteTemplates = new SpriteTemplate[width][HEIGHT];
        
        drawLevel();
        
        findExit();
	}
	
	public void saveLevel(File file) {
		ArrayList<Integer> chunkLevelId = new ArrayList<Integer>();
		for (ScreenChunkWrapper scw : chunkLevel) {
			chunkLevelId.add(scw.sc.getId());
		}
		
		try {
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(chunkLevelId);
			oos.close();
			fos.close();
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
			//throw e;
		}
		/*try {
			FileWriter fw = new FileWriter(file);
			BufferedWriter out = new BufferedWriter(fw);
			
			for (int i = 0; i < chunkLevel.size(); ++i) {
				ScreenChunkWrapper scw = chunkLevel.get(i);
				out.write(scw.sc.getId());
			}
			
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}
	
	private int generateLevel(int width) {
		chunkLevel = new ArrayList<ScreenChunkWrapper>();
		ArrayList<ScreenChunkWrapper> chunkInd = new ArrayList<ScreenChunkWrapper>();
		ScreenChunkLibrary scl = ScreenChunkLibrary.getInstance();
		
		BioCurve curve = new BioCurve();
		
		scl.calcSearchTree();
		
		int x = 0;
		ScreenChunk lastsc = scl.getChunk(curve.curve(0), null); //TODO: get arousal from curve(x)
		ScreenChunkWrapper w = new ScreenChunkWrapper(x, lastsc);
		chunkLevel.add(w);
		x += lastsc.getWidth();
		for (int i = 0; i < x; ++i)
			chunkInd.add(w);
		
		while (x < width) {
			ScreenChunk sc = scl.getChunk(curve.curve((float)x / width), lastsc.getOutWindows()); //TODO: get arousal from curve(x)
			w = new ScreenChunkWrapper(x, sc);
			chunkLevel.add(w);
			x += sc.getWidth();
			lastsc = sc;
			for (int i = 0; i < sc.getWidth(); ++i)
				chunkInd.add(w);
		}
		
		chunkPosition = new ScreenChunkWrapper[chunkInd.size()];
		chunkPosition = chunkInd.toArray(chunkPosition);
		
		return x;
	}
	
	// the current screen chunk where mario is
	public ScreenChunk getChunk(int x) {
		return chunkPosition[x].sc;
	}
	
	// the local x position for the current screen chunk
	public int getLocalX (int x) {
		return x - chunkPosition[x].x;
	}
	
	private void drawLevel() {
		int x = 0;
		for (ScreenChunkWrapper sc : chunkLevel) {
			sc.sc.draw(this, x);
			x += sc.sc.getWidth();
		}
	}
	
	private void findExit() {
		xExit = width - 6;
		yExit = height - 1;
		
		while (map[xExit][yExit] != 0)
			--yExit;
		
		++yExit;
	}
	
	public List<ScreenChunk> getScreenChunks() {
		ArrayList<ScreenChunk> list = new ArrayList<ScreenChunk>();
		
		for (ScreenChunkWrapper scw : chunkLevel) {
			list.add(scw.sc);
		}
		
		return list;
	}
}

class ScreenChunkWrapper {
	int x;
	ScreenChunk sc;
	
	public ScreenChunkWrapper(int x, ScreenChunk sc) {
		this.x = x;
		this.sc = sc;
	}
}

class BioCurve {
	float[] points;
	
	public BioCurve() {
		// hard-coded curve
		points = new float[5];
		points[0] = 0.2f;
		points[1] = 0.2f;
		points[2] = 0.5f;
		points[3] = 0.3f;
		points[4] = 1.0f;
		
		/*for (int i = 0; i < 20; ++i) {
			System.out.println(curve(i/20.0f));
		}*/
	}
	
	public float curve(float x) {
		if (x < 0f || x > 1f)
			return 0f;
		/*
		//ugly interpolation based on hard coded values
		if (x < 0.25f) {
			float a = x / 0.25f;
			return points[0] + (points[1] - points[0]) * a;
		} else if (x < 0.5f) {
			float a = (x - 0.25f) / 0.25f;
			return points[1] + (points[2] - points[1]) * a;
		} else if (x < 0.75f) {
			float a = (x - 0.5f) / 0.25f;
			return points[2] + (points[3] - points[2]) * a;
		} else {
			float a = (x - 0.75f) / 0.25f;
			return points[3] + (points[4] - points[3]) * a;
		}*/
		
		float s = 1f / (points.length - 1);
		int p1 = points.length - 1;
		int p0 = p1 - 1;
		
		int i = 1;
		while (x < 1f - i * s) {
			--p1;
			--p0;
			++i;
		}
		
		--i;
		float a = (x - (1f - i * s)) / s;
		
		return points[p0] + (points[p1] - points[p0] * a);
	}
}
