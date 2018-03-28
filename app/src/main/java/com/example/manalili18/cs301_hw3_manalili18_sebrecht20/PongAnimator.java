package com.example.manalili18.cs301_hw3_manalili18_sebrecht20;

import android.graphics.*;
import android.util.Log;
import android.view.MotionEvent;

import java.util.Random;


/**
 * class that animates a ball bouncing off of walls and two paddles
 * 
 * @author Steve Vegdahl
 * @author Andrew Nuxoll
 * @author Phillip Manalili-Simeon
 * @author Chris Sebrechts
 * @version March 2018
 */
public class PongAnimator implements Animator {

	private AnimationSurface animationSurface;
	private MainActivity mainActivity;
	private Random random = new Random();//the ranndom function
	private final int wallWidth = 25;
	private final int paddleWidth = 20;
	private int width; // width of surfaceView
	private int height; // height of surfaceview
	private int halfPaddleSize = 370;
	private boolean isReady = true; // is player ready for ball?

	//ball variables
	private float[] ballPos; // 2 ints: 0 - x, 1 - y
	private float[] ballVel; // 2 ints: 0 - x, 1 - y
	private float[] ballAcc;
	private int ballRad = 40; // ball radius

	private float paddlePos = wallWidth + halfPaddleSize;
	private int score = 0;

	Bitmap cheesecake;

	//opponent variables
	private int halfPaddleSizeOpponent = 200;
	private float paddlePosOpponent = wallWidth + halfPaddleSizeOpponent;
	private float opponentSpeed = 20.0f; //This is how fast the actual paddle moves for the AI


	/**
	 * Create a new TestAnimator object with references to the AnimationSurface and MainActivity
	 * for instance variables and surface size. Also set up ball variables
	 *
	 * @param animSur
	 * @param activity
	 */
	public PongAnimator (AnimationSurface animSur, MainActivity activity) {
		//AnimationSurface and MainActivity
		this.animationSurface = animSur;
		this.mainActivity = activity;

		// set up ball variables
		ballPos = new float[] {0,0};
		ballVel = randomVelocity();
		ballAcc = new float[] {0,0};

		cheesecake = BitmapFactory.decodeResource(mainActivity.getResources(),
				R.drawable.cheesecake);
		//The following below makes the acutal size of the cheesecake
		cheesecake = Bitmap.createScaledBitmap(cheesecake, ballRad*4, ballRad*4, true);
	}

	/**
	 * Helper method to randomize velocity with a x and y velocity values from 10.0f to 30.0f
	 *
	 * @return (xVel,yVel)
	 */
	private float[] randomVelocity() {
		int x,y;
		x = random.nextBoolean() ? -1 : 1; // left or right
		float xSpd = (random.nextInt(10)+20);
		y = random.nextBoolean() ? -1 : 1; // up or down
		float ySpd = (random.nextInt(20)+10);
		//below is the speed of the actual paddle for the AI depending on the mode of the game
		opponentSpeed = mainActivity.technoActivated ? xSpd + 17.0f : xSpd - 9.0f;

		return new float[] {x*xSpd,y*ySpd};
	}

	/**
	 * Interval between animation frames: .01 seconds
     * This has been changed from .30 seconds to .01 to make graphics much smoother.
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

		//opponent
		Paint opponentPaint = new Paint();
		int opponentColor = mainActivity.technoActivated ?
				//The Color of the oponent is red
				0xFF000000 + random.nextInt(0x00FFFFFF + 1) : 0xFFFF0000;
		opponentPaint.setColor(opponentColor);

		g.drawRect(paddlePosOpponent - halfPaddleSizeOpponent,
				0,
				paddlePosOpponent + halfPaddleSizeOpponent,
				paddleWidth,
				opponentPaint);

		//this is drawing the actual block used by the player
		Paint paddlepaint = new Paint();
		int paddlecolor = mainActivity.technoActivated ?
				0xFF000000 + random.nextInt(0x00FFFFFF + 1) : 0xFF00FFFF;
		paddlepaint.setColor(paddlecolor);

		g.drawRect(paddlePos - halfPaddleSize,
				height - paddleWidth,
				paddlePos + halfPaddleSize,
				height,
				paddlepaint);

		// Draw the ball in the correct position.
		Paint ballPaint = new Paint();
		int ballColor = mainActivity.technoActivated ?
				0xFF000000 + random.nextInt(0x00FFFFFF + 1) : 0xFF0000FF;
		ballPaint.setColor(ballColor);
		g.drawCircle(ballPos[0], ballPos[1], ballRad, ballPaint);

		if(mainActivity.technoActivated)
			g.drawBitmap(cheesecake, ballPos[0] - (float) ballRad * 2f,
					ballPos[1] - (float) ballRad * 2f,
					null); //The following has the png image of the cheesecake move with the ball
	}

	/**
	 * This draws the score on the surface view of the current state of the game
	 * @param g
	 */
	private void drawScore(Canvas g) {
		float textSize = 1000.0f;
		Paint scorePaint = new Paint();
		int scoreColor = mainActivity.technoActivated ?
				0x99000000 + random.nextInt(0x00FFFFFF + 1) : 0x9900FF00;

		/**
		 * External Citation
		 *   Date:		22 March 2018
		 *   Problem:	How to draw text on surface view?
		 *   Resource:  https://developer.android.com/reference/android/graphics/Canvas.html
		 *   Solution: 	Reference API
		 */

		scorePaint.setColor(scoreColor);
		scorePaint.setTextSize(textSize);
		Typeface scoreTypeFace = Typeface.create(scorePaint.getTypeface(),Typeface.BOLD);
		scorePaint.setTypeface(scoreTypeFace);
		scorePaint.setTextAlign(Paint.Align.CENTER);

		String scoreText = ""+score;
		g.drawText(scoreText,0,scoreText.length(),width/2,height/2 + textSize/3,scorePaint);
	}

	/**
	 * This here creates the level of complexity of the AI
	 *
	 */
	private void AICalculations(){
		//ball is to left of opponent
		if(paddlePosOpponent < ballPos[0]){
			paddlePosOpponent += opponentSpeed;
		}
		//ball is to right of opponent
		if(paddlePosOpponent > ballPos[0]){
			paddlePosOpponent -= opponentSpeed;
		}
		//else stay square

		//clamp right
		if(paddlePosOpponent > width - wallWidth - halfPaddleSizeOpponent) {
			paddlePosOpponent = width - wallWidth - halfPaddleSizeOpponent;
		}
		//clamp left
		if(paddlePosOpponent < wallWidth + halfPaddleSizeOpponent) {
			paddlePosOpponent = wallWidth + halfPaddleSizeOpponent;
		}
	}

	/**
	 * Action to perform on clock tick. On every frame the board is drawn with paddles and ball.
     * On every frame, new position of paddles and ball is calculated.
	 * 
	 * @param g the graphics object on which to draw
	 */
	public void tick(Canvas g) {
		// variables
		width = animationSurface.getWidth();
		height = animationSurface.getHeight();
		halfPaddleSize = mainActivity.isBigPaddle ? 370 : 100;

		drawScore(g);
		drawWallAndPaddle(g);

		// collision detection
		// check if hit top, ball going up and position is less than wall plus ballrad
		if(ballVel[1] < 0
				&& ballPos[1] < paddleWidth + ballRad
				&& ballPos[0] > paddlePosOpponent - halfPaddleSizeOpponent
				&& ballPos[0] < paddlePosOpponent + halfPaddleSizeOpponent) {
			ballVel[1] *= -1;
		}
		// ball goes past paddle
		else if (ballVel[1] < 0 && ballPos[1] < 0 - ballRad) {
			// set random location within walls
			ballPos = new float[] {random.nextFloat()*(width-wallWidth-ballRad)+wallWidth+ballRad,
					random.nextFloat()*(height-wallWidth-ballRad)+wallWidth+ballRad};
			ballVel = randomVelocity();
			isReady = false;
			score++;
		}
		// check if hit left, ball going left and pos x < wallwid and ballrad
		else if (ballVel[0] < 0 && ballPos[0] < wallWidth + ballRad) {
			ballVel[0] *= -1;
		}
		// check if hit right, ball going right and pos x < wallwid and ballrad
		else if (ballVel[0] > 0 && ballPos[0] > width - wallWidth - ballRad) {
			ballVel[0] *= -1;
		}
		//hits top of paddle
		else if (ballVel[1] > 0
				&& ballPos[1] > height - paddleWidth - ballRad
				&& ballPos[0] > paddlePos - halfPaddleSize
				&& ballPos[0] < paddlePos + halfPaddleSize) {
			ballVel[1] *= -1;
		}
		// ball goes past paddle
		else if (ballVel[1] > 0 && ballPos[1] > height + ballRad) {
			// set random location within walls
			ballPos = new float[] {random.nextFloat()*(width-wallWidth-ballRad)+wallWidth+ballRad,
					random.nextFloat()*(height-wallWidth-ballRad)+wallWidth+ballRad};
			ballVel = randomVelocity();
			isReady = false;
			score--;
		}
		else {
			Log.i("tag", " this shouldn't happen");
		}

		kinematicsCalculations();
		AICalculations();
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
	public void onTouch(MotionEvent event)	{
		isReady = true;

		float temp = event.getX();
		//this is the left wall for the clamp
		if(temp > width - wallWidth - halfPaddleSize) {
			paddlePos = width - wallWidth - halfPaddleSize;
		}
		//this is the right wall for the clamp
		else if(temp < wallWidth + halfPaddleSize) {
			paddlePos = wallWidth + halfPaddleSize;
		}
		else {
			paddlePos = temp;
		}
	}
}
