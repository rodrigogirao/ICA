package br.com.rolesoft.ica_client.task;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

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
import android.widget.ImageView;
import br.com.rolesoft.ica_client.config.DeviceConfig;
import br.com.rolesoft.ica_client.model.DeviceSpecifications;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ReceiveImageTask extends AsyncTask<Void, Void, Bitmap> {
	
	private ImageView taskView;
	private Activity activity;

	
	public ReceiveImageTask(ImageView taskView, Activity activity) {
		this.taskView = taskView;
		this.activity = activity;
	}
	@Override
	protected Bitmap doInBackground(Void... arg0) {
		Bitmap bmp=null;
		Gson gson = new GsonBuilder().create();
		DeviceSpecifications device = new DeviceSpecifications();
		DeviceConfig.configureDeviceSpecifications(device, activity);
		String jsonDevice = gson.toJson(device);

		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(
				CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		HttpPost httppost = new HttpPost("http://localhost:8080/ICA-Server/image");

		try {
			StringEntity entity = new StringEntity(jsonDevice, "UTF-8");
			entity.setContentType("application/json;charset=UTF-8");
			entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json;charset=UTF-8"));
			httppost.setEntity(entity);
			HttpResponse response = httpclient.execute(httppost);
			String jsonResponse = EntityUtils.toString(response.getEntity());
			System.out.println(jsonResponse);
			byte[] bytesImage = gson.fromJson(jsonResponse, byte[].class);
			//Decode image size
			//BitmapFactory.Options o = new BitmapFactory.Options();
			//o.inJustDecodeBounds = true;
			//o.inSampleSize = 100;
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inDither = false;
			opt.inScaled = false;
			opt.inPurgeable = true;
			
			bmp = BitmapFactory.decodeByteArray(bytesImage, 0, bytesImage.length,opt);

			httpclient.getConnectionManager().shutdown();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bmp;
	}
	@Override
	protected void onPostExecute(Bitmap bmp) {
		System.out.println("ON POST EXECUTE");
		System.out.println(bmp);
		taskView.setImageBitmap(bmp);
	}
}