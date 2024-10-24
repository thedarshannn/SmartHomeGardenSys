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
