package com.example.manalili18.cs301_hw3_manalili18_sebrecht20;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    protected boolean technoActivated = false;
    //protected int ballCount = 1;
    protected boolean isBigPaddle = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Connect the animation surface with the animator
        AnimationSurface mySurface = (AnimationSurface) this
                .findViewById(R.id.animationSurface);
        mySurface.setAnimator(new TestAnimator(mySurface,this));

        Button paddle = (Button) this.findViewById(R.id.paddle);
        Button tech = (Button) this.findViewById(R.id.tech);

        paddle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isBigPaddle = !isBigPaddle;
            }
        });

        tech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                technoActivated = !technoActivated;
            }
        });
        /*
        ball.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ballCount +=
            }
        });
        */

    }
}
