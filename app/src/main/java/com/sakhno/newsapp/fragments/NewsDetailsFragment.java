package com.sakhno.newsapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sakhno.newsapp.NewsItem;
import com.sakhno.newsapp.R;


public class NewsDetailsFragment extends Fragment {
    private NewsItem item;
    public NewsDetailsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_news_details, container, false);
        Bundle args = getArguments();
        if (args != null) {
            this.item = new NewsItem();
            item.setTitle(args.getString("title"));
            item.setCategory(args.getString("category"));
            item.setSource(args.getString("source"));
            item.setUrl(args.getString("url"));
            item.setDescription(args.getString("description"));

            TextView title = view.findViewById(R.id.news_title);
            title.setText(item.getTitle());
            TextView category = view.findViewById(R.id.category);
            category.setText(item.getCategory());
            TextView source = view.findViewById(R.id.source);
            source.setText(item.getSource());
            TextView description = view.findViewById(R.id.description);
            description.setText(item.getDescription());

            ImageView image = view.findViewById(R.id.image);

            Glide.with(this)
                    .load(item.getUrl())
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(image);
        }

        return view;
    }
}