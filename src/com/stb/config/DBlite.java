package com.stb.config;

import android.R.string;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.StaticLayout;

public class DBlite extends SQLiteOpenHelper {
    public DBlite(Context context) {
            super(context, StbConfig.DBNAME, null, StbConfig.VERSION);
            // TODO Auto-generated constructor stub
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
    	
    	
                    db.execSQL("create table stbconfig  if not exists emperors(" +
                            StbConfig._ID+" integer primary key autoincrement not null,"+
                            StbConfig.NAME+" text not null," +
                            StbConfig.VALUE+" text not null," +
                            StbConfig.ATTRIBUTE+" text not null);");
                    
                    db.execSQL("create table authentication if not exists emperors(" +
                            StbConfig._ID+" integer primary key autoincrement not null,"+
                            StbConfig.NAME+" text not null," +
                            StbConfig.VALUE+" text not null);" );
 
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
    }
    
    
    public void add(SQLiteDatabase db ,String TableName ,int id ,String name,String value,String attribute){
  //          SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(StbConfig._ID, id);
            values.put(StbConfig.NAME, name);
            values.put(StbConfig.VALUE, value);
            values.put(StbConfig.ATTRIBUTE, attribute);
            db.insert(TableName,null,values);
    }
    
    
    public void add(SQLiteDatabase db , String TableName , int id ,String name,String value){
    	  //          SQLiteDatabase db = getWritableDatabase();
    	            ContentValues Table2Values = new ContentValues();
    	            Table2Values.put(StbConfig._ID, id);
    	            Table2Values.put(StbConfig.NAME, name);
    	            Table2Values.put(StbConfig.VALUE,value);
    	            db.insert(TableName,null,Table2Values);
    	    }
}


