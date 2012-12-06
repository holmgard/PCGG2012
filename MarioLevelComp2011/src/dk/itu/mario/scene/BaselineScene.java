package dk.itu.mario.scene;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import dk.itu.mario.engine.IMarioComponent;

public class BaselineScene extends Scene {

	int baselineTime = 30;
	int countDown;
	
	IMarioComponent mComp;
	
	public BaselineScene(IMarioComponent mComp, int baselineTime)
	{
		this.mComp = mComp;
		this.baselineTime = baselineTime;
		countDown = baselineTime * 24;
	}
	
	public float getX(float alpha) {
		// TODO Auto-generated method stub
		return 0;
	}

	public float getY(float alpha) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void mouseClicked(MouseEvent me) {
		// TODO Auto-generated method stub

	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void tick() {
		countDown--;
		if(countDown < 1)
		{
			mComp.baseline();
		}
	}

	@Override
	public void render(Graphics og, float alpha) {
		// TODO Auto-generated method stub
		og.setColor(Color.BLUE);
		og.fillRect(0, 0, 320, 240);
		og.setColor(Color.white);
		og.drawString( (countDown/24) + " " , 160, 120);
	}

}
