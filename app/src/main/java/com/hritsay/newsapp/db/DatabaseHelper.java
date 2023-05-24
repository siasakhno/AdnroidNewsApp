package com.hritsay.newsapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "mydatabase.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Создание таблицы Categories
        String createCategoriesTableQuery = "CREATE TABLE Categories (id INTEGER PRIMARY KEY, name TEXT)";
        db.execSQL(createCategoriesTableQuery);

        // Создание таблицы News
        String createNewsTableQuery = "CREATE TABLE News (id INTEGER PRIMARY KEY, title TEXT, description TEXT, image_url TEXT, category_id INTEGER, source TEXT, FOREIGN KEY (category_id) REFERENCES Categories(id))";
        db.execSQL(createNewsTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Обновление базы данных
        String dropCategoriesTableQuery = "DROP TABLE IF EXISTS Categories";
        db.execSQL(dropCategoriesTableQuery);

        String dropNewsTableQuery = "DROP TABLE IF EXISTS News";
        db.execSQL(dropNewsTableQuery);

        onCreate(db);
    }



    // Добавьте здесь методы для выполнения операций с данными
    // Например, методы для вставки, обновления, удаления и запроса данных
}