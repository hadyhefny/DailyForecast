package com.hefny.hady.dailyforecast.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hefny.hady.dailyforecast.R
import com.hefny.hady.dailyforecast.models.Forecast
import kotlinx.android.synthetic.main.weather_item.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class WeatherAdapter : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {
    var forecastList = ArrayList<Forecast>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.weather_item, parent, false)
        return WeatherViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val forecast = forecastList[position]
        holder.bind(forecast)
    }

    override fun getItemCount(): Int {
        return forecastList.size
    }

    fun setForecastData(forecastList: ArrayList<Forecast>) {
        this.forecastList = forecastList
        notifyDataSetChanged()
    }

    class WeatherViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: Forecast) {
            val formattedTime = SimpleDateFormat("dd/M/yyyy hh:mm aa", Locale.ENGLISH).format(Date(item.dateInSeconds * 1000))
            itemView.date_textview.text = formattedTime
            itemView.description_textview.text = item.weatherDescription[0].description
            itemView.temp_textview.text = String.format(itemView.context.getString(R.string.temp), item.weatherData.temp.toString())
            itemView.temp_max_textview.text = String.format(itemView.context.getString(R.string.temp), item.weatherData.tempMax.toString())
            itemView.temp_min_textview.text = String.format(itemView.context.getString(R.string.temp), item.weatherData.tempMin.toString())
        }
    }
}