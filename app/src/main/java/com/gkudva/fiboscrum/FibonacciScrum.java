package com.gkudva.fiboscrum;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.text.DecimalFormatSymbols;

public class FibonacciScrum extends AppCompatActivity {

    private ViewFlipper mFlipper;
    private TextView mTextView1, mTextView2;
    private int mCurrentLayoutState, mCount;
    private int fib1, fib2;
    private GestureDetector mGestureDetector;
    public static final String FIB_COUNT = "CurrentValue";
    public static final String FIB_VALUE1 = "Fib1Value";
    public static final String FIB_VALUE2 = "Fib2Value";
    private int MY_DATA_CHECK_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fibonacci_scrum);

        mCurrentLayoutState = 0;
        fib1 = 1;
        fib2 = 1;
        mCount = fib1;

        mFlipper = (ViewFlipper) findViewById(R.id.view_flipper);
        mTextView1 = (TextView) findViewById(R.id.textView1);
        mTextView2 = (TextView) findViewById(R.id.textView2);

        mTextView1.setText(String.valueOf(mCount));

        mGestureDetector = new GestureDetector(this,
                new GestureDetector.SimpleOnGestureListener() {
                    private static final int SWIPE_THRESHOLD = 100;
                    private static final int SWIPE_VELOCITY_THRESHOLD = 100;
                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2,
                                           float velocityX, float velocityY) {
                        boolean result = false;

                        try {
                            float diffX = e2.getX() - e1.getX();
                            float diffY = e2.getY() - e1.getY();
                            if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD)
                            {
                                mCurrentLayoutState = mCurrentLayoutState == 0 ? 1 :0;


                                if (diffX > 0)
                                {
                                    switchLayoutStateTo(mCurrentLayoutState, false);
                                } else {
                                    switchLayoutStateTo(mCurrentLayoutState, true);
                                }

                                result = true;
                            }
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                        return result;
                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }
    public void switchLayoutStateTo(int switchTo,
                                    boolean left) {
        String disp;
        mCurrentLayoutState = switchTo;

        mFlipper.setInAnimation(inFromRightAnimation());
        mFlipper.setOutAnimation(outToLeftAnimation());

        if (left)
        {
            mCount = fib1 + fib2;

            fib1 = fib2;
            fib2 = mCount;
        } else
        {
            mCount = fib1;

            if (mCount <= 0)
            {
                mCount = 1;
                fib1 = 1;
                fib2 = 1;
            }
            else
            {
                fib1 = fib2 - fib1;
                fib2 = mCount;
            }
        }

        if (mCount > 100)
        {
            disp = DecimalFormatSymbols.getInstance().getInfinity();
            Toast.makeText(getApplicationContext(), "Story needs to be broken down into smaller tasks", Toast.LENGTH_LONG).show();
        }
        else
        {
            disp = String.valueOf(mCount);
        }

        if (switchTo == 0) {
            mTextView1.setText(disp);
        } else {
            mTextView2.setText(disp);
        }

        mFlipper.showPrevious();
    }

    private Animation inFromRightAnimation() {
        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromRight.setDuration(500);
        inFromRight.setInterpolator(new LinearInterpolator());
        return inFromRight;
    }

    private Animation outToLeftAnimation() {
        Animation outtoLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        outtoLeft.setDuration(500);
        outtoLeft.setInterpolator(new LinearInterpolator());
        return outtoLeft;
    }

    @Override
    public void onSaveInstanceState(Bundle saveBundle)
    {
        saveBundle.putInt(FIB_COUNT, mCount);
        saveBundle.putInt(FIB_VALUE1, fib1);
        saveBundle.putInt(FIB_VALUE2, fib2);
        super.onSaveInstanceState(saveBundle);

    }

    @Override
    public void onRestoreInstanceState(Bundle saveBundle)
    {
        super.onRestoreInstanceState(saveBundle);
        mCount = saveBundle.getInt(FIB_COUNT);
        fib1 = saveBundle.getInt(FIB_VALUE1);
        fib2 = saveBundle.getInt(FIB_VALUE2);

        if (mCount > 100)
        {
            mTextView1.setText(DecimalFormatSymbols.getInstance().getInfinity());
        }
        else {
            mTextView1.setText(String.valueOf(mCount));
        }

    }
}
