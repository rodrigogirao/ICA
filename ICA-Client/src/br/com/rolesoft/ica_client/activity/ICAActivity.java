package br.com.rolesoft.ica_client.activity;

import br.com.rolesoft.ica_client.model.DeviceSpecifications;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;

public class ICAActivity extends Activity {
	private DeviceSpecifications device;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		device = new DeviceSpecifications();
		configureDeviceSpecifications();
		super.onCreate(savedInstanceState);
	}
	
	private void configureDeviceSpecifications() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		device.setWidth(metrics.widthPixels);
		device.setHeight(metrics.heightPixels);

		float widthDpi = metrics.xdpi;
		float heightDpi = metrics.ydpi;
		float widthInches = device.getWidth() / widthDpi;
		float heightInches = device.getHeight() / heightDpi;

		double diagonalInches = Math.sqrt((widthInches * widthInches)
				+ (heightInches * heightInches));
		device.setScreenSize(diagonalInches);

		ActivityManager actManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		MemoryInfo memInfo = new ActivityManager.MemoryInfo();
		actManager.getMemoryInfo(memInfo);
		device.setAvaiableMemory(memInfo.availMem);
	}
}
