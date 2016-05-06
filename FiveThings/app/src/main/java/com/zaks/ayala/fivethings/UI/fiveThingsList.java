package com.zaks.ayala.fivethings.UI;

import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.zaks.ayala.fivethings.Data.FactsContract;
import com.zaks.ayala.fivethings.Data.FactsProvider;
import com.zaks.ayala.fivethings.R;


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
        fillDataFromDB();
        flipper.setInAnimation(this, R.anim.left_in);
        flipper.setOutAnimation(this, R.anim.right_out);
        BindAnimations();
        lastButton = (Button) buttons.getChildAt(0);

    }

    private void fillDataFromDB() {
        String URL = "content://com.zaks.ayala.provider.facts/items";
        Uri uri = Uri.parse(URL);
        Cursor c = getContentResolver().query(uri, null, null, null, FactsContract.FactsEntry.COLUMN_ORDER);
        if (c.moveToFirst()) {
            do {
                Integer index = c.getInt(c.getColumnIndexOrThrow(FactsContract.FactsEntry.COLUMN_ORDER));
                String text = c.getString(c.getColumnIndexOrThrow(FactsContract.FactsEntry.COLUMN_TEXT));
                int image = c.getInt(c.getColumnIndexOrThrow(FactsContract.FactsEntry.COLUMN_IMAGE));
                ((Button) buttons.getChildAt(index - 1)).setText(index.toString());
                RelativeLayout l = (RelativeLayout) flipper.getChildAt(index - 1);
                ((TextView) l.getChildAt(0)).setText(text);
                ((ImageView) l.getChildAt(1)).setImageDrawable(getResources().getDrawable(image));
            } while (c.moveToNext());

        }

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
