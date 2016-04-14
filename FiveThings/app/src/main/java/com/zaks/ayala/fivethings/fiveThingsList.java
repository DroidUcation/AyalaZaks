package com.zaks.ayala.fivethings;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;


public class fiveThingsList extends AppCompatActivity {
    private ViewFlipper flipper;
    LinearLayout buttons;
    private float lastX;
    Button lastButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_five_things_list);
        buttons = (LinearLayout) findViewById(R.id.buttons);
        flipper = (ViewFlipper) findViewById(R.id.flipper);
        flipper.setInAnimation(this, R.anim.left_in);
        flipper.setOutAnimation(this, R.anim.right_out);
        BindAnimations();
        lastButton = (Button) buttons.getChildAt(0);

    }

    public void ShowThing(View view) {
        flipper.setInAnimation(this, R.anim.left_in);
        flipper.setOutAnimation(this, R.anim.right_out);
        BindAnimations();
        int num = Integer.parseInt(((TextView) view).getText().toString());
        flipper.setDisplayedChild(num - 1);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                float currentX = event.getX();
                if (lastX < currentX) {
                    if (flipper.getDisplayedChild() == 4)
                        break;
                    flipper.setInAnimation(this, R.anim.left_in);
                    flipper.setOutAnimation(this, R.anim.right_out);
                    flipper.showNext();
                }
                if (lastX > currentX) {
                    if (flipper.getDisplayedChild() == 0)
                        break;
                    flipper.setInAnimation(this, R.anim.right_in);
                    flipper.setOutAnimation(this, R.anim.left_out);
                    flipper.showPrevious();
                }
                BindAnimations();
                break;
        }
        return false;
    }

    private void BindAnimations() {
        flipper.getInAnimation().setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
                int flipNum = flipper.getDisplayedChild();
                Button button = (Button) buttons.getChildAt(flipNum);
                button.setBackground(getResources().getDrawable(R.drawable.button_on));
                button.setTextColor(getResources().getColor(R.color.androidColor));
                lastButton.setBackground(getResources().getDrawable(R.drawable.button));
                lastButton.setTextColor(Color.WHITE);
                lastButton = button;
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {

            }
        });
    }

}
