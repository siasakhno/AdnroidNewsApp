package com.sakhno.newsapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sakhno.newsapp.NewsItem;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "mydatabase.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createCategoriesTableQuery = "CREATE TABLE Categories (id INTEGER PRIMARY KEY, name TEXT)";
        db.execSQL(createCategoriesTableQuery);

        String createNewsTableQuery = "CREATE TABLE News (id INTEGER PRIMARY KEY, title TEXT, description TEXT, image_url TEXT, category_id INTEGER, source TEXT, FOREIGN KEY (category_id) REFERENCES Categories(id))";
        db.execSQL(createNewsTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropCategoriesTableQuery = "DROP TABLE IF EXISTS Categories";
        db.execSQL(dropCategoriesTableQuery);

        String dropNewsTableQuery = "DROP TABLE IF EXISTS News";
        db.execSQL(dropNewsTableQuery);

        onCreate(db);
    }

    public List<NewsItem> loadAllNews() {
        //insertTestData();
        List<NewsItem> news = new ArrayList<>();

        SQLiteDatabase database = this.getWritableDatabase();

        String query = "SELECT News.id, News.title, News.source, News.image_url, News.description, Categories.name AS category_name " +
                "FROM News " +
                "JOIN Categories ON News.category_id = Categories.id ";
        Cursor cursor = database.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String source = cursor.getString(cursor.getColumnIndex("source"));
                String imageUrl = cursor.getString(cursor.getColumnIndex("image_url"));
                String categoryName = cursor.getString(cursor.getColumnIndex("category_name"));
                String description = cursor.getString(cursor.getColumnIndex("description"));

                NewsItem item = new NewsItem(id, title, categoryName, source, imageUrl, description);
                news.add(item);

            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        return news;
    }

    public void deleteArticleById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = "id = ?";
        String[] selectionArgs = {String.valueOf(id)};

        db.delete("News", selection, selectionArgs);
    }

    public void addNews(String title, String description, String imageUrl, int categoryId, String source) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("description", description);
        values.put("image_url", imageUrl);
        values.put("category_id", categoryId);
        values.put("source", source);

        db.insert("News", null, values);
    }

    public void insertTestData() {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues categoryValues = new ContentValues();
        categoryValues.put("name", "Politics");
        database.insert("Categories", null, categoryValues);

        categoryValues.clear();
        categoryValues.put("name", "Sports");
        database.insert("Categories", null, categoryValues);

        categoryValues.clear();
        categoryValues.put("name", "Technology");
        database.insert("Categories", null, categoryValues);

        ContentValues newsValues = new ContentValues();
        newsValues.put("title", "Political News 1");
        newsValues.put("description", "Description of political news 1");
        newsValues.put("image_url", "https://img.pravda.com/images/doc/0/6/0629b28-zelenskiy.jpg");
        newsValues.put("category_id", 1);
        newsValues.put("source", "News Source 1");
        database.insert("News", null, newsValues);

        newsValues.clear();
        newsValues.put("title", "Political News 2");
        newsValues.put("description", "Description of political news 2");
        newsValues.put("image_url", "https://gordonua.com/img/article/16648/77_tn.jpeg?v1684666486");
        newsValues.put("category_id", 1);
        newsValues.put("source", "News Source 2");
        database.insert("News", null, newsValues);

        newsValues.clear();
        newsValues.put("title", "Political News 3");
        newsValues.put("description", "Description of political news 3");
        newsValues.put("image_url", "https://gdb.rferl.org/0ffc0000-0aff-0242-fcf4-08da4d353037_cx0_cy9_cw0_w1023_r1_s.jpg");
        newsValues.put("category_id", 1);
        newsValues.put("source", "News Source 3");
        database.insert("News", null, newsValues);

    }



}