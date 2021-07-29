package com.skf.weatherapp.viewmodels

import androidx.lifecycle.*
import com.skf.weatherapp.data.network.WeatherApi
import com.skf.weatherapp.models.TodayWeather
import com.skf.weatherapp.utils.CURRENT_LOCATION
import com.skf.weatherapp.utils.Resource
import com.skf.weatherapp.utils.SharedPref
import kotlinx.coroutines.launch
import timber.log.Timber

class TodayForecastViewModel : ViewModel() {

    private val apiService by lazy { WeatherApi.service }

    private val _todayWeather = MutableLiveData<Resource<TodayWeather>>()
    val todayWeather:LiveData<Resource<TodayWeather>> = _todayWeather


    init {
        Timber.d("TodayForecastViewModel: Created")
        getCurrentWeather()
    }

    private fun getCurrentWeather(){

        val location = SharedPref.getString(CURRENT_LOCATION)
        if(location.isEmpty()){ return }

        _todayWeather.postValue(Resource.loading(data = null))
        viewModelScope.launch{
            try {
                _todayWeather.postValue((Resource.success(data = apiService.getWeather(location = location))))
            } catch (exception: Exception) {
                _todayWeather.postValue(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun refreshData(){
        getCurrentWeather()
    }

    override fun onCleared() {
        super.onCleared()
        Timber.d("TodayForecastViewModel: Cleared")
    }
}