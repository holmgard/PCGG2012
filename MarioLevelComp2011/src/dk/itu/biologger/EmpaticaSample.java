package dk.itu.biologger;

enum EmpaticaSampleType{
	BVP,
	TONIC,
	PHASIC,
	X,
	Y,
	Z,
	TEMP,
	BATT,
	EMPTY
}

public class EmpaticaSample {
	
	String stringTime;
	int sampleType;
	float sampleValue;
	
	public EmpaticaSample(String stringTime, int channelID, float sampleValue)
	{
		this.stringTime = stringTime;
		this.sampleType = channelID;
		this.sampleValue = sampleValue;
	}
	
	public String toString()
	{
		return stringTime + "\t" + sampleType + "\t" + sampleValue;
	}
}
