package com.xinyuyua.comichub;

import java.util.LinkedList;
import java.util.List;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnItemClickListener{

	private static String[] autostr;
	private AutoCompleteTextView autoCompleteTextView = null; 
	//private GridView gridView;
	
	
	//Use pulltorefresh scource code
	private PullToRefreshGridView gridView;  
	private gridAdapter gridAdapter;
	private LinkedList<String> mListItems;  
    private ArrayAdapter<String> mAdapter;  
    private int mItemCount = 10;  
    
	public static PicsService picsServer;
	public static List<Pic> pics;

	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);  
		autoCompleteTextView.addTextChangedListener(new TextWatcher(){
			public void onTextChanged(CharSequence s, int start, int before, int count){
				String str = s.toString();
				try {
					autostr = AutoCompleteService.getTagList(str);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				ArrayAdapter<String> adapter=new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_expandable_list_item_1, autostr);
				//This line works for android sdk6
				//ArrayAdapter<String> adapter=new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_dropdown_item_1line, autostr);
				//This line works for android sdk 8 
				autoCompleteTextView.setAdapter(adapter);  

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {	
			}

		});	

	}
	
	
	/**
	 * Menu part
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, Menu.FIRST + 1, 1, "prevpage");
        menu.add(Menu.NONE, Menu.FIRST + 2, 2, "nextpage");
        menu.add(Menu.NONE, Menu.FIRST + 3, 3, "setting");
        menu.add(Menu.NONE, Menu.FIRST + 4, 4, "quit");
        return true;
	}
	 public boolean onOptionsItemSelected(MenuItem item) {
         switch (item.getItemId()) {
         case Menu.FIRST + 1:
        	 try {
        		 if(PicsService.prevPageEnable())
        			 toPrevPage();
        		 else{
        			 Toast.makeText(getApplicationContext(), R.string.preverror, 2).show();
        		 }
			} catch (Exception e) {
				e.printStackTrace();
			}
            break;
         case Menu.FIRST + 2:
			try {
				if(PicsService.nextPageEnable())
					toNextPage();
				else{
					Toast.makeText(getApplicationContext(), R.string.nexterror, 2).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
                 break;
         case Menu.FIRST + 4:
        	 BaseActivity.exit();
                 break;
         }
         return false;
	 }

	 
	/*
	 * After start Searching 
	 */
	public void searchImage(View v) throws Exception{
		String str = autoCompleteTextView.getText().toString();
		PicsService.settag(str);
		if(PicsService.isEmpty()){
			Toast.makeText(getApplicationContext(), R.string.nopicerror, 2).show();
		}
		else{
			closeInputMethod();
			
			pics = PicsService.getPics();
			gridView = (PullToRefreshGridView) findViewById(R.id.pull_refresh_grid); 
			gridAdapter = new gridAdapter(this); 
			gridView.setAdapter(gridAdapter); 
	        
	        gridView.setOnRefreshListener(new OnRefreshListener<GridView>()  
            {  
				public void onRefresh(PullToRefreshBase<GridView> refreshView) {
					// TODO Auto-generated method stub
//					String label = DateUtils.formatDateTime(  
//                            getApplicationContext(),  
//                            System.currentTimeMillis(),  
//                            DateUtils.FORMAT_SHOW_TIME  
//                                    | DateUtils.FORMAT_SHOW_DATE  
//                                    | DateUtils.FORMAT_ABBREV_ALL);  
                    // Last time update  
//                    refreshView.getLoadingLayoutProxy()  
//                            .setLastUpdatedLabel(label);  

                    // show next page  
                    new GetDataTask().execute();  
				}  
            });  
		}
	}
	public void toPrevPage() throws Exception{
		pics = PicsService.getPrevPics();
		gridAdapter.notifyDataSetChanged();
		
	}
	public void toNextPage() throws Exception{
		pics = PicsService.getNextPics();
		gridAdapter.notifyDataSetChanged();

	}
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent disIntent=new Intent(this,PicShowActivty.class);
		disIntent.putExtra("count",position);
		startActivity(disIntent);
	}
	private void closeInputMethod() {
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);  
		if (imm.isActive()) {
		imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS); 
		}
	}
  
    private class GetDataTask extends AsyncTask<Void, Void, Void>  
    {  
        protected Void doInBackground(Void... params)  
        {  
            try  
            {  
                Thread.sleep(1000);  
            } catch (InterruptedException e)  
            {  
            }  
            return null;  
        }  
  
        protected void onPostExecute(Void result)  
        {  
        	if(PicsService.nextPageEnable())
        		try {
    				pics = PicsService.getNextPics();
    				gridAdapter.notifyDataSetChanged();
    			} catch (Exception e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
			else{
				Toast.makeText(getApplicationContext(), R.string.nexterror, 2).show();
			}
        	gridView.onRefreshComplete();  
        }  
    }  
    
}
