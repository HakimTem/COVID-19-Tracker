package com.zerodeveloper.covid19_infotracker

import android.app.Activity
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.zerodeveloper.covid19_infotracker.ui.country.CountryViewModel
import com.zerodeveloper.covid19_infotracker.ui.fullscreenActivity
import com.zerodeveloper.covid19_infotracker.ui.home.Country
import com.zerodeveloper.covid19_infotracker.ui.home.HomeViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.ref.WeakReference
import java.net.URL
import java.util.*
import javax.net.ssl.HttpsURLConnection
import kotlin.collections.ArrayList
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        Mapbox.getInstance(
//            applicationContext,
//            getString(R.string.mapbox_access_token)
//        )

        refresh_swipe.apply {
            setOnRefreshListener {

                val coronavirusDataTask = CoronavirusDataTask(this@MainActivity)
                coronavirusDataTask.execute()
            }
        }

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)

        fullscreenActivity(this, container)

        val coronavirusDataTask = CoronavirusDataTask(this)
        coronavirusDataTask.execute()

    }

    fun recordCovidData(json: String) {
        val homeViewModel: HomeViewModel by viewModels()
        val countryViewModel: CountryViewModel by viewModels()

        val jsonObject = JSONObject(json)
        with(jsonObject.getJSONObject("Global")) {
            val totalDeaths = getInt("TotalDeaths")
            val totalRecovered = getInt("TotalRecovered")

            val fatalityRate =
                ((totalDeaths.toDouble() / (totalDeaths + totalRecovered)) * 100).roundToInt()

            val recoveryRate = 100 - fatalityRate

            homeViewModel.selectCasesData(
                arrayOf(
                    getInt("TotalConfirmed"),
                    totalDeaths,
                    totalRecovered,
                    fatalityRate,
                    recoveryRate,
                    getInt("NewConfirmed"),
                    getInt("NewDeaths"),
                    getInt("NewRecovered")
                )
            )
        }

//        val countries = ArrayList<Country>()
        val detailedCountries = ArrayList<Country>()
        with(jsonObject.getJSONArray("Countries")) {
            for (countryIndex in 0 until length()) {
                val currentCountry = this[countryIndex] as JSONObject
                val totalDeaths = currentCountry.getInt("TotalDeaths")
                val totalRecovered = currentCountry.getInt("TotalRecovered")
                val fatalityRate =
                    if (totalDeaths + totalRecovered != 0) {
                        ((totalDeaths.toDouble() / (totalDeaths + totalRecovered)) * 100).roundToInt()
                    } else {
                        0
                    }
                val recoveryRate = 100 - fatalityRate

                detailedCountries.add(
                    Country(
                        currentCountry.getString("Slug").toProper(),
                        currentCountry.getInt("TotalConfirmed"),
                        totalDeaths,
                        totalRecovered,
                        fatalityRate,
                        recoveryRate,
                        currentCountry.getInt("NewConfirmed"),
                        currentCountry.getInt("NewDeaths"),
                        currentCountry.getInt("NewRecovered"),
                        currentCountry.getString("CountryCode").toLowerCase(Locale.getDefault())
                    )
                )
            }
        }
        homeViewModel.selectGlobalData(detailedCountries)
        countryViewModel.selectCountryData(detailedCountries)
    }

    class CoronavirusDataTask(activity: Activity) : AsyncTask<Void, Int, String>() {
        private val mActivity = WeakReference(activity)

        override fun doInBackground(vararg params: Void?): String? {
            return try {
                val url = URL("https://api.covid19api.com/summary")
                val httpsURLConnection = url.openConnection() as HttpsURLConnection
                val inputStream = InputStreamReader(httpsURLConnection.inputStream)
                val bufferedReader = BufferedReader(inputStream)

                var totalString = ""
                var line: String? = bufferedReader.readLine()
                while (line != null) {
                    totalString += line
                    line = bufferedReader.readLine()
                }
                bufferedReader.close()
                totalString
            } catch (e: IOException) {
                Log.e("COVIDAPI", "Error reading coronavirus api - $e")
                null
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            with(mActivity.get()!! as MainActivity) {
                if (result != null) {
                    recordCovidData(result)
                    covid_data_progress.visibility = View.GONE
                    progress_slide.visibility = View.GONE

                    if (refresh_swipe.isRefreshing) {
                        refresh_swipe.isRefreshing = false
                    }
                } else {
                    progress_slide.visibility = View.VISIBLE
                    covid_data_progress.visibility = View.INVISIBLE
                    connectivity_header.text = getString(R.string.title_no_internet)
                }
            }
        }
    }
}

fun String.toProper(): String {
    return this.split("-").joinToString(separator = " ") { it.capitalize() }
}

