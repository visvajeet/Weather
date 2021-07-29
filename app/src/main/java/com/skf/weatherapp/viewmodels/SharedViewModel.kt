package com.skf.weatherapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import timber.log.Timber



enum class NewLocationStatus{NEW,DONE}

class SharedViewModel : ViewModel() {


    private val _isLocationChanged = MutableLiveData<Boolean>()
    val isLocationChanged: LiveData<Boolean> = _isLocationChanged

     val newLocationStatusTodayForecast =   MutableLiveData<NewLocationStatus>()
     val newLocationStatusFutureForecast =  MutableLiveData<NewLocationStatus>()

    val update = Transformations.map(isLocationChanged){
        if(it){
            newLocationStatusTodayForecast.postValue(NewLocationStatus.NEW)
            newLocationStatusFutureForecast.postValue(NewLocationStatus.NEW)
        }
    }

    init {
        Timber.d("SharedViewModel: Created")
        newLocationStatusTodayForecast.postValue(NewLocationStatus.DONE)
        newLocationStatusFutureForecast.postValue(NewLocationStatus.DONE)
        isLocationChanged(false)

    }

    fun isLocationChanged(value:Boolean){
        _isLocationChanged.postValue(value)
    }

    fun newLocationUpdateDoneFutureForecast(){
        newLocationStatusFutureForecast.postValue(NewLocationStatus.DONE)
    }
    fun newLocationUpdateDoneTodayForecast(){
        newLocationStatusTodayForecast.postValue(NewLocationStatus.DONE)
    }

    override fun onCleared() {
        super.onCleared()
        Timber.d("SharedViewModel: Cleared")
    }

}