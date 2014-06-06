package br.com.rolesoft.ica_client.config;

import br.com.rolesoft.ica_client.model.DeviceSpecifications;
import br.com.rolesoft.ica_client.model.DeviceSpecifications.ConnectionType;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
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
		
		ConnectivityManager cm = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info != null && info.isConnected()) {
			if (info.getType() == ConnectivityManager.TYPE_WIFI) {
				device.setConnectionType(ConnectionType.WIFI);
			} else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
				device.setConnectionType(ConnectionType.DATA_CONNECTION);
			}
		} else {
			device.setConnectionType(ConnectionType.NO_CONNECTION);
		}
		
		IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		Intent batteryStatus = activity.registerReceiver(null, ifilter);
		int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
		double levelDouble = Integer.valueOf(level).doubleValue();
		double scaleDouble = Integer.valueOf(scale).doubleValue();
		double batteryPercentage = (levelDouble / scaleDouble) * 100;
		device.setBatteryAvailablePercent(batteryPercentage);
		
	}
}
