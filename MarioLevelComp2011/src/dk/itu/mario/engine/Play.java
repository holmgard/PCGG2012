package dk.itu.mario.engine;
import java.awt.*;

import javax.swing.*;

public class Play {
	    
	public static void main(String[] args)
	    {

			int bioLoggerPort = Integer.parseInt(args[0]);
			
	    	JFrame frame = new JFrame("Mario Experience Showcase");
	    	//MarioComponent mario = new MarioComponent(640, 480,false);
	    	MarioComponentRecording mario = new MarioComponentRecording(640, 480,false);
	    	mario.biologgerPort = bioLoggerPort;
	    	
	    	frame.setContentPane(mario);
	    	frame.setResizable(false);
	        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	        frame.pack();

	        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	        frame.setLocation((screenSize.width-frame.getWidth())/2, (screenSize.height-frame.getHeight())/2);

	        frame.setVisible(true);

	        mario.start();
	   
	}

}
