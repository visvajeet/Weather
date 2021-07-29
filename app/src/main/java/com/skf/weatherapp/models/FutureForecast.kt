package com.skf.weatherapp.models

import com.skf.weatherapp.utils.toFormattedDay
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlin.math.roundToInt

@JsonClass(generateAdapter = true)
data class FutureForecast(
    @Json(name = "location")
    val location:Location?,
    @Json(name = "current")
    val current:Current?,
    @Json(name = "forecast")
    val forecast:Forecast?,
)

@JsonClass(generateAdapter = true)
data class Forecast(
    @Json(name = "forecastday")
    val forecastList:List<ForecastDay>?,
)

@JsonClass(generateAdapter = true)
data class ForecastDay(
    @Json(name = "day")
    val day:Day?,
    @Json(name = "date_epoch")
    val dateEpoch:Long?,
){
    val time = dateEpoch?.toFormattedDay()
    val avgTemp = day?.avgTempC?.roundToInt().toString().plus("°")
    private val maxTemp = day?.maxTempC?.roundToInt().toString()
    private val minTemp = day?.minTempC?.roundToInt().toString()
    val avgHumidity = day?.avgHumidity?.roundToInt().toString().plus("%")
    val minMaxTemp = "$maxTemp°/$minTemp°"
    val conditionImage = "https://${day?.condition?.icon?.drop(2)}".replace("64","128")
}

@JsonClass(generateAdapter = true)
data class Astro(
    @Json(name = "sunrise")
    val text:String?,
    @Json(name = "sunset")
    val icon:String?,
)

@JsonClass(generateAdapter = true)
data class Day(
    @Json(name = "maxtemp_c")
    val maxTempC:Double?,
    @Json(name = "mintemp_c")
    val minTempC:Double?,
    @Json(name = "avgtemp_c")
    val avgTempC:Double?,
    @Json(name = "avghumidity")
    val avgHumidity:Double?,
    @Json(name = "maxwind_kph")
    val maxWindKph:Double?,
    @Json(name = "condition")
    val condition:Condition?,
    @Json(name = "astro")
    val astro:Astro?
)
