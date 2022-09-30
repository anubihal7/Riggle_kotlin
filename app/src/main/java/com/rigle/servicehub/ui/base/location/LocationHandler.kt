package com.rigle.servicehub.ui.base.location

import android.Manifest
import android.app.Activity
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*

class LocationHandler(
    private val activity: FragmentActivity,
    val locationResultListener: LocationResultListener
) {
    init {
        initLocationVariables()
    }

    var lostLocation: Location? = null
    private lateinit var locationManager: LocationManager
    private lateinit var locationRequest: LocationRequest
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    private fun initLocationVariables() {
        locationManager = activity.getSystemService(LOCATION_SERVICE) as LocationManager
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)
        locationRequest = LocationRequest
            .create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(0)
            .setFastestInterval(0)
            .setWaitForAccurateLocation(false)
            .setSmallestDisplacement(0f)
        initLocationCallBack()
    }

    private fun initLocationCallBack() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                Log.e("location", "${locationResult.lastLocation.toString()}")
                lostLocation = locationResult.lastLocation
                locationResultListener.onLocationRefresh(locationResult.lastLocation)
                locationResultListener.onRefreshStop()
                fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            }
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 10 && resultCode == Activity.RESULT_OK) {
            getUserLocation()
        }
    }

    private fun isLocationEnabled(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) &&
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun promptUserToEnableLocation() {
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)
        LocationServices
            .getSettingsClient(activity)
            .checkLocationSettings(builder.build())
            .addOnSuccessListener { getLastKnownLocation() }
            .addOnFailureListener { e ->

                when ((e as ResolvableApiException).statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        e.startResolutionForResult(activity, 10)
                    } catch (exception: IntentSender.SendIntentException) {
                        exception.printStackTrace()
                        locationResultListener.onError(exception.message)
                    }

                }
            }
    }

    fun getUserLocation() {
        if (!isGooglePlayServicesAvailable(activity)) {
            locationResultListener.onError("Play service not available")
            return
        }
        if (!isLocationEnabled()) {
            promptUserToEnableLocation()
            return
        }
        getLastKnownLocation()
    }

    private fun getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            locationResultListener.onError("Permission not available")
            return
        }
        fusedLocationProviderClient.lastLocation.addOnCompleteListener { it ->
            it.result?.let {
                lostLocation = it
                locationResultListener.onOldLocation(it)
            }
            Looper.myLooper()?.let { it1 ->
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    it1
                )
                locationResultListener.onRefreshStart()
            }
        }
    }


    private fun isGooglePlayServicesAvailable(activity: Activity): Boolean {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val status = googleApiAvailability.isGooglePlayServicesAvailable(activity)
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(activity, status, 2404)?.show()
            }
            return false
        }
        return true
    }

    fun removeLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            .addOnCompleteListener {
            }
    }

}