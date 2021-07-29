package com.skf.weatherapp.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.skf.weatherapp.R
import timber.log.Timber


class LocationProvideService(
    private val activity: FragmentActivity?,
    private val locationPermission: ActivityResultLauncher<String>? = null,
    private val listener: LocationListener) {

    init {
        Timber.d("Location Service")
    }

     private fun askOnGps(){

         activity?.let { act->

             fun openSettings(){ act.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }

             act.apply {
                 val ask = AskPermissionModel(
                     getString(R.string.gps_not_enabled),
                     getString(R.string.open_settings_location),
                     getString(R.string.settings),
                     getString(R.string.cancel)
                 )
                 AskPermission().ask(act, ask, ::openSettings)
             }
         }
    }

     private fun askGrantPermissionLocation(){

      activity?.let {act->

          fun openSettings(){
              val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
              val uri = Uri.fromParts("package", activity.packageName, null)
              intent.data = uri
              act.startActivity(intent)
          }

          act.apply {
              val ask = AskPermissionModel(
                  getString(R.string.location_permission_required),
                  getString(R.string.please_open_settings_allow_gps),
                  getString(R.string.settings),
                  getString(R.string.cancel)
              )
              AskPermission().ask(act, ask, ::openSettings)
          }

      }
    }


    private fun checkGpsStatus(): Boolean {

        val locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val  gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        return if (gpsStatus) { true } else {
            try { askOnGps()
            }catch (e:Exception){}
            false
        }
    }

    fun getLocation():Int {

        activity?.let {act->

            val criteria = Criteria()
            criteria.accuracy = Criteria.ACCURACY_COARSE
            criteria.powerRequirement = Criteria.POWER_LOW

            val  locationManager = act.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            if (ActivityCompat.checkSelfPermission(act, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(act,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

                if(ActivityCompat.shouldShowRequestPermissionRationale(act,  Manifest.permission.ACCESS_FINE_LOCATION)){
                    askGrantPermissionLocation()
                    return 0
                }else{
                    locationPermission?.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
                return 0
            }

            if(!checkGpsStatus()){ return 0 }

            locationManager.requestSingleUpdate(criteria, listener, null)
            return 1

        }?:run { return 0 }
    }

}