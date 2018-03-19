package com.example.manalili18.cs301_hw3_manalili18_sebrecht20;

import android.graphics.*;
import android.util.Log;
import android.view.MotionEvent;

import java.util.Random;


/**
 * class that animates a ball bouncing off of walls and a paddle
 * 
 * @author Steve Vegdahl
 * @author Andrew Nuxoll
 * @author Phillip Manalili-Simeon
 * @author Chris Sebrechts
 * @version March 2018
 */
public class TestAnimator implements Animator {

	private AnimationSurface animationSurface;
	private MainActivity mainActivity;
	private Random random = new Random();
	private final int wallWidth = 25;
	private final int paddleWidth = 10;
	private int width; // width of surfaceView
	private int height; // height of surfaceview
	private int halfPaddleSize;
	private boolean isReady = true; // is player ready for ball?

	//ball variables
	private float[] ballPos; // 2 ints: 0 - x, 1 - y
	private float[] ballVel; // 2 ints: 0 - x, 1 - y
	private float[] ballAcc;
	private int ballRad = 40; // ball radius

	/**
	 * Create a new TestAnimator object with references to the AnimationSurface and MainActivity
	 * for instance variables and surface size. Also set up ball variables
	 *
	 * @param animSur
	 * @param activity
	 */
	public TestAnimator (AnimationSurface animSur, MainActivity activity) {
		//AnimationSurface and MainActivity
		this.animationSurface = animSur;
		this.mainActivity = activity;

		// set up ball variables
		ballPos = new float[] {0,0};
		ballVel = randomVelocity();
		ballAcc = new float[] {0,0};
	}

	/**
	 * Helper method to randomize velocity with a x and y velocity values from 10.0f to 30.0f
	 *
	 * @return (xVel,yVel)
	 */
	private float[] randomVelocity() {
		int x,y;
		x = random.nextBoolean() ? -1 : 1; // left or right
		y = random.nextBoolean() ? -1 : 1; // up or down
		return new float[] {x*(random.nextInt(20)+10),y*(random.nextInt(20)+10)};
	}

	/**
	 * Interval between animation frames: .01 seconds
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
	 * Helper method to draw walls and paddle every tick
	 * @param g
	 */
	private void drawWallAndPaddle(Canvas g){
		//set wall color
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
		g.drawRect(width/2- halfPaddleSize,height-paddleWidth,width/2+ halfPaddleSize,height,paddlepaint);

		// Draw the ball in the correct position.
		Paint ballPaint = new Paint();
		int ballColor = mainActivity.technoActivated ?
				0xFF000000 + random.nextInt(0x00FFFFFF + 1) : 0xFF0000FF;
		ballPaint.setColor(ballColor);
		g.drawCircle(ballPos[0], ballPos[1], ballRad, ballPaint);
	}

	/**
	 * Action to perform on clock tick
	 * 
	 * @param g the graphics object on which to draw
	 */
	public void tick(Canvas g) {
		// variables
		width = animationSurface.getWidth();
		height = animationSurface.getHeight();
		halfPaddleSize = mainActivity.isBigPaddle ? 370 : 100;

		drawWallAndPaddle(g);

		// collision detection
		// check if hit top, ball going up and position is less than wall plus ballrad
		if(ballVel[1] < 0 && ballPos[1] < wallWidth + ballRad) {
			ballVel[1] *= -1;
		}
		// check if hit left, ball going left and pos x < wallwid and ballrad
		else if (ballVel[0] < 0 && ballPos[0] < wallWidth + ballRad) {
			ballVel[0] *= -1;
		}
		// check if hit right, ball going right and pos x < wallwid and ballrad
		else if (ballVel[0] > 0 && ballPos[0] > width - wallWidth - ballRad) {
			ballVel[0] *= -1;
		}
		// bot, flip y velocity if hit paddle
		else if (ballVel[1] > 0
				&& ballPos[1] > height - paddleWidth - ballRad
				&& ballPos[0] > width/2 - halfPaddleSize
				&& ballPos[0] < width/2 + halfPaddleSize) {
			ballVel[1] *= -1;
		}
		// ball goes past paddle
		else if (ballVel[1] > 0 && ballPos[1] > height + ballRad) {
			// set random location within walls
			ballPos = new float[] {random.nextFloat()*(width-wallWidth-ballRad)+wallWidth+ballRad,
					random.nextFloat()*(height-wallWidth-ballRad)+wallWidth+ballRad};
			ballVel = randomVelocity();
			isReady = false;
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

		if(isReady) {
			ballPos[0] += ballVel[0];
			ballPos[1] += ballVel[1];

			ballVel[0] += ballAcc[0];
			ballVel[1] += ballAcc[1];
		}
		else {
			// during techno mode build up velocity until isReady
			ballVel[0] += ballAcc[0];
			ballVel[1] += ballAcc[1];
		}
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
	 * Launch ball when screen is clicked
	 * @param event touch event
	 */
	public void onTouch(MotionEvent event)
	{
		isReady = true;
	}
	
	

}//class TextAnimator
