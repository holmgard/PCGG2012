package dk.itu.mario.level.generator.bio;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import dk.itu.biologger.EmpaticaPhysioLogger;
import dk.itu.biologger.LightstonePhysioLogger;
import dk.itu.mario.MarioInterface.GamePlay;
import dk.itu.mario.MarioInterface.LevelGenerator;
import dk.itu.mario.MarioInterface.LevelInterface;

public class BioLevelGenerator implements LevelGenerator {

	//EmpaticaPhysioLogger physLogger;
	LightstonePhysioLogger physLogger;
	
	/*public BioLevelGenerator(EmpaticaPhysioLogger physLogger)
	{
		this.physLogger = physLogger;
	}*/
	
	public BioLevelGenerator(LightstonePhysioLogger physLogger)
	{
		this.physLogger = physLogger;
	}
	
	public LevelInterface generateLevel(GamePlay playerMetrics) {
		ChunkLibrary cl = ChunkLibrary.getInstance();
		ScreenChunkLibrary scl = ScreenChunkLibrary.getInstance();
		
		try {
			cl.readLibFromFile(new File("chunkLibrary.res"));
			scl.readLibFromFile(new File("screenChunkLibrary.res"));
			System.out.println("Chunk Libs Read");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		scl.calcSearchTree();
		
		BioLevel level = new BioLevel(320*2,true);
		
		String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		level.saveLevel(new File(timestamp + "_level.res"));
		System.out.println("level Saved");
		
		/*BioLevel level = new BioLevel(new File("level.res"));
		System.out.println("Level loaded");*/
		
		physLogger.setLevel(level);
		
		return level;
	}

	public LevelInterface generateLevel(String detailedInfo) {
		LevelInterface level = new BioLevel(320,false);
		return level;
	}

}
