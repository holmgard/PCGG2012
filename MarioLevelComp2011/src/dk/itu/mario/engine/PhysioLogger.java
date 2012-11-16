package dk.itu.mario.engine;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import dk.itu.mario.scene.LevelScene;

public class PhysioLogger {
	
	private StringBuilder log;
		
	public PhysioLogger() {
		log = new StringBuilder();
		log.append("MarioTime\tBioTime\tChannelID\tData\n");
	}
	
	public void Tick(String timeStampMario, String bioData) {
		log.append(timeStampMario + "\t" + bioData + "\n");
	}
	
	public void write(String filename) {
		try {
			FileWriter file = new FileWriter(new File("PhysioLog_" + filename + ".txt"));
			file.write(log.toString());
			file.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
}
