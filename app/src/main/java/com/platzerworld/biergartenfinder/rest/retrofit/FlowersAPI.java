package com.platzerworld.biergartenfinder.rest.retrofit;

import com.platzerworld.biergartenfinder.rest.retrofit.model.Flower;

import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;

public interface FlowersAPI {

	@GET("/feeds/flowers.json")
	Call<List<Flower>> getFeed();
	
}
