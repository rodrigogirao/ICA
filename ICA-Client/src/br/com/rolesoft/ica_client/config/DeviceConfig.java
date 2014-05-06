package br.com.rolesoft.ica_client.config;

import br.com.rolesoft.ica_client.model.DeviceSpecifications;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.util.DisplayMetrics;

public class DeviceConfig {

	public static void configureDeviceSpecifications(DeviceSpecifications device, Activity activity) {
		DisplayMetrics metrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

		device.setWidth(metrics.widthPixels);
		device.setHeight(metrics.heightPixels);

		float widthDpi = metrics.xdpi;
		float heightDpi = metrics.ydpi;
		float widthInches = device.getWidth() / widthDpi;
		float heightInches = device.getHeight() / heightDpi;

		double diagonalInches = Math.sqrt((widthInches * widthInches)
				+ (heightInches * heightInches));
		device.setScreenSize(diagonalInches);

		ActivityManager actManager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo memInfo = new ActivityManager.MemoryInfo();
		actManager.getMemoryInfo(memInfo);
		device.setAvaiableMemory(memInfo.availMem);
	}
}
