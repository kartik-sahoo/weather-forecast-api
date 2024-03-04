package com.weather.api.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.weather.api.connector.WeatherApiConnector;
import com.weather.api.model.WeatherAPIDto;
import com.weather.api.model.WeatherForecastDto;

import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WeatherService {

	@Cacheable("weatherForecast")
	public WeatherForecastDto getWeatherForZipCode(String zipCode) {

		WeatherForecastDto weatherFCDto = null;
		log.info("Get weather forecast by zipcode API is being called");
		WeatherApiConnector connector = new WeatherApiConnector();
		WeatherAPIDto connectorWeatherForZipCode = connector.getWeatherForZipCode(zipCode);
		weatherFCDto = getWeatherForecast(connectorWeatherForZipCode);
		return weatherFCDto;
	}

	private WeatherForecastDto getWeatherForecast(WeatherAPIDto weatherAPIDto) {

		JSONObject jsonWeatherForecast = null;
		CloseableHttpResponse response = null;
		WeatherForecastDto weatherDto = null;
		try {
			URIBuilder builder = new URIBuilder(
					"https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/weatherdata/forecast");
			builder.setParameter("aggregateHours", "24").setParameter("contentType", "json")
					.setParameter("unitGroup", "metric").setParameter("locationMode", "single")
					.setParameter("key", "79Z3M6LN7SADZTJGRV8YH58UD")
					.setParameter("locations", weatherAPIDto.getLocation().getTz_id());

			HttpGet get = new HttpGet(builder.build());

			CloseableHttpClient httpclient = HttpClients.createDefault();

			response = httpclient.execute(get);

			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String rawResult = EntityUtils.toString(entity, Charset.forName("utf-8"));
				jsonWeatherForecast = new JSONObject(rawResult);
			}
			JSONObject location = jsonWeatherForecast.getJSONObject("location");
			JSONObject cc = location.getJSONObject("currentConditions");
			JSONArray valuesArr = location.getJSONArray("values");
			JSONObject valouObject = valuesArr.getJSONObject(0);

			weatherDto = new WeatherForecastDto();
			weatherDto.setLocation(location.getString("address"));
			weatherDto.setTimestamp(cc.getString("datetime"));
			weatherDto.setCurrentTemp(cc.getDouble("temp"));
			weatherDto.setMaxTemp(valouObject.getDouble("maxt"));
			weatherDto.setMinTemp(valouObject.getDouble("mint"));
			weatherDto.setWindSpeed_mps(Math.round(cc.getDouble("wspd") / 3.6));
			weatherDto.setRel_humidity((int) cc.getDouble("humidity"));
			weatherDto.setWeather_description(valouObject.getString("conditions"));
			weatherDto.setTimeZone(weatherAPIDto.getLocation().getTz_id());
			weatherDto.setWind_direction(weatherAPIDto.getCurrent().getWind_dir());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return weatherDto;
	}
}