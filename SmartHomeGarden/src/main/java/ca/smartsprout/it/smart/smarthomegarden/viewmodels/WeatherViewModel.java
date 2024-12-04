/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01574247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */

package ca.smartsprout.it.smart.smarthomegarden.viewmodels;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ca.smartsprout.it.smart.smarthomegarden.data.model.WeatherResponse;
import ca.smartsprout.it.smart.smarthomegarden.data.repository.WeatherRepository;

public class WeatherViewModel extends AndroidViewModel {
    private WeatherRepository weatherRepository;
    private MutableLiveData<WeatherResponse> weatherData;
    private MutableLiveData<Boolean> isCelsius;

    public WeatherViewModel(Application application) {
        super(application);
        weatherRepository = new WeatherRepository();
        weatherData = new MutableLiveData<>();
        isCelsius = new MutableLiveData<>();

        // Load initial temperature unit from SharedPreferences
        SharedPreferences prefs = application.getSharedPreferences("settings", Context.MODE_PRIVATE);
        isCelsius.setValue(prefs.getBoolean("isCelsius", true));
    }

    public LiveData<WeatherResponse> getWeatherData() {
        return weatherData;
    }

    public LiveData<Boolean> getIsCelsius() {
        return isCelsius;
    }

    public void fetchWeatherData(double lat, double lon) {
        // Observe weather data and update immediately
        weatherRepository.getWeatherData(lat, lon).observeForever(weatherData::setValue);
    }

    public void toggleTemperatureUnit() {
        boolean currentUnit = isCelsius.getValue() != null && isCelsius.getValue();
        isCelsius.setValue(!currentUnit);

        SharedPreferences prefs = getApplication().getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isCelsius", !currentUnit);
        editor.apply();
    }
}
