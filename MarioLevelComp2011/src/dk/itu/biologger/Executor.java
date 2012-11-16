package dk.itu.biologger;

public class Executor {

	public static void main(String[] args)
	{
		System.out.println("--= Biologger =--");
		
		EmpaticaHandler handler = new EmpaticaHandler("localhost",55641);
		
		handler.connect();
		handler.scan();
		handler.open();
		
		handler.startReading();
		System.out.println("--= Reading started =--");
		
		/*handler.stop();
		handler.saveData();
		handler.close();
		handler.disconnect();*/
		
		System.out.println("--= Biologger done =--");
	}
}
