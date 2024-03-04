package com.weather.api.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weather.api.model.WeatherForecastDto;
import com.weather.api.service.WeatherService;

@RestController
public class WeatherController {

	@RequestMapping("weather/forecast/{zipCode}")
	WeatherForecastDto getWeatherForZipCode(@PathVariable("zipCode") String zipCode) {

		WeatherService weatherService = new WeatherService();
		return weatherService.getWeatherForZipCode(zipCode);
	}
}