package dk.itu.mario.engine.recorder;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;


/**
 * 
 * @author Peter Pedersen
 * @version 1.0
 * 
 * A class for recording gameplay to a movie file.
 *
 */
public class ScreenRecorder {
	
	private final int WIDTH = 320;
	private final int HEIGHT = 240 + 32;

	private IMediaWriter writer;
	private int strInd;
	private BufferedImage bgrImg;
	private Graphics2D g;
	
	public ScreenRecorder() {
		String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		makeWriter(timestamp);
	}
	
	public ScreenRecorder(String name) {
		makeWriter(name);
	}
	
	private void makeWriter(String name) {
		writer = ToolFactory.makeWriter(name + ".mp4");
		
		strInd = writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_MPEG4, WIDTH, HEIGHT);
		
		bgrImg = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
		g = bgrImg.createGraphics();
	}
	
	public void addFrame(Image img, long timestamp, int recstamp) {
		//bgrImg = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
		//g = bgrImg.createGraphics();
		g.drawImage(img, 0, 0, null);
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 240, WIDTH, 32);
		g.setColor(Color.WHITE);
		g.drawString("Time: " + recstamp, 0, 250);
		
		writer.encodeVideo(0, bgrImg, timestamp, TimeUnit.NANOSECONDS);
		//System.out.println(timestamp);
	}
	
	public void finishMovie() {
//		try {
//			ImageIO.write(bgrImg, "png", new File("screen.png"));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		//writer.flush();
		System.out.println("Movie Finished");
		writer.close();
	}
}
