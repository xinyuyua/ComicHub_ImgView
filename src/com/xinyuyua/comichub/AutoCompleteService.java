package com.xinyuyua.comichub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 
 * @author yuanxinyu
 *	get autocomplete strings
 * 
 */

public class AutoCompleteService {
	public static String[] getTagList(String str) throws Exception{
		List<Tag> tags = TagsService.getTags(str);
		List<HashMap<String,Object>> data = new ArrayList<HashMap<String,Object>>();
		for(Tag tag:tags){
			HashMap<String,Object> item = new HashMap<String,Object>();
			item.put("id",tag.getId());
			item.put("name",tag.getTagname());
			item.put("post",tag.getPost());
			data.add(item);
		}
		int size = 10<data.size()?10:data.size();
		String[] result = new String[size];
		for(int i = 0;i<size;i++){
			result[i] = (String) data.get(i).get("name");
		}
		return result;
	}
	

}