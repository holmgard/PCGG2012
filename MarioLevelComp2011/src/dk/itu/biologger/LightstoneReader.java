package dk.itu.biologger;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class LightstoneReader {
	
	String buffer = "";
	
	ArrayList<LightstoneSample> samples;
	
	public static LightstoneReader instance;
	
	public LightstoneReader(){
		samples = new ArrayList<LightstoneSample>();
	}
	
	public static LightstoneReader getInstance(){
		if(LightstoneReader.instance == null)
		{
			instance = new LightstoneReader();
			return instance;
		} else {
			return instance;
		}
	}
	
	public void addToBuffer(char toAdd){
		buffer += toAdd;
		scanBuffer();
	}
	
	public void scanBuffer(){
		int nextRaw = buffer.indexOf("<\\RAW>");
		if( nextRaw > -1){
			if(buffer.length() < 20){
				buffer = "";
			}
			else{	
				//System.out.println(buffer.length());
				String dataString = buffer.substring(nextRaw-9, nextRaw);
				String scl = dataString.substring(0, 4);
				String hrv = dataString.substring(5);
				
				int sclInt = Integer.parseInt(scl,16);
				int hrvInt = Integer.parseInt(hrv,16);
				
				System.out.println("SCL: " + sclInt + " HRV: " + hrvInt);
				buffer = "";
				
				samples.add(new LightstoneSample(System.currentTimeMillis(),sclInt, hrvInt));
			}
		} 
	}
	
	public ArrayList<LightstoneSample> getLatestSamples()
	{
		ArrayList<LightstoneSample> copy = new ArrayList<LightstoneSample>(samples);
		samples.clear();
		return copy;
	}
}
