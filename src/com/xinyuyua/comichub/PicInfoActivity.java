package com.xinyuyua.comichub;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class PicInfoActivity extends Activity {

	TextView show_info_id;
	TextView show_info_uploader;
	TextView show_info_date;
	TextView show_info_width;
	TextView show_info_height;
	TextView show_info_path;
	TextView show_info_tags;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pic_info);
		
		Intent showIntent=getIntent();
		int count = showIntent.getIntExtra("count", 0);
		
		HashMap<String, String> info = new HashMap<String,String>();
		try {
			info = PicsService.getPicInfo(MainActivity.pics.get(count).getId());
			System.out.println(info.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		show_info_id =  (TextView) findViewById(R.id.show_info_id);
		show_info_uploader =  (TextView) findViewById(R.id.show_info_uploader);
		show_info_date =  (TextView) findViewById(R.id.show_info_date);
		show_info_width =  (TextView) findViewById(R.id.show_info_width);
		show_info_height =  (TextView) findViewById(R.id.show_info_height);
		show_info_path =  (TextView) findViewById(R.id.show_info_path);
		show_info_tags =  (TextView) findViewById(R.id.show_info_tags);
		
		show_info_id.setText(info.get("id"));
		show_info_uploader.setText(info.get("uploader_name"));
		show_info_date.setText(info.get("create date"));
		show_info_width.setText(info.get("width"));
		show_info_height.setText(info.get("height"));
		show_info_path.setText(info.get("picpath"));
		show_info_tags.setText(info.get("tags"));
		
		
	}
}
