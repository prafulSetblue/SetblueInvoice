package com.setblue.invoice.application;

import android.app.Application;

import com.androidquery.AQuery;
import com.setblue.invoice.utils.TypefaceUtil;


//@ReportsCrashes(formUri = commonVariables.CRASH_REPORT_SERVER_URL, formKey = "", mode = ReportingInteractionMode.TOAST, resToastText = R.string.crash_toast_text)
public class App extends Application {
	@Override
	public void onCreate() {
		// The following line triggers the initialization of ACRA
		super.onCreate();
		// ACRA.init(this);
		AQuery aQuery = new AQuery(this);
		//TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "Roboto-Regular.ttf"); // font
																									// from
																									// assets:
																									// "assets/fonts/Roboto-Regular.ttf
	}
}
