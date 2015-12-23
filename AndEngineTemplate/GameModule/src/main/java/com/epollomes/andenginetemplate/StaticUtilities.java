package com.epollomes.andenginetemplate;

public class StaticUtilities {

	// this method assumes that the design was made with an HD file
	public static float getScaledBasedOnHDSize(float width, float valueForHD) {
		if (width < 600) {
			// Asset is based on 480X320
			return (valueForHD * (1200.0f / 480.0f));
		} else if (width < 1100) {
			// Asset based on 800X480
			return (valueForHD * (1200.0f / 800.0f));
		} else {
			// Asset based on 1200X720
			return (valueForHD);
		}
	}

	public static float getScaledSmallOrNot(float width, float value) {
		if (width < 600) {
			// Asset is based on 480X320
			return (value * (800.0f / 480.0f));
		} else {
			return (value);
		}
	}

}
