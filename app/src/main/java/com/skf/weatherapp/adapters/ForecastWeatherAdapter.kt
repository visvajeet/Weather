package com.skf.weatherapp.adapters
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.skf.weatherapp.databinding.ListItemFutureForecastBinding
import com.skf.weatherapp.databinding.ListItemHeadingBinding
import com.skf.weatherapp.models.ForecastDay
import com.skf.weatherapp.models.FutureForecast
import com.skf.weatherapp.models.TodayWeather
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val HEADER_ITEM_ID = "HEADER_ITEM"

private const val VIEW_HEADER = 0
private const val VIEW_FUTURE_FORECAST = 3
private const val VIEW_HEADING = 4

class ForecastWeatherAdapter : ListAdapter<DataItemsForecast, RecyclerView.ViewHolder>(DiffCallback) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder{
        return when (viewType) {

            VIEW_HEADER -> TodayWeatherAdapter.HeaderViewHolder.from(parent)
            VIEW_FUTURE_FORECAST -> FutureForecastHolder.from(parent)
            VIEW_HEADING-> HeadingViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }


    class HeadingViewHolder private constructor(private val binding: ListItemHeadingBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            heading: String?) {
            binding.heading = heading
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): HeadingViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemHeadingBinding.inflate(layoutInflater, parent, false)
                return HeadingViewHolder(binding)
            }
        }
    }

    class FutureForecastHolder private constructor(private val binding: ListItemFutureForecastBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            day: ForecastDay
        ) {
            binding.day = day
            binding.executePendingBindings()

        }

        companion object {
            fun from(parent: ViewGroup): FutureForecastHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemFutureForecastBinding.inflate(layoutInflater, parent, false)
                return FutureForecastHolder(binding)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<DataItemsForecast>() {
        override fun areItemsTheSame(oldItem: DataItemsForecast, newItem: DataItemsForecast): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: DataItemsForecast, newItem: DataItemsForecast): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun getItemViewType(position: Int): Int {

        return  when(getItem(position)){

            is DataItemsForecast.Header -> VIEW_HEADER
            is DataItemsForecast.FutureForecast -> VIEW_FUTURE_FORECAST
            is DataItemsForecast.Heading -> VIEW_HEADING

            else -> VIEW_HEADER
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(holder){

            is TodayWeatherAdapter.HeaderViewHolder -> {
                val item = getItem(position) as DataItemsForecast.Header
                holder.bind(item.header)
            }
            is FutureForecastHolder -> {
                val item = getItem(position) as DataItemsForecast.FutureForecast
                holder.bind(item.day)
            }
            is HeadingViewHolder -> {
                val item = getItem(position) as DataItemsForecast.Heading
                holder.bind(item.heading)
            }
        }
    }

    fun submitFutureForecast(futureForecast: FutureForecast?){

        adapterScope.launch {

            val items = ArrayList<DataItemsForecast>()

            futureForecast?.let {
                items.add(DataItemsForecast.Header(TodayWeather(it.location, it.current)))
                if (!it.forecast?.forecastList.isNullOrEmpty()) {
                    items.add(DataItemsForecast.Heading("Next ${it.forecast?.forecastList?.size} Days"))
                    it.forecast?.forecastList?.map { forecastDay -> items.add(DataItemsForecast.FutureForecast(forecastDay)) }
                }
              }
                withContext(Dispatchers.Main){ submitList(items.distinct()) }
         }

    }

}

sealed class DataItemsForecast {

    data class Heading(val heading: String): DataItemsForecast() { override val id = heading }
    data class Header(val header: TodayWeather): DataItemsForecast() { override val id = HEADER_ITEM_ID }
    data class FutureForecast(val day: ForecastDay): DataItemsForecast() { override val id = day.dateEpoch.toString() }
    abstract val id: String

}