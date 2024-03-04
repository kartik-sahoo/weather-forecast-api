package com.weather.api.model;

import lombok.Data;

@Data
public class WeatherForecastDto {

	private String location;
	private String timeZone;
	private String timestamp;
	private double currentTemp;
	private double maxTemp;
	private double minTemp;
	private double windSpeed_mps;
	private double rel_humidity;
	private String wind_direction;
	private String weather_description;
}