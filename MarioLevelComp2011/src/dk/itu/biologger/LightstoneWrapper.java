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
    	
    public void run(){
            JnaHid jnaHid = new JnaHid();
            jnaHid.setVendorIds(new short[]{0x14fa, 0x0483});
            jnaHid.setProductIds(new short[]{0x0001, 0x0035});

            
            jnaHid.run(new IByteHandler()
            {
                    public void handleBytes(byte[] bytes, int length) 
                    {
                            //for(int count = 0; count < length; count++)   //raw data

                    		for(int count = 0; count < bytes[1]; count++)   //first two bytes are length
                            {
                    			//chars[count] = (char)bytes[count +2];
                    			//System.out.print((char)bytes[count + 2]);
                    			char nextChar = (char)bytes[count + 2];
                    			if(nextChar != '\n')
                    				LightstoneReader.getInstance().addToBuffer(nextChar);
                    			//chars[count] = (char)bytes[count +2];
                            }
                    		//System.out.print("*");
                    		
                    }
                    
                    
                    
                    public boolean isActive() 
                    {
                            return true;
                    }
            });
    }
    
    public void stop(){
    	//TODO: Doesn't do anything atm.
    }
}
