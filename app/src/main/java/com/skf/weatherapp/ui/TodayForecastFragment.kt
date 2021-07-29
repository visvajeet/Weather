package com.skf.weatherapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.skf.weatherapp.adapters.TodayWeatherAdapter
import com.skf.weatherapp.databinding.FragmentTodayForecastBinding
import com.skf.weatherapp.utils.*
import com.skf.weatherapp.viewmodels.NewLocationStatus
import com.skf.weatherapp.viewmodels.SharedViewModel
import com.skf.weatherapp.viewmodels.TodayForecastViewModel

class TodayForecastFragment : Fragment() {


    private lateinit var binding: FragmentTodayForecastBinding
    private val viewModel:TodayForecastViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var adapter:TodayWeatherAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentTodayForecastBinding.inflate(inflater, container, false)
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

        adapter.submitTodayWeather(viewModel.todayWeather.value?.data)

        return binding.root

    }

    private fun observeLocationChanged(){
        sharedViewModel.newLocationStatusTodayForecast.observe(viewLifecycleOwner,{
            if(it == NewLocationStatus.NEW){
                viewModel.refreshData()
                sharedViewModel.newLocationUpdateDoneTodayForecast()
            }
        })
    }

    private fun setAdapter() {
        adapter = TodayWeatherAdapter()
        binding.rvToday.adapter = adapter
    }

    private fun observeTodayWeather(){

        lifecycleScope.launchWhenResumed {

            viewModel.todayWeather.observe(viewLifecycleOwner, {
                when (it.status) {
                    Status.LOADING -> { binding.srl.isRefreshing = true }
                    Status.ERROR -> {
                        binding.srl.isRefreshing = false
                        requireContext().showToast(it.message)
                    }
                    Status.SUCCESS -> {
                        binding.srl.isRefreshing = false
                        adapter.submitTodayWeather(viewModel.todayWeather.value?.data)
                    }
                }
            })
        }
    }
}