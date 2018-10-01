package com.adam.app.demofloatwindowapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class FloatingViewService extends JobIntentService implements View.OnClickListener {

    private static final int JOB_ID = 1;


    private WindowManager mWM;
    private View mFloatingView;
    private View mColapseView;
    private View mExtendView;

    private final Object STOP_LOCK = new Object();

    @Override
    public void onCreate() {
        Utils.inFo(this, "onCreate enter");
        super.onCreate();

        // Get floating view from layout
        this.mFloatingView = LayoutInflater.from(this).inflate(R.layout.floating_widget, null);

        // Config layout parameter
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSPARENT);

        // Assign layout parameter to the floating view by window manager service
        this.mWM = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        this.mWM.addView(this.mFloatingView, params);

        // Get collapse and extend view from layout
        this.mColapseView = this.mFloatingView.findViewById(R.id.colapse_layout);
        this.mExtendView = this.mFloatingView.findViewById(R.id.expend_layout);

        // Set click listner for expemd layout and close button
        this.mFloatingView.findViewById(R.id.btn_close_widget).setOnClickListener(this);
        this.mExtendView.setOnClickListener(this);

        // Add touch listener
        this.mFloatingView.findViewById(R.id.floationg_layout).setOnTouchListener(new View.OnTouchListener() {

            private int mInitX;
            private int mInitY;
            private float mRowX;
            private float mRowY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mInitX = params.x;
                        mInitY = params.y;
                        mRowX = event.getRawX();
                        mRowY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        // Update layout parameter
                        params.x = mInitX + (int) (event.getRawX() - mRowX);
                        params.y = mInitX + (int) (event.getRawY() - mRowY);
                        mWM.updateViewLayout(mFloatingView, params);
                        return true;
                    case MotionEvent.ACTION_UP:
                        mColapseView.setVisibility(View.GONE);
                        mExtendView.setVisibility(View.VISIBLE);
                        return true;
                }

                return false;
            }
        });

    }

    @Override
    public void onDestroy() {
        Utils.inFo(this, "onDestroy enter");
        super.onDestroy();

        if (this.mFloatingView != null) {
            this.mWM.removeView(this.mFloatingView);
        }
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Utils.inFo(this, "onHandleWork enter");
        // Keep service running
        synchronized(STOP_LOCK) {
            try {
                STOP_LOCK.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public void onClick(View v) {
        Utils.inFo(this, "onClick enter");
        switch (v.getId()) {
            case R.id.expend_layout:
                Utils.inFo(this, "R.id.expend_layout");
                // Switch views
                this.mColapseView.setVisibility(View.VISIBLE);
                this.mExtendView.setVisibility(View.GONE);
                break;
            case R.id.btn_close_widget:
                Utils.inFo(this, "R.id.btn_close_widget");
                synchronized(STOP_LOCK) {
                    // stop service
                    STOP_LOCK.notify();
                }
                break;
            default:
                break;
        }

    }

    /**
     * Start floating window job
     * @param context
     * @param intent
     */
    public static void startJobService(Context context, Intent intent) {
        JobIntentService.enqueueWork(context, FloatingViewService.class, JOB_ID, intent);
    }
}
