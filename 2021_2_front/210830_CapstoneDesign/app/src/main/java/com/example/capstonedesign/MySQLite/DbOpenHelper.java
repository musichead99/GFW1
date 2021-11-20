package com.example.capstonedesign.MySQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbOpenHelper {

    private static final String DATABASE_NAME = "InnerDatabase(SQLITE).db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private DatabaseHelper mDBHelper;
    private Context mContext;

    private class DatabaseHelper extends SQLiteOpenHelper{
        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DataBases.CreateDB._CREATE0);
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+DataBases.CreateDB._TABLENAME0);
            onCreate(db);
        }
    }
    public DbOpenHelper(Context context){
        this.mContext = context;
    }
    public DbOpenHelper open() throws SQLException{
        mDBHelper = new DatabaseHelper(mContext, DATABASE_NAME,null,DATABASE_VERSION);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }
    public void create(){
        mDBHelper.onCreate(mDB);
    }
    public void close(){
        mDB.close();
    }
    public long insertColumn(String img_src,String message_title,String message_content){
        ContentValues values = new ContentValues();
        values.put(DataBases.CreateDB.IMAGE_SRC,img_src);
        values.put(DataBases.CreateDB.MESSAGE_TITLE,message_title);
        values.put(DataBases.CreateDB.MESSAGE_CONTENT,message_content);
        return mDB.insert(DataBases.CreateDB._TABLENAME0, null, values);
    }
    public void deleteAllColumns(){
        mDB.delete(DataBases.CreateDB._TABLENAME0,null,null);
    }
    public boolean deleteColumn(long id){
        return mDB.delete(DataBases.CreateDB._TABLENAME0, "_id="+id,null) > 0;
    }
    public Cursor selectColumns(){
        return mDB.query(DataBases.CreateDB._TABLENAME0, null, null, null, null, null, null);
    }
    public Cursor sortColumn(String sort){
        Cursor c = mDB.rawQuery( "SELECT * FROM usertable ORDER BY " + sort + ";", null);
        return c;
    }

}
