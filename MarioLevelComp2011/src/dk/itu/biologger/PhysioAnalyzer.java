package dk.itu.biologger;

import java.util.ArrayList;

import dk.itu.mario.scene.LevelScene;

public class PhysioAnalyzer {
	
	/**
	 * Returns the average phasic arousal for a given levelchunk
	 * @param tileStart the start of the levelchunk in tile counts
	 * @param tileEnd the end of the levelchunk in tile count
	 * @return
	 */
	
	LevelScene level;
	ArrayList<InterpolatedSample> phasicSamples;
	ArrayList<InterpolatedSample> smoothedSamples;
	ArrayList<InterpolatedSample> interpolatedSamples;
	ArrayList<InterpolatedSample> normalizedSamples;
	
	/**
	 * 
	 * @param level The level that the physio-recordings were made from
	 */
	public PhysioAnalyzer(LevelScene level, ArrayList<InterpolatedSample> phasicSamples)
	{
		this.level = level;
		this.phasicSamples = phasicSamples;
	}
	
	/**
	 * Applies all transformations on the raw sample data.
	 */
	public void analyze()
	{
		smoothSamples();
		interpolateSamples();
		normalizeSamples();
	}
	
	/**
	 * Returns the mean activation for a part of the level.
	 * @param tileStart The first tile of the chunk.
	 * @param tileEnd The last tile of the chunk.
	 * @return
	 */
	public float getLevelChunkMean(float tileStart, float tileEnd)
	{
		if(normalizedSamples == null)
			this.analyze();
		
		ArrayList<InterpolatedSample> temp = new ArrayList<InterpolatedSample>();
		float cumulativeSum = 0;
		int count = 0;
		
		for(InterpolatedSample sample : normalizedSamples)
		{
			if(sample.getTile() >= tileStart && sample.getTile() <= tileEnd)
			{
				temp.add(sample);
				cumulativeSum += sample.getValue();
				count++;
			}
		}
		
		float mean = cumulativeSum/(float)count;
		return mean;
	}
	
	/**
	 * Removes noise from the raw phasic signal by applying a moving average.
	 * Currently looks 2 samples back and forth.  
	 */
	public void smoothSamples()
	{
		smoothedSamples = new ArrayList<InterpolatedSample>();
		
		for(int i = 2; i < phasicSamples.size()-2; i++)
		{ 
			float average =  (phasicSamples.get(i-2).getValue() + phasicSamples.get(i-1).getValue() + phasicSamples.get(i).getValue() + phasicSamples.get(i+1).getValue() + phasicSamples.get(i+2).getValue())/5;
			smoothedSamples.add(new InterpolatedSample(phasicSamples.get(i).getType(), phasicSamples.get(i).getFrame(), average, phasicSamples.get(i).getMarioX()));
		}
	}
	
	public void interpolateSamples()
	{
		//No interpolation needed atm.
	}
	
	/**
	 * Scales all sample values to be between 0 and 1.
	 */
	public void normalizeSamples()
	{
		float min = Float.MAX_VALUE;
		float max = Float.MIN_VALUE;
		
		for(InterpolatedSample sample : smoothedSamples)
		{
			if(sample.getValue() < min) min = sample.getValue();
			if(sample.getValue() > max) max = sample.getValue();
		}
		
		normalizedSamples = new ArrayList<InterpolatedSample>();
		for(InterpolatedSample sample : smoothedSamples)
		{
			float normalizedValue = (sample.getValue() - min) / max;
			normalizedSamples.add(new InterpolatedSample(sample.getType(), sample.getFrame(), normalizedValue, sample.getMarioX()));
		}
	}
	
	/**
	 * Writes all variations of samples to disk
	 */
	public void saveSamples(){
		DataWriter writer = new DataWriter("originalPhasic", phasicSamples);
		writer.writeData();
		writer = new DataWriter("smoothed", smoothedSamples);
		writer.writeData();
		//writer = new DataWriter("interpolated", interpolatedSamples);
		//writer.writeData();
		writer = new DataWriter("normalized", normalizedSamples);
		writer.writeData();
	}
}
