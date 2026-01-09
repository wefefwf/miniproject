package com.springbootsmini.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		http.authorizeHttpRequests(auth -> 
				auth.requestMatchers("/**").permitAll());
		
		http.csrf(csrf -> 
				csrf.ignoringRequestMatchers("/h2-console/**"));
		
		http.csrf(csrf -> csrf.disable());
		
		http.logout(logout -> logout
				.logoutSuccessUrl("/loginForm")
				.invalidateHttpSession(true));
		
		return http.build();
	}
	
}
