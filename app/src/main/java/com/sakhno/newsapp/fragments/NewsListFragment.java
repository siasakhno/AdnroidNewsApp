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
import android.widget.Button;

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
        adapter.setNewsList(databaseHelper.loadAllNews());
        adapter.setDatabaseHelper(databaseHelper);
        adapter.setItemClickListener(this);
        adapter.setRemoveButtonClickListener(this);

        View rootView = fragmentNewsListBinding.getRoot();

        Button createButton = rootView.findViewById(R.id.createArticleBtn);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Btn clicked!");
            onCreateButtonClick();            }
        });


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        adapter.setNewsList(databaseHelper.loadAllNews());
        adapter.notifyDataSetChanged();
    }

    private void initRecyclerView() {
        RecyclerView newsRecyclerView = fragmentNewsListBinding.recyclerViewList;
        if(adapter == null) {
            adapter = new NewsListAdapter();
            newsRecyclerView.setAdapter(adapter);
            newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }

    public void onCreateButtonClick() {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        CreateNewsItemFragment fragment = new CreateNewsItemFragment(databaseHelper);

        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onItemClick(NewsItem item) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        NewsDetailsFragment fragment = new NewsDetailsFragment();

        Bundle args = new Bundle();
        args.putString("title", item.getTitle());
        args.putString("category", item.getCategory());
        args.putString("source", item.getSource());
        args.putString("url", item.getUrl());
        args.putString("description", item.getDescription());
        fragment.setArguments(args);

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

    private void deleteArticleById(int id) {
        System.out.println("Delete : " + id);

        databaseHelper.deleteArticleById(id);

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