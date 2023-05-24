package com.sakhno.newsapp.fragments;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sakhno.newsapp.NewsItem;
import com.sakhno.newsapp.NewsListAdapter;
import com.sakhno.newsapp.R;
import com.sakhno.newsapp.databinding.FragmentNewsListBinding;
import com.sakhno.newsapp.db.DatabaseHelper;
import com.sakhno.newsapp.list.OnRecyclerViewItemClickListener;
import com.sakhno.newsapp.list.OnRemoveButtonClickListener;

import java.util.ArrayList;
import java.util.List;


public class NewsListFragment extends Fragment implements OnRecyclerViewItemClickListener, OnRemoveButtonClickListener {
    private FragmentNewsListBinding fragmentNewsListBinding;
    private NewsListAdapter adapter;
    private DatabaseHelper databaseHelper;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentNewsListBinding = FragmentNewsListBinding.inflate(getLayoutInflater());
        databaseHelper = new DatabaseHelper(this.getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initRecyclerView();
        adapter.setNewsList(loadNews());
        adapter.setDatabaseHelper(databaseHelper);
        adapter.setItemClickListener(this);
        adapter.setRemoveButtonClickListener(this);
        return fragmentNewsListBinding.getRoot();
    }

    private void openFragment() {
        FragmentManager fragmentManager = getParentFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        NewsDetailsFragment fragment = new NewsDetailsFragment();
        fragmentTransaction.replace(R.id.newsDetailsFragment, fragment); // R.id.fragment_container - идентификатор контейнера, в который нужно поместить фрагмент
        fragmentTransaction.addToBackStack(null); // Добавление в стек возврата, чтобы можно было вернуться к предыдущему фрагменту при нажатии кнопки "Назад"
        fragmentTransaction.commit();
    }

    private void initRecyclerView() {
        RecyclerView newsRecyclerView = fragmentNewsListBinding.recyclerViewList;
        if(adapter == null) {
            adapter = new NewsListAdapter();
            newsRecyclerView.setAdapter(adapter);
            newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }

    private List<NewsItem> loadNews() {
        //insertTestData();
        List<NewsItem> news = new ArrayList<>();

        SQLiteDatabase database = databaseHelper.getWritableDatabase();

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

        // Закрытие курсора после использования
        if (cursor != null) {
            cursor.close();
        }

        return news;
    }

    public void insertTestData() {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

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


    @Override
    public void onItemClick(NewsItem item) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Создаем экземпляр фрагмента, который вы хотите открыть
        NewsDetailsFragment fragment = new NewsDetailsFragment();

        // Передаем необходимые данные в фрагмент, если требуется
        Bundle args = new Bundle();
        args.putString("title", item.getTitle());
        args.putString("category", item.getCategory());
        args.putString("source", item.getSource());
        args.putString("url", item.getUrl());
        args.putString("description", item.getDescription());
        fragment.setArguments(args);

        // Заменяем текущий фрагмент на новый
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onRemoveBtnClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setMessage("Are you sure you want to delete this article?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteArticleById(Integer.parseInt(v.getContentDescription().toString()));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

    }


    //TODO: database move to another class
    private void deleteArticleById(int id) {
        System.out.println("Delete : " + id);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        String selection = "id = ?";
        String[] selectionArgs = {String.valueOf(id)};

        db.delete("News", selection, selectionArgs);

        List<NewsItem> articles = adapter.getNewsList();

        for (NewsItem item : articles) {
            if (item.getId() == id) {
                articles.remove(item);
                break;
            }
        }
        adapter.setNewsList(articles);

        adapter.notifyDataSetChanged();
    }
}