package com.xinyuyua.comichub;

import com.xinyuyua.comichub.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * 
 * @author yuanxinyu
 * Set picture for girdView 
 * 
 */

class gridAdapter extends BaseAdapter{ 
	
	 private LayoutInflater inflater; 

        public gridAdapter(Context context){ 
        	super();
        	 inflater = LayoutInflater.from(context); 
        } 
        
		public View getView(int position, View convertView, ViewGroup parent) { 
			 ViewHolder viewHolder; 
		        if (convertView == null) 
		        { 
		            convertView = inflater.inflate(R.layout.gridview, null); 
		            viewHolder = new ViewHolder(); 
		            viewHolder.image = (ImageView) convertView.findViewById(R.id.image); 
		            convertView.setTag(viewHolder); 
		        } else
		        { 
		            viewHolder = (ViewHolder) convertView.getTag(); 
		        } 
		        String path = "http://danbooru.donmai.us/"+MainActivity.pics.get(position).getSmallUrl();
		        try {
					viewHolder.image.setImageBitmap(ImageService.getImage(path));
				} catch (Exception e) {
					e.printStackTrace();
				} 
		        return convertView; 
        }

		@Override
		public int getCount() {
			if (null != MainActivity.pics) 
	        { 
	            return MainActivity.pics.size(); 
	        } else
	        { 
	            return 0; 
	        } 
		}

		@Override
		public Object getItem(int position) {
			return MainActivity.pics.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		} 
} 

class ViewHolder 
{ 
    public ImageView image; 
} 