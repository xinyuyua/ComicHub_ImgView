package com.xinyuyua.comichub;

import java.util.List;

import org.json.JSONObject;

public class AdvanceSearch {
	
	private static boolean safesearch = true;
	
	public static boolean check(JSONObject jsonObject) throws Exception {
		if(safesearch){
			if(jsonObject.getString("rating").equals("s"))
				return true;
			else
				return false;
		}
		return true;
	}
	
	public static boolean safeSearchOpen(){
		return safesearch;
	}
	public void setSafeSearch(boolean input){
		safesearch = input;
	}
}
