package com.sakhno.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Navigation.findNavController(this, R.id.nav_host_fragment);
    }
}