package com.example.face_detection_countfaces;

import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class FullImage extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_full_image);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Intent i = getIntent();
		Uri targetUri= Uri.parse(getIntent().getExtras().getString("uri"));
		ImageAdapter imgadp = new ImageAdapter(this);
		
		ImageView img =(ImageView)findViewById(R.id.imageView1);
		
		Bitmap bitmap;
		try {
			bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), targetUri);
			img.setImageBitmap(bitmap);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	
	}
	
	
}
