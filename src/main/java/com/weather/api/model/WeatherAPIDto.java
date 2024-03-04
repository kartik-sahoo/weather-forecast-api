package com.weather.api.model;

import lombok.Data;

@Data
public class WeatherAPIDto {

	private Location location;
	private Current current;
}