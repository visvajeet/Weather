package com.skf.weatherapp.data.network

import com.skf.weatherapp.models.FutureForecast
import com.skf.weatherapp.models.LocationModel
import com.skf.weatherapp.models.TodayWeather
import com.skf.weatherapp.utils.FUTURE_FORECAST_DAYS
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("current.json")
    suspend fun getWeather(@Query("key") string: String = API_KEY, @Query("q") location:String): TodayWeather

    @GET("forecast.json")
    suspend fun getForecast(@Query("key") string: String = API_KEY, @Query("q") location:String,
                            @Query("days") days:String = FUTURE_FORECAST_DAYS): FutureForecast

    @GET("search.json")
    suspend fun searchLocation(  @Query("key") string: String = API_KEY, @Query("q") location:String): List<LocationModel>



}