package com.example.paraf_test_project.services.api

import com.example.paraf_test_project.model.VenueResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface VenueApi {
    /**
     * Venue Recommendations
     * GET v2/venues/explore
     */
    @GET("v2/venues/explore?client_id=3ZHVETCQZV3XKIEFADUGKXEPCS1IMPDC32JJJXRDHQO3QGTK&client_secret=XIEIVCNH4NDYXDGMEM101P2RC2Y2N2NEWSMHPUL0FWVX4IX5&v=220120609&query=pizza")
    fun getVenues(
        @Query("ll") ll: String,
        @Query("limit") limit: Int,
        @Query("radius") radius: Int,
    ): Single<Response<VenueResponse>>
}