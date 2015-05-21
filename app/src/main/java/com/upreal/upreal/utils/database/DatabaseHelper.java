package com.upreal.upreal.utils.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Nunkh on 21/05/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static String  DB_NAME = "uprealdb.sqlite";
    private SQLiteDatabase myDatabase;
    private final Context mContext;

    public DatabaseHelper(Context context) {

        super(context, DB_NAME, null, 1);
        this.mContext = context;

    }

    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if (dbExist) {
            } else {
                this.getReadableDatabase();
                /*try {
                //  copyDataBase();
                }catch (IOException e){
                    throw new Error("Error while copying Database");
                }*/
            }
    }

    private boolean checkDataBase(){
        boolean checkdb = false;
        try{
            String myPath = mContext.getFilesDir().getAbsolutePath().replace("files", "databases")+File.separator + DB_NAME;
            File dbfile = new File(myPath);
            checkdb = dbfile.exists();
        }
        catch(SQLiteException e){
            System.out.println("Database doesn't exist");
        }

        return checkdb;
    }

    public SQLiteDatabase openDataBase() throws SQLException {

        File db = mContext.getDatabasePath(DB_NAME);

        if (!checkDataBase()) {
            try {
                this.getReadableDatabase();
                copyDataBase(db);
            }catch (IOException e){
                throw new RuntimeException("Error while creating database/", e);
            }
        }
        return (myDatabase = SQLiteDatabase.openDatabase(db.getPath(), null, SQLiteDatabase.OPEN_READWRITE));
    }

    public void copyDataBase(File db) throws IOException {

        InputStream myInput = mContext.getAssets().open(DB_NAME);
        OutputStream mOutPut = new FileOutputStream(db, false);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))> 0) {
            mOutPut.write(buffer, 0, length);
        }

        mOutPut.flush();
        mOutPut.close();
        myInput.close();
    }

    public synchronized void close() {
        if (myDatabase != null)
            myDatabase.close();

        super.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

    }
}
