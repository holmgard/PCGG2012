package dk.itu.biologger;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import dk.itu.mario.level.generator.bio.ScreenChunkLibrary;

public class ArousalHandler {
	
	ArousalPredictor predictor;
	
	ScreenChunkLibrary chunkLib;
	String dataFile;
	String baseNetwork;
	
	
	public ArousalHandler(ScreenChunkLibrary chunkLib, String dataFile, String baseNetwork)
	{
		this.chunkLib = chunkLib;
		this.dataFile = dataFile;
		this.baseNetwork = baseNetwork;
	}
	
	public ArousalPredictor loadBasePredictor(String predictorFile)
	{
		 try
         {
            FileInputStream fileIn = new FileInputStream(predictorFile + ".pred");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            ArousalPredictor predictor = (ArousalPredictor) in.readObject();
            in.close();
            fileIn.close();
            return predictor;
        }catch(IOException i)
        {
            i.printStackTrace();
            return null;
        }catch(ClassNotFoundException c)
        {
            c.printStackTrace();
            return null;
        }
	}
	
	public void savePredictor(String fileName)
	{
		try
	      {
	         FileOutputStream fileOut = new FileOutputStream(fileName + ".pred");
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(predictor);
	         out.close();
	         fileOut.close();
	      }catch(IOException i)
	      {
	          i.printStackTrace();
	      }
	}
}
