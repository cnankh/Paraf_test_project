package com.example.paraf_test_project.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.location.Geocoder
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.paraf_test_project.model.Item
import com.example.paraf_test_project.model.Venue
import com.example.paraf_test_project.model.VenueResponse
import com.example.paraf_test_project.repository.VenueDatabase
import com.example.paraf_test_project.services.FoursquareApiService
import com.example.paraf_test_project.util.SharedPreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


@SuppressLint("StaticFieldLeak")
class VenueViewModel(application: Application) : BaseViewModel(application) {
    private final val TAG = "tag-venueViewModel: "
    private val context = getApplication<Application>().applicationContext
    private val prefHelper = SharedPreferencesHelper.buildHelper(context)
    private var previousLatitude: Float? = prefHelper.getPreviousLatitude()
    private var previousLongitude: Float? = prefHelper.getPreviousLongitude()
    private lateinit var coordinates: String

    /**
     * set the minimum required distance to fetch from remote
     */
    private final val DISTANCE_LIMIT = 500.0

    val venuesList = MutableLiveData<List<Venue>>()

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
    fun fetch(latitude: Double, longitude: Double) {
        coordinates = "${latitude},${longitude}"
        getPreviousCoordinates()
        storeCurrentCoordinates(latitude, longitude)
        /**
         * if the current position of user is within (DISTANCE_LIMIT = 500.0) in comparison to the
         * prev position , then fetch from database , otherwise fetch from remote
         */
        if (getDistance(latitude, longitude) < DISTANCE_LIMIT) {
            if (venuesList.value.isNullOrEmpty())
                fetchFromDatabase()
            return
        }
        fetchFromRemote(coordinates)
    }

    /**
     * store the current position
     * @param latitude : Double
     * @param longitude : Double
     */
    private fun storeCurrentCoordinates(latitude: Double, longitude: Double) {
        SharedPreferencesHelper.buildHelper(context).setLatitude(latitude.toFloat())
        SharedPreferencesHelper.buildHelper(context).setLongitude(longitude.toFloat())
    }

    /**
     * get previous position
     */
    private fun getPreviousCoordinates() {
        previousLatitude = prefHelper.getPreviousLatitude()
        previousLongitude = prefHelper.getPreviousLongitude()
    }

    /**
     * extract each venue from ItemList and finally put it into venuesList
     * @param list : List<Item>
     */
    private fun separateVenuesFromItem(list: List<Item>) {
        val _venueList = arrayListOf<Venue>()
        for (item in list) {
            item.venue?.let { _venueList.add(it) }
        }

        venuesRetrieved(_venueList)
    }

    /**
     * send request to the server to get the venues
     * @param coordinates : String
     */
    private fun fetchFromRemote(coordinates: String) {
        Toast.makeText(context, "fetch from remote", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "fetch from remote")
        disposable.add(
            service.getVenues(coordinates, limit, radius)
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
     * @param list : List<Venue>
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
     * fetch cached venues from VenueDatabase
     */
    private fun fetchFromDatabase() {
        launch {
            Toast.makeText(context, "fetch from db", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "fetch from db")
            val venues = VenueDatabase(getApplication()).venueDao().getAllVenues()

            /**
             * if database is empty then try the fetch from remote
             */
            if (venues.isNullOrEmpty()) {
                fetchFromRemote(coordinates)
                return@launch
            }

            venuesRetrieved(venues)
            Log.d(TAG, venuesList.value.toString())
        }
    }

    private fun venuesRetrieved(list: List<Venue>) {

        Toast.makeText(context, "wtf is happening????", Toast.LENGTH_SHORT).show();
        venuesList.value = list
//            return

//        Toast.makeText(context, "no need to update list", Toast.LENGTH_SHORT).show();
    }

    /**
     * calculate the distance between currentPosition and previousPosition
     * @param currentLatitude : Double
     * @param currentLongitude : Double
     */
    private fun getDistance(currentLatitude: Double, currentLongitude: Double): Float {
        val tag = "distance"
        Log.d(tag, "prev : ${previousLatitude},${previousLongitude}")
        Log.d(tag, "new : ${currentLatitude},${currentLongitude}")

        val startPoint = Location("A")
        startPoint.latitude = previousLatitude!!.toDouble()
        startPoint.longitude = previousLongitude!!.toDouble()

        val endPoint = Location("b")
        endPoint.latitude = currentLatitude
        endPoint.longitude = currentLongitude
        val distance = startPoint.distanceTo(endPoint)
        Log.d(tag, "distance $distance")

        return distance
    }
}


