package dk.itu.biologger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DataWriter {
	
	String fileName;
	ArrayList<? extends Object> dataToWrite;
	
	public DataWriter(String fileName, ArrayList<? extends Object> dataToWrite){
		this.fileName = fileName;
		this.dataToWrite = dataToWrite;
	}
	
	public void writeData()
	{
		String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		StringBuilder outString = new StringBuilder();
		for(Object sample : dataToWrite)
		{
			outString.append(sample.toString());
			outString.append("\n");
		}
		try {
			FileWriter file = new FileWriter(new File(fileName + "_" + timestamp + ".txt"));
			file.write(outString.toString());
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
