package com.sma.smartauto.controller;

import static org.springframework.http.ResponseEntity.ok;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sma.smartauto.dao.ClientDaoService;
import com.sma.smartauto.dao.SafeDaoService;
import com.sma.smartauto.domain.Client;
import com.sma.smartauto.domain.Safe;
import com.sma.smartauto.exception.ApiErrorCode;
import com.sma.smartauto.exception.InvalidRequestParameterException;
import com.sma.smartauto.exception.ResourceNotFoundException;
import com.sma.smartauto.model.SafeDto;
import com.sma.smartauto.utils.CommonUtils;

@RequestMapping("/safes")
@RestController
public class SafeController {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	private MessageSource messageSource;

	@Autowired
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	@Autowired
	private SafeDaoService safeDaoService;
	
	@Autowired
	private ClientDaoService clientDaoService;
	
	//Get all users
	@GetMapping("/allSafes")
	public ResponseEntity<Map<String, Object>> getAllSafes() throws Exception {
		List<SafeDto> safes = new ArrayList<>();		
		List<Safe> safeList = safeDaoService.findAll();
		if(safeList != null && !safeList.isEmpty()) {
			for(Safe safe : safeList) {
				SafeDto safeDto = new SafeDto(safe.getSafeId(), safe.getName(), safe.getClient().getId(), safe.getClient().getName(),
						safe.getLocation().getLocId(), safe.getLocation().getName(),
						CommonUtils.deriveActionByName(safe.getCreatedBy(), safe.getClient().getName(), safe.getLocation().getName()), safe.getCreatedDate(),
						CommonUtils.deriveActionByName(safe.getLastUpdatedBy(), safe.getClient().getName(), safe.getLocation().getName()), safe.getLastUpdatedDate());
				safes.add(safeDto);
			}
		}
		Map<String, Object> model = new HashMap<>();
		model.put("safes", safes);
		model.put("statusDetail", safes.isEmpty() ? messageSource.getMessage("no.safe.found", null, Locale.US) : messageSource.getMessage("total.safes.found", null, Locale.US).replace("x", safes.size()+""));
		model.put("success", true);
		return ok(model);
	}
	
	@GetMapping("/")
	public ResponseEntity<Map<String, Object>> getAllSafesForClient(@RequestParam("clientId") Integer clientId) throws Exception {
		List<SafeDto> safes = new ArrayList<>();
		if(clientId == null) {
			throw new ResourceNotFoundException(ApiErrorCode.MISSING_QUERY_PARAM + " Missing query param clientId");
		}
		Client client = clientDaoService.findClientById(clientId);
		List<Safe> safeList = safeDaoService.findAllSafeForClient((clientId));
		if(safeList != null && !safeList.isEmpty()) {
			for(Safe safe : safeList) {
				SafeDto safeDto = new SafeDto(safe.getSafeId(), safe.getName(), (clientId), safe.getClient().getName(),
						safe.getLocation().getLocId(), safe.getLocation().getName(),
						CommonUtils.deriveActionByName(safe.getCreatedBy(), client.getName(), safe.getLocation().getName()), safe.getCreatedDate(),
						CommonUtils.deriveActionByName(safe.getLastUpdatedBy(), client.getName(), safe.getLocation().getName()), safe.getLastUpdatedDate());
				safes.add(safeDto);
			}
		}
		Map<String, Object> model = new HashMap<>();
		model.put("safes", safes);
		model.put("statusDetail", safes.isEmpty() ? messageSource.getMessage("no.safe.found", null, Locale.US) : messageSource.getMessage("total.safes.found", null, Locale.US).replace("x", safes.size()+""));
		model.put("success", true);
		return ok(model);
	}
	
	//Get safe by id
	@GetMapping("/{safeId}")
	public ResponseEntity<SafeDto> getSafeById(@RequestParam("clientId") Integer clientId, @PathVariable Integer safeId) {
		if(clientId == null) {
			throw new ResourceNotFoundException(ApiErrorCode.MISSING_QUERY_PARAM + " Missing query param clientId");
		}
		Client client = clientDaoService.findClientById(clientId);
		Safe safe = safeDaoService.find(clientId, safeId);
		if(safe == null) {
			throw new ResourceNotFoundException(messageSource.getMessage("safe.not.found", null, Locale.US) + ": " + safeId.toString());
		}
		SafeDto safeDto = new SafeDto(safe.getSafeId(), safe.getName(), (clientId), safe.getClient().getName(),
				safe.getLocation().getLocId(), safe.getLocation().getName(),
				CommonUtils.deriveActionByName(safe.getCreatedBy(), client.getName(), safe.getLocation().getName()), safe.getCreatedDate(),
				CommonUtils.deriveActionByName(safe.getLastUpdatedBy(), client.getName(), safe.getLocation().getName()), safe.getLastUpdatedDate());
		safeDto.setStatusDetail(messageSource.getMessage("safe.found", null, Locale.US));
		safeDto.setSuccess(true);
		return ok(safeDto);
	}
	
	//Save Safe
		@PostMapping("/")
		@ResponseStatus(HttpStatus.CREATED)
		public ResponseEntity<SafeDto> saveSafe(@RequestParam("clientId") Integer clientId, @Valid @RequestBody SafeDto safe, @RequestHeader("User-Agent") String userAgent) throws Exception {
			if(clientId == null) {
				throw new ResourceNotFoundException(ApiErrorCode.MISSING_QUERY_PARAM + " Missing query param clientId");
			}
			if(safe == null || safe.getName() == null || safe.getName().isEmpty() || safe.getSafeId() != null) {
				throw new InvalidRequestParameterException("Incorrect request input data");
			}
			if(safeDaoService.findByName(clientId, safe.getName()) != null) {
				throw new InvalidRequestParameterException("Safe with same name already exist");
			}
			safe.setLastUpdatedById(CommonUtils.getActionById(userAgent, clientId));
			SafeDto savedSafe = safeDaoService.save(clientId, safe);
			savedSafe.setStatusDetail(messageSource.getMessage("safe.created", null, Locale.US));
			savedSafe.setSuccess(true);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedSafe);
		}
		
		//Update Safe
		@PutMapping("/")
		public ResponseEntity<SafeDto> updateSafe(@RequestParam("clientId") Integer clientId, @Valid @RequestBody SafeDto safe, @RequestHeader("User-Agent") String userAgent) throws Exception {
			if(clientId == null) {
				throw new ResourceNotFoundException(ApiErrorCode.MISSING_QUERY_PARAM + " Missing query param clientId");
			}
			if(safe == null || safe.getSafeId() == null || safe.getName() == null || safe.getName().isEmpty()) {
				throw new InvalidRequestParameterException(" Safe id or Safe name is missing");
			}
			Safe savedData = safeDaoService.find(clientId, safe.getSafeId());
			if(savedData == null) {				
				throw new ResourceNotFoundException(safe.getSafeId().toString());
			}
			Safe safeWithSameName = safeDaoService.findByName(clientId, safe.getName());
			if(safeWithSameName != null && !safeWithSameName.getSafeId().equals(safe.getSafeId())) {
				throw new InvalidRequestParameterException("Safe with same name already exist");
			}
			safe.setLastUpdatedById(CommonUtils.getActionById(userAgent, clientId));
			SafeDto savedSafe = safeDaoService.save(clientId, safe);
			savedSafe.setStatusDetail(messageSource.getMessage("safe.updated", null, Locale.US));
			savedSafe.setSuccess(true);
			return ok(savedSafe);
		}
	
		//Delete user by id
		@DeleteMapping("/{safeId}")
		public SafeDto deleteSafe(@RequestParam("clientId") Integer clientId, @PathVariable int safeId) throws Exception {
			if(clientId == null) {
				throw new ResourceNotFoundException(ApiErrorCode.MISSING_QUERY_PARAM + " Missing query param clientId");
			}
			Safe safe = safeDaoService.deleteSafe(clientId, safeId);
			if(safe == null) {
				LOGGER.error("Invalid Safe " +safeId);
				throw new ResourceNotFoundException("id:" + safeId);
			}
			SafeDto safeDto = new SafeDto(safeId, safe.getName(), clientId, safe.getClient().getName(), safe.getLocation().getLocId(), safe.getLocation().getName());
			safeDto.setStatusDetail(messageSource.getMessage("safe.deleted", null, Locale.US));
			safeDto.setSuccess(true);
			return safeDto;
		}

}
