package com.zawadz88.realestate.util;

import android.os.Build;

/**
 * Created: 17.11.13
 *
 * @author Zawada
 */
public class DeviceUtils {

	public static boolean isEqualOrHigherThanHoneycomb() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	}
	public static boolean isEqualOrHigherThanICS() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
	}

}
