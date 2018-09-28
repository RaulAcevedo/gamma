package com.gamma.test.raul.myapplication.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.gamma.test.raul.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import butterknife.ButterKnife;

abstract public class BaseActivity extends AppCompatActivity {

    private static final String PREFERENCE = "APP_PREFERENCE";

    private Gson gson;

    protected SharedPreferences preferences(){
        return getSharedPreferences(PREFERENCE,MODE_PRIVATE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        createView();
        ButterKnife.bind(this);
        createController();
        setupView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }


    public void onBackClick(View pView){
        onBackPressed();
    }

    public Gson getGson() {
        if(gson == null){
            gson = new GsonBuilder().create();
        }
        return gson;
    }

    abstract void createView();
    abstract void createController();
    abstract void setupView();
    abstract void refresh();
}
