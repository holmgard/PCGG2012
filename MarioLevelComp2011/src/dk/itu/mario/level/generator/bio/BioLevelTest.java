package dk.itu.mario.level.generator.bio;

import java.io.File;

import dk.itu.mario.engine.sprites.SpriteTemplate;

public class BioLevelTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Test01();
		//Test02();
		//Test03();
		//Test04();
	}

	// Test chunks are loaded with correct id
	private static void Test04() {
		ChunkLibrary cl = ChunkLibrary.getInstance();
		ScreenChunkLibrary scl = ScreenChunkLibrary.getInstance();
		
		try {
			cl.readLibFromFile(new File("chunks.res"));
			scl.readLibFromFile(new File("screenchunks.res"));
			System.out.println("Chunk Libs Read");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Chunk chunk = cl.getChunk(0);
		System.out.println("Chunk 0");
		if (chunk != null) {
			System.out.println("Chunk Id: " + chunk.getId());
		}
		
		chunk = cl.getChunk(1);
		System.out.println("Chunk 1");
		if (chunk != null) {
			System.out.println("Chunk Id: " + chunk.getId());
		}
		
		chunk = cl.getChunk(2);
		System.out.println("Chunk 2");
		if (chunk != null) {
			System.out.println("Chunk Id: " + chunk.getId());
		}
		
		chunk = cl.getChunk(3);
		System.out.println("Chunk 3");
		if (chunk != null) {
			System.out.println("Chunk Id: " + chunk.getId());
		}
		
		ScreenChunk screenChunk = scl.getChunk(0);
		System.out.println("Screen Chunk 0");
		if (screenChunk != null) {
			System.out.println("Screen Chunk ID: " + screenChunk.getId());
		}
		
		screenChunk = scl.getChunk(1);
		System.out.println("Screen Chunk 1");
		if (screenChunk != null) {
			System.out.println("Screen Chunk ID: " + screenChunk.getId());
		}
		
		screenChunk = scl.getChunk(2);
		System.out.println("Screen Chunk 2");
		if (screenChunk != null) {
			System.out.println("Screen Chunk ID: " + screenChunk.getId());
		}
		
		screenChunk = scl.getChunk(3);
		System.out.println("Screen Chunk 3");
		if (screenChunk != null) {
			System.out.println("Screen Chunk ID: " + screenChunk.getId());
		}
		
		screenChunk = scl.getChunk(4);
		System.out.println("Screen Chunk 4");
		if (screenChunk != null) {
			System.out.println("Screen Chunk ID: " + screenChunk.getId());
		}
	}

	// Create a level save it, reload it, and save it again
	private static void Test03() {
		ChunkLibrary cl = ChunkLibrary.getInstance();
		ScreenChunkLibrary scl = ScreenChunkLibrary.getInstance();
		
		try {
			cl.readLibFromFile(new File("chunks.res"));
			scl.readLibFromFile(new File("screenchunks.res"));
			System.out.println("Chunk Libs Read");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		scl.calcSearchTree();
		BioLevel gen = new BioLevel(320,false);
		gen.saveLevel(new File("gen.res"));
		System.out.println("Gen Saved");
		
		BioLevel load = new BioLevel(new File("gen.res"));
		System.out.println("Gen Loaded");
		
		load.saveLevel(new File("load.res"));
		System.out.println("Load Saved");
	}

	// Create Screen chunks
	private static void Test02() {
		ChunkLibrary cl = ChunkLibrary.getInstance();
		ScreenChunkLibrary scl = ScreenChunkLibrary.getInstance();
		
		try {
			cl.readLibFromFile(new File("chunks.res"));
			System.out.println("Chunks Read");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ScreenChunk plane = new ScreenChunk(scl.getNextId(), (byte) 15);
		plane.addChunk(0, 0, 14);
		plane.addChunk(0, 5, 14);
		plane.addChunk(0, 10, 14);
		plane.addInWindow((byte)0, (byte)14);
		plane.addOutWindow((byte)0, (byte)13);
		plane.setValue(0.2f);
		scl.addChunk(plane);
		System.out.println("Plane Screen Chunk Added");
		
		ScreenChunk gaps = new ScreenChunk(scl.getNextId(), (byte) 20);
		gaps.addChunk(0, 0, 14);
		gaps.addChunk(1, 5, 14);
		gaps.addChunk(0, 10, 14);
		gaps.addChunk(1, 15, 14);
		gaps.addInWindow((byte)0, (byte)14);
		gaps.addOutWindow((byte)0, (byte)13);
		gaps.setValue(0.5f);
		scl.addChunk(gaps);
		System.out.println("Gaps Screen Chunk Added");
		
		ScreenChunk enemy = new ScreenChunk(scl.getNextId(), (byte) 13);
		enemy.addChunk(0, 0, 14);
		enemy.addChunk(2, 5, 13);
		enemy.addChunk(0, 8, 14);
		enemy.addInWindow((byte)0, (byte)14);
		enemy.addOutWindow((byte)0, (byte)13);
		enemy.setValue(0.5f);
		scl.addChunk(enemy);
		System.out.println("Enemy Screen Chunk Added");
		
		ScreenChunk gapEnemy = new ScreenChunk(scl.getNextId(), (byte) 13);
		gapEnemy.addChunk(1, 0, 14);
		gapEnemy.addChunk(2, 5, 13);
		gapEnemy.addChunk(1, 8, 14);
		gapEnemy.addInWindow((byte)0, (byte)14);
		gapEnemy.addOutWindow((byte)0, (byte)13);
		gapEnemy.setValue(0.8f);
		scl.addChunk(gapEnemy);
		System.out.println("Gap Enemy Screen Chunk Added");
		
		try {
			scl.writeLibToFile(new File("screenchunks.res"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Screen Chunks Saved");
	}

	// Create chunks
	private static void Test01() {
		ChunkLibrary cl = ChunkLibrary.getInstance();
		
		Chunk ground = new Chunk(cl.getNextId(), 5, 1);
		
		byte[][] map = ground.getMap();
		map[0][0] = (byte) (5 + 8 * 16);
		map[1][0] = (byte) (5 + 8 * 16);
		map[2][0] = (byte) (5 + 8 * 16);
		map[3][0] = (byte) (5 + 8 * 16);
		map[4][0] = (byte) (5 + 8 * 16);
		
		SpriteTemplate[][] sprites = ground.getSprites();
		// nothing
		
		cl.addChunk(ground);
		System.out.println("Ground Chunk Added");
		
		
		Chunk gap = new Chunk(cl.getNextId(), 5, 1);
		
		map = gap.getMap();
		map[0][0] = (byte) (4 + 8 * 16);
		map[1][0] = (byte) 0;
		map[2][0] = (byte) 0;
		map[3][0] = (byte) 0;
		map[4][0] = (byte) (6 + 8 * 16);
		
		cl.addChunk(gap);
		System.out.println("Gap Chunk Added");
		
		
		Chunk enemy = new Chunk(cl.getNextId(), 3, 2);
		
		map = enemy.getMap();
		map[0][1] = (byte) (4 + 8 * 16);
		map[1][1] = (byte) (5 + 8 * 16);
		map[2][1] = (byte) (6 + 8 * 16);
		
		sprites = enemy.getSprites();
		sprites[1][0] = new SpriteTemplate(SpriteTemplate.GREEN_TURTLE, false);
		
		cl.addChunk(enemy);
		System.out.println("Enemy Chunk Added");
		
		try {
			cl.writeLibToFile(new File("chunks.res"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Chunks saved");
	}

}
