package com.skf.weatherapp.data.network


import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit


private const val BASE_URL = "http://api.weatherapi.com/v1/"
const val API_KEY = "af508774c7344ef68e2132354212507"

private val interceptor =  HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

private val client by lazy {

         OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .addInterceptor(interceptor)
       // .addInterceptor(NetworkInterceptor())
}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()



private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .client(client.build())
    .build()


object WeatherApi{

    val service : WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }
}