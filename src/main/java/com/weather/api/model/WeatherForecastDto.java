package com.weather.api.model;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WeatherForecastDto implements Serializable {

	private static final long serialVersionUID = 7245956406977078618L;
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