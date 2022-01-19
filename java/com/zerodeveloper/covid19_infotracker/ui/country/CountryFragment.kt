package com.zerodeveloper.covid19_infotracker.ui.country

import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.zerodeveloper.covid19_infotracker.R
import com.zerodeveloper.covid19_infotracker.ui.home.orderNumericalValue
import kotlinx.android.synthetic.main.fragment_country.view.*

class CountryFragment : Fragment() {

    private val countryViewModel: CountryViewModel by activityViewModels()
//    private var mapView: MapView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_country, container, false)

//        mapView = root.mapView.apply {
//            onCreate(savedInstanceState)
//            getMapAsync { mapboxMap ->
//                mapboxMap.setStyle(Style.LIGHT) {
//                    val geocoder = Geocoder(context)
//                    val addresses = geocoder.getFromLocationName("Canada", 1)
//                    mapboxMap.cameraPosition =
//                        CameraPosition.Builder().apply {
//                            target(LatLng(addresses[0].latitude, addresses[0].longitude))
//                            zoom(1.5)
//                        }.build()
//                }
//            }
//        }

        root.country_name_input.apply {
            val autoCorrectList = arrayListOf<String>()
            for(country in countryViewModel.countryData.value!!){
                autoCorrectList.add(country.mData[0].toString())
            }
            setAdapter(
                ArrayAdapter(
                    context,
                    android.R.layout.simple_dropdown_item_1line,
                    autoCorrectList.toArray()
                )
            )

            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    val selectedCountry = countryViewModel.countryData.value!!.find { predicate ->
                        s.toString().contains((predicate.mData[0].toString()), ignoreCase = true)
                    }

                    if (selectedCountry != null) {
                        root.country_cases.text =
                            orderNumericalValue(selectedCountry.mData[1] as Int)
                        root.country_deaths.text =
                            orderNumericalValue(selectedCountry.mData[2] as Int)
                        root.country_recoveries.text =
                            orderNumericalValue(selectedCountry.mData[3] as Int)
                        root.country_fatality_rate.text = "${selectedCountry.mData[4]}%"
                        root.country_recovery_rate.text = "${selectedCountry.mData[5]}%"
                        root.daily_country_cases.text =
                            "+${orderNumericalValue(selectedCountry.mData[6] as Int)}"
                        root.daily_country_deaths.text =
                            "+${orderNumericalValue(selectedCountry.mData[7] as Int)}"
                        root.daily_country_recoveries.text =
                            "+${orderNumericalValue(selectedCountry.mData[8] as Int)}"

//                        root.mapView.getMapAsync { mapboxMap ->
//                                val geocoder = Geocoder(context)
//                                val addresses = geocoder.getFromLocationName(s.toString(), 1)
//                                mapboxMap.cameraPosition =
//                                    CameraPosition.Builder().apply {
//                                        target(
//                                            if(addresses.size > 0) {
//                                                LatLng(
//                                                    addresses[0].latitude,
//                                                    addresses[0].longitude
//                                                )
//                                            }
//                                        else{
//                                                LatLng(0.0, 0.0)
//                                            }
//                                        )
//                                        zoom(1.0)
//                                    }.build()
//                        }

                        val assetsFolder = context.assets
                        val imageFile = assetsFolder.open("${selectedCountry.mData[9]}.png")
                        root.country_flag.setImageBitmap(BitmapFactory.decodeStream(imageFile))
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
        }

        return root
    }
}