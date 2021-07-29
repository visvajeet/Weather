package com.skf.weatherapp.adapters
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.skf.weatherapp.databinding.ListItemWeatherAdditionalInfoBinding
import com.skf.weatherapp.databinding.ListItemWeatherHeaderBinding
import com.skf.weatherapp.models.AdditionalInfo
import com.skf.weatherapp.models.TodayWeather
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val HEADER_ITEM_ID = "HEADER_ITEM"
private const val ADDITIONAL_INFO_ITEM_ID = "ADDITIONAL_INFO_ITEM"

private const val VIEW_HEADER = 0
private const val VIEW_ADDITIONAL_INFO = 1

class TodayWeatherAdapter : ListAdapter<DataItemTodayWeather, RecyclerView.ViewHolder>(DiffCallback) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {

            VIEW_HEADER -> HeaderViewHolder.from(parent)
            VIEW_ADDITIONAL_INFO -> AdditionalInfoHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    class AdditionalInfoHolder private constructor(private val binding: ListItemWeatherAdditionalInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            info: List<AdditionalInfo>?
        ) {
            binding.executePendingBindings()

            val weatherAdditionalInfoAdapter = WeatherAdditionalInfoAdapter()
            binding.rv.setHasFixedSize(true)
            binding.rv.adapter = weatherAdditionalInfoAdapter
            weatherAdditionalInfoAdapter.submitList(info)
        }

        companion object {
            fun from(parent: ViewGroup): AdditionalInfoHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    ListItemWeatherAdditionalInfoBinding.inflate(layoutInflater, parent, false)
                return AdditionalInfoHolder(binding)
            }
        }
    }

    class HeaderViewHolder private constructor(private val binding: ListItemWeatherHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            weather: TodayWeather
        ) {
            binding.todayWeather = weather
            binding.executePendingBindings()

        }

        companion object {
            fun from(parent: ViewGroup): HeaderViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemWeatherHeaderBinding.inflate(layoutInflater, parent, false)
                return HeaderViewHolder(binding)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<DataItemTodayWeather>() {
        override fun areItemsTheSame(
            oldItem: DataItemTodayWeather,
            newItem: DataItemTodayWeather
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: DataItemTodayWeather,
            newItem: DataItemTodayWeather
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun getItemViewType(position: Int): Int {

        return when (getItem(position)) {

            is DataItemTodayWeather.AdditionalInformation -> VIEW_ADDITIONAL_INFO
            is DataItemTodayWeather.Header -> VIEW_HEADER

            else -> VIEW_HEADER
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {

            is AdditionalInfoHolder -> {
                val item = getItem(position) as DataItemTodayWeather.AdditionalInformation
                holder.bind(item.info)
            }
            is HeaderViewHolder -> {
                val item = getItem(position) as DataItemTodayWeather.Header
                holder.bind(item.header)
            }
        }
    }

    fun submitTodayWeather(todayWeather: TodayWeather?) {
        adapterScope.launch {

            val items = ArrayList<DataItemTodayWeather>()
            todayWeather?.let {
                items.add(DataItemTodayWeather.Header(it))
                items.add(DataItemTodayWeather.AdditionalInformation(it.listOfAdditionalData))
            }
            withContext(Dispatchers.Main) { submitList(items.distinct()) }
        }
    }
}

sealed class DataItemTodayWeather {

    data class Header(val header: TodayWeather): DataItemTodayWeather() { override val id = HEADER_ITEM_ID }
    data class AdditionalInformation(val info: List<AdditionalInfo>): DataItemTodayWeather() { override val id = ADDITIONAL_INFO_ITEM_ID }
    abstract val id: String

}