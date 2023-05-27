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

    public List<String> getAllCategories() {
        SQLiteDatabase db = getReadableDatabase();

        List<String> categories = new ArrayList<>();

        String[] projection = {"name"};
        Cursor cursor = db.query("Categories", projection, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String category = cursor.getString(cursor.getColumnIndex("name"));
                categories.add(category);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }

        return categories;
    }

    public int getCategoryIdByName(String categoryName) {
        SQLiteDatabase db = getReadableDatabase();
        int categoryId = -1;

        String[] projection = {"id"};
        String selection = "name = ?";
        String[] selectionArgs = {categoryName};

        Cursor cursor = db.query("Categories", projection, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            categoryId = cursor.getInt(cursor.getColumnIndex("id"));
        }

        if (cursor != null) {
            cursor.close();
        }

        return categoryId;
    }

    public List<NewsItem> getNewsByTitle(String searchTitle) {
        List<NewsItem> newsList = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        String query =
                "SELECT News.id, News.title, News.source, News.image_url, News.description, Categories.name AS category_name " +
                "FROM News " +
                "JOIN Categories ON News.category_id = Categories.id " +
                "WHERE News.title LIKE ?";

        String[] selectionArgs = {"%" + searchTitle + "%"};

        Cursor cursor = db.rawQuery(query, selectionArgs);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                String imageUrl = cursor.getString(cursor.getColumnIndex("image_url"));
                String categoryName = cursor.getString(cursor.getColumnIndex("category_name"));
                String source = cursor.getString(cursor.getColumnIndex("source"));

                NewsItem newsItem = new NewsItem(id, title, categoryName, source, imageUrl, description);
                newsList.add(newsItem);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        return newsList;
    }

/*
    public void cleanAll() {
        SQLiteDatabase db = getReadableDatabase();

        db.delete("News", null, null);
        db.delete("Categories", null, null);

        initCategories();
    }


    public void initCategories() {
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

        categoryValues.clear();
        categoryValues.put("name", "War");
        database.insert("Categories", null, categoryValues);

        categoryValues.clear();
        categoryValues.put("name", "Economics");
        database.insert("Categories", null, categoryValues);

        categoryValues.clear();
        categoryValues.put("name", "History");
        database.insert("Categories", null, categoryValues);

    }
*/



}