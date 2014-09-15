package com.xinyuyua.comichub;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;

/**
 * 
 * @author yuanxinyu
 * Get picture from internet
 * @param String PicturePath
 * @return Bitmap picture
 *
 */

public class ImageService {

	public static Bitmap getImage(String path) throws Exception{
		URL url = new URL(path);
		//StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		if(conn.getResponseCode()==200){
			InputStream inStream = conn.getInputStream();
			Bitmap bitmap = BitmapFactory.decodeStream(inStream);
			return bitmap;
		}
		return null;
	}
}
