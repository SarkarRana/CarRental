package com.sma.smartauto.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import com.sma.smartauto.security.jwt.JwtConfigurer;
import com.sma.smartauto.security.jwt.JwtTokenProvider;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
    JwtTokenProvider jwtTokenProvider;
	
	@Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
        //@formatter:off
		 http.httpBasic().disable().csrf().disable()
		 .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		 .and()
		 .authorizeRequests()
		 .antMatchers("/auth/generateToken").permitAll()
		 .antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security", "/swagger-ui.html", "/webjars/**","/swagger-resources/configuration/**","/swagger-ui.html").permitAll() 
		 .antMatchers("/springfox*").permitAll()
//		 .antMatchers(HttpMethod.POST, "/clients/**").permitAll()
		 .anyRequest().authenticated()
		 .and().apply(new JwtConfigurer(jwtTokenProvider));
        //@formatter:on
    }

}
