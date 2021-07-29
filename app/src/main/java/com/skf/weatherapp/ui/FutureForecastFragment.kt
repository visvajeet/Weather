package com.skf.weatherapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.skf.weatherapp.adapters.ForecastWeatherAdapter
import com.skf.weatherapp.databinding.FragmentFutureForecastBinding
import com.skf.weatherapp.utils.Status
import com.skf.weatherapp.utils.showToast
import com.skf.weatherapp.utils.verifyInternet
import com.skf.weatherapp.viewmodels.FutureForecastViewModel
import com.skf.weatherapp.viewmodels.NewLocationStatus
import com.skf.weatherapp.viewmodels.SharedViewModel

class FutureForecastFragment : Fragment() {

    private lateinit var binding: FragmentFutureForecastBinding
    private lateinit var adapter: ForecastWeatherAdapter
    private val viewModel: FutureForecastViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentFutureForecastBinding.inflate(inflater, container, false)
        setAdapter()
        observeTodayWeather()
        observeLocationChanged()
        binding.srl.setOnRefreshListener {
            if(!requireContext().verifyInternet()) {
                binding.srl.isRefreshing = false
                return@setOnRefreshListener
            }
            viewModel.refreshData()
        }

        if (!viewModel.forecastWeather.value?.message.isNullOrEmpty()) {
            viewModel.refreshData()
        }
        adapter.submitFutureForecast(viewModel.forecastWeather.value?.data)

        return binding.root

    }

    private fun setAdapter() {
        adapter = ForecastWeatherAdapter()
        binding.rvFutureForecast.adapter = adapter
    }

    private fun observeLocationChanged(){
        sharedViewModel.newLocationStatusFutureForecast.observe(viewLifecycleOwner,{
            if(it == NewLocationStatus.NEW){
                viewModel.refreshData()
                sharedViewModel.newLocationUpdateDoneFutureForecast()
            }
        })
    }
    private fun observeTodayWeather() {

        lifecycleScope.launchWhenResumed {

            viewModel.forecastWeather.observe(viewLifecycleOwner, {
                when (it.status) {
                    Status.LOADING -> {
                        binding.srl.isRefreshing = true
                    }
                    Status.ERROR -> {
                        binding.srl.isRefreshing = false
                        requireContext().showToast(it.message)
                    }
                    Status.SUCCESS -> {
                        binding.srl.isRefreshing = false
                        adapter.submitFutureForecast(viewModel.forecastWeather.value?.data)
                    }
                }
            })
        }
    }
}

