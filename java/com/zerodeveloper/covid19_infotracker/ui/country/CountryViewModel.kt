package com.zerodeveloper.covid19_infotracker.ui.country

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zerodeveloper.covid19_infotracker.ui.home.Country

class CountryViewModel : ViewModel() {

    private val _countryData = MutableLiveData<ArrayList<Country>>().apply {
        value = arrayListOf(Country("null", 10, 10, 10))
    }
    val countryData: LiveData<ArrayList<Country>> = _countryData
    fun selectCountryData(countryData : ArrayList<Country>){
        _countryData.value = countryData
    }
}