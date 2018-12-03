package com.gocode.sharkfeed;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewTreeObserver;


public class ZoomImageView extends AppCompatImageView implements ScaleGestureDetector.OnScaleGestureListener, View.OnTouchListener, ViewTreeObserver.OnGlobalLayoutListener {

    private static final String TAG = ZoomImageView.class.getSimpleName();
    public static final float SCALE_MAX = 3.0f;
    private static final float SCALE_MID = 1.5f;

    /**
     * initialize scale, if picture is bigger than window,
     * this value would be negative
     */
    private float initScale = 1.0f;
    private boolean once = true;

    /**
     * for storing 9 values of a matrix
     */
    private final float[] matrixValues = new float[9];

    /**
     * detecting gesture
     */
    private ScaleGestureDetector mScaleGestureDetector = null;
    private final Matrix mScaleMatrix = new Matrix();

    /**
     * detect double tap
     */
    private GestureDetector mGestureDetector;
    private boolean isAutoScale;

    private int mTouchSlop;

    private float mLastX;
    private float mLastY;

    private boolean isCanDrag;
    private int lastPointerCount;

    private boolean isCheckTopAndBottom = true;
    private boolean isCheckLeftAndRight = true;

    public ZoomImageView(Context context) {
        this(context, null);
    }

    public ZoomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.setScaleType(ScaleType.MATRIX);
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (isAutoScale == true)
                    return true;

                float x = e.getX();
                float y = e.getY();
                Log.e("DoubleTap", getScale() + " , " + initScale);
                if (getScale() < SCALE_MID) {
                    //postDelayed(); 16 for how long implement a change
                    ZoomImageView.this.postDelayed(new AutoScaleRunnable(SCALE_MID, x, y), 16);
                    isAutoScale = true;
                } /*else if (getScale() >= SCALE_MID  //double tap zoom in
                        && getScale() < SCALE_MAX) {
                    ZoomImageView.this.postDelayed(new AutoScaleRunnable(SCALE_MAX, x, y), 16);
                    isAutoScale = true;
                }*/ else {
                    ZoomImageView.this.postDelayed(new AutoScaleRunnable(initScale, x, y), 16);
                    isAutoScale = true;
                }

                return true;
            }
        });
        mScaleGestureDetector = new ScaleGestureDetector(context, this);
        this.setOnTouchListener(this);
    }

    /**
     * Runnable for auto scale
     */
    private class AutoScaleRunnable implements Runnable {
        static final float BIGGER = 1.07f;
        static final float SMALLER = 0.93f;
        private float mTargetScale;
        private float tmpScale;

        /**
         * center of scale
         */
        private float x;
        private float y;

        /**
         * target scale, compared with current scale to decide if zoom in or out
         * @param targetScale
         */
        public AutoScaleRunnable(float targetScale, float x, float y) {
            this.mTargetScale = targetScale;
            this.x = x;
            this.y = y;
            if (getScale() < mTargetScale) {
                tmpScale = BIGGER;
            } else {
                tmpScale = SMALLER;
            }
        }

        @Override
        public void run() {
            // do scaling
            mScaleMatrix.postScale(tmpScale, tmpScale, x, y);
            checkBorderAndCenterWhenScale();
            setImageMatrix(mScaleMatrix);

            final float currentScale = getScale();
            // if value is in appropriate change，continue
            if (((tmpScale > 1f) && (currentScale < mTargetScale)) || ((tmpScale < 1f) && (mTargetScale < currentScale))) {

                ZoomImageView.this.postDelayed(this, 16);
            } else {
                // set target scale factor
                final float deltaScale = mTargetScale / currentScale;
                mScaleMatrix.postScale(deltaScale, deltaScale, x, y);
                checkBorderAndCenterWhenScale();
                setImageMatrix(mScaleMatrix);
                isAutoScale = false;
            }
        }
    }

    /**
     * control scale factor，first decide factor，then scale of mScaleMatrix
     * @param detector
     * @return
     */
    @SuppressLint("NewApi")
    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float scale = getScale();
        float scaleFactor = detector.getScaleFactor();

        if (getDrawable() == null)
            return true;

        /**
         * control scale factor
         */
        if ((scale < SCALE_MAX && scaleFactor > 1.0f) || (scale > initScale && scaleFactor < 1.0f)) {
            /**
             * determine scale max and min
             */
            if (scaleFactor * scale < initScale) {
                scaleFactor = initScale / scale;
            }
            if (scaleFactor * scale > SCALE_MAX) {
                scaleFactor = SCALE_MAX / scale;
            }
            /**
             * set scale factor
             */
            mScaleMatrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());

            checkBorderAndCenterWhenScale();
            setImageMatrix(mScaleMatrix);
        }
        return true;
    }

    /**
     * check border and center when scale
     */
    private void checkBorderAndCenterWhenScale() {

        RectF rect = getMatrixRectF();
        float deltaX = 0;
        float deltaY = 0;

        int width = getWidth();
        int height = getHeight();

        // control rect when height or width is bigger than window
        if (rect.width() >= width) {
            if (rect.left > 0) {
                deltaX = -rect.left;
            }
            if (rect.right < width) {
                deltaX = width - rect.right;
            }
        }
        if (rect.height() >= height) {
            if (rect.top > 0) {
                deltaY = -rect.top;
            }
            if (rect.bottom < height) {
                deltaY = height - rect.bottom;
            }
        }
        // if image is smaller than window, then center
        if (rect.width() < width) {
            deltaX = width * 0.5f - rect.right + 0.5f * rect.width();
        }
        if (rect.height() < height) {
            deltaY = height * 0.5f - rect.bottom + 0.5f * rect.height();
        }
        Log.e(TAG, "deltaX = " + deltaX + " , deltaY = " + deltaY);

        mScaleMatrix.postTranslate(deltaX, deltaY);

    }

    /**
     * get Image's Rect based on Matrix
     * @return
     */
    private RectF getMatrixRectF() {
        Matrix matrix = mScaleMatrix;
        RectF rect = new RectF();
        Drawable d = getDrawable();
        if (null != d) {
            rect.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            matrix.mapRect(rect);
        }
        return rect;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
    }

    /**
     * Let ScaleGestureDetector deal with motionEvent from onTouchListener
     * public boolean onTouch(View v, MotionEvent event){
     * return mScaleGestureDetector.onTouchEvent(event);
     * }
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (mGestureDetector.onTouchEvent(event))
            return true;
        mScaleGestureDetector.onTouchEvent(event);

        float x = 0, y = 0;
        // get touch point count
        final int pointerCount = event.getPointerCount();
        // get x/ys from each
        for (int i = 0; i < pointerCount; i++) {
            x += event.getX(i);
            y += event.getY(i);
        }
        x = x / pointerCount;
        y = y / pointerCount;

        /**
         * when touch point changes，reset mLasX , mLastY
         */
        if (pointerCount != lastPointerCount) {
            isCanDrag = false;
            mLastX = x;
            mLastY = y;
        }

        lastPointerCount = pointerCount;
        RectF rectF = getMatrixRectF();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (rectF.width() > getWidth() || rectF.height() > getHeight()) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (rectF.width() > getWidth() || rectF.height() > getHeight()) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                Log.e(TAG, "ACTION_MOVE");
                float dx = x - mLastX;
                float dy = y - mLastY;

                if (!isCanDrag) {
                    isCanDrag = isCanDrag(dx, dy);
                }
                if (isCanDrag) {

                    if (getDrawable() != null) {
                        // if (getMatrixRectF().left == 0 && dx > 0)
                        // {
                        // getParent().requestDisallowInterceptTouchEvent(false);
                        // }
                        //
                        // if (getMatrixRectF().right == getWidth() && dx < 0)
                        // {
                        // getParent().requestDisallowInterceptTouchEvent(false);
                        // }
                        isCheckLeftAndRight = isCheckTopAndBottom = true;
                        // if width is smaller than window size, stop moving left and right
                        if (rectF.width() < getWidth()) {
                            dx = 0;
                            isCheckLeftAndRight = false;
                        }
                        // if height is smaller than window size, stop moving up and down
                        if (rectF.height() < getHeight()) {
                            dy = 0;
                            isCheckTopAndBottom = false;
                        }

                        //set offset
                        mScaleMatrix.postTranslate(dx, dy);
                        //check again
                        checkMatrixBounds();
                        setImageMatrix(mScaleMatrix);
                    }
                }
                mLastX = x;
                mLastY = y;
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                Log.e(TAG, "ACTION_UP");
                lastPointerCount = 0;
                break;
        }
        return true;
    }

    /**
     * get current scale
     * @return
     */
    public final float getScale() {
        mScaleMatrix.getValues(matrixValues);
        return matrixValues[Matrix.MSCALE_X];
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeGlobalOnLayoutListener(this);
    }

    /**
     * Based on both image and window size, move to center
     * if image is too small, do nothing
     */
    @Override
    public void onGlobalLayout() {
        if (once) {
            Drawable d = getDrawable();
            if (d == null)
                return;
            Log.e(TAG, d.getIntrinsicWidth() + " , " + d.getIntrinsicHeight());
            int width = getWidth();
            int height = getHeight();
            // get height and width of image
            int dw = d.getIntrinsicWidth();
            int dh = d.getIntrinsicHeight();
            float scale = 1.0f;
            // if over height/width, then match screen height/width
            if (dw > width && dh <= height) {
                scale = width * 1.0f / dw;
            }
            if (dh > height && dw <= width) {
                scale = height * 1.0f / dh;
            }
            // if both over window size, then match with proportion
            if (dw > width && dh > height) {
                scale = Math.min(width * 1.0f / dw, height * 1.0f / dh);
            }
            initScale = scale;

            Log.e(TAG, "initScale = " + initScale);
            mScaleMatrix.postTranslate((width - dw) / 2, (height - dh) / 2);
            mScaleMatrix.postScale(scale, scale, getWidth() / 2, getHeight() / 2);
            // Move image to center
            setImageMatrix(mScaleMatrix);
            once = false;
        }
    }

    /**
     * When moving check if bound is over height/width of window
     */
    private void checkMatrixBounds() {
        RectF rect = getMatrixRectF();

        float deltaX = 0, deltaY = 0;
        final float viewWidth = getWidth();
        final float viewHeight = getHeight();
        // check if bounds over edge of window
        if (rect.top > 0 && isCheckTopAndBottom) {
            deltaY = -rect.top;
        }
        if (rect.bottom < viewHeight && isCheckTopAndBottom) {
            deltaY = viewHeight - rect.bottom;
        }
        if (rect.left > 0 && isCheckLeftAndRight) {
            deltaX = -rect.left;
        }
        if (rect.right < viewWidth && isCheckLeftAndRight) {
            deltaX = viewWidth - rect.right;
        }
        mScaleMatrix.postTranslate(deltaX, deltaY);
    }

    /**
     * If can drag
     *
     * @param dx
     * @param dy
     * @return
     */
    private boolean isCanDrag(float dx, float dy) {
        return Math.sqrt((dx * dx) + (dy * dy)) >= mTouchSlop;
    }

}
