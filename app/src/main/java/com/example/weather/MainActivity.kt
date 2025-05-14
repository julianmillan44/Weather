package com.example.weather

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var txtISOCode: TextInputEditText
    private lateinit var txtCityName: TextInputEditText
    private lateinit var txtCurrentTemperature: TextView
    private lateinit var txtMaximumTemperature: TextView
    private lateinit var txtMinimumTemperature: TextView
    private lateinit var btnGetInfo: MaterialButton

    //ApiKey de OpenWeather
    private val apiKey = "983451457c84601521a69e20d30e0c91"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar las vistas
        txtISOCode = findViewById(R.id.txtISOCode)
        txtCityName = findViewById(R.id.txtCityName)
        txtCurrentTemperature = findViewById(R.id.txtCurrentTemperature)
        txtMaximumTemperature = findViewById(R.id.txtMaximumTemperature)
        txtMinimumTemperature = findViewById(R.id.txtMinimum)
        btnGetInfo = findViewById(R.id.btnGetInfo)

        btnGetInfo.setOnClickListener {
            fetchWeatherInfo()
        }
    }

    private fun fetchWeatherInfo() {
        val isoCode = txtISOCode.text.toString().trim()
        val cityName = txtCityName.text.toString().trim()

        if (isoCode.isEmpty() || cityName.isEmpty()) {
            Toast.makeText(this, "Por favor ingresar los dos campos", Toast.LENGTH_LONG).show()
        } else {
            val url = "https://api.openweathermap.org/data/2.5/weather?q=$cityName,$isoCode&units=metric&appid=$apiKey"

            // Crear solicitudes con Volley
            val requestQueue: RequestQueue = Volley.newRequestQueue(this)

            // Crear la solicitud al API
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                { response ->
                    this.handleWeatherResponse(response)
                },
                { error ->
                    Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_LONG).show()
                }
            )
            // Agregar la solicitud
            requestQueue.add(jsonObjectRequest)
        }
    }

    private fun handleWeatherResponse(response: JSONObject) {
        // Obtener los datos del clima, desde la respuesta JSON de la API
        val main = response.getJSONObject("main")
        val currentTemperature = "${main.getDouble("temp")}°C"
        val minTemperature = "${main.getDouble("temp_min")}°C"
        val maxTemperature = "${main.getDouble("temp_max")}°C"

        txtCurrentTemperature.apply {
            visibility = View.VISIBLE
            text = currentTemperature
        }
        txtMinimumTemperature.apply {
            visibility = View.VISIBLE
            text = minTemperature
        }
        txtMaximumTemperature.apply {
            visibility = View.VISIBLE
            text = maxTemperature
        }
    }
}