package com.sakhno.newsapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.sakhno.newsapp.R;
import com.sakhno.newsapp.db.DatabaseHelper;


public class CreateNewsItemFragment extends Fragment {
    private DatabaseHelper databaseHelper;

    public CreateNewsItemFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.databaseHelper = new DatabaseHelper(this.getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news_create, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

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

                title.clearFocus();
                title.clearComposingText();

                title.clearComposingText();
                description.clearComposingText();
                category.clearComposingText();
                imgurl.clearComposingText();
                source.clearComposingText();

                requireActivity().onBackPressed();
            }
        });


    }



    }