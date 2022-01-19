package com.zerodeveloper.covid19_infotracker.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


class Country(vararg countryData: Any) {
    var mData = countryData
}

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val _cases = MutableLiveData<Array<Int>>().apply {
        value = arrayOf(10, 10, 10, 10, 10, 10, 10, 10)
    }
    val cases: LiveData<Array<Int>> = _cases
    fun selectCasesData(casesData : Array<Int>){
        this._cases.value = casesData
    }

    private val _globalData = MutableLiveData<ArrayList<Country>>().apply {
        value = arrayListOf()
    }
    val globalData: LiveData<ArrayList<Country>> = _globalData
    fun selectGlobalData(globalData : ArrayList<Country>){
        this._globalData.value = globalData
    }


}