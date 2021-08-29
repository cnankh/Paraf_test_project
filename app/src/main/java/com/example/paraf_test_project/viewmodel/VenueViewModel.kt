package com.example.paraf_test_project.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.paraf_test_project.model.Item
import com.example.paraf_test_project.model.VenueResponse
import com.example.paraf_test_project.services.FoursquareApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response


@SuppressLint("StaticFieldLeak")
class VenueViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext
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
    fun fetch(coordinates: String?) {
        Log.d("TAG viewModel:", coordinates.toString())
        disposable.add(
            service.getVenues(coordinates!!, limit, radius)
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

    private fun fetchFromRemote(coordinates: String?) {}
}


