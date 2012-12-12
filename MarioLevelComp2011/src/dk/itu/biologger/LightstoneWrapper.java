package dk.itu.biologger;

import java.util.ArrayList;

import com.jlsm.data.jna.JnaHid;
import com.jlsm.logic.bytehandler.IByteHandler;

public class LightstoneWrapper implements Runnable {
	
	
	public LightstonePhysioLogger physLogger;
	
	private LightstoneReader reader;
	
    public static void main(String[] args) 
    {
    	LightstoneWrapper test = new LightstoneWrapper();
    	test.run();
    }
    
    public LightstoneWrapper(){
    	reader = LightstoneReader.getInstance();
    }
    
    public LightstoneWrapper(LightstonePhysioLogger physLogger){
    	reader = LightstoneReader.getInstance();
    	this.physLogger = physLogger;
    }
    
    public ArrayList<LightstoneSample> getLatestSamples(){
    	return reader.getLatestSamples();
    }
    	
    LighstoneByteHandler r = new LighstoneByteHandler();

    JnaHid jnaHid;
    public void run(){
            jnaHid = new JnaHid();
            jnaHid.setVendorIds(new short[]{0x14fa, 0x0483});
            jnaHid.setProductIds(new short[]{0x0001, 0x0035});
            jnaHid.run(r);
    }
    
    public void stop(){
    		r.setRun(false);
    }
}
