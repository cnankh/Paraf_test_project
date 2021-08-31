package com.example.paraf_test_project.services

import com.example.paraf_test_project.model.VenueResponse
import com.example.paraf_test_project.services.api.VenueApi
import io.reactivex.Single
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class FoursquareApiService {
    private val BASE_URL = "https://api.foursquare.com/"
    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(VenueApi::class.java)

    /**
     * @param ll string - latitude and longitude of user's current position
     * @param radius Int - Radius to search within, in meters
     * @return Single<Response<VenueResponse>>
     */
    fun getVenues(ll: String, radius: Int): Single<Response<VenueResponse>> {
        return api.getVenues(ll, radius)
    }

}