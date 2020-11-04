package com.example.getweatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val backgroundImage = findViewById<ImageView>(R.id.backgroundImage);

        val weatherTypeText = findViewById<TextView>(R.id.weatherTypeText);
        val currentTempText = findViewById<TextView>(R.id.currentTempText);

        val tomorrowText = findViewById<TextView>(R.id.tomorrowText);
        val overmorrowText = findViewById<TextView>(R.id.overmorrowText);
        val tomorrowTemp = findViewById<TextView>(R.id.tomorrowTemp);
        val overmorrowTemp = findViewById<TextView>(R.id.overmorrowTemp);

        val queue = Volley.newRequestQueue(this);
        val url = "https://api.weatherbit.io/v2.0/current?key=********************************&postal_code=04-761&country=PL";
        val urltmrw = "https://api.weatherbit.io/v2.0/forecast/hourly?key=********************************&postal_code=04-761&country=PL&hours=24";
        val urlomrw = "https://api.weatherbit.io/v2.0/forecast/hourly?key=********************************&postal_code=04-761&country=PL";

        val sdf_to = SimpleDateFormat("EEEE");
        val sdf_from = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        val request = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                var typeText : String;
                val data = response.getJSONArray("data").getJSONObject(0);
                if(data.getDouble("precip") != 0.0) { typeText = "Rainy"; }
                    else if(data.getInt("clouds") > 50) { typeText = "Cloudy"; }
                    else { typeText = "Clear"; }

                backgroundImage.setImageResource(if (typeText == "Rainy") R.drawable.ic_rainy_bg else (if (typeText == "Cloudy") R.drawable.ic_clear_bg else R.drawable.ic_sunny_bg));

                weatherTypeText.text = typeText;
                currentTempText.text = data.getInt("temp").toString() + "º";
            },
            Response.ErrorListener { error ->
                Log.d("ERROR", error.toString());
            });

        // there surely is a better way to do is but it's 3am and i cba
        val request2electicboogaloo = JsonObjectRequest(Request.Method.GET, urltmrw, null,
            Response.Listener { response ->
                val data = response.getJSONArray("data").getJSONObject(0);
                Log.d("tomorrow", data.toString());
                tomorrowText.text = sdf_to.format(sdf_from.parse(data.getString("timestamp_local")));
                tomorrowTemp.text = data.getInt("temp").toString() + "º";
            },
            Response.ErrorListener { error ->
                Log.d("ERROR", error.toString());
            })

        val request3twiddledeedee = JsonObjectRequest(Request.Method.GET, urlomrw, null,
            Response.Listener { response ->
                val data = response.getJSONArray("data").getJSONObject(0);
                Log.d("overmorrow", data.toString());
                overmorrowText.text = sdf_to.format(sdf_from.parse(data.getString("timestamp_local")));
                overmorrowTemp.text = data.getInt("temp").toString() + "º";
            },
            Response.ErrorListener { error ->
                Log.d("ERROR", error.toString());
            });

        queue.add(request);
        queue.add(request2electicboogaloo);
        queue.add(request3twiddledeedee);
    }
}