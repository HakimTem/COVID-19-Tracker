package com.zerodeveloper.covid19_infotracker.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.zerodeveloper.covid19_infotracker.R
import com.zerodeveloper.covid19_infotracker.ui.adapter.CountryAdapter
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        homeViewModel.cases.observe(viewLifecycleOwner, Observer {
            root.confirmed_cases_text.text = orderNumericalValue(it[0])
            root.deaths_text.text = orderNumericalValue(it[1])
            root.recoveries_text.text = orderNumericalValue(it[2])
            root.fatality_rate.text = "${it[3]}%"
            root.recovery_rate.text = "${it[4]}%"
            root.daily_cases.text = "+ ${orderNumericalValue(it[5])}"
            root.daily_deaths.text = "+ ${orderNumericalValue(it[6])}"
            root.daily_recoveries.text = "+ ${orderNumericalValue(it[7])}"
        })

        root.country_list.apply {
            adapter = CountryAdapter(homeViewModel.globalData.value!!)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        homeViewModel.globalData.observe(viewLifecycleOwner, Observer {
            for (country in it) {
                root.country_list.adapter = CountryAdapter(it)
            }
        })
        return root
    }

}

fun orderNumericalValue(n: Int): String {
    val formatter = NumberFormat.getInstance(Locale.US) as DecimalFormat
    formatter.applyPattern("#,###")
    return formatter.format(n)
}
