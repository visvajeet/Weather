package com.skf.weatherapp.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LocationModel(
    @Json(name = "id")
    val id:Int?,
    @Json(name = "name")
    val name:String?,
    @Json(name = "region")
    val region:String?,
    @Json(name = "country")
    val country:String?,
    @Json(name = "url")
    val url:String?,
    @Json(name = "lat")
    val let:Double?,
    @Json(name = "lon")
    val lon:Double?
    )