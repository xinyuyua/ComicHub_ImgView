package com.xinyuyua.comichub;

import com.xinyuyua.comichub.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class PicShowActivty extends Activity {

	private ImageView imageView;
	private int count;
	private Button beforeButton;
	private Button nextButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pic_show);
		beforeButton = (Button) findViewById(R.id.beforeButton);
		nextButton = (Button) findViewById(R.id.nextButton);
		imageView = (ImageView) this.findViewById(R.id.imageView);
		Intent showIntent=getIntent();
		count = showIntent.getIntExtra("count", 0);
		checkClickable();
        showPic();
	}
	
	public void showNext(View v) throws Exception{
		count++;
		checkClickable();
		if(count == MainActivity.pics.size() && MainActivity.picsServer.nextPageEnable()){
			MainActivity.pics = PicsService.getNextPics();
			count = 0;
		}
		showPic();
	}
	
	public void showBefore(View v) throws Exception{
		count--;
		checkClickable();
		if(count == -1 && MainActivity.picsServer.nextPageEnable()){
			MainActivity.pics = PicsService.getPrevPics();
			count = MainActivity.pics.size()-1;
		}
		showPic();
	}
	
	private void showPic(){
		String path = "http://danbooru.donmai.us/"+MainActivity.pics.get(count).getLargeUrl();
			try{
				Bitmap bitmap = ImageService.getImage(path);
				imageView.setImageBitmap(bitmap);
			}catch(Exception e){
				e.printStackTrace();
				Toast.makeText(getApplicationContext(), R.string.error, 2).show();
			}
	}
	
	private void checkClickable(){
		if(count == 0 && !PicsService.prevPageEnable())
			beforeButton.setEnabled(false);
		else
			beforeButton.setEnabled(true);
		if(count == MainActivity.pics.size()-1 && !PicsService.nextPageEnable())
			nextButton.setEnabled(false);
		else
			nextButton.setEnabled(true);
	}
}
