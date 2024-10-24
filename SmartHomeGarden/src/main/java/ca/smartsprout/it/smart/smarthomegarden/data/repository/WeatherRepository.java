package ca.smartsprout.it.smart.smarthomegarden.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ca.smartsprout.it.smart.smarthomegarden.data.model.WeatherResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherRepository {
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";
    private WeatherService weatherService;
    private static final String API_KEY = "16f364b7879945df5f09f3f5b081fb6a";

    public WeatherRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        weatherService = retrofit.create(WeatherService.class);
    }

    public LiveData<WeatherResponse> getWeatherData(double lat, double lon) {
        MutableLiveData<WeatherResponse> weatherData = new MutableLiveData<>();

        weatherService.getWeather(lat, lon, API_KEY).enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    weatherData.setValue(response.body());
                } else {
                    weatherData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                weatherData.setValue(null);
            }
        });

        return weatherData;
    }
}
