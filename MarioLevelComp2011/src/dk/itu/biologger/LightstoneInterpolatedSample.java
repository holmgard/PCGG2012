package dk.itu.biologger;

public class LightstoneInterpolatedSample {
	//private EmpaticaSampleType sampleType;
	private int sampleType;
	private int marioFrame;
	private float scl;
	private float hrv;
	private float marioX;
	private int marioTile;
	
	public LightstoneInterpolatedSample(int type, int frame, float scl, float hrv, float marioX)
	{
		this.sampleType = type;
		this.marioFrame = frame;
		this.scl = scl;
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
		return scl;
	}
	
	public float getHrv(){
		return hrv;
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
		return Integer.toString(sampleType) + "\t" + Integer.toString(marioFrame) + "\t" + Float.toString(scl) + "\t" + Float.toString(marioX) + "\t" + Integer.toString(marioTile);
	}
}
