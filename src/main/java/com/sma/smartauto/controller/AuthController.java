package com.sma.smartauto.controller;

import static org.springframework.http.ResponseEntity.ok;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sma.smartauto.domain.RestClient;
import com.sma.smartauto.model.AuthenticationRequestDto;
import com.sma.smartauto.repository.RestClientRepository;
import com.sma.smartauto.security.jwt.JwtTokenProvider;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "API key Authentication for access token.")
@RestController
@RequestMapping("/auth")
public class AuthController {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtTokenProvider jwtTokenProvider;

	@Autowired
	RestClientRepository restClientRepo;

	@PostMapping("/generateToken")
	public ResponseEntity<Map<Object, Object>> generateToken(@RequestBody AuthenticationRequestDto data) {

		try {
			String restClientName = data.getClientName();
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(restClientName, data.getClientKey()));
			Optional<RestClient> userOpt = this.restClientRepo.findByRestClientName(restClientName);
			if (!userOpt.isPresent()) {
				LOGGER.error("Rest Client: " + restClientName + "not found");
				throw new UsernameNotFoundException("Rest Client: " + restClientName + "not found");
			}
			List<String> roleList = new ArrayList<>();
			roleList.add(userOpt.get().getRole());
			String token = jwtTokenProvider.createToken(restClientName, roleList);

			Map<Object, Object> model = new HashMap<>();
			model.put("client", restClientName);
			model.put("token", token);
			model.put("success", true);
			return ok(model);
		} catch (AuthenticationException e) {
			LOGGER.error("Invalid username or password: " + data.toString());
			throw new BadCredentialsException("Invalid username or password");
		}
	}
}
