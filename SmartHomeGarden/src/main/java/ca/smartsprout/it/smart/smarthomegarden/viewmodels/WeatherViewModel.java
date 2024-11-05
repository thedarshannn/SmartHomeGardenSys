/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01584247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */
package ca.smartsprout.it.smart.smarthomegarden.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ca.smartsprout.it.smart.smarthomegarden.data.model.WeatherResponse;
import ca.smartsprout.it.smart.smarthomegarden.data.repository.WeatherRepository;

public class WeatherViewModel extends ViewModel {
    private WeatherRepository weatherRepository;
    private MutableLiveData<WeatherResponse> weatherData;

    public WeatherViewModel() {
        weatherRepository = new WeatherRepository();
        weatherData = new MutableLiveData<>();
    }

    public LiveData<WeatherResponse> getWeatherData() {
        return weatherData;
    }

    public void fetchWeatherData(double lat, double lon) {
        weatherRepository.getWeatherData(lat, lon).observeForever(weatherResponse -> {
            weatherData.setValue(weatherResponse);
        });
    }
}
