package com.pollyglot.weathapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import static android.R.attr.country;

public class MainActivity extends AppCompatActivity {

    private TextView mWeatherStatus;
    private TextView mTemperature;
    private TextView mCity;
    private ImageView mWeatherIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWeatherStatus = (TextView) findViewById(R.id.weatherStatus);
        mTemperature = (TextView) findViewById(R.id.tempView);
        mCity = (TextView) findViewById(R.id.locationView);
        mWeatherIcon = (ImageView) findViewById(R.id.weatherIcon);

        String url = "http://api.openweathermap.org/data/2.5/weather?q=Lille&appid=86093eb04213b81b8eadf0741b08483e&units=metric";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
//                        mTemperature.setText("Response: " + response.toString());
                        Log.v("WEATHER", "Response: " + response.toString());

                        try {

                            JSONObject mainObject = response.getJSONObject("main");
                            JSONArray weatherArray = response.getJSONArray("weather");
                            JSONObject firstWeatherObject = weatherArray.getJSONObject(0);
                            JSONObject countryObject = response.getJSONObject("sys");

                            String temp = Integer.toString((int)Math.round(mainObject.getDouble("temp")));
                            String weatherDescription = firstWeatherObject.getString("description");
                            String city = response.getString("name");
                            String country = countryObject.getString("country");

                            mTemperature.setText((temp) + "\u00B0");
                            mWeatherStatus.setText(weatherDescription);
                            mCity.setText(city + ", " + country) ;

                            int iconResourceID = getResources().getIdentifier("icon_" + weatherDescription.replace(" ", ""), "drawable", getPackageName());
                            mWeatherIcon.setImageResource(iconResourceID);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });

        // Access the RequestQueue through your singleton class.
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsObjRequest);

    }
}
