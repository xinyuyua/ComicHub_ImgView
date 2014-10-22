package com.xinyuyua.comichub;

import com.xinyuyua.comichub.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class PicShowActivty extends Activity implements OnGestureListener {

	private ImageView imageView;
	private int count;
	private Button beforeButton;
	private Button nextButton;
	
	private GestureDetector mGestureDetector;  
    private static final int FLING_MIN_DISTANCE = 50;  
    private static final int FLING_MIN_VELOCITY = 0;  
	
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
        
        mGestureDetector = new GestureDetector(this);  
		RelativeLayout ll=(RelativeLayout)findViewById(R.id.picShowLayout);  
        ll.setLongClickable(true);  
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

	@Override
	public boolean onDown(MotionEvent e) {
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
	}
	
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,  
            float velocityY)  
    {  
        // left -> right
        if (e1.getX() - e2.getX() > 120)  
        {  
//            this.viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,  
//                    R.anim.push_left_in));  
//            this.viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,  
//                    R.anim.push_left_out));  
            //this.nextButton.callOnClick();
            try {
				showNext(this.getCurrentFocus());
			} catch (Exception e) {
				e.printStackTrace();
			}
            return true;  
        }// right -> left
        else if (e1.getX() - e2.getX() < -120)  
        {  
//            this.viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,  
//                    R.anim.push_right_in));  
//            this.viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,  
//                    R.anim.push_right_out));  
            try {
				showBefore(this.getCurrentFocus());
			} catch (Exception e) {
				e.printStackTrace();
			}
            return true;  
        }  
        return true;  
    }  
	
	//amazing!!solve the conflict problem
	public boolean dispatchTouchEvent(MotionEvent event)
	{
	     if(mGestureDetector.onTouchEvent(event))
	     {
	            event.setAction(MotionEvent.ACTION_CANCEL);
	     }
	     return super.dispatchTouchEvent(event);
	}
	
}
