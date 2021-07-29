package com.skf.weatherapp.viewmodels

import androidx.lifecycle.*
import com.skf.weatherapp.data.network.WeatherApi
import com.skf.weatherapp.models.FutureForecast
import com.skf.weatherapp.utils.CURRENT_LOCATION
import com.skf.weatherapp.utils.Resource
import com.skf.weatherapp.utils.SharedPref
import kotlinx.coroutines.launch
import timber.log.Timber

class FutureForecastViewModel : ViewModel() {

    private val apiService by lazy { WeatherApi.service }

    private val _forecastWeather = MutableLiveData<Resource<FutureForecast>>()
    val forecastWeather: LiveData<Resource<FutureForecast>> = _forecastWeather

    init {
        Timber.d("FutureForecastViewModel: Created")
        getFutureForecast()
    }

    private fun getFutureForecast(){

        val location = SharedPref.getString(CURRENT_LOCATION)
        if(location.isEmpty()){ return }

        _forecastWeather.postValue(Resource.loading(data = null))
        viewModelScope.launch{
            try {
                _forecastWeather.postValue((Resource.success(data = apiService.getForecast(location = location))))
            } catch (exception: Exception) {
                _forecastWeather.postValue(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun refreshData(){
        getFutureForecast()
    }

    override fun onCleared() {
        super.onCleared()
        Timber.d("FutureForecastViewModel: Cleared")
    }
}