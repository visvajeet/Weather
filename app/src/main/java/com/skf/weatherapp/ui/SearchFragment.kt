package com.skf.weatherapp.ui

import com.skf.weatherapp.utils.LocationProvideService
import android.content.DialogInterface
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.lifecycleScope
import com.skf.weatherapp.R
import com.skf.weatherapp.adapters.LocationSearchAdapter
import com.skf.weatherapp.databinding.FragmentSearchBinding
import com.skf.weatherapp.models.LocationModel
import com.skf.weatherapp.utils.*
import com.skf.weatherapp.viewmodels.SearchViewModel
import com.skf.weatherapp.viewmodels.SharedViewModel
import timber.log.Timber

class SearchFragment : DialogFragment(), LifecycleObserver, DialogInterface.OnDismissListener {


    private val locationListener = object : LocationListener{
        override fun onLocationChanged(location: Location) {
            Timber.d("Location : ${location.latitude}")
            viewModel.statusLocation.postValue(LocationLoadingStatus.DONE)
            viewModel.searchLocation("${location.latitude},${location.longitude}")
        }

        override fun onProviderEnabled(provider: String) {
            Timber.d("Location enabled : $provider")
            super.onProviderEnabled(provider)
        }
        override fun onProviderDisabled(provider: String) {
            Timber.d("Location disabled : $provider")
            viewModel.statusLocation.postValue(LocationLoadingStatus.DONE)
            super.onProviderDisabled(provider)
        }

    }

    private val locationPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        if(it){
            Timber.d("Location Permission : Granted")
            getCurrentLocation()
        }else{ Timber.d("Location Permission : Denied") }
    }

    private val locationService by lazy {
        LocationProvideService(requireActivity(),locationPermission,locationListener)
    }

    lateinit var binding: FragmentSearchBinding
    lateinit var adapter: LocationSearchAdapter
    private val  viewModel: SearchViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        activity?.lifecycle?.addObserver(this)

        setAdapter()
        observeSearchData()
        listenEditText()

        binding.ivBack.setOnClickListener { dialog?.dismiss() }

        viewModel.status.observe(viewLifecycleOwner,{
            Timber.d("SEARCH STATUS: ${it.name}")
        })

        lifecycleScope.launchWhenResumed {
            if(SharedPref.getString(CURRENT_LOCATION).isEmpty()){ getCurrentLocation() }
            else{ binding.etSearch.requestFocus() }
        }

        binding.llCurrentLocation.setOnClickListener { getCurrentLocation() }

        dialog?.show()
        return binding.root
    }

    private fun getCurrentLocation(){

       binding.etSearch.setText("")
       if(locationService.getLocation() == 1){
           viewModel.statusLocation.postValue(LocationLoadingStatus.LOADING)
       }
    }
    private fun listenEditText() {
        binding.etSearch.addTextChangedListener {
            viewModel.searchLocation(it.toString())
        }
    }

    private fun setAdapter() {
        adapter = LocationSearchAdapter(locationClickListener)
        binding.rvSearch.adapter = adapter
    }

    private fun observeSearchData(){
        viewModel.searchLocation.observe(viewLifecycleOwner,{
            adapter.submitList(it.data)

        })
    }
    private val locationClickListener = LocationSearchAdapter.ClickListener{ view: View, location: LocationModel ->

        SharedPref.setString(CURRENT_LOCATION, location.url.toString())
        sharedViewModel.isLocationChanged(true)
        dismiss()

    }

    override fun getTheme(): Int {
        return R.style.FullScreenDialog
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if(SharedPref.getString(CURRENT_LOCATION).isEmpty()){
            activity?.finish()
        }
    }


}