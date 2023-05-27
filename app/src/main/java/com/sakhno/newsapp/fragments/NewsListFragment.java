package com.sakhno.newsapp.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.sakhno.newsapp.NewsItem;
import com.sakhno.newsapp.NewsListAdapter;
import com.sakhno.newsapp.R;
import com.sakhno.newsapp.databinding.FragmentNewsListBinding;
import com.sakhno.newsapp.db.DatabaseHelper;
import com.sakhno.newsapp.list.OnRecyclerViewItemClickListener;
import com.sakhno.newsapp.list.OnRemoveButtonClickListener;

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
                Navigation.findNavController(v)
                        .navigate(R.id.action_newsListFragment_to_newsCreateFragment, null);
            }
        });


        Button searchButton = rootView.findViewById(R.id.buttonSearch);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearchButtonClick(rootView);
            }
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

    public void onSearchButtonClick(View rootView) {
        EditText searchText = rootView.findViewById(R.id.searchEditText);
        String searchTextStr = searchText.getText().toString();

        if (searchTextStr.equals("")) {
            adapter.setNewsList(databaseHelper.loadAllNews());
        } else {
            List<NewsItem> foundArticles = databaseHelper.getNewsByTitle(searchText.getText().toString());
            adapter.setNewsList(foundArticles);
        }
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onItemClick(NewsItem item, View view) {
        Bundle args = new Bundle();
        args.putString("title", item.getTitle());
        args.putString("category", item.getCategory());
        args.putString("source", item.getSource());
        args.putString("url", item.getUrl());
        args.putString("description", item.getDescription());

        Navigation.findNavController(view).navigate(R.id.action_newsListFragment_to_newsDetailsFragment, args);

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