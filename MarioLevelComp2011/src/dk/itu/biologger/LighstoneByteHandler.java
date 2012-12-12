package dk.itu.biologger;

import com.jlsm.logic.bytehandler.IByteHandler;

public class LighstoneByteHandler implements IByteHandler {

	public boolean shouldRun = true;
	
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
            return shouldRun;
    }
    
    public void setRun(boolean setRun){
    	shouldRun = setRun;
    }

}
