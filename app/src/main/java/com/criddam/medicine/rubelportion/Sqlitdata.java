package com.criddam.medicine.rubelportion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class Sqlitdata extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "newgraph.db";
    public static final String TABLE_NAME = "healthstatus";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "temperature";
    public static final String COL_3 = "weight";
    public static final String COL_4 = "pulse";    public static final String COL_5 = "systolic";
    public static final String COL_6 = "diastolic";
    public static final String COL_7= "date";



    public static final String TABLE_NAME2 = "notetable";
    public static final String COL_12 = "ID";
    public static final String COL_22 = "notes";

    public Sqlitdata(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,temperature INT,weight INT, pulse INT,systolic INT,diastolic INT,date String)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);

        onCreate(db);
    }

    public boolean insertData(int  temperature,int weight,int pulse,int systolic, int diastolic,String date ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,temperature);
        contentValues.put(COL_3,weight);
        contentValues.put(COL_4,pulse);
        contentValues.put(COL_5,systolic);
        contentValues.put(COL_6,diastolic);
        contentValues.put(COL_7,date);
        long result = db.insert(TABLE_NAME,null ,contentValues);
        return result != -1;
    }



    public Cursor getdata(){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM healthstatus",null);
        return cursor;
    }



    public boolean talbecheck(SQLiteDatabase db){


        String query  = "select DISTINCT TABLE_NAME2 from sqlite_master where tbl_name = '"+TABLE_NAME2+"'";
        try {
            Cursor cursor = db.rawQuery(query,null);
            if(cursor!=null){
                return cursor.getCount() > 0;

            }
        }catch (Exception e){

        }

        return false;

    }
}
