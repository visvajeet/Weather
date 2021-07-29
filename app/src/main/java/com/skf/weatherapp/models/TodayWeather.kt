package com.skf.weatherapp.models

import com.skf.weatherapp.R
import com.skf.weatherapp.utils.toFormattedTime
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlin.math.roundToInt

@JsonClass(generateAdapter = true)
data class TodayWeather(
    @Json(name = "location")
    val location:Location?,
    @Json(name = "current")
    val current:Current?,

){
    val locationName = "${location?.name},\n${location?.country}"
    val time = location?.localtimeEpoch?.toFormattedTime()
    val type = current?.condition?.text
    val degree = current?.tempC?.roundToInt().toString()
    val conditionImage = "https://${current?.condition?.icon?.drop(2)}".replace("64","128")

    val listOfAdditionalData = listOf<AdditionalInfo>(
        AdditionalInfo("Wind",current?.windKph?.roundToInt().toString(), R.drawable.wind),
        AdditionalInfo("Feels Like","${current?.feelsLikeC?.roundToInt().toString()}°C", R.drawable.temperature),
        AdditionalInfo("Humidity","${current?.humidity?.toString()}%", R.drawable.humidity),
        AdditionalInfo("UV",current?.uv?.roundToInt().toString(), R.drawable.uv),
        AdditionalInfo("Cloud",current?.cloud?.toString(), R.drawable.cloud),
        AdditionalInfo("Wind Degree",current?.windDegree?.toString(), R.drawable.wind_socket)

    )

}

data class AdditionalInfo(val title:String?, val value:String?, val icon: Int?)

@JsonClass(generateAdapter = true)
data class Location(
    @Json(name = "name")
    val name:String?,
    @Json(name = "region")
    val region: String?,
    @Json(name = "country")
    val country: String?,
    @Json(name = "localtime")
    val localtime: String?,
    @Json(name = "localtime_epoch")
    val localtimeEpoch: Long?,
)

@JsonClass(generateAdapter = true)
data class Condition(
    @Json(name = "text")
    val text:String?,
    @Json(name = "icon")
    val icon:String?,
)

@JsonClass(generateAdapter = true)
data class Current(
    @Json(name = "temp_c")
    val tempC:Double?,
    @Json(name = "wind_kph")
    val windKph:Double?,
    @Json(name = "wind_degree")
    val windDegree:Long?,
    @Json(name = "humidity")
    val humidity:Long?,
    @Json(name = "cloud")
    val cloud:Long?,
    @Json(name = "condition")
    val condition:Condition?,
    @Json(name = "feelslike_c")
    val feelsLikeC:Double?,
    @Json(name = "uv")
    val uv:Double?,

)