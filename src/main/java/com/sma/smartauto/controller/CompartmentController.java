package com.sma.smartauto.controller;

import static org.springframework.http.ResponseEntity.ok;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sma.smartauto.dao.ClientDaoService;
import com.sma.smartauto.dao.CompartmentDaoService;
import com.sma.smartauto.dao.SafeDaoService;
import com.sma.smartauto.domain.Asset;
import com.sma.smartauto.domain.Client;
import com.sma.smartauto.domain.Compartment;
import com.sma.smartauto.domain.Safe;
import com.sma.smartauto.exception.ApiErrorCode;
import com.sma.smartauto.exception.InvalidRequestParameterException;
import com.sma.smartauto.exception.ResourceNotFoundException;
import com.sma.smartauto.exception.SmartAutoSafeException;
import com.sma.smartauto.model.CompartmentDto;
import com.sma.smartauto.utils.CommonUtils;

//@RequestMapping("/safes/compartments")
@RestController
public class CompartmentController {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	private MessageSource messageSource;

	@Autowired
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	@Autowired
	private ClientDaoService clientDaoService;
	
	@Autowired
	private SafeDaoService safeDaoService;
	
	@Autowired
	private CompartmentDaoService compartmentDaoService;
	
	@GetMapping("/safes/{safeId}/compartments")
	public ResponseEntity<Map<String, Object>> getCompartmentsForSafe(@RequestParam("clientId") Integer clientId, @PathVariable Integer safeId) throws Exception {
		List<CompartmentDto> compartments = new ArrayList<>();
		if(clientId == null) {
			throw new ResourceNotFoundException(ApiErrorCode.MISSING_QUERY_PARAM + " Missing query param clientId");
		}
		List<Compartment> compartmentList = compartmentDaoService.findAllCompartmentOfSafe(clientId, safeId);
		if(compartmentList != null && !compartmentList.isEmpty()) {
			for(Compartment compartment : compartmentList) {
				CompartmentDto compartmentDto = new CompartmentDto(compartment.getId(), compartment.getCompartmentId(), compartment.getName(), compartment.getClient().getId(), 
						compartment.getClient().getName(), safeId, compartment.getSafe().getName(),
						compartment.getEmpty(), CommonUtils.deriveActionByName(compartment.getCreatedBy(), compartment.getClient().getName(), compartment.getSafe().getLocation().getName()),
						compartment.getCreatedDate(), CommonUtils.deriveActionByName(compartment.getLastUpdatedBy(), compartment.getClient().getName(), compartment.getSafe().getLocation().getName()),
						compartment.getLastUpdatedDate());
				compartments.add(compartmentDto);
			}
		}
		Map<String, Object> model = new HashMap<>();
		model.put("compartments", compartments);
		model.put("statudDetail", compartments.isEmpty() ? messageSource.getMessage("no.compartment.found", null, Locale.US) : messageSource.getMessage("total.compartment.found", null, Locale.US).replace("x", compartments.size()+""));
		model.put("success", true);
		return ok(model);
	}
	
	@GetMapping("/safes/{safeId}/compartments/withAssetData")
	public ResponseEntity<Map<String, Object>> getCompartmentWithAssetDetailForSafe(@RequestParam("clientId") Integer clientId, @PathVariable Integer safeId) throws Exception {
		List<CompartmentDto> compartments = new ArrayList<>();
		if(clientId == null) {
			throw new ResourceNotFoundException(ApiErrorCode.MISSING_QUERY_PARAM + " Missing query param clientId");
		}
		List<Compartment> compartmentList = compartmentDaoService.findAllCompartmentOfSafe(clientId, safeId);
		if(compartmentList != null && !compartmentList.isEmpty()) {
			for(Compartment compartment : compartmentList) {
				CompartmentDto compartmentDto = new CompartmentDto(compartment.getId(), compartment.getCompartmentId(), compartment.getName(), compartment.getClient().getId(), 
						compartment.getClient().getName(), safeId, compartment.getSafe().getName(),compartment.getEmpty(), CommonUtils.deriveActionByName(compartment.getCreatedBy(), compartment.getClient().getName(), compartment.getSafe().getLocation().getName()),
						compartment.getCreatedDate(), CommonUtils.deriveActionByName(compartment.getLastUpdatedBy(), compartment.getClient().getName(), compartment.getSafe().getLocation().getName()),
						compartment.getLastUpdatedDate());
				Asset asset = compartmentDaoService.getAssetByCompartment(compartment);
				if(asset != null) {
					compartmentDto.setAssetId(asset.getId());
					compartmentDto.setIdentifier(asset.getIdentifier());
					compartmentDto.setIdentifierType(asset.getIdentifierType());
				}
				compartments.add(compartmentDto);
			}
		}
		Map<String, Object> model = new HashMap<>();
		model.put("compartments", compartments);
		model.put("statudDetail", compartments.isEmpty() ? messageSource.getMessage("no.compartment.found", null, Locale.US) : messageSource.getMessage("total.compartment.found", null, Locale.US).replace("x", compartments.size()+""));
		model.put("success", true);
		return ok(model);
	}
	
	//Get compartment by id
		@GetMapping("/safes/{safeId}/compartments/{compartmentId}")
		public ResponseEntity<CompartmentDto> getCompartmentByIdAndSafe(@RequestParam("clientId") Integer clientId, @PathVariable Integer compartmentId, @PathVariable Integer safeId) throws Exception{
			if(clientId == null) {
				throw new ResourceNotFoundException(ApiErrorCode.MISSING_QUERY_PARAM + " Missing query param clientId");
			}
			Compartment compartment = compartmentDaoService.findCompartmentById(clientId, compartmentId, safeId);
			if(compartment == null) {
				throw new ResourceNotFoundException(compartmentId.toString());
			}
			CompartmentDto compartmentDto = new CompartmentDto(compartment.getId(), compartment.getCompartmentId(), compartment.getName(), compartment.getClient().getId(), 
					compartment.getClient().getName(), safeId, compartment.getSafe().getName(),
					compartment.getEmpty(), CommonUtils.deriveActionByName(compartment.getCreatedBy(), compartment.getClient().getName(), compartment.getSafe().getLocation().getName()),
					compartment.getCreatedDate(), CommonUtils.deriveActionByName(compartment.getLastUpdatedBy(), compartment.getClient().getName(), compartment.getSafe().getLocation().getName()),
					compartment.getLastUpdatedDate());
			compartmentDto.setStatusDetail(messageSource.getMessage("compartment.found", null, Locale.US));
			compartmentDto.setSuccess(true);
			return ok(compartmentDto);
		}
		
		@GetMapping("/safes/{safeId}/compartments/withAsset/{compartmentId}")
		public ResponseEntity<CompartmentDto> getCompartmentAssetDataByIdAndSafe(@RequestParam("clientId") Integer clientId, @PathVariable Integer compartmentId, @PathVariable Integer safeId) throws Exception{
			if(clientId == null) {
				throw new ResourceNotFoundException(ApiErrorCode.MISSING_QUERY_PARAM + " Missing query param clientId");
			}
			Compartment compartment = compartmentDaoService.findCompartmentById(clientId, compartmentId, safeId);
			if(compartment == null) {
				throw new ResourceNotFoundException(compartmentId.toString());
			}
			CompartmentDto compartmentDto = new CompartmentDto(compartment.getId(), compartment.getCompartmentId(), compartment.getName(), compartment.getClient().getId(), 
					compartment.getClient().getName(), safeId, compartment.getSafe().getName(), compartment.getEmpty(), CommonUtils.deriveActionByName(compartment.getCreatedBy(), compartment.getClient().getName(), compartment.getSafe().getLocation().getName()),
					compartment.getCreatedDate(), CommonUtils.deriveActionByName(compartment.getLastUpdatedBy(), compartment.getClient().getName(), compartment.getSafe().getLocation().getName()),
					compartment.getLastUpdatedDate());
			Asset asset = compartmentDaoService.getAssetByCompartment(compartment);
			if(asset != null) {
				compartmentDto.setAssetId(asset.getId());
				compartmentDto.setIdentifier(asset.getIdentifier());
				compartmentDto.setIdentifierType(asset.getIdentifierType());
			}
			compartmentDto.setStatusDetail(messageSource.getMessage("compartment.found", null, Locale.US));
			compartmentDto.setSuccess(true);
			return ok(compartmentDto);
		}
	
	//Get compartment by id
	@GetMapping("/safes/compartments/{compartmentId}")
	public ResponseEntity<CompartmentDto> getCompartmentById(@RequestParam("clientId") Integer clientId, @PathVariable Integer compartmentId) throws Exception {
		if(clientId == null) {
			throw new ResourceNotFoundException(ApiErrorCode.MISSING_QUERY_PARAM + " Missing query param clientId");
		}
		Compartment compartment = compartmentDaoService.findCompartmentById(clientId, compartmentId, null);
		if(compartment == null) {
			throw new ResourceNotFoundException(messageSource.getMessage("compartment.not.found.for.client", null, Locale.US) + ": " + clientId);
		}
		CompartmentDto compartmentDto = new CompartmentDto(compartment.getId(), compartment.getCompartmentId(),
				compartment.getName(), compartment.getClient().getId(), compartment.getClient().getName(),
				compartment.getSafe().getSafeId(), compartment.getSafe().getName(), compartment.getEmpty(),
				CommonUtils.deriveActionByName(compartment.getCreatedBy(), compartment.getClient().getName(), compartment.getSafe().getLocation().getName()), compartment.getCreatedDate(),
				CommonUtils.deriveActionByName(compartment.getLastUpdatedBy(), compartment.getClient().getName(), compartment.getSafe().getLocation().getName()), compartment.getLastUpdatedDate());
		compartmentDto.setStatusDetail(messageSource.getMessage("compartment.found", null, Locale.US));
		compartmentDto.setSuccess(true);
		return ok(compartmentDto);
	}
	
	//Get compartment by id
	@GetMapping("/safes/{safeId}/getEmptyCompartment")
	public ResponseEntity<CompartmentDto> getEmptyCompartment(@RequestParam("clientId") Integer clientId, @PathVariable Integer safeId) throws Exception {
		if(clientId == null) {
			throw new ResourceNotFoundException(ApiErrorCode.MISSING_QUERY_PARAM + " Missing query param clientId");
		}
		Compartment compartment = compartmentDaoService.findEmptyCompartment(clientId, safeId);
		if(compartment == null) {
			throw new SmartAutoSafeException("No empty compartment found for safe: " + safeId);
		}

		String compartmentNumStr = compartment.getCompartmentId().toString();
		String compartmentNum = compartmentNumStr.substring(compartmentNumStr.length()-2, compartmentNumStr.length());
		CompartmentDto compartmentDto = new CompartmentDto(compartment.getId(), compartment.getCompartmentId(),
				compartmentNum, compartment.getName(), compartment.getClient().getId(),
				compartment.getClient().getName(), compartment.getSafe().getSafeId(), compartment.getSafe().getName(),
				compartment.getEmpty(), CommonUtils.deriveActionByName(compartment.getCreatedBy(), compartment.getClient().getName(), compartment.getSafe().getLocation().getName()),
				compartment.getCreatedDate(), CommonUtils.deriveActionByName(compartment.getLastUpdatedBy(), compartment.getClient().getName(), compartment.getSafe().getLocation().getName()),
				compartment.getLastUpdatedDate());
		compartmentDto.setStatusDetail(messageSource.getMessage("empty.compartment.found", null, Locale.US));
		compartmentDto.setSuccess(true);
		return ok(compartmentDto);
	}
	
	//Save Compartment
	@PostMapping("/safes/compartments")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<CompartmentDto> saveCompartment(@RequestParam("clientId") Integer clientId, @RequestBody CompartmentDto compartmentDto, @RequestHeader("User-Agent") String userAgent) throws Exception {
		if(clientId == null) {
			throw new ResourceNotFoundException(ApiErrorCode.MISSING_QUERY_PARAM + " Missing query param clientId");
		}
		if(compartmentDto == null || compartmentDto.getId() != null || compartmentDto.getCompartmentNum() == null || compartmentDto.getCompartmentNum().isEmpty() || compartmentDto.getName() == null || compartmentDto.getName().isEmpty() || compartmentDto.getSafeId() == null) {
			throw new InvalidRequestParameterException("Bad input data");
		}
		Client client = clientDaoService.findClientById(clientId);
		if(client == null) {
			throw new ResourceNotFoundException(messageSource.getMessage("client.not.found", null, Locale.US) + ":" + clientId);
		}
		Safe safe = safeDaoService.findSafeById(compartmentDto.getSafeId());
		if(safe == null) {
			throw new ResourceNotFoundException(messageSource.getMessage("safe.not.found", null, Locale.US) + ":" + compartmentDto.getSafeId());
		}
		if(!safe.getClient().getId().equals(clientId)) {
			throw new InvalidRequestParameterException("Safe does not belong to client " + clientId);
		}
		Integer compartmentId = compartmentDaoService.generateCompartmentId(safe.getSafeId(), compartmentDto.getCompartmentNum());
		if(compartmentDaoService.findCompartmentById(clientId, compartmentId, safe.getSafeId()) != null) {
			throw new InvalidRequestParameterException("Compartment already exist: " + compartmentDto.getName());
		}
		compartmentDto.setLastUpdatedById(CommonUtils.getActionById(userAgent, clientId));
		CompartmentDto savedCompartment = compartmentDaoService.save(clientId, compartmentDto);
		savedCompartment.setStatusDetail(messageSource.getMessage("compartment.created", null, Locale.US));
		savedCompartment.setSuccess(true);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedCompartment);
	}
		
	//Update Compartment
	@PutMapping("/safes/compartments")
	public ResponseEntity<CompartmentDto> updateCompartment(@RequestParam("clientId") Integer clientId, @RequestBody CompartmentDto compartmentDto, @RequestHeader("User-Agent") String userAgent) throws Exception {
		if(clientId == null) {
			throw new ResourceNotFoundException(ApiErrorCode.MISSING_QUERY_PARAM + " Missing query param clientId");
		}
		if(compartmentDto == null || compartmentDto.getId() == null || compartmentDto.getName() == null || compartmentDto.getName().isEmpty() || compartmentDto.getSafeId() == null) {
			throw new InvalidRequestParameterException("Bad input data");
		}
		Safe safe = safeDaoService.find(clientId, compartmentDto.getSafeId());
		if(safe == null) {				
			throw new ResourceNotFoundException(compartmentDto.getSafeId().toString());
		}
		Compartment compartment = compartmentDaoService.findCompartmentById(compartmentDto.getId());
		if(compartment == null) {				
			throw new ResourceNotFoundException(compartmentDto.getCompartmentId().toString());
		}
		if(!safe.getClient().getId().equals(clientId) || !compartment.getClient().getId().equals(clientId) || !compartment.getSafe().getSafeId().equals(safe.getSafeId())) {
			throw new InvalidRequestParameterException("Incorrect combination of client, safe and compartment");
		}
		compartmentDto.setLastUpdatedById(CommonUtils.getActionById(userAgent, clientId));
		CompartmentDto savedCompartment = compartmentDaoService.save(clientId, compartmentDto);
		savedCompartment.setStatusDetail(messageSource.getMessage("compartment.updated", null, Locale.US));
		savedCompartment.setSuccess(true);
		return ok(savedCompartment);
	}
	
	// Delete compartment by id
	@DeleteMapping("/safes/{safeId}/compartments/{compartmentId}")
	public CompartmentDto deleteCompartment(@RequestParam("clientId") Integer clientId, @PathVariable int safeId,
			@PathVariable int compartmentId) throws Exception {
		if(clientId == null) {
			throw new ResourceNotFoundException(ApiErrorCode.MISSING_QUERY_PARAM + " Missing query param clientId");
		}
		Compartment compartment = compartmentDaoService.deleteCompartment(clientId, safeId, compartmentId);
		if (compartment == null) {
			LOGGER.error("Invalid Compartment " + compartmentId);
			throw new ResourceNotFoundException(messageSource.getMessage("compartment.not.found.for.client", null, Locale.US) + ": " + clientId );
		} else {
			CompartmentDto compartmentDto = new CompartmentDto();
			compartmentDto.setCompartmentId(compartment.getCompartmentId());
			compartmentDto.setStatusDetail(messageSource.getMessage("compartment.deleted", null, Locale.US));
			compartmentDto.setSuccess(true);
			return compartmentDto;
		}

	}

}
