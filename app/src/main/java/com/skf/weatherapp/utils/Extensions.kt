package com.skf.weatherapp.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.skf.weatherapp.R
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

private var toast: Toast? = null

fun Context?.showToast(msg: String?){
    this?.let {
        if (toast !=null) {   toast?.cancel(); toast = null }
        toast = Toast(this)
        toast?.setText(msg)
        toast?.duration = Toast.LENGTH_SHORT
        toast?.show()
    }
}


@BindingAdapter("bindImageURL")
fun bindImageURL(imgView: ImageView?, imgUrl: String?) {
    imgUrl?.let {
        if (!imgUrl.isNullOrEmpty())
            imgView?.context?.let { it1 ->
                Glide.with(it1)
                    .load(imgUrl)
                    .into(imgView) }
    }
}


@BindingAdapter("setImageDrawable")
fun setImageDrawable(imgView: ImageView?, id: Int?) {
    imgView?.let {
       it.setImageDrawable(id?.let { it1 -> ContextCompat.getDrawable(it.context, it1) })
    }
}

fun Long.toFormattedTime(): String {
    return try {
        val sdf = SimpleDateFormat("EEE, dd MMM hh:mm a")
        val netDate = Date(this * 1000)
        sdf.format(netDate)

    }catch (e: Exception){ "Date" }
}
fun Long.toFormattedDay(): String {
    return try {
        val sdf = SimpleDateFormat("EEE, dd")
        val netDate = Date(this * 1000)
        sdf.format(netDate)

    }catch (e: Exception){ "Day" }
}

fun Context.openAppSystemSettings() {
    try {
        startActivity(Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = Uri.fromParts("package", packageName, null)
        })
    }catch (e: Exception){ Timber.d("openAppSystemSettings : ${e.message}") }

}
fun Context.verifyInternet(): Boolean {

    return if(CheckNetwork.isNetworkConnected(this)){ true }else{
        this.showToast(this.getString(R.string.check_internet_connection))
        false
    }
}

