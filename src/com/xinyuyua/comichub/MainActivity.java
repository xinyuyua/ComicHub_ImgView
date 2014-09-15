package com.xinyuyua.comichub;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
	private GridView gridView;
	private gridAdapter gridAdapter;
	
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
					autostr = null;
					autostr = AutoCompleteService.getTagList(str);
				} catch (Exception e) {
					e.printStackTrace();
				}
				//ArrayAdapter<String> adapter=new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_expandable_list_item_1, autostr);
				ArrayAdapter<String> adapter=new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_dropdown_item_1line, autostr);
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
	
	public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, Menu.FIRST + 1, 1, "prevpage");
        menu.add(Menu.NONE, Menu.FIRST + 2, 2, "nextpage");
        menu.add(Menu.NONE, Menu.FIRST + 3, 3, "quit");
        return true;
	}
	
	 public boolean onOptionsItemSelected(MenuItem item) {
         switch (item.getItemId()) {
         case Menu.FIRST + 1:
        	 try {
        		 if(picsServer.prevPageEnable())
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
				if(picsServer.nextPageEnable())
					toNextPage();
				else{
					Toast.makeText(getApplicationContext(), R.string.nexterror, 2).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
                 break;
         case Menu.FIRST + 3:
        	 BaseActivity.exit();
                 break;
         }
         return false;
	 }

	public void searchImage(View v) throws Exception{
		String str = autoCompleteTextView.getText().toString();
		PicsService.settag(str);
		if(PicsService.isEmpty()){
			Toast.makeText(getApplicationContext(), R.string.nopicerror, 2).show();
		}
		else{
			pics = PicsService.getPics();
			gridView = (GridView) findViewById(R.id.gridView); 
			gridAdapter = new gridAdapter(this); 
			gridView.setAdapter(gridAdapter); 
			gridView.setOnItemClickListener(this);
			closeInputMethod();
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
}
