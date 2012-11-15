package dk.itu.mario.engine;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public interface IMarioComponent {

	public static final int TICKS_PER_SECOND = 24;
	public static final int EVOLVE_VERSION = 4;
	public static final int GAME_VERSION = 4;

	public abstract void paint(Graphics g);

	public abstract void update(Graphics g);

	public abstract void start();

	public abstract void stop();

	public abstract void run();

	public abstract void keyPressed(KeyEvent arg0);

	public abstract void keyReleased(KeyEvent arg0);

	public abstract void keyTyped(KeyEvent arg0);

	public abstract void focusGained(FocusEvent arg0);

	public abstract void focusLost(FocusEvent arg0);

	public abstract void levelWon();

	public static final int OPTIMIZED_FIRST = 0;
	public static final int MINIMIZED_FIRST = 1;

	/**
	 * Part of the fun increaser
	 */
	public abstract void toRandomGame();

	public abstract void toCustomGame();

	public abstract void lose();

	public abstract void win();

	public abstract void mouseClicked(MouseEvent e);

	public abstract void mouseEntered(MouseEvent e);

	public abstract void mouseExited(MouseEvent e);

	public abstract void mousePressed(MouseEvent e);

	public abstract void mouseReleased(MouseEvent e);

	/**
	 * Must return the actual fill of the viewable components
	 */
	public abstract Dimension getPreferredSize();

}