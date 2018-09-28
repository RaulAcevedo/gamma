package com.gamma.test.raul.myapplication.control;

import android.content.Context;
import android.net.wifi.aware.SubscribeConfig;
import android.support.annotation.NonNull;

import com.gamma.test.raul.myapplication.model.ApplicationConstants;
import com.gamma.test.raul.myapplication.model.Beer;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class BeerController extends BaseController {

    public BeerController(@NonNull Context context) {
        super(context);
    }

    public void getBeers (Subscriber<List<Beer>> subscriber){
        if(isNetworkingOnline()){
            ServiceEndpoint endpoint = getRetrofit().create(ServiceEndpoint.class);
                    endpoint
                            .getBeers()
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(subscriber);
        }else{
            subscriber.onError(new Exception(ApplicationConstants.ERROR_NETWORK_NOT_AVAILABLE));
        }
    }
}
