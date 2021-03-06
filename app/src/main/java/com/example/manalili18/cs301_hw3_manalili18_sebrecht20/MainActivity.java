package com.example.manalili18.cs301_hw3_manalili18_sebrecht20;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Class that represents activity_main.xml in java.
 * It contains all relevant information to the game.
 * New tick time changed from 30ms to 1ms.
 *
 * Enhancements:
 *  - [5%] Keep running score. When ball passes opponents paddle, add one. When ball passes own
 *         paddle, subtract one.
 *  - [5%] Change ball appearance during TechnoMode. Draws a .png over the ball of a cheesecake.
 *         Physics stays the same.
 *  - [15%] Computer player on opposite side of player. Misses sometimes.
 *
 *  Instructions / Other Details:
 *   - Change paddle size by clicking Change Paddle button.
 *   - Toggle TechnoMode by clicking Techno Mode button.
 *   - Launch ball from stasis by clicking on the canvas.
 *   - During ball stasis in Techno Mode ball gradually revs up (acceleration adds to velocity).
 *   - AI Opponent is better (faster) in Techno Mode.
 *
 * @author Steve Vegdahl
 * @author Andrew Nuxoll
 * @author Phillip Manalili-Simeon
 * @author Chris Sebrechts
 * @version March 2018
 */
public class MainActivity extends AppCompatActivity {

    protected boolean technoActivated = false;
    protected boolean isBigPaddle = true;

    /**
     * onCreate is called when the app is started and the main activity is created.
     * This method gives java object references to the buttons and SurfaceView created in xml.
     * This method also controls the boolean states of techno mode and big paddle.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Connect the animation surface with the animator
        AnimationSurface mySurface = (AnimationSurface) this
                .findViewById(R.id.animationSurface);
        mySurface.setAnimator(new PongAnimator(mySurface,this));

        // Connect the buttons
        Button paddle = (Button) this.findViewById(R.id.paddle);
        Button tech = (Button) this.findViewById(R.id.tech);

        // Connect the paddle button to toggle between big and small
        paddle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isBigPaddle = !isBigPaddle;
            }
        });

        // Connect the techno button to toggle between techno and nah
        tech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                technoActivated = !technoActivated;
            }
        });
    }
}
