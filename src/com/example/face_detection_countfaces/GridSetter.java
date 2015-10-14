package com.example.face_detection_countfaces;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

public class GridSetter extends Activity {
	GridView grid;
	ImageAdapter adapter = new ImageAdapter(this);
	//private ArrayAdapter<Bitmap> adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grid_setter);
		//getActionBar().setDisplayHomeAsUpEnabled(true);
		
		grid=(GridView)findViewById(R.id.gridView1);
		
		ArrayList<String> uri = getIntent().getExtras().getStringArrayList("uri_data");
		
		//Toast.makeText(getBaseContext(), uri.get(0).toString(), Toast.LENGTH_SHORT).show();
		
		
		adapter.set_image_array(uri);
		
		grid.setAdapter(adapter);
		
		grid.setOnItemClickListener(new OnItemClickListener() {
			 @Override
	            public void onItemClick(AdapterView<?> parent, View v,
	                    int position, long id) {
	 
	                // Sending image id to FullScreenActivity
	               	
				 	Intent i = new Intent(getApplicationContext(), FullImage.class);
	                // passing array index
	                i.putExtra("uri",adapter.get_image_uri(position).toString());
	                startActivity(i);
	            }

		
		});
		
	}
}