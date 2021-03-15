package com.bestpearlstudio.diwaliphotoframe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.OverScroller;
import android.widget.Scroller;

@SuppressLint("AppCompatCustomView")
public class diwali_bui_TouchImageView extends ImageView {
    private static final String DEBUG = "DEBUG";
    private static final float SUPER_MAX_MULTIPLIER = 1.25f;
    private static final float SUPER_MIN_MULTIPLIER = 0.75f;
    private Context context;
    private ZoomVariables delayedZoomVariables;
    private OnDoubleTapListener doubleTapListener = null;
    private Fling fling;
    private boolean imageRenderedAtLeastOnce;
    private float[] f1662m;
    private GestureDetector mGestureDetector;
    private ScaleGestureDetector mScaleDetector;
    private ScaleType mScaleType;
    private float matchViewHeight;
    private float matchViewWidth;
    private Matrix matrix;
    private float maxScale;
    private float minScale;
    private float normalizedScale;
    private boolean onDrawReady;
    private float prevMatchViewHeight;
    private float prevMatchViewWidth;
    private Matrix prevMatrix;
    private int prevViewHeight;
    private int prevViewWidth;
    private State state;
    private float superMaxScale;
    private float superMinScale;
    private OnTouchImageViewListener touchImageViewListener = null;
    private OnTouchListener userTouchListener = null;
    private int viewHeight;
    private int viewWidth;

    static  class C11061 {
        static final  int[] $SwitchMap$android$widget$ImageView$ScaleType = new int[ScaleType.values().length];

        static {
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.CENTER.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.CENTER_CROP.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.CENTER_INSIDE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.FIT_CENTER.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.FIT_XY.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    private class CompatScroller {
        boolean isPreGingerbread;
        OverScroller overScroller;
        Scroller scroller;

        public CompatScroller(Context context) {
            if (VERSION.SDK_INT < 9) {
                this.isPreGingerbread = true;
                this.scroller = new Scroller(context);
                return;
            }
            this.isPreGingerbread = false;
            this.overScroller = new OverScroller(context);
        }

        public void fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY) {
            if (this.isPreGingerbread) {
                this.scroller.fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY);
            } else {
                this.overScroller.fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY);
            }
        }

        public void forceFinished(boolean finished) {
            if (this.isPreGingerbread) {
                this.scroller.forceFinished(finished);
            } else {
                this.overScroller.forceFinished(finished);
            }
        }

        public boolean isFinished() {
            if (this.isPreGingerbread) {
                return this.scroller.isFinished();
            }
            return this.overScroller.isFinished();
        }

        public boolean computeScrollOffset() {
            if (this.isPreGingerbread) {
                return this.scroller.computeScrollOffset();
            }
            this.overScroller.computeScrollOffset();
            return this.overScroller.computeScrollOffset();
        }

        public int getCurrX() {
            if (this.isPreGingerbread) {
                return this.scroller.getCurrX();
            }
            return this.overScroller.getCurrX();
        }

        public int getCurrY() {
            if (this.isPreGingerbread) {
                return this.scroller.getCurrY();
            }
            return this.overScroller.getCurrY();
        }
    }

    private class DoubleTapZoom implements Runnable {
        private static final float ZOOM_TIME = 500.0f;
        private float bitmapX;
        private float bitmapY;
        private PointF endTouch;
        private AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();
        private long startTime;
        private PointF startTouch;
        private float startZoom;
        private boolean stretchImageToSuper;
        private float targetZoom;

        DoubleTapZoom(float targetZoom, float focusX, float focusY, boolean stretchImageToSuper) {
            diwali_bui_TouchImageView.this.setState(State.ANIMATE_ZOOM);
            this.startTime = System.currentTimeMillis();
            this.startZoom = diwali_bui_TouchImageView.this.normalizedScale;
            this.targetZoom = targetZoom;
            this.stretchImageToSuper = stretchImageToSuper;
            PointF bitmapPoint = diwali_bui_TouchImageView.this.transformCoordTouchToBitmap(focusX, focusY, false);
            this.bitmapX = bitmapPoint.x;
            this.bitmapY = bitmapPoint.y;
            this.startTouch = diwali_bui_TouchImageView.this.transformCoordBitmapToTouch(this.bitmapX, this.bitmapY);
            this.endTouch = new PointF((float) (diwali_bui_TouchImageView.this.viewWidth / 2), (float) (diwali_bui_TouchImageView.this.viewHeight / 2));
        }

        public void run() {
            float t = interpolate();
            diwali_bui_TouchImageView.this.scaleImage(calculateDeltaScale(t), this.bitmapX, this.bitmapY, this.stretchImageToSuper);
            translateImageToCenterTouchPosition(t);
            diwali_bui_TouchImageView.this.fixScaleTrans();
            diwali_bui_TouchImageView.this.setImageMatrix(diwali_bui_TouchImageView.this.matrix);
            if (diwali_bui_TouchImageView.this.touchImageViewListener != null) {
                diwali_bui_TouchImageView.this.touchImageViewListener.onMove();
            }
            if (t < 1.0f) {
                diwali_bui_TouchImageView.this.compatPostOnAnimation(this);
            } else {
                diwali_bui_TouchImageView.this.setState(State.NONE);
            }
        }

        private void translateImageToCenterTouchPosition(float t) {
            float targetX = this.startTouch.x + ((this.endTouch.x - this.startTouch.x) * t);
            float targetY = this.startTouch.y + ((this.endTouch.y - this.startTouch.y) * t);
            PointF curr = diwali_bui_TouchImageView.this.transformCoordBitmapToTouch(this.bitmapX, this.bitmapY);
            diwali_bui_TouchImageView.this.matrix.postTranslate(targetX - curr.x, targetY - curr.y);
        }

        private float interpolate() {
            return this.interpolator.getInterpolation(Math.min(1.0f, ((float) (System.currentTimeMillis() - this.startTime)) / ZOOM_TIME));
        }

        private double calculateDeltaScale(float t) {
            return ((double) (this.startZoom + ((this.targetZoom - this.startZoom) * t))) / ((double) diwali_bui_TouchImageView.this.normalizedScale);
        }
    }

    private class Fling implements Runnable {
        int currX;
        int currY;
        CompatScroller scroller;

        Fling(int velocityX, int velocityY) {
            int minX;
            int maxX;
            int minY;
            int maxY;
            diwali_bui_TouchImageView.this.setState(State.FLING);
            this.scroller = new CompatScroller(diwali_bui_TouchImageView.this.context);
            diwali_bui_TouchImageView.this.matrix.getValues(diwali_bui_TouchImageView.this.f1662m);
            int startX = (int) diwali_bui_TouchImageView.this.f1662m[2];
            int startY = (int) diwali_bui_TouchImageView.this.f1662m[5];
            if (diwali_bui_TouchImageView.this.getImageWidth() > ((float) diwali_bui_TouchImageView.this.viewWidth)) {
                minX = diwali_bui_TouchImageView.this.viewWidth - ((int) diwali_bui_TouchImageView.this.getImageWidth());
                maxX = 0;
            } else {
                maxX = startX;
                minX = startX;
            }
            if (diwali_bui_TouchImageView.this.getImageHeight() > ((float) diwali_bui_TouchImageView.this.viewHeight)) {
                minY = diwali_bui_TouchImageView.this.viewHeight - ((int) diwali_bui_TouchImageView.this.getImageHeight());
                maxY = 0;
            } else {
                maxY = startY;
                minY = startY;
            }
            this.scroller.fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY);
            this.currX = startX;
            this.currY = startY;
        }

        public void cancelFling() {
            if (this.scroller != null) {
                diwali_bui_TouchImageView.this.setState(State.NONE);
                this.scroller.forceFinished(true);
            }
        }

        public void run() {
            if (diwali_bui_TouchImageView.this.touchImageViewListener != null) {
                diwali_bui_TouchImageView.this.touchImageViewListener.onMove();
            }
            if (this.scroller.isFinished()) {
                this.scroller = null;
            } else if (this.scroller.computeScrollOffset()) {
                int newX = this.scroller.getCurrX();
                int newY = this.scroller.getCurrY();
                int transX = newX - this.currX;
                int transY = newY - this.currY;
                this.currX = newX;
                this.currY = newY;
                diwali_bui_TouchImageView.this.matrix.postTranslate((float) transX, (float) transY);
                diwali_bui_TouchImageView.this.fixTrans();
                diwali_bui_TouchImageView.this.setImageMatrix(diwali_bui_TouchImageView.this.matrix);
                diwali_bui_TouchImageView.this.compatPostOnAnimation(this);
            }
        }
    }

    private class GestureListener extends SimpleOnGestureListener {
        private GestureListener() {
        }

        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (diwali_bui_TouchImageView.this.doubleTapListener != null) {
                return diwali_bui_TouchImageView.this.doubleTapListener.onSingleTapConfirmed(e);
            }
            return diwali_bui_TouchImageView.this.performClick();
        }

        public void onLongPress(MotionEvent e) {
            diwali_bui_TouchImageView.this.performLongClick();
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (diwali_bui_TouchImageView.this.fling != null) {
                diwali_bui_TouchImageView.this.fling.cancelFling();
            }
            diwali_bui_TouchImageView.this.fling = new Fling((int) velocityX, (int) velocityY);
            diwali_bui_TouchImageView.this.compatPostOnAnimation(diwali_bui_TouchImageView.this.fling);
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        public boolean onDoubleTap(MotionEvent e) {
            boolean consumed = false;
            if (diwali_bui_TouchImageView.this.doubleTapListener != null) {
                consumed = diwali_bui_TouchImageView.this.doubleTapListener.onDoubleTap(e);
            }
            if (diwali_bui_TouchImageView.this.state != State.NONE) {
                return consumed;
            }
            diwali_bui_TouchImageView.this.compatPostOnAnimation(new DoubleTapZoom(diwali_bui_TouchImageView.this.normalizedScale == diwali_bui_TouchImageView.this.minScale ? diwali_bui_TouchImageView.this.maxScale : diwali_bui_TouchImageView.this.minScale, e.getX(), e.getY(), false));
            return true;
        }

        public boolean onDoubleTapEvent(MotionEvent e) {
            if (diwali_bui_TouchImageView.this.doubleTapListener != null) {
                return diwali_bui_TouchImageView.this.doubleTapListener.onDoubleTapEvent(e);
            }
            return false;
        }
    }

    public interface OnTouchImageViewListener {
        void onMove();
    }

    private class PrivateOnTouchListener implements OnTouchListener {
        private PointF last;

        private PrivateOnTouchListener() {
            this.last = new PointF();
        }

        public boolean onTouch(View v, MotionEvent event) {
            diwali_bui_TouchImageView.this.mScaleDetector.onTouchEvent(event);
            diwali_bui_TouchImageView.this.mGestureDetector.onTouchEvent(event);
            PointF curr = new PointF(event.getX(), event.getY());
            if (diwali_bui_TouchImageView.this.state == State.NONE || diwali_bui_TouchImageView.this.state == State.DRAG || diwali_bui_TouchImageView.this.state == State.FLING) {
                switch (event.getAction()) {
                    case 0:
                        this.last.set(curr);
                        if (diwali_bui_TouchImageView.this.fling != null) {
                            diwali_bui_TouchImageView.this.fling.cancelFling();
                        }
                        diwali_bui_TouchImageView.this.setState(State.DRAG);
                        break;
                    case 1:
                    case 6:
                        diwali_bui_TouchImageView.this.setState(State.NONE);
                        break;
                    case 2:
                        if (diwali_bui_TouchImageView.this.state == State.DRAG) {
                            float deltaY = curr.y - this.last.y;
                            diwali_bui_TouchImageView.this.matrix.postTranslate(diwali_bui_TouchImageView.this.getFixDragTrans(curr.x - this.last.x, (float) diwali_bui_TouchImageView.this.viewWidth, diwali_bui_TouchImageView.this.getImageWidth()), diwali_bui_TouchImageView.this.getFixDragTrans(deltaY, (float) diwali_bui_TouchImageView.this.viewHeight, diwali_bui_TouchImageView.this.getImageHeight()));
                            diwali_bui_TouchImageView.this.fixTrans();
                            this.last.set(curr.x, curr.y);
                            break;
                        }
                        break;
                }
            }
            diwali_bui_TouchImageView.this.setImageMatrix(diwali_bui_TouchImageView.this.matrix);
            if (diwali_bui_TouchImageView.this.userTouchListener != null) {
                diwali_bui_TouchImageView.this.userTouchListener.onTouch(v, event);
            }
            if (diwali_bui_TouchImageView.this.touchImageViewListener != null) {
                diwali_bui_TouchImageView.this.touchImageViewListener.onMove();
            }
            return true;
        }
    }

    private class ScaleListener extends SimpleOnScaleGestureListener {
        private ScaleListener() {
        }

        public boolean onScaleBegin(ScaleGestureDetector detector) {
            diwali_bui_TouchImageView.this.setState(State.ZOOM);
            return true;
        }

        public boolean onScale(ScaleGestureDetector detector) {
            diwali_bui_TouchImageView.this.scaleImage((double) detector.getScaleFactor(), detector.getFocusX(), detector.getFocusY(), true);
            if (diwali_bui_TouchImageView.this.touchImageViewListener != null) {
                diwali_bui_TouchImageView.this.touchImageViewListener.onMove();
            }
            return true;
        }

        public void onScaleEnd(ScaleGestureDetector detector) {
            super.onScaleEnd(detector);
            diwali_bui_TouchImageView.this.setState(State.NONE);
            boolean animateToZoomBoundary = false;
            float targetZoom = diwali_bui_TouchImageView.this.normalizedScale;
            if (diwali_bui_TouchImageView.this.normalizedScale > diwali_bui_TouchImageView.this.maxScale) {
                targetZoom = diwali_bui_TouchImageView.this.maxScale;
                animateToZoomBoundary = true;
            } else if (diwali_bui_TouchImageView.this.normalizedScale < diwali_bui_TouchImageView.this.minScale) {
                targetZoom = diwali_bui_TouchImageView.this.minScale;
                animateToZoomBoundary = true;
            }
            if (animateToZoomBoundary) {
                diwali_bui_TouchImageView.this.compatPostOnAnimation(new DoubleTapZoom(targetZoom, (float) (diwali_bui_TouchImageView.this.viewWidth / 2), (float) (diwali_bui_TouchImageView.this.viewHeight / 2), true));
            }
        }
    }

    private enum State {
        NONE,
        DRAG,
        ZOOM,
        FLING,
        ANIMATE_ZOOM
    }

    private class ZoomVariables {
        public float focusX;
        public float focusY;
        public float scale;
        public ScaleType scaleType;

        public ZoomVariables(float scale, float focusX, float focusY, ScaleType scaleType) {
            this.scale = scale;
            this.focusX = focusX;
            this.focusY = focusY;
            this.scaleType = scaleType;
        }
    }

    public diwali_bui_TouchImageView(Context context) {
        super(context);
        sharedConstructing(context);
    }

    public diwali_bui_TouchImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        sharedConstructing(context);
    }

    public diwali_bui_TouchImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        sharedConstructing(context);
    }

    private void sharedConstructing(Context context) {
        super.setClickable(true);
        this.context = context;
        this.mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        this.mGestureDetector = new GestureDetector(context, new GestureListener());
        this.matrix = new Matrix();
        this.prevMatrix = new Matrix();
        this.f1662m = new float[9];
        this.normalizedScale = 1.0f;
        if (this.mScaleType == null) {
            this.mScaleType = ScaleType.FIT_CENTER;
        }
        this.minScale = 1.0f;
        this.maxScale = 3.0f;
        this.superMinScale = 0.75f * this.minScale;
        this.superMaxScale = SUPER_MAX_MULTIPLIER * this.maxScale;
        setImageMatrix(this.matrix);
        setScaleType(ScaleType.MATRIX);
        setState(State.NONE);
        this.onDrawReady = false;
        super.setOnTouchListener(new PrivateOnTouchListener());
    }

    public void setOnTouchListener(OnTouchListener l) {
        this.userTouchListener = l;
    }

    public void setOnTouchImageViewListener(OnTouchImageViewListener l) {
        this.touchImageViewListener = l;
    }

    public void setOnDoubleTapListener(OnDoubleTapListener l) {
        this.doubleTapListener = l;
    }

    public void setImageResource(int resId) {
        super.setImageResource(resId);
        savePreviousImageValues();
        fitImageToView();
    }

    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        savePreviousImageValues();
        fitImageToView();
    }

    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        savePreviousImageValues();
        fitImageToView();
    }

    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        savePreviousImageValues();
        fitImageToView();
    }

    public void setScaleType(ScaleType type) {
        if (type == ScaleType.FIT_START || type == ScaleType.FIT_END) {
            throw new UnsupportedOperationException("TouchImageView does not support FIT_START or FIT_END");
        } else if (type == ScaleType.MATRIX) {
            super.setScaleType(ScaleType.MATRIX);
        } else {
            this.mScaleType = type;
            if (this.onDrawReady) {
                setZoom(this);
            }
        }
    }

    public ScaleType getScaleType() {
        return this.mScaleType;
    }

    public boolean isZoomed() {
        return this.normalizedScale != 1.0f;
    }

    public RectF getZoomedRect() {
        if (this.mScaleType == ScaleType.FIT_XY) {
            throw new UnsupportedOperationException("getZoomedRect() not supported with FIT_XY");
        }
        PointF topLeft = transformCoordTouchToBitmap(0.0f, 0.0f, true);
        PointF bottomRight = transformCoordTouchToBitmap((float) this.viewWidth, (float) this.viewHeight, true);
        float w = (float) getDrawable().getIntrinsicWidth();
        float h = (float) getDrawable().getIntrinsicHeight();
        return new RectF(topLeft.x / w, topLeft.y / h, bottomRight.x / w, bottomRight.y / h);
    }

    private void savePreviousImageValues() {
        if (this.matrix != null && this.viewHeight != 0 && this.viewWidth != 0) {
            this.matrix.getValues(this.f1662m);
            this.prevMatrix.setValues(this.f1662m);
            this.prevMatchViewHeight = this.matchViewHeight;
            this.prevMatchViewWidth = this.matchViewWidth;
            this.prevViewHeight = this.viewHeight;
            this.prevViewWidth = this.viewWidth;
        }
    }

    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putFloat("saveScale", this.normalizedScale);
        bundle.putFloat("matchViewHeight", this.matchViewHeight);
        bundle.putFloat("matchViewWidth", this.matchViewWidth);
        bundle.putInt("viewWidth", this.viewWidth);
        bundle.putInt("viewHeight", this.viewHeight);
        this.matrix.getValues(this.f1662m);
        bundle.putFloatArray("matrix", this.f1662m);
        bundle.putBoolean("imageRendered", this.imageRenderedAtLeastOnce);
        return bundle;
    }

    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            this.normalizedScale = bundle.getFloat("saveScale");
            this.f1662m = bundle.getFloatArray("matrix");
            this.prevMatrix.setValues(this.f1662m);
            this.prevMatchViewHeight = bundle.getFloat("matchViewHeight");
            this.prevMatchViewWidth = bundle.getFloat("matchViewWidth");
            this.prevViewHeight = bundle.getInt("viewHeight");
            this.prevViewWidth = bundle.getInt("viewWidth");
            this.imageRenderedAtLeastOnce = bundle.getBoolean("imageRendered");
            super.onRestoreInstanceState(bundle.getParcelable("instanceState"));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    protected void onDraw(Canvas canvas) {
        this.onDrawReady = true;
        this.imageRenderedAtLeastOnce = true;
        if (this.delayedZoomVariables != null) {
            setZoom(this.delayedZoomVariables.scale, this.delayedZoomVariables.focusX, this.delayedZoomVariables.focusY, this.delayedZoomVariables.scaleType);
            this.delayedZoomVariables = null;
        }
        super.onDraw(canvas);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        savePreviousImageValues();
    }

    public float getMaxZoom() {
        return this.maxScale;
    }

    public void setMaxZoom(float max) {
        this.maxScale = max;
        this.superMaxScale = SUPER_MAX_MULTIPLIER * this.maxScale;
    }

    public float getMinZoom() {
        return this.minScale;
    }

    public float getCurrentZoom() {
        return this.normalizedScale;
    }

    public void setMinZoom(float min) {
        this.minScale = min;
        this.superMinScale = 0.75f * this.minScale;
    }

    public void resetZoom() {
        this.normalizedScale = 1.0f;
        fitImageToView();
    }

    public void setZoom(float scale) {
        setZoom(scale, 0.5f, 0.5f);
    }

    public void setZoom(float scale, float focusX, float focusY) {
        setZoom(scale, focusX, focusY, this.mScaleType);
    }

    public void setZoom(float scale, float focusX, float focusY, ScaleType scaleType) {
        if (this.onDrawReady) {
            if (scaleType != this.mScaleType) {
                setScaleType(scaleType);
            }
            resetZoom();
            scaleImage((double) scale, (float) (this.viewWidth / 2), (float) (this.viewHeight / 2), true);
            this.matrix.getValues(this.f1662m);
            this.f1662m[2] = -((getImageWidth() * focusX) - (((float) this.viewWidth) * 0.5f));
            this.f1662m[5] = -((getImageHeight() * focusY) - (((float) this.viewHeight) * 0.5f));
            this.matrix.setValues(this.f1662m);
            fixTrans();
            setImageMatrix(this.matrix);
            return;
        }
        this.delayedZoomVariables = new ZoomVariables(scale, focusX, focusY, scaleType);
    }

    public void setZoom(diwali_bui_TouchImageView img) {
        PointF center = img.getScrollPosition();
        setZoom(img.getCurrentZoom(), center.x, center.y, img.getScaleType());
    }

    public PointF getScrollPosition() {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return null;
        }
        int drawableWidth = drawable.getIntrinsicWidth();
        int drawableHeight = drawable.getIntrinsicHeight();
        PointF point = transformCoordTouchToBitmap((float) (this.viewWidth / 2), (float) (this.viewHeight / 2), true);
        point.x /= (float) drawableWidth;
        point.y /= (float) drawableHeight;
        return point;
    }

    public void setScrollPosition(float focusX, float focusY) {
        setZoom(this.normalizedScale, focusX, focusY);
    }

    private void fixTrans() {
        this.matrix.getValues(this.f1662m);
        float transX = this.f1662m[2];
        float transY = this.f1662m[5];
        float fixTransX = getFixTrans(transX, (float) this.viewWidth, getImageWidth());
        float fixTransY = getFixTrans(transY, (float) this.viewHeight, getImageHeight());
        if (fixTransX != 0.0f || fixTransY != 0.0f) {
            this.matrix.postTranslate(fixTransX, fixTransY);
        }
    }

    private void fixScaleTrans() {
        fixTrans();
        this.matrix.getValues(this.f1662m);
        if (getImageWidth() < ((float) this.viewWidth)) {
            this.f1662m[2] = (((float) this.viewWidth) - getImageWidth()) / 2.0f;
        }
        if (getImageHeight() < ((float) this.viewHeight)) {
            this.f1662m[5] = (((float) this.viewHeight) - getImageHeight()) / 2.0f;
        }
        this.matrix.setValues(this.f1662m);
    }

    private float getFixTrans(float trans, float viewSize, float contentSize) {
        float minTrans;
        float maxTrans;
        if (contentSize <= viewSize) {
            minTrans = 0.0f;
            maxTrans = viewSize - contentSize;
        } else {
            minTrans = viewSize - contentSize;
            maxTrans = 0.0f;
        }
        if (trans < minTrans) {
            return (-trans) + minTrans;
        }
        if (trans > maxTrans) {
            return (-trans) + maxTrans;
        }
        return 0.0f;
    }

    private float getFixDragTrans(float delta, float viewSize, float contentSize) {
        if (contentSize <= viewSize) {
            return 0.0f;
        }
        return delta;
    }

    private float getImageWidth() {
        return this.matchViewWidth * this.normalizedScale;
    }

    private float getImageHeight() {
        return this.matchViewHeight * this.normalizedScale;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable drawable = getDrawable();
        if (drawable == null || drawable.getIntrinsicWidth() == 0 || drawable.getIntrinsicHeight() == 0) {
            setMeasuredDimension(0, 0);
            return;
        }
        int drawableWidth = drawable.getIntrinsicWidth();
        int drawableHeight = drawable.getIntrinsicHeight();
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        this.viewWidth = setViewSize(widthMode, widthSize, drawableWidth);
        this.viewHeight = setViewSize(heightMode, heightSize, drawableHeight);
        setMeasuredDimension(this.viewWidth, this.viewHeight);
        fitImageToView();
    }

    private void fitImageToView() {
        Drawable drawable = getDrawable();
        if (drawable != null && drawable.getIntrinsicWidth() != 0 && drawable.getIntrinsicHeight() != 0 && this.matrix != null && this.prevMatrix != null) {
            int drawableWidth = drawable.getIntrinsicWidth();
            int drawableHeight = drawable.getIntrinsicHeight();
            float scaleX = ((float) this.viewWidth) / ((float) drawableWidth);
            float scaleY = ((float) this.viewHeight) / ((float) drawableHeight);
            switch (C11061.$SwitchMap$android$widget$ImageView$ScaleType[this.mScaleType.ordinal()]) {
                case 1:
                    scaleY = 1.0f;
                    scaleX = 1.0f;
                    break;
                case 2:
                    scaleY = Math.max(scaleX, scaleY);
                    scaleX = scaleY;
                    break;
                case 3:
                    scaleY = Math.min(1.0f, Math.min(scaleX, scaleY));
                    scaleX = scaleY;
                    break;
                case 4:
                    break;
                case 5:
                    break;
                default:
                    throw new UnsupportedOperationException("TouchImageView does not support FIT_START or FIT_END");
            }
            scaleY = Math.min(scaleX, scaleY);
            scaleX = scaleY;
            float redundantXSpace = ((float) this.viewWidth) - (((float) drawableWidth) * scaleX);
            float redundantYSpace = ((float) this.viewHeight) - (((float) drawableHeight) * scaleY);
            this.matchViewWidth = ((float) this.viewWidth) - redundantXSpace;
            this.matchViewHeight = ((float) this.viewHeight) - redundantYSpace;
            if (isZoomed() || this.imageRenderedAtLeastOnce) {
                if (this.prevMatchViewWidth == 0.0f || this.prevMatchViewHeight == 0.0f) {
                    savePreviousImageValues();
                }
                this.prevMatrix.getValues(this.f1662m);
                this.f1662m[0] = (this.matchViewWidth / ((float) drawableWidth)) * this.normalizedScale;
                this.f1662m[4] = (this.matchViewHeight / ((float) drawableHeight)) * this.normalizedScale;
                float transX = this.f1662m[2];
                float transY = this.f1662m[5];
                translateMatrixAfterRotate(2, transX, this.prevMatchViewWidth * this.normalizedScale, getImageWidth(), this.prevViewWidth, this.viewWidth, drawableWidth);
                translateMatrixAfterRotate(5, transY, this.prevMatchViewHeight * this.normalizedScale, getImageHeight(), this.prevViewHeight, this.viewHeight, drawableHeight);
                this.matrix.setValues(this.f1662m);
            } else {
                this.matrix.setScale(scaleX, scaleY);
                this.matrix.postTranslate(redundantXSpace / 2.0f, redundantYSpace / 2.0f);
                this.normalizedScale = 1.0f;
            }
            fixTrans();
            setImageMatrix(this.matrix);
        }
    }

    private int setViewSize(int mode, int size, int drawableWidth) {
        switch (mode) {
            case Integer.MIN_VALUE:
                return Math.min(drawableWidth, size);
            case 0:
                return drawableWidth;
            case 1073741824:
                return size;
            default:
                return size;
        }
    }

    private void translateMatrixAfterRotate(int axis, float trans, float prevImageSize, float imageSize, int prevViewSize, int viewSize, int drawableSize) {
        if (imageSize < ((float) viewSize)) {
            this.f1662m[axis] = (((float) viewSize) - (((float) drawableSize) * this.f1662m[0])) * 0.5f;
        } else if (trans > 0.0f) {
            this.f1662m[axis] = -((imageSize - ((float) viewSize)) * 0.5f);
        } else {
            this.f1662m[axis] = -((((Math.abs(trans) + (((float) prevViewSize) * 0.5f)) / prevImageSize) * imageSize) - (((float) viewSize) * 0.5f));
        }
    }

    private void setState(State state) {
        this.state = state;
    }

    public boolean canScrollHorizontallyFroyo(int direction) {
        return canScrollHorizontally(direction);
    }

    public boolean canScrollHorizontally(int direction) {
        this.matrix.getValues(this.f1662m);
        float x = this.f1662m[2];
        if (getImageWidth() < ((float) this.viewWidth)) {
            return false;
        }
        if (x >= -1.0f && direction < 0) {
            return false;
        }
        if ((Math.abs(x) + ((float) this.viewWidth)) + 1.0f < getImageWidth() || direction <= 0) {
            return true;
        }
        return false;
    }

    private void scaleImage(double deltaScale, float focusX, float focusY, boolean stretchImageToSuper) {
        float lowerScale;
        float upperScale;
        if (stretchImageToSuper) {
            lowerScale = this.superMinScale;
            upperScale = this.superMaxScale;
        } else {
            lowerScale = this.minScale;
            upperScale = this.maxScale;
        }
        float origScale = this.normalizedScale;
        this.normalizedScale = (float) (((double) this.normalizedScale) * deltaScale);
        if (this.normalizedScale > upperScale) {
            this.normalizedScale = upperScale;
            deltaScale = (double) (upperScale / origScale);
        } else if (this.normalizedScale < lowerScale) {
            this.normalizedScale = lowerScale;
            deltaScale = (double) (lowerScale / origScale);
        }
        this.matrix.postScale((float) deltaScale, (float) deltaScale, focusX, focusY);
        fixScaleTrans();
    }

    private PointF transformCoordTouchToBitmap(float x, float y, boolean clipToBitmap) {
        this.matrix.getValues(this.f1662m);
        float origW = (float) getDrawable().getIntrinsicWidth();
        float origH = (float) getDrawable().getIntrinsicHeight();
        float transX = this.f1662m[2];
        float finalX = ((x - transX) * origW) / getImageWidth();
        float finalY = ((y - this.f1662m[5]) * origH) / getImageHeight();
        if (clipToBitmap) {
            finalX = Math.min(Math.max(finalX, 0.0f), origW);
            finalY = Math.min(Math.max(finalY, 0.0f), origH);
        }
        return new PointF(finalX, finalY);
    }

    private PointF transformCoordBitmapToTouch(float bx, float by) {
        this.matrix.getValues(this.f1662m);
        return new PointF(this.f1662m[2] + (getImageWidth() * (bx / ((float) getDrawable().getIntrinsicWidth()))), this.f1662m[5] + (getImageHeight() * (by / ((float) getDrawable().getIntrinsicHeight()))));
    }

    private void compatPostOnAnimation(Runnable runnable) {
        if (VERSION.SDK_INT >= 16) {
            postOnAnimation(runnable);
        } else {
            postDelayed(runnable, 16);
        }
    }

    private void printMatrixInfo() {
        float[] n = new float[9];
        this.matrix.getValues(n);
        Log.d(DEBUG, "Scale: " + n[0] + " TransX: " + n[2] + " TransY: " + n[5]);
    }
}
