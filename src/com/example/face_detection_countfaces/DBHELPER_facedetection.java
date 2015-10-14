package com.example.face_detection_countfaces;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class DBHELPER_facedetection extends SQLiteOpenHelper{

	public DBHELPER_facedetection(Context context){
		super(context,DATABASE_NAME, null , DATABASE_VERSION);
	}
	
	private static final int DATABASE_VERSION=2;
	private static final String DATABASE_NAME= "ImageDataBase";
	private static final String TABLE_NAME="Num_face";
	
	
	private static final String CREATE_TABLE=
			"CREATE TABLE "
			+TABLE_NAME
			+"("
			+"photo_id INTEGER PRIMARY KEY AUTOINCREMENT,"
			+"location TEXT NOT NULL,"
			+"faces INTEGER NOT NULL"
			+");"
			;


	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_TABLE);
	
	}


	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);       
		onCreate(db);
	}

	public void insert_image(String imageuri){
		
		SQLiteDatabase db= this.getWritableDatabase();
		 
		ContentValues values =new ContentValues();
		 values.put("location", imageuri);
		 values.put("faces",0);
		 db.insert(TABLE_NAME, null, values);

		
	}
	public int insert_count(String imageuri,int n_detected){
		SQLiteDatabase db= this.getWritableDatabase();
		String selectquery= "SELECT * FROM "
				+TABLE_NAME
				+" WHERE location = "
				+imageuri
				;
		
		Log.e("message:",selectquery);
		Cursor c = db.rawQuery(selectquery, null);
		if(c!=null)
			c.moveToFirst();
		
		int id = c.getInt(c.getColumnIndex("photo_id"));
		
		
			ContentValues values = new ContentValues();
			values.put("photo_id", id);
			values.put("faces", n_detected);
			values.put("location",imageuri);
		
			return db.update(TABLE_NAME, values, "location" + " = ?"  ,new String[]{imageuri});
		
	}
	
	public List<Integer> get_uri_from_count(int count){
		
		List<Integer> list = new ArrayList<Integer>();
		SQLiteDatabase db= this.getReadableDatabase();
		String selectquery= "SELECT * FROM "
							+TABLE_NAME
							+" WHERE faces="
							+count;
     	Log.e("message:",selectquery);
     	Cursor c=db.rawQuery(selectquery,null);
     	if(c.getCount()!=0)
     	{
     		if(c!=null) c.moveToFirst();
     	
     		do{
     		
     			int uri =c.getInt(c.getColumnIndex("location"));
     			list.add(uri);
     		}
     		while(c.moveToNext());
    		return list;
     	}
     	else{
     		List<Integer> empty_list= new ArrayList<Integer>();
     		return empty_list;
     	}
	}
}
