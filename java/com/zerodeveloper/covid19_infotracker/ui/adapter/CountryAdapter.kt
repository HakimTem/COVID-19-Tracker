package com.zerodeveloper.covid19_infotracker.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zerodeveloper.covid19_infotracker.R
import com.zerodeveloper.covid19_infotracker.ui.home.Country
import com.zerodeveloper.covid19_infotracker.ui.home.orderNumericalValue
import kotlinx.android.synthetic.main.country_data_item.view.*

class CountryAdapter(private val countryList : ArrayList<Country>): RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {

    class CountryViewHolder(itemView : View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.country_data_item, parent, false)
        return CountryViewHolder(view)
    }

    override fun getItemCount(): Int = countryList.size

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val currentCountry = countryList[position]
        holder.itemView.apply {
            name_item.text = currentCountry.mData[0].toString()
            cases_item.text = orderNumericalValue(currentCountry.mData[1] as Int)
            deaths_item.text = orderNumericalValue(currentCountry.mData[2] as Int)
        }
    }
}