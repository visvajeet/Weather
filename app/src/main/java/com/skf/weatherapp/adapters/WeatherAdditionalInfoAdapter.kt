package com.skf.weatherapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.skf.weatherapp.databinding.ListItemWeatherInfoBinding
import com.skf.weatherapp.models.AdditionalInfo


class WeatherAdditionalInfoAdapter() : ListAdapter<AdditionalInfo, WeatherAdditionalInfoAdapter.MyViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(ListItemWeatherInfoBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    class MyViewHolder(private var binding: ListItemWeatherInfoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            info: AdditionalInfo
        ) {

            binding.info = info
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<AdditionalInfo>() {
        override fun areItemsTheSame(oldItem: AdditionalInfo, newItem: AdditionalInfo): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: AdditionalInfo, newItem: AdditionalInfo): Boolean {
            return oldItem.title == newItem.title
        }
    }

}