package com.android.denysyuk.conlab.animation;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by dreamfire on 02.10.15.
 */
public class FlipAnimation extends Animation {
    private Camera mCamera;

    private View mFromView;
    private View mToView;

    private float mCenterX;
    private float mCenterY;

    private boolean mForward = true;

    public FlipAnimation(View _fromView, View _toView) {
        this.mFromView = _fromView;
        this.mToView = _toView;

        setDuration(700);
        setFillAfter(false);
        setInterpolator(new AccelerateDecelerateInterpolator());
    }

    public void reverse() {
        mForward = false;
        View switchView = mToView;
        mToView = mFromView;
        mFromView = switchView;
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        mCenterX = width / 2;
        mCenterY = height / 2;
        mCamera = new Camera();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        final double radians = Math.PI * interpolatedTime;
        float degrees = (float) (180.0 * radians / Math.PI);

        if (interpolatedTime >= 0.5f) {
            degrees -= 180.f;
            mFromView.setVisibility(View.GONE);
            mToView.setVisibility(View.VISIBLE);
        }

        if (mForward)
            degrees = -degrees;

        final Matrix matrix = t.getMatrix();
        mCamera.save();
        mCamera.rotateY(degrees);
        mCamera.getMatrix(matrix);
        mCamera.restore();
        matrix.preTranslate(-mCenterX, -mCenterY);
        matrix.postTranslate(mCenterX, mCenterY);
    }
}
