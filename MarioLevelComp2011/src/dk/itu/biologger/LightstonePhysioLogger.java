package dk.itu.biologger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dk.itu.mario.level.generator.bio.BioLevel;
import dk.itu.mario.level.generator.bio.ScreenChunkWrapper;
import dk.itu.mario.scene.LevelScene;
import dk.itu.mario.scene.Scene; 

public class LightstonePhysioLogger {
	
	private StringBuilder log;
	LightstoneWrapper biologger;
	BioLevel level;
	
	ArrayList<InterpolatedSample> interPhasic;
	ArrayList<InterpolatedSample> interTonic;
	ArrayList<InterpolatedSample> interBVP;
	ArrayList<LightstoneInterpolatedSample> interSamples;
	
	/*
	 * Going to put in some data structure for mapping mario frames to interpolated physio data
	 * Need distance from last measurement in frames
	 * What do we do about excess data?
	 * We average any overlapping values?
	 */
	
	
	public LightstonePhysioLogger() {
		log = new StringBuilder();
		log.append("MarioTime\tPhasicTime\tPhasicChannel\tPhasicData\tTonicTime\tTonicChannel\tDataTonic\tBVPTime\tBVPChannel\tBVPData\n");
		interPhasic = new ArrayList<InterpolatedSample>();
		interTonic = new ArrayList<InterpolatedSample>();
		interBVP = new ArrayList<InterpolatedSample>();
		interSamples = new ArrayList<LightstoneInterpolatedSample>();
	}

	Thread bioThread;
	public void initBioLogging(){
		biologger = new LightstoneWrapper(this); //Registering for callback
		bioThread = new Thread(biologger);
		bioThread.start();
		//biologger.run();
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
	
	public void logBio(int t, String timeString, float marioX){
    	ArrayList<LightstoneSample> latestSamples = biologger.getLatestSamples();
    	
    	if(latestSamples.isEmpty())
    		return;
    	
    	
        for(int i = 0; i < latestSamples.size(); i++)
        {
        	String logString = "\t";
            logString += latestSamples.get(i).toString();
        	this.Tick( timeString, logString );
        }
        
        if(t >= 0 && latestSamples.size() > 0)
        {
        	saveToMemory(t,latestSamples,interSamples,marioX);
        }
    }
	
	
	public void saveToMemory(int readingFrame, ArrayList<LightstoneSample> samples, ArrayList<LightstoneInterpolatedSample> targetList, float marioX)
	{		
			for(LightstoneSample sample : samples)
			{
				targetList.add(new LightstoneInterpolatedSample(-1, readingFrame, (float)sample.scl, (float)sample.hrv, marioX));
			}
	}
	
	public void stop() {
		bioThread.interrupt();
		biologger.stop();
		this.analyzeSignal();
	}
	
	public void setLevel(BioLevel level)
	{
		this.level = level;
	}
	
	public void analyzeSignal()
	{
		LightstonePhysioAnalyzer analyzer = new LightstonePhysioAnalyzer(interSamples);
		analyzer.analyze();
		analyzer.smoothSamples();
		analyzer.saveSamples();
		List<ScreenChunkWrapper> scwl = level.getChunkLevel();
		analyzer.saveChunkData(scwl);
	}
}
