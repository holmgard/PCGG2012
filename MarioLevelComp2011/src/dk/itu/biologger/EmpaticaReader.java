package dk.itu.biologger;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class EmpaticaReader implements Runnable {
	
	boolean reading;
	
	DataInputStream inStream;
	
	int bufferSize; //How many samples do we keep
	public volatile ArrayList<EmpaticaSample> bvp;
	public volatile ArrayList<EmpaticaSample> phasic;
	public volatile ArrayList<EmpaticaSample> tonic;
	public volatile ArrayList<EmpaticaSample> x;
	public volatile ArrayList<EmpaticaSample> y;
	public volatile ArrayList<EmpaticaSample> z;
	public volatile ArrayList<EmpaticaSample> temp;
	public volatile ArrayList<EmpaticaSample> batt;
	
	public EmpaticaReader(Socket socket)
	{
		bvp = new ArrayList<EmpaticaSample>();
		phasic = new ArrayList<EmpaticaSample>();
		tonic = new ArrayList<EmpaticaSample>();
		x = new ArrayList<EmpaticaSample>();
		y = new ArrayList<EmpaticaSample>();
		z = new ArrayList<EmpaticaSample>();
		temp = new ArrayList<EmpaticaSample>();
		batt = new ArrayList<EmpaticaSample>();
		
		try{
			inStream = new DataInputStream(socket.getInputStream());
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void startReading()
	{
		reading = true;
		try{
			while(reading)
			{	
				int channelID = (int) inStream.readByte();
				int dataLength = (int) inStream.readByte();
				
				//System.out.println("- Incoming data -");
				//System.out.println(channelID);
				//System.out.println(dataLength);
				
				for(int i = 0; i < dataLength; i++)
				{
					float sampleValue = inStream.readFloat();
					//System.out.println(channelID + " " + sampleValue);
					EmpaticaSample sample = new EmpaticaSample(Long.toString(System.currentTimeMillis()), channelID, sampleValue );
					
					if(channelID == 2)
						System.out.println(sample.toString());
					
					switch(channelID)
					{
					case 1:
						bvp.add(sample);
						break;
					case 2:
						phasic.add(sample);
						break;
					case 3:
						tonic.add(sample);
						break;
					case 4:
						x.add(sample);
						break;
					case 5:
						y.add(sample);
						break;
					case 6:
						z.add(sample);
						break;
					case 7:
						temp.add(sample);
						break;
					case 8:
						batt.add(sample);
						break;
					default:
						break;
					}
				}				
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void stopReading()
	{
		System.out.println("- Reader stopping reading -");
		reading = false;
		try{
			inStream.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public List<EmpaticaSample> getLatestPhasic(int numSamples)
	{
		List<EmpaticaSample> result = null;
		if(phasic.size() > numSamples)
		{
				result = phasic.subList((phasic.size()-numSamples)	, phasic.size());
		}
		return result;
	}
	
	public List<EmpaticaSample> getLatestTonic(int numSamples)
	{
		List<EmpaticaSample> result = null;
		if(tonic.size() > numSamples)
		{
				result = tonic.subList((tonic.size()-numSamples)	, tonic.size());
		}
		return result;
	}
	
	public List<EmpaticaSample> getLatestBvp(int numSamples)
	{
		List<EmpaticaSample> result = null;
		if(bvp.size() > numSamples)
		{
				result = bvp.subList((bvp.size()-numSamples)	, bvp.size());
		}
		return result;
	}

	public void run() {
		System.out.println("Starting thread");
		this.startReading();
	}
}