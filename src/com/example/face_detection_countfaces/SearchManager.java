package com.example.face_detection_countfaces;

import java.util.ArrayList;
import java.util.List;



import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchManager extends ListActivity {
	DBHELPER_facedetection db;
	EditText txtQuery;
	Button search;
	private ArrayAdapter<String> arrayadapter;
	String uri_start;
	private ArrayList<String> arr_uri= new ArrayList<String>();
	List<Integer> temp_list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_manager);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		db= new DBHELPER_facedetection(getApplicationContext());
		txtQuery = (EditText) findViewById(R.id.editText1);
		search=(Button)findViewById(R.id.button1);
		
		
		uri_start = getIntent().getExtras().get("uri").toString();
		
		//Toast.makeText(getApplicationContext(), uri_start, Toast.LENGTH_LONG).show();
		search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				
				arrayadapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1);
				if(txtQuery.getText().toString()!=""){
				show_in_list(txtQuery.getText().toString());
				setListAdapter(arrayadapter);}
				
				 arrayadapter.notifyDataSetChanged();
		 
				 create_uri_list();
			}
			
		});

	}
 
	public void show_in_list(String count){										//always called to set list view
		arr_uri.clear();
		
		List<Integer> list=db.get_uri_from_count(Integer.parseInt(count));
		temp_list=list;
        if(!list.isEmpty()){
		for (int i : list) {
        	//insert data to list view using array adapter HERE!!!!!
        	//Event_object event =new Event_object();
        	//event = db.get_event(i);
        	//StringBuffer s=new StringBuffer();
        	//s.append( Integer.toString(event.event_id) + " " + event.get_event_name() + " " + event.get_event_date() );
        	

			//********************************************
        	arrayadapter.add(uri_start+String.valueOf(i));
        	
        	}
			
		}
        else{
        	Toast.makeText(getApplicationContext(), "No Image Found!!", Toast.LENGTH_LONG).show();
        }
		
	}
	
	public void create_uri_list(){
		
		if(!temp_list.isEmpty()){
			
			for(int i: temp_list){
				
				String uri= uri_start+ String.valueOf(i);
				arr_uri.add(uri);
			}
		}
		
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		
		Toast.makeText(getApplicationContext(), "just clicked" + Long.toString(id), Toast.LENGTH_LONG).show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		 MenuInflater inflater = getMenuInflater();
	        inflater.inflate(R.menu.mymenu_2, menu);
	      
	        return super.onCreateOptionsMenu(menu);
	        }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		switch(item.getItemId()){
		
		case R.id.action_view_as_grid:
			//Toast.makeText(getApplicationContext(),"Searching for something??", Toast.LENGTH_LONG).show();
			Intent i = new Intent(getApplicationContext(),com.example.face_detection_countfaces.GridSetter.class);
			i.putStringArrayListExtra("uri_data", (ArrayList<String>)arr_uri);
			startActivity(i);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
