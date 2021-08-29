package com.example.paraf_test_project.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.paraf_test_project.model.Item
import com.example.paraf_test_project.model.Venue
import com.example.paraf_test_project.model.VenueResponse
import com.example.paraf_test_project.repository.VenueDatabase
import com.example.paraf_test_project.services.FoursquareApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import retrofit2.Response


@SuppressLint("StaticFieldLeak")
class VenueViewModel(application: Application) : BaseViewModel(application) {
    private final val TAG = "tag-venueViewModel: "
    private val context = getApplication<Application>().applicationContext

    private val venuesList = MutableLiveData<List<Venue>>()

    /**
     * the maximum number of discovered venue
     */
    private val limit: Int = 10

    // meters
    private val radius: Int = 3000

    private val service = FoursquareApiService()
    private val disposable = CompositeDisposable()

    private var venueResponse = VenueResponse()

    /**
     * retrieved items from VenueResponse
     */
    private val items = MutableLiveData<List<Item>>()

    /**
     * send request to the server to retrieve the near venues
     */
    fun fetch(coordinates: String?, hasLocationChanged: Boolean) {
        if (hasLocationChanged) {
            fetchFromRemote(coordinates)
            return
        }
        fetchFromDatabase()
    }

    /**
     * extract each venue from ItemList and finally put it into venuesList
     */
    private fun separateVenuesFromItem(list: List<Item>) {
        val _venueList = arrayListOf<Venue>()
        for (item in list) {
            item.venue?.let { _venueList.add(it) }
        }
        venuesList.value = _venueList
    }

    /**
     * send request to the server to get the venues
     */
    private fun fetchFromRemote(coordinates: String?) {
        Toast.makeText(context, "fetch from remote", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "fetch from remote")
        disposable.add(
            service.getVenues(coordinates!!, limit, radius)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :
                    DisposableSingleObserver<retrofit2.Response<VenueResponse>>() {
                    override fun onSuccess(t: Response<VenueResponse>) {
                        venueResponse = t.body()!!
                        items.value = venueResponse.response?.group?.get(0)?.item!!
                        separateVenuesFromItem(items.value!!)
                        storeVenuesLocally(venuesList.value as ArrayList<Venue>)
                        Log.d(TAG, venuesList.value.toString())
                    }

                    override fun onError(e: Throwable) {
                        Log.e(TAG, e.message.toString())
                        Toast.makeText(context, e.message.toString(), Toast.LENGTH_LONG).show()
                    }

                })
        )
    }

    /**
     * store and cache retrieved data from server to VenuesDatabase
     */
    private fun storeVenuesLocally(list: List<Venue>) {
        launch {
            Toast.makeText(context, "store in db", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "store in db")
            val dao = VenueDatabase(getApplication()).venueDao()
            dao.deleteAllVenues()
            val result = dao.insertAll(*list.toTypedArray())
            //*list.toTypedArray expand the list into individual elements
            var i = 0
            while (i < list.size) {
                list[i].uuid = result[i].toInt()
                ++i
            }

            venuesRetrieved(list)
        }
    }

    /**
     * retrieve cached data from VenueDatabase
     */
    private fun fetchFromDatabase() {
        launch {
            Toast.makeText(context, "fetch from db", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "fetch from db")
            val venues = VenueDatabase(getApplication()).venueDao().getAllVenues()
            venuesRetrieved(venues)
            Log.d(TAG, venuesList.value.toString())
        }
    }

    private fun venuesRetrieved(list: List<Venue>) {
        venuesList.value = list
    }
}


