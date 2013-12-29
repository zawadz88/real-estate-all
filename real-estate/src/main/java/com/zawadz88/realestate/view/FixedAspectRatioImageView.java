/*******************************************************************************
 * Copyright (c) 2013 Piotr Zawadzki.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Piotr Zawadzki - initial API and implementation
 ******************************************************************************/
package com.zawadz88.realestate.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.zawadz88.realestate.R;

public class FixedAspectRatioImageView extends ImageView {

	public static final float DEFAULT_ASPECT_RATIO = 16.0f / 9.0f;
	private float aspectRatio;

	public FixedAspectRatioImageView(Context context) {
        super(context);
    }

    public FixedAspectRatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
		init(context, attrs);
    }

    public FixedAspectRatioImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
		init(context, attrs);
    }

	private void init(Context context, AttributeSet attrs) {
		TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.FixedAspectRatioView);
		aspectRatio = styledAttrs.getFloat(R.styleable.FixedAspectRatioView_ratio, DEFAULT_ASPECT_RATIO);
		styledAttrs.recycle();
	}

	@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	if(getDrawable() == null || aspectRatio == 0) {
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
    		return;
    	}    	
        int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = (int)(width / aspectRatio);
        setMeasuredDimension(width, height);
    }
}
