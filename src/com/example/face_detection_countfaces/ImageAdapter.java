package com.example.face_detection_countfaces;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	private Context mContext;
	public ArrayList<String> mthumbs= new ArrayList<String>();
	
	public ImageAdapter(Context c) {
		// TODO Auto-generated constructor stub
		mContext=c;
	
	}


	 @Override
	    public int getCount() {
	        return mthumbs.size();
	    }
	 
	    @Override
	    public Object getItem(int position) {
	        return mthumbs.get(position);
	    }
	 
	    @Override
	    public long getItemId(int position) {
	        return 0;
	    }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		 	ImageView imageView = new ImageView(mContext);
	      //  imageView.setImageResource(mthumbs[position]);
		 //	imageView.setImageBitmap(bmp);
		 	imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
	        imageView.setLayoutParams(new GridView.LayoutParams(200,200));
	        
	        Uri targetUri = Uri.parse(mthumbs.get(position));
	      
	        Bitmap bitmap;
	        try {
				
	        	bitmap =  MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), targetUri);
				imageView.setImageBitmap(bitmap);
			
	        } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	      
	        return imageView;	 
	}
	
	public void set_image_array(ArrayList<String> uri){
		mthumbs=uri;
	}
	
	public String get_image_uri(int pos){
		
		return mthumbs.get(pos);
		
		
	}

}
