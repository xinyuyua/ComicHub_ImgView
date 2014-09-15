package com.xinyuyua.comichub;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;


/**
 * 
 * @author yuanxinyu
 * get Tag list from a url of json
 * 
 */
public class TagsService {

	public static List<Tag> getTags(String str) throws Exception{
		String path = "http://danbooru.donmai.us/tags.json?search[name_matches]="+str+"*&search[order]=count";
		//StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
		HttpURLConnection conn = (HttpURLConnection)new URL(path).openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		if(conn.getResponseCode()==200){
			InputStream json = conn.getInputStream();
			return parseXML(json);
		}
		return null;
	}

	private static List<Tag> parseXML(InputStream jsonStream) throws Exception{
		List<Tag> tags = new ArrayList<Tag>();
		byte[] data = StreamTool.read(jsonStream);
		String json = new String(data);
		JSONArray jsonArray = new JSONArray(json);
		for(int i =0;i<jsonArray.length();i++){
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			Tag tag = new Tag();
			tag.setId(jsonObject.getInt("id"));
			tag.setTagname(jsonObject.getString("name"));
			tag.setPost(jsonObject.getInt("post_count"));
			tags.add(tag);
		}
		return tags;
	}
	
	public static int getcount(String str) throws Exception{
		String path = "http://danbooru.donmai.us/tags.json?search[name_matches]="+str;
//		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
		HttpURLConnection conn = (HttpURLConnection)new URL(path).openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		if(conn.getResponseCode()==200){
			InputStream json = conn.getInputStream();
			byte[] data = StreamTool.read(json);
			String jsonstr = new String(data);
			JSONArray jsonArray = new JSONArray(jsonstr);
			if(jsonArray.isNull(0))
				return 0;
			JSONObject jsonObject = jsonArray.getJSONObject(0);
			return jsonObject.getInt("post_count");
		}
		return 0;
	}
}
