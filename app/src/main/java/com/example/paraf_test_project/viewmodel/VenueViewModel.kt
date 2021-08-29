package com.example.paraf_test_project.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.paraf_test_project.model.Group
import com.example.paraf_test_project.model.Item
import com.example.paraf_test_project.model.VenueResponse
import com.example.paraf_test_project.services.FoursquareApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response


class VenueViewModel : ViewModel() {
    /**
     * latitude and longitude of user's current position
     * --WARNING-- this should be replaced by real values later
     */
    private val ll: String = "36.308895,59.512478"

    /**
     * the maximum number of discovered venue
     */
    private val limit: Int = 3

    // meters
    private val radius: Int = 3000

    private val service = FoursquareApiService()
    private val disposable = CompositeDisposable()

    private var venueResponse = VenueResponse()
    val items = MutableLiveData<List<Item>>()

    /**
     * send request to the server to retrieve the near venues
     */
    fun fetch() {

        disposable.add(
            service.getVenues(ll, limit, radius)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :
                    DisposableSingleObserver<retrofit2.Response<VenueResponse>>() {
                    override fun onSuccess(t: Response<VenueResponse>) {
                        venueResponse = t.body()!!
                        items.value = venueResponse.response?.group?.get(0)?.item!!
                        Log.d("tag items:", items.value!!.toString())
                    }

                    override fun onError(e: Throwable) {
                        Log.d("tag error :", e.message.toString())
                    }

                })
        )
    }

}


