package com.xinyuyua.comichub;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.StrictMode;

public class PicsService {

	private static int page = 1;
	private static int maxpage;
	private static String tag;
	private static boolean isEmpty;
	
	public static void settag(String str) throws Exception{
		page = 1;
		tag = str;
		int count = TagsService.getcount(tag);
		maxpage = (count+19)/20; 
		if(count == 0)
			isEmpty = true;
		else
			isEmpty = false;
	}
		
	
	public static List<Pic> getPics() throws Exception{
		String path = "http://danbooru.donmai.us/posts.json?tags="+tag+"&limit=9&page="+String.valueOf(page);
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

	private static List<Pic> parseXML(InputStream jsonStream) throws Exception{
		List<Pic> pics = new ArrayList<Pic>();
		byte[] data = StreamTool.read(jsonStream);
		String json = new String(data);
		JSONArray jsonArray = new JSONArray(json);
		for(int i =0;i<jsonArray.length();i++){
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			try{
				jsonObject.getString("large_file_url");
			}catch (Exception e){
				continue;
			}
			Pic pic = new Pic();
			pic.setId(jsonObject.getInt("id"));
			pic.setLargeUrl(jsonObject.getString("large_file_url"));
			pic.setSmallUrl(jsonObject.getString("preview_file_url"));
			pics.add(pic);
		}
		return pics;
	}
	
	public static List<Pic> getNextPics() throws Exception{
		page++;
		return getPics();
	}
	
	public static List<Pic> getPrevPics() throws Exception{
		page--;
		return getPics();
	}
	
	public static boolean nextPageEnable(){
		return page<maxpage;
	}
	
	public static boolean prevPageEnable(){
		return page>1;
	}
	
	public static boolean isEmpty(){
		return isEmpty;
	}
}
