package zig.qiubo.utils;

import java.util.ArrayList;

import zig.qiubo.base.Appcons;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelp  extends SQLiteOpenHelper{

	private Context context;
	private SQLiteDatabase db;
	private static DBHelp dbHelp;
	private DBHelp(Context context){
		super(context, Appcons.DB_NAME, null, 1);
		this.context = context;
		db = getWritableDatabase();
	}

	public static DBHelp getInstance(Context context){
		if(dbHelp == null){
			dbHelp = new DBHelp(context);
		}
		return dbHelp;
	}

	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table "+Appcons.DB_TABLE_NAME_STAR+"("+
			Appcons.DB_FIELD_NAME_ID+"  integer primary key autoincrement,"+
			Appcons.DB_FIELD_NAME_PHONE+" text)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
	
	
	public  ArrayList<String> getAllStarPhone(){
		Cursor cursor = db.query(Appcons.DB_TABLE_NAME_STAR, null, null,null, null, null, null);
		ArrayList<String> numberList = null;
		if(cursor != null){
			if(cursor.getCount()>0){
				numberList  = new ArrayList<String>();
				String number = null;
				while(cursor.moveToNext()){
					number = cursor.getString(cursor.getColumnIndex(Appcons.DB_FIELD_NAME_PHONE));
					numberList.add(number);
				}
			}
			cursor.close();
		}
		return numberList;
	}
	

}
