package dk.itu.mario.engine;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import dk.itu.mario.scene.LevelScene;

public class PhysioLogger {
	
	private StringBuilder log;
	private LevelScene scene;
	
	private int LT;

	public PhysioLogger(LevelScene scene) {
		log = new StringBuilder();
		log.append("Time\tData\n");
		
		this.scene = scene;
		
		LT = LevelScene.LVLT * LevelScene.TPS;
	}
	
	public void Tick() {
		log.append(LT - scene.timeLeft + "\tSome data\n");
	}
	
	public void write() {
		try {
			FileWriter file = new FileWriter(new File("PhysioLog.txt"));
			file.write(log.toString());
			file.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
}
