package dk.itu.biologger;

public class InterpolatedSample {
	//private EmpaticaSampleType sampleType;
	private int sampleType;
	private int marioFrame;
	private float sampleValue;
	private float marioX;
	private int marioTile;
	
	public InterpolatedSample(int type, int frame, float value, float marioX)
	{
		this.sampleType = type;
		this.marioFrame = frame;
		this.sampleValue = value;
		this.marioX = marioX;
		this.marioTile = (int)Math.round(marioX/16.0f);
	}
	
	/*public EmpaticaSampleType getType(){
		return sampleType;
	}*/
	
	public int getType(){
		return sampleType;
	}
	
	public int getFrame(){
		return marioFrame;
	}
	
	public float getValue(){
		return sampleValue;
	}
	
	public float getMarioX(){
		return marioX;
	}
	
	public int getTile()
	{
		return marioTile;
	}
	
	@Override
	public String toString(){
		return Integer.toString(sampleType) + "\t" + Integer.toString(marioFrame) + "\t" + Float.toString(sampleValue) + "\t" + Float.toString(marioX) + "\t" + Integer.toString(marioTile);
	}
}
