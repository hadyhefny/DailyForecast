package com.hefny.hady.dailyforecast.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hefny.hady.dailyforecast.R
import com.hefny.hady.dailyforecast.models.Forecast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_main.*

@AndroidEntryPoint
class MainFragment : Fragment() {
    private val TAG = "AppDebug"
    lateinit var uiCommunicationListener: UICommunicationListener
    lateinit var weatherAdapter: WeatherAdapter
    val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerview()
        var cityName: String
        search_btn.setOnClickListener {
            cityName = city_name_edittext.text.toString()
            if (cityName.isNotBlank()){
                viewModel.setCityName(cityName)
                uiCommunicationListener.hideKeyboard()
            }
        }
        retry_btn.setOnClickListener {
            cityName = city_name_edittext.text.toString()
            viewModel.setCityName(cityName)
            uiCommunicationListener.hideKeyboard()
        }
        viewModel.currentWeatherData.observe(viewLifecycleOwner, { dataResource ->
            // handle loading
            uiCommunicationListener.showProgressBar(dataResource.loading)
            // handle success (loading data from remote data source and cache)
            dataResource.data?.peekContent()?.let {
                Log.d(TAG, "onViewCreated: success: ${it}")
                weatherAdapter.setForecastData(it.forecastList as ArrayList<Forecast>, it.city.name)
                error_constraint_layout.visibility = View.GONE
                weather_recyclerview.visibility = View.VISIBLE
            }
            // handle loading data from cache only
            not_accurate_data_textview.isVisible = !dataResource.message.isNullOrBlank()
            // handle error
            dataResource.error?.peekContent()?.let {
                error_constraint_layout.visibility = View.VISIBLE
                error_textview.text = it
                weather_recyclerview.visibility = View.GONE
            }
        })
    }

    private fun initRecyclerview(){
        weather_recyclerview.run {
            layoutManager = LinearLayoutManager(requireContext())
            weatherAdapter = WeatherAdapter()
            adapter = weatherAdapter
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            uiCommunicationListener = context as UICommunicationListener
        } catch (e: ClassCastException) {
            Log.e(TAG, "onAttach: $context must implement UICommunicationListener")
        }
    }
}