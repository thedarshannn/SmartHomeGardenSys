package ca.smartsprout.it.smart.smarthomegarden.data.model;

public class WeatherResponse {
    public Main main;

    public class Main {
        public float temp_max;
        public float temp_min;
    }
}
