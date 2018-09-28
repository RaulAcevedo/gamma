package com.gamma.test.raul.myapplication.control;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import com.gamma.test.raul.myapplication.model.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

public class BaseController {

    private Context context;
    private Retrofit retrofit;


    public BaseController(@NonNull Context context) {
        this.context = context;
    }


    protected Context getContext() {
        return this.context;
    }

    protected Retrofit getRetrofit() {

        Gson gson = new GsonBuilder()
                .setDateFormat("MM/yyyy")
                .create();
        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(ApplicationConstants.SERVICE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(rxAdapter)
                    .build();
        }

        return retrofit;

    }

    protected Boolean isNetworkingOnline() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeConnection = cm.getActiveNetworkInfo();
        return (activeConnection != null) && activeConnection.isConnected();
    }

}
