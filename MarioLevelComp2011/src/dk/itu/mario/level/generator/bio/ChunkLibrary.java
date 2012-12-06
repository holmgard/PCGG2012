package dk.itu.mario.level.generator.bio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class ChunkLibrary {
	
	private static ChunkLibrary instance = null;
	
	
	private HashMap<Integer, Chunk> lib;
	private int nextId;
	
	private ChunkLibrary() {
		lib = new HashMap<Integer, Chunk>();
		nextId = 0;
	}
	
	public static ChunkLibrary getInstance() {
		if (instance == null)
			instance = new ChunkLibrary();
		
		return instance;
	}
	
	public boolean addChunk(Chunk chunk) {
		if (lib.containsKey(chunk.getId()))
			return false;
		
		lib.put(chunk.getId(), chunk);
		
		return true;
	}
	
	public void removeChunk(int id) {
		lib.remove(id);
	}
	
	public void removeChunk(Chunk chunk) {
		removeChunk(chunk.getId());
	}
	
	public int getNextId() {
		while (lib.containsKey(nextId))
			++nextId;
		
		return nextId;
	}
	
	/**
	 * Returns a chunk. 
	 * @param id the id of the chunck.
	 * @return
	 */
	public Chunk getChunk(int id) {
		return lib.get(id);
	}
	
	@SuppressWarnings("unchecked")
	public void readLibFromFile(File file) throws Exception {
		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			lib = (HashMap<Integer, Chunk>) ois.readObject();
			ois.close();
			fis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}
	
	public void writeLibToFile(File file) throws Exception {
		try {
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(lib);
			oos.close();
			fos.close();
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
			throw e;
		}
	}
	
	public int getNumOfChunks() {
		return lib.size();
	}
}
