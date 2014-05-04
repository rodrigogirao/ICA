package br.com.rolesoft.ica_server.webservice;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;

import br.com.rolesoft.ica_server.model.DeviceSpecifications;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ImageAdapterWS {
	int width;
	int height;
	
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public String getImage(String json){
		byte[] adaptedImage=null;
		Gson gson =  new GsonBuilder().create();
		DeviceSpecifications ds = gson.fromJson(json, DeviceSpecifications.class);
		width=ds.getWidth();
		height=ds.getHeight();
		if(ds.getScreenSize()<3.5){
			width=width/2;
			height=height/2;
		}
		if(ds.getAvaiableMemory()<52428800){//52428800==50mb
			width=width/2;
			height=height/2;
		}
		
		
		
		
		try {
			File file = new File("/home/rodrigo/workspace/web/ImageCloudAdapterServer-Prototype/WebContent/images/android_robot-3333px.png");
			Image img = ImageIO.read(file);
//			BufferedImage image;
//			image = ImageIO.read(file);
//			image = (BufferedImage) image.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING+Image.SCALE_SMOOTH);  
//			BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
//
//		    // Draw the image on to the buffered image
//		    Graphics2D bGr = bimage.createGraphics();
//		    bGr.drawImage(img, 0, 0, null);
//		    bGr.dispose();
			
			adaptedImage=imageToArray(img);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 		
		String jsonResponse = gson.toJson(adaptedImage);
		return jsonResponse;
	}
	
	private byte[] imageToArray(Image image){
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
