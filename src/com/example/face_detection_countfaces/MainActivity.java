package com.example.face_detection_countfaces;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.util.CharArrayBuffer;

import android.R.color;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.media.FaceDetector.Face;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import com.example.face_detection_countfaces.DBHELPER_facedetection;
import android.app.SearchManager;
import android.app.SearchableInfo;
public class MainActivity extends Activity {

	private static final int SELECT_GALLERY_CODE = 1;
	private static final int TAKE_PICTURE_CODE = 2;

	Button Detect, Capture;
	ImageView imgview;
	ContentValues values;
	Uri imageuri;
	Bitmap bmp;
	InputStream stream;
	 int imageWidth;
	 int imageHeight;
	 int n_faces=10;
	FaceDetector myfaceDetect;
	FaceDetector.Face[] myface;
	float eyedist;
	int detected;
	float eyedistance;
	Canvas canvas;
	public Uri temp_uri;
	public Bitmap temp_bmp;
	StringBuffer temp_uri_end,temp_uri_start;
	
	DBHELPER_facedetection db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		getActionBar().setTitle("!!Face Count!!");
		
		Detect=(Button)findViewById(R.id.button2);
		Capture=(Button)findViewById(R.id.button1);
		imgview=(ImageView)findViewById(R.id.imageView1);
		
		if(savedInstanceState!=null){
			temp_bmp=savedInstanceState.getParcelable("image");
			imgview.setImageBitmap(temp_bmp);
		}
		//make_uri_start();
		
		//Toast.makeText(getApplicationContext(), "Inside on create", Toast.LENGTH_LONG).show();
		Capture.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*values=new ContentValues();
				values.put(MediaStore.Images.Media.TITLE, "New pic");
				values.put(MediaStore.Images.Media.DESCRIPTION,"Camera image YO!!");
				imageuri= getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
				*/
				
	//	Toast.makeText(getApplicationContext(), imageuri.toString(), Toast.LENGTH_LONG).show();
				
				//make_uri_start();
				
				Intent i= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				i.putExtra("mydata", imageuri);
				i.putExtra(MediaStore.EXTRA_OUTPUT, imageuri);
				//temp_uri=imageuri;
				startActivityForResult(i,TAKE_PICTURE_CODE);
				
				temp_uri_end = new StringBuffer();
				
				
			//	Toast.makeText(getApplicationContext(),Integer.toString(imageuri.toString().lastIndexOf('/')), Toast.LENGTH_LONG).show();
				
				for(int index=imageuri.toString().lastIndexOf('/')+1;index < imageuri.toString().length();index++){
					temp_uri_end.append(imageuri.toString().charAt(index));	
				}
				
				Toast.makeText(getApplicationContext(),temp_uri_end, Toast.LENGTH_LONG).show();
			

			
				store_db_image(temp_uri_end);
				
			}
		});
		
		Detect.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(DetectFaces()==1)
				 store_db_count(temp_uri_end,detected);
			}
		});
		
	
	}
	
	public String make_uri_start(){
		
		values=new ContentValues();
		values.put(MediaStore.Images.Media.TITLE, "New pic");
		values.put(MediaStore.Images.Media.DESCRIPTION,"Camera image YO!!");
		imageuri= getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
		imageuri= getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
		temp_uri_start= new StringBuffer();
		
		for(int index=0; index <= imageuri.toString().lastIndexOf('/') ;index++){
			
			temp_uri_start.append(imageuri.toString().charAt(index));
		}
		
		return temp_uri_start.toString();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		make_uri_start();
		//Toast.makeText(getApplicationContext(), "Inside on resume", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		//Toast.makeText(getApplicationContext(), "Inside onsaveunstance", Toast.LENGTH_SHORT).show();
		outState.putParcelable("image", temp_bmp);
	}

	

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		 MenuInflater inflater = getMenuInflater();
	        inflater.inflate(R.menu.my_menu, menu);
	       
	        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
	                .getActionView();
	       
	       // searchView.setSearchableInfo(searchManager
	       //     .getSearchableInfo(getComponentName()));
	            
	        return super.onCreateOptionsMenu(menu);
	        }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		switch(item.getItemId()){
		
		case R.id.action_search:
			Toast.makeText(getApplicationContext(),"Searching for something??", Toast.LENGTH_LONG).show();
			Intent i = new Intent(getApplicationContext(),com.example.face_detection_countfaces.SearchManager.class);
			i.putExtra("uri",temp_uri_start.toString());
			startActivity(i);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == SELECT_GALLERY_CODE && resultCode== Activity.RESULT_OK){
			if(bmp!=null){
				bmp.recycle();
			}
			try {
				stream= getContentResolver().openInputStream(data.getData());
				Bitmap b= BitmapFactory.decodeStream(stream);
				bmp =b.copy(Bitmap.Config.RGB_565,true);
				b.recycle();
				imgview.setImageBitmap(bmp);
				//DetectFaces();
				stream.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		else if(resultCode== Activity.RESULT_OK && requestCode== TAKE_PICTURE_CODE  ){
			
			try {
				/*
				if(data.getExtras().getParcelable("mydata")==null)
					imageuri=temp_uri;
				else imageuri=data.getExtras().getParcelable("mydata");
				*/
				//temp_uri=null;
				Bitmap b= MediaStore.Images.Media.getBitmap(getContentResolver(), imageuri);
				bmp= b.copy(Bitmap.Config.RGB_565, true);
				temp_bmp=b.copy(Bitmap.Config.RGB_565, true);
				b.recycle();
				imgview.setImageBitmap(bmp);
				
				//temp_uri=imageuri;
				Toast.makeText(getApplicationContext(),imageuri.toString(),Toast.LENGTH_LONG).show();				
				
				//store_db_image(imageuri.toString());
				//DetectFaces();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		//temp_uri=null; //important to flush prev uri
		
	}
	
	public int DetectFaces(){
		
		if(bmp==null){
			Toast.makeText(getApplicationContext(), "Click Capture to get a picture!",Toast.LENGTH_SHORT).show();
			return 0;
		}
		imageHeight=bmp.getHeight();
		imageWidth=bmp.getWidth();
		imgview.setImageBitmap(bmp);
		 
		myface = new FaceDetector.Face[n_faces];
		myfaceDetect= new FaceDetector(imageWidth, imageHeight, n_faces);
		
		detected= myfaceDetect.findFaces(bmp, myface);
		
		Toast.makeText(getApplicationContext(),"No.Of Face Detected:"+ detected,Toast.LENGTH_LONG).show();
		Paint p = new Paint();
		p.setColor(Color.GREEN);
		p.setStyle(Paint.Style.STROKE);
		p.setStrokeWidth(10);
		
		canvas = new Canvas();
		canvas.setBitmap(bmp);
		canvas.drawBitmap(bmp,0,0, p);
		
		if(detected > 0){
			for(int i=0;i< detected;i++){
				
				Face face = myface[i];
				PointF mid = new PointF();
			//	float conf=face.confidence();
				face.getMidPoint(mid);
				eyedistance =face.eyesDistance();
			//	if(conf>0.3)
					{
					canvas.drawCircle(mid.x, mid.y, (float) (1.5*eyedistance), p);
					//Toast.makeText(getApplicationContext(), "Confidence: " + Float.toString(conf),Toast.LENGTH_SHORT).show();
					}
			}
		}
		//store_db_count(detected); //store detected no. of faces to database
		return 1;
	}
	
	
	
	public void store_db_image(StringBuffer uri_end){
		
	
		db=new DBHELPER_facedetection(getApplicationContext());
		
		if(uri_end!=null){
			
			//Toast.makeText(getApplicationContext(), "Address: "+ temp.toString(),Toast.LENGTH_LONG).show();
			db.insert_image(uri_end.toString());
			Toast.makeText(getApplicationContext(), "Image Stored",Toast.LENGTH_LONG).show();
			
		}
		else
			Log.e("store_db",null);
	}
	
	public void store_db_count(StringBuffer uri_end,int count){
		db=new DBHELPER_facedetection(getApplicationContext());
		int test=-999;
		
	
		if(uri_end!=null)
		{ 
		
		//	t =uri.replaceAll(":", "':");
		    test = db.insert_count(uri_end.toString(),count);
		}
		//Toast.makeText(getBaseContext(), Integer.toString(test) ,Toast.LENGTH_LONG ).show();
		if(test==1){
			Toast.makeText(getApplicationContext(), "FaceCount Inserted",Toast.LENGTH_LONG).show();
		}
		else {
			Toast.makeText(getApplicationContext(), "Something wrong!!!",Toast.LENGTH_LONG).show();
		}
	}

	
	
}
