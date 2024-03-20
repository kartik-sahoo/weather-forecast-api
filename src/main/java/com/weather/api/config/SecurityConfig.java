
package com.weather.api.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	@Order(1)
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Order(2)
	InMemoryUserDetailsManager configureGlobal(PasswordEncoder encoder, @Value("${auth.username}") String username,
			@Value("${auth.password}") String password) {

		return new InMemoryUserDetailsManager(
				User.withUsername(username).password(encoder.encode(password)).authorities("ROLE_USER").build());
	}

	@Bean
	@Order(3)
	SecurityFilterChain clientEnablement(HttpSecurity http) throws Exception {
		http.csrf().disable().sessionManagement().sessionCreationPolicy(STATELESS).and()
				.authorizeHttpRequests(auth -> auth.anyRequest().authenticated()).httpBasic();

		return http.build();
	}
}