package ca.smartsprout.it.smart.smarthomegarden.data.repository;

import ca.smartsprout.it.smart.smarthomegarden.data.model.WeatherResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {
    @GET("weather")
    Call<WeatherResponse> getWeather(@Query("lat") double lat, @Query("lon") double lon, @Query("appid") String apiKey);
}
