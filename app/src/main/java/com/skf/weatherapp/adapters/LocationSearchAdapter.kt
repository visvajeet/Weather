package com.skf.weatherapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.skf.weatherapp.databinding.ListItemLocationNameBinding
import com.skf.weatherapp.models.LocationModel


class LocationSearchAdapter(private val listener: ClickListener) : ListAdapter<LocationModel, LocationSearchAdapter.MyViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(ListItemLocationNameBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data,listener)
    }

    class MyViewHolder(private var binding: ListItemLocationNameBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            location: LocationModel,
            listener: ClickListener
        ) {

            binding.location = location
            binding.listener = listener
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<LocationModel>() {
        override fun areItemsTheSame(oldItem: LocationModel, newItem: LocationModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: LocationModel, newItem: LocationModel): Boolean {
            return oldItem.id == newItem.id
        }
    }

    class ClickListener(val clickListener: (view: View, notification: LocationModel) -> Unit) {
        fun onClick(view: View, location: LocationModel) = clickListener(view, location)
    }


}