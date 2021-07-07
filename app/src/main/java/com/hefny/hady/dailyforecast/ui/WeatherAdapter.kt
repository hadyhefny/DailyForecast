package com.hefny.hady.dailyforecast.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hefny.hady.dailyforecast.R
import com.hefny.hady.dailyforecast.models.Forecast
import kotlinx.android.synthetic.main.weather_item.view.*

class WeatherAdapter : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {
    var forecastList = ArrayList<Forecast>()
    lateinit var cityName: String
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.weather_item, parent, false)
        return WeatherViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val forecast = forecastList[position]
        holder.bin(forecast, cityName)
    }

    override fun getItemCount(): Int {
        return forecastList.size
    }

    fun setForecastData(forecastList: ArrayList<Forecast>, cityName: String) {
        this.forecastList = forecastList
        this.cityName = cityName
        notifyDataSetChanged()
    }

    class WeatherViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bin(item: Forecast, cityName: String) {
            itemView.city_name_textview.text = cityName
            itemView.description_textview.text = item.weatherDescription[0].description
            itemView.temp_textview.text = item.weatherData.temp.toString()
            itemView.temp_max_textview.text = item.weatherData.tempMax.toString()
            itemView.temp_min_textview.text = item.weatherData.tempMin.toString()
        }
    }
}