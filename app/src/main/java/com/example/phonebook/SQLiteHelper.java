package com.example.phonebook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String TAG = "SQLHelper";
    static final String DB_NAME = "OrderFoods.db";
    static final String DB_TABLE = "Foods";
    static final int DB_VERSION = 1;

    SQLiteDatabase sqLiteDatabase;
    ContentValues contentValues;

    String KEY_NAME = "name";
    String KEY_NUMBER = "number";

    public SQLiteHelper(Context context) {
        super(context,DB_NAME,null,DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryCreateTable = "CREATE TABLE Foods(" +
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "name Text," +
                "number INTEGER)";
        db.execSQL(queryCreateTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
            onCreate(db);
        }
    }

    public void insertPerson(Person person) {
        sqLiteDatabase = getWritableDatabase();
        contentValues = new ContentValues();

        contentValues.put(KEY_NAME, person.getName());
        contentValues.put(KEY_NUMBER, person.getNumber());

        sqLiteDatabase.insert(DB_TABLE, null, contentValues);
    }


    public int deletePerson(String id) {
        sqLiteDatabase = getWritableDatabase();
        int delete = sqLiteDatabase.delete(DB_TABLE, "id=?",
                new String[]{String.valueOf(id)});

        return delete;
    }

    public int delAllPerson() {
        sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.delete(DB_TABLE, null, null);
    }

    public void updatePerson(String id, Person person) {
        sqLiteDatabase = getWritableDatabase();
        contentValues = new ContentValues();

        contentValues.put("name", person.getName());
        contentValues.put("number", person.getNumber());

        sqLiteDatabase.update(DB_TABLE, contentValues, "id=?",
                new String[]{String.valueOf(id)});
    }

    public void onInsertToTB(String name,String number){
        sqLiteDatabase=getWritableDatabase();
        contentValues=new ContentValues();

        contentValues.put(KEY_NAME,name);
        contentValues.put(KEY_NUMBER,number);

        sqLiteDatabase.insert(DB_TABLE,null,contentValues);
    }

    public List<Person> getAllPesron() {
        List<Person> PersonList = new ArrayList<>();
        Person person;
        Cursor cursor = sqLiteDatabase.query(false, DB_TABLE,
                null,
                null,
                null,
                null,
                null,
                null,
                null);

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String number = cursor.getString(cursor.getColumnIndex("number"));

            PersonList.add(new Person(name, number));
        }
        return PersonList;
    }

}
