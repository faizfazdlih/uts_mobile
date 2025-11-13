package com.example.uts_mobile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.uts_mobile.databinding.FragmentCuacaBinding
import java.text.SimpleDateFormat
import java.util.*

class CuacaFragment : Fragment() {

    private var _binding: FragmentCuacaBinding? = null
    private val binding get() = _binding!!

    private val dataCuaca = mapOf(
        "Bandung" to CuacaInfo(
            suhu = 23,
            kondisi = "Berawan",
            kelembapan = 80,
            gambar = R.drawable.ic_cloudy,
            rainChance = 55,
            windSpeed = 12,
            hourlyData = listOf(
                HourlyWeather("14:00", 22, R.drawable.ic_cloudy),
                HourlyWeather("15:00", 22, R.drawable.ic_sunny2),
                HourlyWeather("16:00", 21, R.drawable.ic_cloudy),
                HourlyWeather("17:00", 20, R.drawable.ic_rain),
                HourlyWeather("18:00", 19, R.drawable.ic_night)
            ),
            dailyData = listOf(
                DailyWeather("Selasa", 22, R.drawable.ic_cloudy),
                DailyWeather("Rabu", 23, R.drawable.ic_sunny2),
                DailyWeather("Kamis", 24, R.drawable.ic_sunny2),
                DailyWeather("Jumat", 22, R.drawable.ic_rain)
            )
        ),
        "Jakarta" to CuacaInfo(
            suhu = 31,
            kondisi = "Cerah",
            kelembapan = 65,
            gambar = R.drawable.ic_sunny2,
            rainChance = 20,
            windSpeed = 8,
            hourlyData = listOf(
                HourlyWeather("14:00", 30, R.drawable.ic_sunny2),
                HourlyWeather("15:00", 31, R.drawable.ic_sunny2),
                HourlyWeather("16:00", 30, R.drawable.ic_sunny2),
                HourlyWeather("17:00", 28, R.drawable.ic_cloudy),
                HourlyWeather("18:00", 26, R.drawable.ic_night)
            ),
            dailyData = listOf(
                DailyWeather("Selasa", 30, R.drawable.ic_sunny2),
                DailyWeather("Rabu", 31, R.drawable.ic_sunny2),
                DailyWeather("Kamis", 32, R.drawable.ic_sunny2),
                DailyWeather("Jumat", 29, R.drawable.ic_cloudy)
            )
        ),
        "Surabaya" to CuacaInfo(
            suhu = 34,
            kondisi = "Panas",
            kelembapan = 60,
            gambar = R.drawable.ic_sunny2,
            rainChance = 15,
            windSpeed = 10,
            hourlyData = listOf(
                HourlyWeather("14:00", 33, R.drawable.ic_sunny2),
                HourlyWeather("15:00", 34, R.drawable.ic_sunny2),
                HourlyWeather("16:00", 33, R.drawable.ic_sunny2),
                HourlyWeather("17:00", 30, R.drawable.ic_cloudy),
                HourlyWeather("18:00", 28, R.drawable.ic_night)
            ),
            dailyData = listOf(
                DailyWeather("Selasa", 33, R.drawable.ic_sunny2),
                DailyWeather("Rabu", 34, R.drawable.ic_sunny2),
                DailyWeather("Kamis", 35, R.drawable.ic_sunny2),
                DailyWeather("Jumat", 32, R.drawable.ic_cloudy)
            )
        ),
        "Yogyakarta" to CuacaInfo(
            suhu = 28,
            kondisi = "Hujan Ringan",
            kelembapan = 85,
            gambar = R.drawable.ic_rain,
            rainChance = 75,
            windSpeed = 15,
            hourlyData = listOf(
                HourlyWeather("14:00", 27, R.drawable.ic_rain),
                HourlyWeather("15:00", 27, R.drawable.ic_rain),
                HourlyWeather("16:00", 26, R.drawable.ic_rain),
                HourlyWeather("17:00", 25, R.drawable.ic_cloudy),
                HourlyWeather("18:00", 24, R.drawable.ic_night)
            ),
            dailyData = listOf(
                DailyWeather("Selasa", 27, R.drawable.ic_rain),
                DailyWeather("Rabu", 28, R.drawable.ic_cloudy),
                DailyWeather("Kamis", 29, R.drawable.ic_sunny2),
                DailyWeather("Jumat", 28, R.drawable.ic_cloudy)
            )
        ),
        "Bali" to CuacaInfo(
            suhu = 30,
            kondisi = "Cerah Berawan",
            kelembapan = 70,
            gambar = R.drawable.ic_cloudy,
            rainChance = 40,
            windSpeed = 11,
            hourlyData = listOf(
                HourlyWeather("14:00", 29, R.drawable.ic_cloudy),
                HourlyWeather("15:00", 30, R.drawable.ic_sunny2),
                HourlyWeather("16:00", 29, R.drawable.ic_cloudy),
                HourlyWeather("17:00", 27, R.drawable.ic_cloudy),
                HourlyWeather("18:00", 25, R.drawable.ic_night)
            ),
            dailyData = listOf(
                DailyWeather("Selasa", 29, R.drawable.ic_cloudy),
                DailyWeather("Rabu", 30, R.drawable.ic_sunny2),
                DailyWeather("Kamis", 31, R.drawable.ic_sunny2),
                DailyWeather("Jumat", 29, R.drawable.ic_rain)
            )
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCuacaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dateFormat = SimpleDateFormat("'Tanggal', dd/MM", Locale("pt", "BR"))
        binding.tvDate.text = dateFormat.format(Date())

        binding.spinnerCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val kota = parent?.getItemAtPosition(position).toString()
                tampilkanCuaca(kota)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        tampilkanCuaca("Bandung")
    }

    private fun tampilkanCuaca(kota: String) {
        val info = dataCuaca[kota]
        if (info != null) {

            binding.tvLocation.text = "Cuaca di $kota"

            binding.tvTemperature.text = info.suhu.toString()
            binding.imgWeather.setImageResource(info.gambar)

            binding.tvRainChance.text = "${info.rainChance}%"
            binding.tvWindSpeed.text = "${info.windSpeed} km/h"

            updateHourlyForecast(info.hourlyData)

            updateDailyForecast(info.dailyData)
        }
    }

    private fun updateHourlyForecast(hourlyData: List<HourlyWeather>) {
        binding.layoutHourly.removeAllViews()

        hourlyData.forEachIndexed { index, hourly ->
            val itemView = layoutInflater.inflate(
                R.layout.item_hourly_forecast,
                binding.layoutHourly,
                false
            )


            itemView.findViewById<TextView>(R.id.tvHour).text = hourly.hour
            itemView.findViewById<ImageView>(R.id.imgHourlyWeather)
                .setImageResource(hourly.icon)
            itemView.findViewById<TextView>(R.id.tvHourlyTemp).text = "${hourly.temp}°"

            binding.layoutHourly.addView(itemView)
        }
    }

    private fun updateDailyForecast(dailyData: List<DailyWeather>) {
        binding.layoutDaily.removeAllViews()

        dailyData.forEach { daily ->
            val itemView = layoutInflater.inflate(
                R.layout.item_daily_forecast,
                binding.layoutDaily,
                false
            )

            itemView.findViewById<TextView>(R.id.tvDay).text = daily.day
            itemView.findViewById<ImageView>(R.id.imgDailyWeather)
                .setImageResource(daily.icon)
            itemView.findViewById<TextView>(R.id.tvDailyTemp).text = "${daily.temp}°"

            binding.layoutDaily.addView(itemView)
        }
    }

    data class CuacaInfo(
        val suhu: Int,
        val kondisi: String,
        val kelembapan: Int,
        val gambar: Int,
        val rainChance: Int,
        val windSpeed: Int,
        val hourlyData: List<HourlyWeather>,
        val dailyData: List<DailyWeather>
    )

    data class HourlyWeather(
        val hour: String,
        val temp: Int,
        val icon: Int
    )

    data class DailyWeather(
        val day: String,
        val temp: Int,
        val icon: Int
    )

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}