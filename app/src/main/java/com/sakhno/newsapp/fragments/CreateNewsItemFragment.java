package com.sakhno.newsapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sakhno.newsapp.NewsItem;
import com.sakhno.newsapp.R;
import com.sakhno.newsapp.db.DatabaseHelper;


public class CreateNewsItemFragment extends Fragment {
    private NewsItem item;
    private DatabaseHelper databaseHelper;
    public CreateNewsItemFragment(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news_create, container, false);

        Button submitButton = view.findViewById(R.id.buttonSubmit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText title = view.findViewById(R.id.editTextTitle);
                EditText description = view.findViewById(R.id.editTextDescription);
                EditText category = view.findViewById(R.id.editTextCategory);
                EditText imgurl = view.findViewById(R.id.editTextImgUrl);
                EditText source = view.findViewById(R.id.editTextSource);

                databaseHelper.addNews(
                        title.getText().toString(),
                        description.getText().toString(),
                        imgurl.getText().toString(),
                        Integer.parseInt(category.getText().toString()),
                        source.getText().toString()
                );
            }
        });


        return view;
    }


}