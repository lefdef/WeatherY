package com.brighamdiaz.weathery;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.brighamdiaz.weathery.model.Forecast;
import com.brighamdiaz.weathery.model.ForecastResponse;

import java.util.LinkedHashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Forecast weather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WeatherAPIInterface apiService = WeatherAPIClient.getClient().create(WeatherAPIInterface.class);

        // insert items into a linked hash map to preserve insertion order. Do not set encoded=true
        Map<String, String> query = new LinkedHashMap<>();
        query.put("q", "select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"nome, AK\")");
        query.put("format", "json");
        query.put("env", "store://datatables.org/alltableswithkeys");

        Call<ForecastResponse> call = apiService.getForecast(query);
        call.enqueue(new Callback<ForecastResponse>() {
            @Override
            public void onResponse(Call<ForecastResponse> call, Response<ForecastResponse> response) {
                weather = response.body().getWeatherResult();
                // TODO: pass data model to adapter
            }

            @Override
            public void onFailure(Call<ForecastResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}