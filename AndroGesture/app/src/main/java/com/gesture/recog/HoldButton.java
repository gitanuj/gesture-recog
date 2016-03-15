package com.gesture.recog;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class HoldButton extends AppCompatButton {

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