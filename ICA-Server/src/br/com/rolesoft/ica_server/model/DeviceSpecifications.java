package br.com.rolesoft.ica_server.model;

public class DeviceSpecifications {
	int width;
	int height;
	double screenSize;
	long avaiableMemory;
	private ConnectionType connectionType;
	private double batteryAvailablePercent;

	public enum ConnectionType {
		WIFI, DATA_CONNECTION, NO_CONNECTION
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public double getScreenSize() {
		return screenSize;
	}

	public void setScreenSize(double screenSize) {
		this.screenSize = screenSize;
	}

	public long getAvaiableMemory() {
		return avaiableMemory;
	}

	public void setAvaiableMemory(long avaiableMemory) {
		this.avaiableMemory = avaiableMemory;
	}

	public ConnectionType getConnectionType() {
		return connectionType;
	}

	public void setConnectionType(ConnectionType connectionType) {
		this.connectionType = connectionType;
	}

	public double getBatteryAvailablePercent() {
		return batteryAvailablePercent;
	}

	public void setBatteryAvaiablePercent(double batteryAvailablePercent) {
		this.batteryAvailablePercent = batteryAvailablePercent;
	}
}
