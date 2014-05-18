package br.com.rolesoft.ica_server.webservice;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import br.com.rolesoft.ica_server.model.DeviceSpecifications;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

@Path("/image")
public class ImageAdapterWS {
	int width;
	int height;
	
	@POST	@Path("/{numOfImages}")
	@Consumes("application/json")
	@Produces("application/json")
	public String getImage(String json, @PathParam("numOfImages") int numOfImages){
		System.out.println("entrou no service");
		ArrayList<byte[]> imagesList = new ArrayList<byte[]>();
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
			for (int i = 0; i < numOfImages; i++) {
				File file = new File("/home/rodrigo/workspace/web/ImageCloudAdapterServer-Prototype/WebContent/images/once_upon_a_time_by_daekazu-d5fsvt4.jpg");
				Image img = ImageIO.read(file);
				BufferedImage image;
				image = ImageIO.read(file);
	//			image = (BufferedImage) image.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING+Image.SCALE_SMOOTH);  
				BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
				
				byte[] adaptedImage=imageToArray(img);
				imagesList.add(adaptedImage);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 		
		String jsonResponse = gson.toJson(imagesList, new TypeToken<ArrayList<byte[]>>(){}.getType());
//		System.out.println("Json Response: " + jsonResponse);
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
	
	@POST @Path("/test")
	public String test(){
		System.out.println("entrou no test service");
		return "WORKS";
	}
}
