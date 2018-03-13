package com.example.manalili18.cs301_hw3_manalili18_sebrecht20;

import android.graphics.*;
import android.util.Log;
import android.view.MotionEvent;

import java.util.Random;


/**
 * class that animates a ball repeatedly moving diagonally on
 * simple background
 * 
 * @author Steve Vegdahl
 * @author Andrew Nuxoll
 * @version February 2016
 */
public class TestAnimator implements Animator {

	private AnimationSurface animationSurface;
	private MainActivity mainActivity;
	private Random random = new Random();
	private final int wallWidth = 50;

	//ball variables
	private float[] ballPos; // 2 ints: 0 - x, 1 - y
	private float[] ballVel; // 2 ints: 0 - x, 1 - y
	private float[] ballAcc;
	private int ballRad = 40;


	//private AnimationSurface mySurface0 = (AnimationSurface) Activity.findViewById(R.id.animationSurface);
	//AnimationSurface mySurface = (AnimationSurface) findViewById(R.id.animationSurface);

	/**
	 * Get a reference to our new AnimationSurface for dimensions
	 * @param animSur
	 */
	public TestAnimator (AnimationSurface animSur, MainActivity activity) {
		this.animationSurface = animSur;
		this.mainActivity = activity;

		ballPos = new float[] {animSur.getWidth()/2,animSur.getHeight()/2};
		ballVel = randomVelocity();
		ballAcc = new float[] {0,0};
	}

	/**
	 * Helper method to randomize velocity
	 * @return (xVel,yVel)
	 */
	private float[] randomVelocity() {
		int x,y;

		x = random.nextBoolean() ? -1 : 1;
		y = random.nextBoolean() ? -1 : 1;

		return new float[] {x*(random.nextInt(20)+10),y*(random.nextInt(20)+10)};
	}

	/**
	 * Interval between animation frames: .03 seconds (i.e., about 33 times
	 * per second).
	 * 
	 * @return the time interval between frames, in milliseconds.
	 */
	public int interval() {
		return 1;
	}
	
	/**
	 * The background color: a light blue.
	 * 
	 * @return the background color onto which we will draw the image.
	 */
	public int backgroundColor() {
		// create/return the background color
		return Color.rgb(180, 200, 255);
	}
	

	/**
	 * Action to perform on clock tick
	 * 
	 * @param g the graphics object on which to draw
	 */
	public void tick(Canvas g) {

		//draw walls


		int width = animationSurface.getWidth();
		int height = animationSurface.getHeight();
		int halfPSize = mainActivity.isBigPaddle ? 370 : 100;

		Paint wallPaint = new Paint();
		int wallColor = mainActivity.technoActivated ?
				0xFF000000 + random.nextInt(0x00FFFFFF + 1) : 0xFF0000FF;
		wallPaint.setColor(wallColor);

		//this is drawing the actual walls for the surface view
		g.drawRect(0,0,wallWidth,height,wallPaint); //left wall
		g.drawRect(width-wallWidth,0,width,height,wallPaint); //right wall
		g.drawRect(0,0,width,wallWidth,wallPaint); //top wall

		//this is drawing the actual block used by the player
		Paint paddlepaint = new Paint();
		int paddlecolor = mainActivity.technoActivated ?
				0xFF000000 + random.nextInt(0x00FFFFFF + 1) : 0xFF00FFFF;
		paddlepaint.setColor(paddlecolor);
		g.drawRect(width/2-halfPSize,height-wallWidth,width/2+halfPSize,height,paddlepaint);

		/*
		// bump our count either up or down by one, depending on whether
		// we are in "backwards mode".
		if (goBackwards) {
			count--;
		}
		else {
			count++;
		}*/
		
		// Determine the pixel position of our ball.  Multiplying by 15
		// has the effect of moving 15 pixel per frame.  Modding by 600
		// (with the appropriate correction if the value was negative)
		// has the effect of "wrapping around" when we get to either end
		// (since our canvas size is 600 in each dimension).
		//int num = (count*15)%600;
		//if (num < 0) num += 600;
		
		// Draw the ball in the correct position.
		Paint ballPaint = new Paint();
		int ballColor = mainActivity.technoActivated ?
				0xFF000000 + random.nextInt(0x00FFFFFF + 1) : 0xFF0000FF;
		ballPaint.setColor(ballColor);
		g.drawCircle(ballPos[0], ballPos[1], ballRad, ballPaint);

		//check if hit top, ball going up and position is less than wall plus ballrad
		if(ballVel[1] < 0 && ballPos[1] < wallWidth + ballRad) {
			ballVel[1] *= -1;
		}
		//check if hit left, ball going left and pos x < wallwid and ballrad
		else if (ballVel[0] < 0 && ballPos[0] < wallWidth + ballRad) {
			ballVel[0] *= -1;
		}
		//check if hit right, ball going right and pos x < wallwid and ballrad
		else if (ballVel[0] > 0 && ballPos[0] > width - wallWidth - ballRad) {
			ballVel[0] *= -1;
		}
		// bot
		else if (ballVel[1] > 0
				&& ballPos[1] > height - wallWidth - ballRad //- 1
				//&& ballPos[1] < height - wallWidth - ballRad
				&& ballPos[0] > width/2 - halfPSize
				&& ballPos[0] < width/2 + halfPSize)
		{
			ballVel[1] *= -1;
		}
		// ball goes past paddle
		else if (ballVel[1] > 0 && ballPos[1] > height + ballRad)
		{
			ballPos = new float[] {width/2,height/2};
			ballVel = randomVelocity();

		}

		else {
			Log.i("tag", " this shouldn't happen");
		}

		kinematicsCalculations();

	}

	/**
	 * Updates ball velocity and position based on current conditions.
	 */
	private void kinematicsCalculations(){
		if(mainActivity.technoActivated){
			ballAcc[0] = ballVel[0] > 0 ? 0.035f : -0.035f;
			ballAcc[1] = ballVel[1] > 0 ? 0.035f : -0.035f;
		}
		else {
			ballAcc[0] = 0;
			ballAcc[1] = 0;
		}

		ballPos[0] += ballVel[0];
		ballPos[1] += ballVel[1];

		ballVel[0] += ballAcc[0];
		ballVel[1] += ballAcc[1];
	}


	/**
	 * Tells that we never pause.
	 * 
	 * @return indication of whether to pause
	 */
	public boolean doPause() {
		return false;
	}

	/**
	 * Tells that we never stop the animation.
	 * 
	 * @return indication of whether to quit.
	 */
	public boolean doQuit() {
		return false;
	}
	
	/**
	 * reverse the ball's direction when the screen is tapped
	 */
	public void onTouch(MotionEvent event)
	{

	}
	
	

}//class TextAnimator
