package br.com.rolesoft.ica_client.task;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.ViewGroup;
import android.widget.ImageView;
import br.com.rolesoft.ica_client.config.DeviceConfig;
import br.com.rolesoft.ica_client.model.DeviceSpecifications;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class ReceiveImageTask extends AsyncTask<Void, Void, ArrayList<Bitmap>> {
	
	private ViewGroup taskView;
	private Activity activity;
	private String url;
	private int numOfImages;
	
	public ReceiveImageTask(ViewGroup taskView, Activity activity, String url) {
		this.taskView = taskView;
		this.activity = activity;
		this.url = url;
		this.numOfImages = 1;
	}
	public ReceiveImageTask(ViewGroup taskView, Activity activity, String url, int numOfImages) {
		this.taskView = taskView;
		this.activity = activity;
		this.url = url;
		if (numOfImages > 1) {
			this.numOfImages = numOfImages;
		} else {
			this.numOfImages = 1;
		}
	}
	
	@Override
	protected ArrayList<Bitmap> doInBackground(Void... arg0) {
		ArrayList<Bitmap> bmpList = null;
		Gson gson = new Gson();
		DeviceSpecifications device = new DeviceSpecifications();
		DeviceConfig.configureDeviceSpecifications(device, activity);
		String jsonDevice = gson.toJson(device);

		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(
				CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		HttpPost httppost = new HttpPost(url);

		try {
			StringEntity entity = new StringEntity(jsonDevice, "UTF-8");
			entity.setContentType("application/json;charset=UTF-8");
			entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json;charset=UTF-8"));
			httppost.setEntity(entity);
			HttpResponse response = httpclient.execute(httppost);
			System.out.println("Entity response: " + response.getEntity().getContentLength() );
			String jsonResponse = EntityUtils.toString(response.getEntity());
			System.out.println("Json response: " + jsonResponse );
			if (jsonResponse != null) {
				ArrayList<byte[]> imagesArray = gson.fromJson(jsonResponse, new TypeToken<ArrayList<byte[]>>(){}.getType());
				bmpList = new ArrayList<Bitmap>();
				//Decode image size
				for (int i = 0; i < numOfImages; i++) {
					byte[] bytesImage = imagesArray.get(i);
					
					BitmapFactory.Options opt = new BitmapFactory.Options();
					opt.inDither = false;
					opt.inScaled = false;
					opt.inPurgeable = true;
				
					Bitmap bmp = BitmapFactory.decodeByteArray(bytesImage, 0, bytesImage.length,opt);
					bmpList.add(bmp);
				}
			}

			httpclient.getConnectionManager().shutdown();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bmpList;
	}
	@Override
	protected void onPostExecute(ArrayList<Bitmap> bmpList) {
		System.out.println("ON POST EXECUTE");
		System.out.println(bmpList);
		if(bmpList != null) {
			for (Bitmap bitmap : bmpList) {
				ImageView imageView = new ImageView(activity);
				imageView.setImageBitmap(bitmap);
				taskView.addView(imageView);
			}
		}
		else
			System.out.println("BMP NULL");
	}
}