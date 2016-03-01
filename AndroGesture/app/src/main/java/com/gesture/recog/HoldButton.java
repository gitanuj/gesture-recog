package com.gesture.recog;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class HoldButton extends Button {

    public interface HoldListener {

        void onHoldDown();

        void onRelease();
    }

    private HoldListener mListener;

    public HoldButton(Context context) {
        super(context);
    }

    public HoldButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HoldButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setHoldListener(HoldListener holdListener) {
        mListener = holdListener;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HoldButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                setPressed(true);
                if (mListener != null) {
                    mListener.onHoldDown();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_CANCEL:
                setPressed(false);
                if (mListener != null) {
                    mListener.onRelease();
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
        }

        return true;
    }
}