package com.upreal.upreal.utils.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Nunkh on 21/05/15.
 */
public class DatabaseQuery {

    private SQLiteDatabase mDatabase;
    private String mTable;
    private String[] mTableColumns;
    private String mWhereClause;
    private String[] mWherArgs;

        public DatabaseQuery(DatabaseHelper mDbHelper) {
            mDatabase = mDbHelper.openDataBase();
        }

        public String[][] QueryGetElements(String table, String[] tableColumns, String where, String[] wArgs, String groupby, String having, String orderby) {

            int nbList = 0;
            int i = 0;
            Cursor c;
            String[][] resQuery = null;
            int nbColumns = tableColumns.length;

            nbList = QueryNbElements(table, tableColumns);
            resQuery = new String[nbList][nbColumns];
            c = mDatabase.query(table, tableColumns, where, wArgs, groupby, having, orderby);
            if (c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    for (int k = 0; k < nbColumns; k++) {
                        if (c.getString(k) == null)
                            resQuery[i][k] = "";
                        else
                            resQuery[i][k] = c.getString(k);
                    }
                    c.moveToNext();
                    i++;
                    if (i == nbColumns) {
                        c.close();
                        return resQuery;
                    }
                }
            }
            c.close();
            return resQuery;
        }

        public String[] QueryGetElement(String table, String[] tableColumns, String where, String[] wArgs, String groupby, String having, String orderby) {

            int nbList = 0;
            int i = 0;
            Cursor c;
            String[] resQuery;

            nbList = QueryNbElements(table, tableColumns);
            resQuery = new String[nbList];
            c = mDatabase.query(table, tableColumns, where, wArgs, groupby, having, orderby);
            if (c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    resQuery[i] = c.getString(c.getColumnIndex(tableColumns[0]));
                    i++;
                    c.moveToNext();
                }
            }
            c.close();
            return resQuery;
        }

        public String[] MyRawQuery(String query) {
            Cursor c;
            int i = 0;
            String[] resQuery = new String[10];

            c = mDatabase.rawQuery(query, null);
            if (c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    resQuery[i] = c.getString(0);
                    i++;
                    c.moveToNext();
                }
            }
            c.close();
            return resQuery;
        }

        public int QueryNbElements(String table, String[] tableColumns) {

            int nbList;
            Cursor c;

            c = mDatabase.query(table, tableColumns, null, null, null, null, null);
            nbList = c.getCount();
            c.close();

            return nbList;
        }

        public String QueryIdFrom(String table, String[] tableColumns, String where, String[] wArgs, String groupby, String having, String orderby) {

            String resQ = null;
            Cursor c;

            c = mDatabase.query(table, tableColumns, where, wArgs, groupby, having, orderby);
            if (c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    resQ = c.getString(0);
                    c.moveToNext();
                }
            }
            return resQ;
        }

        public void InsertData(String tableMain, String[] table, String[] value) {

            ContentValues data = new ContentValues();

            for (int i = 0; table.length > i; i++)
                data.put(table[i], value[i]);
            mDatabase.insert(tableMain, null, data);
        }

        public void DeleteData(String table, String where, String[] whereArgs) {

            mDatabase.delete(table, where, whereArgs);
        }

        public void UpdateData(String table, ContentValues values, String where, String[] wArgs) {
            mDatabase.update(table, values, where, wArgs);
        }

        public  String getmTable() {
            return mTable;
        }

        public  void setmTable(String mTable) {
            this.mTable = mTable;
        }

        public  String[] getmTableColumns() {
            return mTableColumns;
        }

        public  void setmTableColumns(String[] mTableColumns) {
            this.mTableColumns = mTableColumns;
        }

        public  String getmWhereClause() {
            return mWhereClause;
        }

        public  void setmWhereClause(String mWhereClause) {
            this.mWhereClause = mWhereClause;
        }

        public  String[] getmWherArgs() {
            return mWherArgs;
        }

        public  void setmWherArgs(String[] mWherArgs) {
            this.mWherArgs = mWherArgs;
        }
}