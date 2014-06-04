package br.com.rolesoft.ica_server.webservice;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import br.com.rolesoft.ica_server.model.DeviceSpecifications;
import br.com.rolesoft.ica_server.model.DeviceSpecifications.ConnectionType;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class ImageAdapter {
	
	public static String getImage(String json, int numOfImages, String imagesIds, String path){
		System.out.println("entrou no service");
		ArrayList<byte[]> imagesList = new ArrayList<byte[]>();
		Gson gson =  new GsonBuilder().create();
		DeviceSpecifications ds = gson.fromJson(json, DeviceSpecifications.class);
		int width=ds.getWidth();
		int height=ds.getHeight();
		if(ds.getScreenSize()<3.5){
			width=width/2;
			height=height/2;
		}
		if(ds.getAvaiableMemory()<52428800){//52428800==50mb
			width=width/2;
			height=height/2;
		}
		
		if(ds.getConnectionType() == ConnectionType.DATA_CONNECTION) {
			width = width/4;
			height = height/4;
		}
		
		String[] images = imagesIds.split(",");

		try {
			for (int i = 0; i < numOfImages; i++) {
				File file = new File(path + images[i]);
				BufferedImage requestedImage = ImageIO.read(file);
				int imageWidth = requestedImage.getWidth();
				int imageHeight = requestedImage.getHeight();
				double scaleX = (double) width / imageWidth;
				double scaleY = (double) height / imageHeight;
				BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
				AffineTransform at = new AffineTransform();
				at.scale(scaleX, scaleY);
				AffineTransformOp scaleOp = 
				   new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
				scaledImage = scaleOp.filter(requestedImage, scaledImage);
				
				byte[] adaptedImage=imageToArray(scaledImage);
				imagesList.add(adaptedImage);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 		
		String jsonResponse = gson.toJson(imagesList, new TypeToken<ArrayList<byte[]>>(){}.getType());
		return jsonResponse;
	}
	
	private static byte[] imageToArray(Image image){
		  BufferedImage bi = new BufferedImage(image.getWidth(null),image.getHeight(null),BufferedImage.TYPE_INT_RGB);  
		    Graphics bg = bi.getGraphics();  
		    bg.drawImage(image, 0, 0, null);  
		    bg.dispose();  
		      
		    ByteArrayOutputStream buff = new ByteArrayOutputStream();         
		    try {    
		        ImageIO.write(bi, "JPG", buff);    
		    } catch (IOException e) {    
		        e.printStackTrace();    
		    }    
		    return buff.toByteArray(); 	
	}
}
