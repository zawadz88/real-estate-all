package com.zawadz88.realestate.view;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import com.zawadz88.realestate.R;

/**
 * A custom implementation of DrawerLayout exposing isDrawerOpen method without any parameters
 * @author Piotr Zawadzki.
 */
public class RealEstateDrawerLayout extends DrawerLayout {
    public RealEstateDrawerLayout(Context context) {
        super(context);
    }

    public RealEstateDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RealEstateDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean isDrawerOpen() {
        return isDrawerOpen(this.findViewById(R.id.navigation_drawer));
    }
}
