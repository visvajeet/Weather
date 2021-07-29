package com.skf.weatherapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skf.weatherapp.data.network.WeatherApi
import com.skf.weatherapp.models.LocationModel
import com.skf.weatherapp.utils.*
import kotlinx.coroutines.launch
import timber.log.Timber


class SearchViewModel : ViewModel() {

    private val apiService by lazy { WeatherApi.service }

    val status = MutableLiveData<SearchStatus>()
    val statusLocation = MutableLiveData<LocationLoadingStatus>()

    private val _searchLocation = MutableLiveData<Resource<List<LocationModel>>>()
    val searchLocation: LiveData<Resource<List<LocationModel>>> = _searchLocation

    init {
        Timber.d("SearchViewModel: Created")
        status.postValue(SearchStatus.IDLE)
        statusLocation.postValue(LocationLoadingStatus.IDLE)
    }

     fun searchLocation(query:String){

         Timber.d("Query is $query")

         if(query.length < 3){
             Timber.d("Query length is less than 3")
             _searchLocation.postValue((Resource.success(data = ArrayList() )))
             return
         }

         status.postValue(SearchStatus.LOADING)
        _searchLocation.postValue(Resource.loading(data = null))
        viewModelScope.launch{
            try {
                 val data = apiService.searchLocation(location = query)
                 _searchLocation.postValue((Resource.success(data = data )))
                 status.postValue(SearchStatus.DONE)

                if(data.isNullOrEmpty()){
                    _searchLocation.postValue((Resource.success(data = ArrayList() )))
                    status.postValue(SearchStatus.EMPTY)
                }

            } catch (exception: Exception) {
                 status.postValue(SearchStatus.ERROR)
                _searchLocation.postValue(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        Timber.d("SearchViewModel: Cleared")
    }
}