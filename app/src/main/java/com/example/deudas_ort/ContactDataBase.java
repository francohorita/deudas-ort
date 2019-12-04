package com.example.deudas_ort;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.HashMap;
import java.util.Map;

public class ContactDataBase {
    /** Column names */
    public static final String KEY_FILA_ID = "_id";
    public static final String KEY_FILA_NAME = "_name";
    public static final String KEY_FILA_EMAIL = "_email";
    public static final String KEY_FILA_PHONE = "_phone";
    public static final String KEY_FILA_AMOUNT = "_amount";
    public static final String KEY_FILA_DESCRIPTION = "_description";

    /** DataBase Credentials */
    private final String DB_NAME = "DBContactos";
    private final String DB_TABLE_NAME = "TableContacts";
    private final int DATABASE_VERSION = 3;

    /** Properties */
    private DBHelper ourHelper;
    private final Context ourContext;
    private SQLiteDatabase ourDatabase;

    /** Constructor */
    public ContactDataBase(Context context) {
        ourContext = context;
    }

    /** DBHelper private Class */
    private class DBHelper extends SQLiteOpenHelper {
        /** Constructor */
        public DBHelper(Context context) {
            super(context, DB_NAME, null, DATABASE_VERSION);
        }

        /** Create table */
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            /** Database Creation SQL Statement */
            String sqlCode= "CREATE TABLE " + DB_TABLE_NAME + "( " + KEY_FILA_ID + " TEXT PRIMARY KEY, " +
                    KEY_FILA_NAME + " TEXT NOT NULL, " + KEY_FILA_EMAIL + " TEXT NOT NULL, " + KEY_FILA_PHONE + " TEXT NOT NULL, "
                    + KEY_FILA_AMOUNT + " TEXT NOT NULL, " + KEY_FILA_DESCRIPTION + " TEXT NOT NULL );";
            sqLiteDatabase.execSQL(sqlCode);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            String sqlCode = "DROP TABLE IF EXISTS " + DB_TABLE_NAME + ";";
            sqLiteDatabase.execSQL(sqlCode);
            onCreate(sqLiteDatabase);
        }
    }

    public ContactDataBase open() throws SQLException {
        ourHelper = new DBHelper(ourContext);
        ourDatabase = ourHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        ourHelper.close();
    }

    public long insert(String id, String nombre, String email, String phone, String amount, String description) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_FILA_ID, id);
        cv.put(KEY_FILA_EMAIL, email);
        cv.put(KEY_FILA_NAME, nombre);
        cv.put(KEY_FILA_PHONE, phone);
        cv.put(KEY_FILA_AMOUNT, amount);
        cv.put(KEY_FILA_DESCRIPTION, description);
        return ourDatabase.insert(DB_TABLE_NAME, null, cv);
    }

    public Map getData(String rowID) {
        Map<String, String> result = new HashMap<String, String>();
        String[] columnas = new String[] {KEY_FILA_ID, KEY_FILA_NAME, KEY_FILA_EMAIL, KEY_FILA_PHONE, KEY_FILA_AMOUNT, KEY_FILA_DESCRIPTION};

        Cursor c = ourDatabase.query(DB_TABLE_NAME, columnas, KEY_FILA_ID + "=?", new String[] {rowID},
                null, null, null);

        if (c != null && c.moveToFirst() && c.getCount() > 0) {
            int iColId = c.getColumnIndex(KEY_FILA_ID);
            int iColName = c.getColumnIndex(KEY_FILA_NAME);
            int iColEmail = c.getColumnIndex(KEY_FILA_EMAIL);
            int iColPhone = c.getColumnIndex(KEY_FILA_PHONE);
            int iColAmount = c.getColumnIndex(KEY_FILA_AMOUNT);
            int iColDescription = c.getColumnIndex(KEY_FILA_DESCRIPTION);
            result.put("id", c.getString(iColId));
            result.put("name", c.getString(iColName));
            result.put("email", c.getString(iColEmail));
            result.put("phone", c.getString(iColPhone));
            result.put("amount", c.getString(iColAmount));
            result.put("description", c.getString(iColDescription));
        }

        return result;
    }

    public long deleteEntry(String rowID) {
        return ourDatabase.delete(DB_TABLE_NAME, KEY_FILA_ID + "=?", new String[] {rowID} );
    }

    public long updateEntry(String rowID, String name, String email, String phone, String amount, String description){
        ContentValues cv = new ContentValues();
        cv.put(KEY_FILA_EMAIL, email);
        cv.put(KEY_FILA_NAME, name);
        cv.put(KEY_FILA_PHONE, phone);
        cv.put(KEY_FILA_AMOUNT, amount);
        cv.put(KEY_FILA_DESCRIPTION, description);
        return ourDatabase.update(DB_TABLE_NAME, cv,KEY_FILA_ID + "=?", new String[] {rowID} );
    }
}
