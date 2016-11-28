package com.psj.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
        intent.putExtra("NEED_REMOVE_TITLE", false);
        intent.putExtra("NEED_REMOVE_TAIL", false);
        intent.putExtra("URL", "http://www.light-house.cc/");
        intent.putExtra("TITLE", "aa");
        startActivity(intent);
    }
}
