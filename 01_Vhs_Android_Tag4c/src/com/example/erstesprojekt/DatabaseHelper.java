package com.example.erstesprojekt;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	//public DatabaseHelper(Context context, String name, CursorFactory factory, int version) {
	public DatabaseHelper(Context context) {
		super(context, "ErstestProjekt.db", null, 1);
		// TODO Auto-generated constructor stub
	}

	//Merke: _id Kleinschreibung ist wichtig
	private static final String SQL_CREATE = 
			"CREATE TABLE PERSON ( _id INTEGER PRIMARY KEY, " +
			     "vorname TEXT, " +
			     "nachname TEXT," +
			     "age INTEGER) ";

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
		//Methode Wird aufgerufen, wenn die App zum ALLER ersten mal gestartet wird. 
		db.execSQL(SQL_CREATE);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
		//verändert die Datenbank, z.B. bei Update der APP-Version
		//TODO THE APP IS ALLREADY PERFECT
		//Merke: Es gibt auch eine "Donwgrade" MEthode.
	}
			    
	public void add(Person p){
		SQLiteDatabase db = getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put("vorname", p.getVorname());
		values.put("nachname", p.getNachname());
		values.put("age", p.getAlter());
		db.insert("PERSON", null, values);
	}
	
	//im onClick Event 
	public void remove(long id){
		SQLiteDatabase db = getWritableDatabase();
		
		db.delete("person", "_id=?", new String[]{id + ""});
	}
}
