package dk.itu.biologger;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.lang.System;

enum EmpaticaState{
	CONNECTED,
	SCANNED,
	OPEN,
	RECEIVING,
	STOPPED,
	CLOSED,
	ERROR,
	NODEVICE
}

public class EmpaticaHandler {
	
	int count = 0;
	
	String host = "localhost";
	int port = 53321;
	Socket socket;
	
	BufferedReader controlReader;
	DataOutputStream controlWriter;
	
	EmpaticaReader reader;
	Thread readingThread;
	
	private EmpaticaState deviceState = EmpaticaState.NODEVICE;
	
	public EmpaticaHandler(){}
	
	public EmpaticaHandler(String host, int port)
	{
		this.host = "localhost";
		this.port = port;
	}

	public EmpaticaReader getEmpaticaReader()
	{
		return reader;
	}
	
	public EmpaticaState connect()
	{
		System.out.println("- Connecting -");
		try{
			socket = new Socket("localhost",port);
			controlWriter = new DataOutputStream(socket.getOutputStream());
			controlReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			deviceState = EmpaticaState.CONNECTED;
		} catch(IOException e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return deviceState;
	}
	
	public EmpaticaState scan()
	{
		System.out.println("- Scanning -");
		try{
			controlWriter.writeBytes("scan");
			if(this.waitForResponse() == "OK")
			{
				deviceState = EmpaticaState.SCANNED;
				System.out.println("Scanning done");
			}
		}
		catch(IOException e){
			System.out.println(e.toString());
		}
		return deviceState;
	}
	
	public String waitForResponse()
	{
		boolean waiting = true;
		String response = "";
		while(waiting)
		{
			System.out.println("Waiting for response...");
			try{
				response = controlReader.readLine();
				System.out.println(count + " " + response);
				count++;
			if(response.contains("OK"))
			{
				System.out.println("Received OK");
				waiting = false;
			} else if(response.contains("ER"))
			{
				System.out.println("Received ER");
				waiting = false;
				throw new EmpaticaErrorException("Emaptica device is in error state - you should restart it");
			}
			} catch (EmpaticaErrorException e){
				System.out.println(e.getMessage());
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		return response;
	}
	
	public EmpaticaState open()
	{
		System.out.println("- Opening -");
		try{
			controlWriter.writeBytes("open");
			if(this.waitForResponse() == "OK")
			{
				deviceState = EmpaticaState.OPEN;
			}
		}
		catch(IOException e){
			System.out.println(e.toString());
		}
		return deviceState;
	}
	
	public EmpaticaState start()
	{
		System.out.println("- Starting -");
		try{
			controlWriter.writeBytes("start");
			if(this.waitForResponse() == "OK")
			{
				deviceState = EmpaticaState.RECEIVING;
			}
		}
		catch(IOException e){
			System.out.println(e.toString());
		}
		return deviceState;
	}
	
	public void startReading()
	{
		System.out.println("Starting read");
		reader = new EmpaticaReader(socket);
		readingThread = new Thread(reader);
		this.start();
		readingThread.start();
	}
	
	public EmpaticaState stop(){
		System.out.println("- Stopping -");
		try{
			reader.stopReading();
			
			controlWriter.writeBytes("stop");
			if(this.waitForResponse() == "OK")
			{
				deviceState = EmpaticaState.STOPPED;
			}
		} catch (IOException e)
		{
			System.out.println(e);
		}
		return deviceState;
	}
	
	public EmpaticaState close(){
		System.out.println("- Closing -");
		try{
			controlWriter.writeBytes("close");
			if(this.waitForResponse() == "OK")
			{
				deviceState = EmpaticaState.STOPPED;
			}
		} catch (IOException e)
		{
			System.out.println(e);
		}
		return deviceState;
	}

	public void disconnect() {
		System.out.println("- Disconnecting -");
		try{
			socket.shutdownOutput();
			socket.shutdownInput();
			socket.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void saveData()
	{
		/*String output = "";
		ArrayList<EmpaticaSample> samples = reader.getSamples();
		for(EmpaticaSample sample : samples)
		{
			output += sample.toString();
		}
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("testSave.txt"));
			writer.write(output);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
}
