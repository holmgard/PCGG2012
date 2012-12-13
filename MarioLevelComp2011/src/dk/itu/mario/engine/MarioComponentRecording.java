package dk.itu.mario.engine;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.image.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;


import dk.itu.biologger.ArousalPredictor;
import dk.itu.biologger.EmpaticaHandler;
import dk.itu.biologger.EmpaticaSample;
import dk.itu.biologger.EmpaticaPhysioLogger;
import dk.itu.biologger.LightstonePhysioLogger;
import dk.itu.mario.level.Level;
import dk.itu.mario.scene.BaselineScene;
import dk.itu.mario.scene.LevelScene;
import dk.itu.mario.scene.LevelSceneTest;
import dk.itu.mario.scene.LoseScene;
import dk.itu.mario.scene.Scene;
import dk.itu.mario.scene.WinScene;

import dk.itu.mario.engine.recorder.ScreenRecorder;
import dk.itu.mario.engine.sonar.FakeSoundEngine;
import dk.itu.mario.engine.sonar.SonarSoundEngine;
import dk.itu.mario.engine.sprites.Mario;

public class MarioComponentRecording extends JComponent implements Runnable, KeyListener, FocusListener, MouseListener, IMarioComponent
	{
		    private static final long serialVersionUID = 739318775993206607L;

		    public int biologgerPort;
		    //EmpaticaPhysioLogger physLogger;
		    LightstonePhysioLogger physLogger;
		    
		    public static final int TICKS_PER_SECOND = 24;

		    public static final int EVOLVE_VERSION = 4;
		    public static final int GAME_VERSION = 4;

		    private boolean running = false;
		    private int width, height;
		    private GraphicsConfiguration graphicsConfiguration;
		    private Scene scene;
		    private SonarSoundEngine sound;
		    private boolean focused = false;
		    private boolean useScale2x = false;
		    private boolean isCustom = false;
		    
		    public boolean isPersonalized = false;

		    private Scale2x scale2x = new Scale2x(320, 240);

		    private double openTime;
		    
		    private ScreenRecorder screenRecorder;

		    public MarioComponentRecording(int width, int height,boolean isCustomized){	    	
		    	addFocusListener(this);
		    	addMouseListener(this);
		    	addKeyListener(this);

		        this.setFocusable(true);
		        this.setEnabled(true);
		        this.width = width;
		        this.height = height;
		        this.isCustom = isCustomized;

		        Dimension size = new Dimension(width, height);
		        setPreferredSize(size);
		        setMinimumSize(size);
		        setMaximumSize(size);

		        try
		        {
		            sound = new SonarSoundEngine(64);
		        }
		        catch (LineUnavailableException e)
		        {
		            e.printStackTrace();
		            sound = new FakeSoundEngine();
		        }
		        this.setFocusable(true);

		        LevelScene.bothPlayed = false;

		        openTime = System.nanoTime();
		    }

		    private void toggleKey(int keyCode, boolean isPressed)
		    {
		        if (keyCode == KeyEvent.VK_LEFT)
		        {
		            scene.toggleKey(Mario.KEY_LEFT, isPressed);
		        }
		        if (keyCode == KeyEvent.VK_RIGHT)
		        {
		            scene.toggleKey(Mario.KEY_RIGHT, isPressed);
		        }
		        if (keyCode == KeyEvent.VK_DOWN)
		        {
		            scene.toggleKey(Mario.KEY_DOWN, isPressed);
		        }
		        if (keyCode == KeyEvent.VK_UP)
		        {
		            scene.toggleKey(Mario.KEY_UP, isPressed);
		        }
		        if (keyCode == KeyEvent.VK_A)
		        {
		            scene.toggleKey(Mario.KEY_SPEED, isPressed);
		        }
		        if (keyCode == KeyEvent.VK_S)
		        {
		            scene.toggleKey(Mario.KEY_JUMP, isPressed);
		        }
		        if (keyCode == KeyEvent.VK_ENTER)
		        {
		        	scene.toggleKey(Mario.KEY_ENTER, isPressed);
		        }
		        if (isPressed && keyCode == KeyEvent.VK_F1)
		        {
		            useScale2x = !useScale2x;
		        }

		        if (isPressed && keyCode == KeyEvent.VK_ESCAPE){
		        	try{
		        		//screenRecorder.finishMovie();
		        		running = false;
		        		
		        		//System.exit(1);
		        	}catch(Exception e){
		        		System.out.println("Unable to exit.");
		        	}
		        }
		    }

		    public void paint(Graphics g){
		    	super.paint(g);
		    }

		    public void update(Graphics g)
		    {
		    }

		    public void start()
		    {
		        if (!running)
		        {
		            running = true;
		            
		            //screenRecorder = new ScreenRecorder();
		            
		            new Thread(this, "Game Thread").start();
		        }
		    }

		    public void stop()
		    {
		    	screenRecorder.finishMovie();
		    	
		        Art.stopMusic();
		        running = false;
		    }

		    public void run()
		    {
		    	String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		    	
		    	//physLogger = new EmpaticaPhysioLogger();
		    	physLogger = new LightstonePhysioLogger();
		    	physLogger.initBioLogging();

		        graphicsConfiguration = getGraphicsConfiguration();

		        Art.init(graphicsConfiguration, sound);

		        VolatileImage image = createVolatileImage(320, 240);
		        Graphics g = getGraphics();
		        Graphics og = image.getGraphics();

		        int lastTick = -1;
		        int renderedFrames = 0;
		        int fps = 0;


		        long startTime = System.nanoTime();

		        float time = (System.nanoTime()- startTime)/1000000000f;
		        float now = time;
		        float averagePassedTime = 0;

		        boolean naiveTiming = true;
		        /*if (isCustom)
		        	toCustomGame();
		        else
		        toRandomGame();*/
		        
		        scene = new BaselineScene(this, 30);
		        scene.init();

		        float correction = 0f;
		        if(System.getProperty("os.name") == "Mac OS X");
		        
		        screenRecorder = new ScreenRecorder(timestamp);

		        while (running)
		        {
		        	float lastTime = time;
		            time = (System.nanoTime()- startTime)/1000000000f;
		            float passedTime = time - lastTime;

		            if (passedTime < 0) naiveTiming = false; // Stop relying on nanotime if it starts skipping around in time (ie running backwards at least once). This sometimes happens on dual core amds.
		            averagePassedTime = averagePassedTime * 0.9f + passedTime * 0.1f;

		            if (naiveTiming)
		            {
		                now = time;
		            }
		            else
		            {
		                now += averagePassedTime;
		            }

		            int tick = (int) (now * TICKS_PER_SECOND);

		            if (lastTick == -1)
		            	lastTick = tick;

		            while (lastTick < tick)
		            {
		            	scene.tick();

		                lastTick++;

		                if (lastTick % TICKS_PER_SECOND == 0)
		                {
		                    fps = renderedFrames;
		                    renderedFrames = 0;
		                }
		            }

		            float alpha = (float) (now * TICKS_PER_SECOND - tick);
		            sound.clientTick(alpha);

		            int x = (int) (Math.sin(now) * 16 + 160);
		            int y = (int) (Math.cos(now) * 16 + 120);

		            og.setColor(Color.WHITE);
		            og.fillRect(0, 0, 320, 240);

		            scene.render(og, alpha);
		            
		            

		            if (!this.hasFocus() && tick/4%2==0)
		            {
		                String msg = "CLICK TO PLAY";

		                drawString(og, msg, 160 - msg.length() * 4 + 1, 110 + 1, 0);
		                drawString(og, msg, 160 - msg.length() * 4, 110, 7);
		            }
		            og.setColor(Color.BLACK);

		            if (width != 320 || height != 240)
		            {

		                if (useScale2x)
		                {
		                    g.drawImage(scale2x.scale(image), 0, 0, null);
		                }
		                else
		                {
		                    g.drawImage(image, 0, 0, 640, 480, null);

		                }
		            }
		            else
		            {
		                g.drawImage(image, 0, 0, null);
		            }
		            
		            if (scene instanceof LevelScene) {
		            	LevelScene s = (LevelScene)(scene);
		            	int t = s.LVLT * s.TPS;
		            	t -= s.timeLeft;
		            	screenRecorder.addFrame(image, (long)(time * 1000000000), t);
		            	
		            	String timeString = Integer.toString(t);
		            	
		            	physLogger.logBio(t,timeString, ((LevelScene) scene).mario.x);
		            			            	
		            } else
		            {
		            	screenRecorder.addFrame(image, (long)(time * 1000000000), -1);
		            	
		            	String timeString = "baseline";
		            	physLogger.logBio(-1, timeString, -1f);
		            }

		            renderedFrames++;

		            try
		            {
		                Thread.sleep(5);
		            }
		            catch (InterruptedException e)
		            {
		            }
		        }
		        
		        screenRecorder.finishMovie();

		        Art.stopMusic();
		        
		        physLogger.stop();
		        physLogger.analyzeSignal();
		        physLogger.write(timestamp);
		        
		        if(isPersonalized){
		        	System.out.println("Finished personalized session");
		        	System.exit(0);
		        }
		        
		        ArousalPredictor aPred = new ArousalPredictor();
		        aPred.init(physLogger.getFilename());    
		        aPred.SavePersonalScreenChunkLibrary();
		        System.out.println("Saved predictions to: " + physLogger.getFilename() + ".res");
		        System.out.println("Finished learning session");
		        System.exit(0);
		        
		    }
		    
		    private void drawString(Graphics g, String text, int x, int y, int c)
		    {
		        char[] ch = text.toCharArray();
		        for (int i = 0; i < ch.length; i++)
		        {
		            g.drawImage(Art.font[ch[i] - 32][c], x + i * 8, y, null);
		        }
		    }

		    public void keyPressed(KeyEvent arg0)
		    {
		        toggleKey(arg0.getKeyCode(), true);
		    }

		    public void keyReleased(KeyEvent arg0)
		    {
		        toggleKey(arg0.getKeyCode(), false);
		    }

		    public void keyTyped(KeyEvent arg0)
		    {
		    }

		    public void focusGained(FocusEvent arg0)
		    {
		        focused = true;
		    }

		    public void focusLost(FocusEvent arg0)
		    {
		    	focused = false;
		    }

		    public void levelWon()
		    {

		    }


		    public static final int OPTIMIZED_FIRST = 0;
		    public static final int MINIMIZED_FIRST = 1;


		    private LevelScene randomLevel;

			private int levelDifficulty = 2;


		    /**
		     * Part of the fun increaser
		     */
		    public void toRandomGame(){
		    	
		    	//long seed = new Random().nextLong();
		    	long seed = 3224411591760346650l;
		    	System.out.println(seed);
				//randomLevel = new LevelSceneTest(graphicsConfiguration,this,seed,levelDifficulty ,0,false);
				randomLevel = new LevelSceneTest(graphicsConfiguration,this,seed,levelDifficulty ,0,false,physLogger);
		    	//randomLevel = new LevelSceneTest(graphicsConfiguration,this,new Random().nextLong(),0,0,false);

		    	Mario.fire = false;
		    	Mario.large = false;
		    	Mario.coins = 0;
		    	Mario.lives = 3;

		    	randomLevel.init();
		    	randomLevel.setSound(sound);
		    	scene = randomLevel;
		    }

		    public void toCustomGame(){

		    	//randomLevel = new LevelSceneTest(graphicsConfiguration,this,new Random().nextLong(),0,0,true);
		    	randomLevel = new LevelSceneTest(graphicsConfiguration,this,new Random().nextLong(),0,0,true,physLogger);

		    	Mario.fire = false;
		    	Mario.large = false;
		    	Mario.coins = 0;
		    	Mario.lives = 3;

		    	randomLevel.init();
		    	randomLevel.setSound(sound);
		    	scene = randomLevel;
		    }

		    public void lose(){
		        scene = new LoseScene();
		        scene.setSound(sound);
		        scene.init();
		    }

		    public void win(){
		        scene = new WinScene();
		        scene.setSound(sound);
		        scene.init();
		    }



			public void mouseClicked(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			public void mouseReleased(MouseEvent e) {

				while(!hasFocus()){
					System.out.println("FORCE IT");
					requestFocus();
				}
			}

			/**
			 * Must return the actual fill of the viewable components
			 */
			public Dimension getPreferredSize(){
				return new Dimension(width,height);
			}

			public void baseline() {
				if(isCustom)
					toCustomGame();
				else
					toRandomGame();
			}
			
//			protected void processWindowEvent(WindowEvent e) {
//				if (e.)
//			}
}
