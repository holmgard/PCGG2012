package dk.itu.biologger;

public class LightstoneSample {
	
	long timestamp;
	int scl;
	int hrv;
	
	public LightstoneSample(long timestamp, int scl, int hrv)
	{
		this.timestamp = timestamp;
		this.scl = scl;
		this.hrv = hrv;
	}
	
	public String toString()
	{
		return timestamp + "\t" + scl + "\t" + hrv;
	}
}
