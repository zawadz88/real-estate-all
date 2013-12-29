package com.zawadz88.realestate.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import com.zawadz88.realestate.R;

/**
 * Created: 17.11.13
 *
 * @author Zawada
 */
public class FixedAspectRatioViewPager extends ViewPager {
    public static final float DEFAULT_ASPECT_RATIO = 16.0f / 9.0f;
	private float aspectRatio;

	public FixedAspectRatioViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

    private void init(Context context, AttributeSet attrs) {
        TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.FixedAspectRatioView);
        aspectRatio = styledAttrs.getFloat(R.styleable.FixedAspectRatioView_ratio, DEFAULT_ASPECT_RATIO);
        styledAttrs.recycle();
	}

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(aspectRatio == 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
            return;
        }
        super.onMeasure(widthMeasureSpec, (int)(widthMeasureSpec / aspectRatio));
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = (int)(width / aspectRatio);
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
        setMeasuredDimension(width, height);
    }
}