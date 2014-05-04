package br.com.rolesoft.ica_server.model;

public class DeviceSpecifications {
	int width;
	int height;
	double screenSize;
	long avaiableMemory;
	
	
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
}
