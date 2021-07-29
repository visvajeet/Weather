package com.skf.weatherapp.utils

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.databinding.DataBindingUtil
import com.skf.weatherapp.R
import com.skf.weatherapp.databinding.LayoutAskPermissionBinding

data class AskPermissionModel(
    val heading: String,
    val subHeading: String= "",
    val yes: String,
    val no: String,
    val cancelable: Boolean = true

)

class AskPermission {

    fun ask(context: Context, ask: AskPermissionModel, func: () -> Unit = {}){

        val dialog =  Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(ask.cancelable)

        val binding = DataBindingUtil.inflate<LayoutAskPermissionBinding>(LayoutInflater.from(context),
            R.layout.layout_ask_permission, null, false)
        dialog.setContentView(binding.root)

        binding.apply {
            tvYes.text = ask.yes
            tvNo.text =  ask.no
            tvNo.setOnClickListener { dialog.dismiss() }
            titleText.text = ask.heading

            if(ask.subHeading.isEmpty()){
                 subTitleText.visibility = View.GONE
            }else{
                subTitleText.visibility = View.VISIBLE
                subTitleText.text = ask.subHeading
            }
                tvYes.setOnClickListener {
                    func()
                    dialog.dismiss()
                }
        }

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()

    }

}