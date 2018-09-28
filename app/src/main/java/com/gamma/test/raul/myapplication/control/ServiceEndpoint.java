package com.gamma.test.raul.myapplication.control;

import com.gamma.test.raul.myapplication.model.Beer;

import java.util.List;


import retrofit2.http.GET;
import rx.Observable;

public interface ServiceEndpoint {

    @GET("beers")
    Observable<List<Beer>> getBeers();
}
