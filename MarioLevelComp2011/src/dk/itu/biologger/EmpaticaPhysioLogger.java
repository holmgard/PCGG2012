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

public class EmpaticaPhysioLogger {
	
	private StringBuilder log;
	EmpaticaHandler biologger;
	BioLevel level;
	
	ArrayList<InterpolatedSample> interPhasic;
	ArrayList<InterpolatedSample> interTonic;
	ArrayList<InterpolatedSample> interBVP;
	
	/*
	 * Going to put in some data structure for mapping mario frames to interpolated physio data
	 * Need distance from last measurement in frames
	 * What do we do about excess data?
	 * We average any overlapping values?
	 */
	
	
	public EmpaticaPhysioLogger() {
		log = new StringBuilder();
		log.append("MarioTime\tPhasicTime\tPhasicChannel\tPhasicData\tTonicTime\tTonicChannel\tDataTonic\tBVPTime\tBVPChannel\tBVPData\n");
		interPhasic = new ArrayList<InterpolatedSample>();
		interTonic = new ArrayList<InterpolatedSample>();
		interBVP = new ArrayList<InterpolatedSample>();
	}

	public void initBioLogging(){
		
		biologger = new EmpaticaHandler("localhost",49301);
		//biologger = new EmpaticaHandler(true);
    	//biologger.autoConnect();
		biologger.connect();
    	biologger.scan();
    	biologger.open();
    	biologger.startReading();
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
    	ArrayList<EmpaticaSample> latestPhasic = biologger.getEmpaticaReader().getLatestPhasic();
    	ArrayList<EmpaticaSample> latestTonic = biologger.getEmpaticaReader().getLatestTonic();
    	ArrayList<EmpaticaSample> latestBVP = biologger.getEmpaticaReader().getLatestBvp();
    	ArrayList<EmpaticaSample>[] lists = new ArrayList[3];
    	lists[0] = latestPhasic;
    	lists[1] = latestTonic;
    	lists[2] = latestBVP;
    	
    	ArrayList<EmpaticaSample> longestList = latestPhasic;
    	if(latestTonic.size() > longestList.size())
    		longestList = latestTonic;
    	if(latestBVP.size() > longestList.size())
    		longestList = latestBVP;
    	
        for(int i = 0; i < longestList.size(); i++)
        {
        	String logString = "";
        	for(int j = 0; j < lists.length; j++)
        	{
        		logString += "\t";
        		if(lists[j] != null && lists[j].size() > i && lists[j].size() > 0)
        			logString += lists[j].get(i).toString();
        		else
        			logString += "NOTIME\tNOCHAN\tNOSIGN";
        	}
        	this.Tick( timeString, logString );
        }
        
        if(t >= 0)
        {
        	if(latestPhasic.size() > 0)
        		saveToMemory(t,latestPhasic,interPhasic,marioX);
        	if(latestTonic.size() > 0)
        		saveToMemory(t,latestTonic,interTonic,marioX);
        	if(latestBVP.size() > 0)
        		saveToMemory(t,latestBVP,interBVP,marioX);
        }
        if(interPhasic != null && interPhasic.size() > 0)
        	System.out.println(interPhasic.get(interPhasic.size()-1).getValue() + " " + interBVP.get(interBVP.size() -1).getValue());
    }
	
	public void saveToMemory(int readingFrame, ArrayList<EmpaticaSample> samples, ArrayList<InterpolatedSample> targetList, float marioX)
	{		
			for(EmpaticaSample sample : samples)
			{
				targetList.add(new InterpolatedSample(sample.sampleType, readingFrame, sample.sampleValue, marioX));
			}
	}
	
	public void stop() {
		biologger.stop();
        biologger.close();
        biologger.disconnect();
        this.analyzeSignal();
	}
	
	public void setLevel(BioLevel level)
	{
		this.level = level;
	}
	
	public void analyzeSignal()
	{
		PhysioAnalyzer analyzer = new PhysioAnalyzer(interPhasic);
		analyzer.analyze();
		analyzer.smoothSamples();
		analyzer.saveSamples();
		List<ScreenChunkWrapper> scwl = level.getChunkLevel();
		analyzer.saveChunkData(scwl);
	}
}
