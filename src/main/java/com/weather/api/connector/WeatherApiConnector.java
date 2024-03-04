package com.weather.api.connector;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.weather.api.model.WeatherAPIDto;

import java.net.URI;
import java.net.URISyntaxException;

public class WeatherApiConnector {

	private static String baseUrl = "https://api.weatherapi.com/v1/";
	private static String APIKey = "8e8fa98598464f778a395930231312";
	private static String zipUrlParameters = "forecast.json?key=";
	private static String zipUrl = baseUrl + zipUrlParameters + APIKey + "&q=";

	public WeatherAPIDto getWeatherForZipCode(String zipCode) {

		RestTemplate template = new RestTemplate();
		URI uri = null;
		try {
			uri = new URI(zipUrl + zipCode);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		ResponseEntity<WeatherAPIDto> response = template.getForEntity(uri, WeatherAPIDto.class);
		return response.getBody();
	}
}