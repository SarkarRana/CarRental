package com.sma.smartauto.controller;

import static org.springframework.http.ResponseEntity.ok;

import java.util.ArrayList;
import java.util.Date;
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

import com.sma.smartauto.dao.AssetDaoService;
import com.sma.smartauto.dao.ClientNotificationDaoService;
import com.sma.smartauto.dao.CodeDaoService;
import com.sma.smartauto.domain.Asset;
import com.sma.smartauto.domain.Code;
import com.sma.smartauto.exception.ApiErrorCode;
import com.sma.smartauto.exception.InvalidRequestParameterException;
import com.sma.smartauto.exception.ResourceNotFoundException;
import com.sma.smartauto.exception.SmartAutoSafeException;
import com.sma.smartauto.model.CodeDto;
import com.sma.smartauto.utils.CommonUtils;
import com.sma.smartauto.utils.Constants;

@RequestMapping("/codes")
@RestController
public class CodeController {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	private MessageSource messageSource;

	@Autowired
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	@Autowired
	private CodeDaoService codeDaoService;
	
	@Autowired
	private AssetDaoService assetDaoService;

	@Autowired
	private ClientNotificationDaoService clientNotificationDaoService;
	
	//Get all users
	@GetMapping("/allCodes")
	public ResponseEntity<Map<String, Object>> getAllCodes(@RequestParam("clientId") Integer clientId) throws Exception {
		List<CodeDto> codes = new ArrayList<>();
		if(clientId == null) {
			throw new ResourceNotFoundException(ApiErrorCode.MISSING_QUERY_PARAM + " Missing query param clientId");
		}
		List<Code> codeList = codeDaoService.findCodeByClient(clientId);
		if(codeList != null && !codeList.isEmpty()) {
			for(Code code : codeList) {
				String dateFrom = code.getValidFrom() != null ? CommonUtils.formatDate(code.getValidFrom(), Constants.DATETIME__FORMAT_yyyyMMdd) : null;
				String dateTill = code.getValidTill() != null ? CommonUtils.formatDate(code.getValidTill(), Constants.DATETIME__FORMAT_yyyyMMdd) : null;
				CodeDto codeDto = new CodeDto(code.getId(), code.getCode(), clientId, code.getClient().getName(), (code.getAsset() != null ? code.getAsset().getId() : null), 
						(code.getAsset() != null ? code.getAsset().getName() : null), (code.getSafe() != null ? code.getSafe().getSafeId() : null), 
						(code.getSafe() != null ? code.getSafe().getName() : null), (code.getAsset() != null && code.getAsset().getCompartment() != null ? code.getAsset().getCompartment().getCompartmentId() : null), 
						(code.getAsset() != null && code.getAsset().getCompartment() != null ? code.getAsset().getCompartment().getName() : null), code.getIdentifier(), code.getIdentifierType(), null, dateFrom, dateTill, code.getActive(),
						CommonUtils.deriveActionByName(code.getCreatedBy(), code.getClient().getName(), code.getLocation()!=null?code.getLocation().getName():null), code.getCreatedDate(), CommonUtils.deriveActionByName(code.getLastUpdatedBy(), code.getClient().getName(), code.getLocation()!=null?code.getLocation().getName():null), code.getLastUpdatedDate());
				codes.add(codeDto);
			}
		}
		Map<String, Object> model = new HashMap<>();
		model.put("codes", codes);
		model.put("statusDetail", codes.isEmpty() ? messageSource.getMessage("no.code.found", null, Locale.US) : messageSource.getMessage("total.code.found", null, Locale.US).replace("x", codes.size()+""));
		model.put("success", true);
		return ok(model);
	}
	
	//Get code by id
	@GetMapping("/{codeId}")
	public ResponseEntity<CodeDto> getCodeByCode(@RequestParam("clientId") Integer clientId, @PathVariable String codeId) throws Exception {
		if(clientId == null) {
			throw new ResourceNotFoundException(ApiErrorCode.MISSING_QUERY_PARAM + " Missing query param clientId");
		}
		
		Code code = codeDaoService.findCodeByCode(codeId);
		if(code == null) {
			throw new ResourceNotFoundException("Code does not exist :" + codeId);
		}
		if(!code.getClient().getId().equals(clientId)) {
			throw new InvalidRequestParameterException("Code does not belong to client: " + clientId);
		}
		String dateFrom = code.getValidFrom() != null ? CommonUtils.formatDate(code.getValidFrom(), Constants.DATETIME__FORMAT_yyyyMMdd) : null;
		String dateTill = code.getValidTill() != null ? CommonUtils.formatDate(code.getValidTill(), Constants.DATETIME__FORMAT_yyyyMMdd) : null;
		CodeDto codeDto = new CodeDto(code.getId(), code.getCode(), clientId, code.getClient().getName(), (code.getAsset() != null ? code.getAsset().getId() : null), 
				(code.getAsset() != null ? code.getAsset().getName() : null), (code.getSafe() != null ? code.getSafe().getSafeId() : null), 
				(code.getSafe() != null ? code.getSafe().getName() : null), (code.getAsset() != null && code.getAsset().getCompartment() != null ? code.getAsset().getCompartment().getCompartmentId() : null), 
				(code.getAsset() != null && code.getAsset().getCompartment() != null ? code.getAsset().getCompartment().getName() : null), code.getIdentifier(), code.getIdentifierType(), null, dateFrom, dateTill, code.getActive(), 
				CommonUtils.deriveActionByName(code.getCreatedBy(), code.getClient().getName(), code.getLocation()!=null?code.getLocation().getName():null), code.getCreatedDate(), CommonUtils.deriveActionByName(code.getLastUpdatedBy(), code.getClient().getName(), code.getLocation()!=null?code.getLocation().getName():null), code.getLastUpdatedDate());
		codeDto.setStatusDetail(messageSource.getMessage("code.found", null, Locale.US));
		codeDto.setSuccess(true);
		return ok(codeDto);
	}
	
	//Save Code
	@PostMapping("/")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<CodeDto> saveCode(@RequestParam("clientId") Integer clientId, @Valid @RequestBody CodeDto code, @RequestHeader("User-Agent") String userAgent) throws Exception {
		if(clientId == null) {
			throw new ResourceNotFoundException(ApiErrorCode.MISSING_QUERY_PARAM + " Missing query param clientId");
		}
		if(code == null || code.getSafeId() == null || code.getCode() == null || code.getCode().isEmpty() || code.getValidFrom() == null || code.getValidFrom().isEmpty() || code.getValidTill() == null || code.getValidTill().isEmpty() 
				|| ((code.getIdentifier() == null || code.getIdentifier().isEmpty()) && (code.getIdentifierType() == null || code.getIdentifierType().isEmpty()))) {
			throw new InvalidRequestParameterException("Incorrect input data");
		}
		if(CommonUtils.formatDate(code.getValidFrom(), Constants.DATETIME__FORMAT_yyyyMMdd).after(CommonUtils.formatDate(code.getValidTill(), Constants.DATETIME__FORMAT_yyyyMMdd))) {
			throw new InvalidRequestParameterException("Valid from date is after valid till date");
		}
		
		code.setLastUpdatedById(CommonUtils.getActionById(userAgent, clientId));
		CodeDto savedCode = codeDaoService.save(clientId, code);
		savedCode.setStatusDetail(messageSource.getMessage("code.created", null, Locale.US));
		savedCode.setSuccess(true);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedCode);
	}
	
	//Save Code
	@PostMapping("/saveFutureCode")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<CodeDto> createFutureCodes
	(@RequestParam("clientId") Integer clientId, @Valid @RequestBody CodeDto code, @RequestHeader("User-Agent") String userAgent) throws Exception {
		if(clientId == null) {
			throw new ResourceNotFoundException(ApiErrorCode.MISSING_QUERY_PARAM + " Missing query param clientId");
		}
		if (code == null || code.getCode() == null || code.getLocationId() == null || code.getValidFrom() == null 
				|| code.getValidFrom().isEmpty()  || code.getValidTill() == null || code.getValidTill().isEmpty() ||  ((code.getIdentifier() == null || code.getIdentifier().isEmpty())
				&& (code.getIdentifierType() == null || code.getIdentifierType().isEmpty()))) {
			throw new InvalidRequestParameterException("Incorrect input data");
		}
		if(CommonUtils.formatDate(code.getValidFrom(), Constants.DATETIME__FORMAT_yyyyMMdd).after(CommonUtils.formatDate(code.getValidTill(), Constants.DATETIME__FORMAT_yyyyMMdd))) {
			throw new InvalidRequestParameterException("Valid from date is after valid till date");
		}
		code.setLastUpdatedById(CommonUtils.getActionById(userAgent, clientId));
		CodeDto savedCode = codeDaoService.saveCodeByLocation(clientId, code);
		savedCode.setStatusDetail(messageSource.getMessage("code.created", null, Locale.US));
		savedCode.setSuccess(true);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedCode);
	}
	
	//Update Code
	@PutMapping("/updateFutureCode")
	public ResponseEntity<CodeDto> updateFutureCode(@RequestParam("clientId") Integer clientId, @Valid @RequestBody CodeDto code, @RequestHeader("User-Agent") String userAgent) throws Exception {
		if(clientId == null) {
			throw new ResourceNotFoundException(ApiErrorCode.MISSING_QUERY_PARAM + " Missing query param clientId");
		}
		if(code == null || code.getId() == null || code.getCode() == null || code.getLocationId() == null || ((code.getIdentifier() == null || code.getIdentifier().isEmpty()) && (code.getIdentifierType() == null || code.getIdentifierType().isEmpty()))) {
			throw new InvalidRequestParameterException("Incorrect input data");
		}
		code.setLastUpdatedById(CommonUtils.getActionById(userAgent, clientId));
		CodeDto savedCode = codeDaoService.saveCodeByLocation(clientId, code);
		savedCode.setStatusDetail(messageSource.getMessage("code.updated", null, Locale.US));
		savedCode.setSuccess(true);
		return ok(savedCode);
	}
	
	//Update Code
	@PutMapping("/")
	public ResponseEntity<CodeDto> updateCode(@RequestParam("clientId") Integer clientId, @Valid @RequestBody CodeDto code, @RequestHeader("User-Agent") String userAgent) throws Exception {
		if(clientId == null) {
			throw new ResourceNotFoundException(ApiErrorCode.MISSING_QUERY_PARAM + " Missing query param clientId");
		}
		if(code == null || code.getSafeId() == null || code.getId() == null || code.getCode() == null || code.getSafeId() == null || ((code.getIdentifier() == null || code.getIdentifier().isEmpty()) && (code.getIdentifierType() == null || code.getIdentifierType().isEmpty()))) {
			throw new InvalidRequestParameterException("Incorrect input data");
		}
		code.setLastUpdatedById(CommonUtils.getActionById(userAgent, clientId));
		CodeDto savedCode = codeDaoService.save(clientId, code);
		savedCode.setStatusDetail(messageSource.getMessage("code.updated", null, Locale.US));
		savedCode.setSuccess(true);
		return ok(savedCode);
	}
	
	//Delete Code by id
	@DeleteMapping("/{id}")
	public CodeDto deleteCodeById(@RequestParam("clientId") Integer clientId, @PathVariable int id) throws Exception {
		if(clientId == null) {
			throw new ResourceNotFoundException(ApiErrorCode.MISSING_QUERY_PARAM + " Missing query param clientId");
		}
		Code code = codeDaoService.deleteCodeById(id, clientId);
		if(code == null) {
			LOGGER.error("Invalid Code " +id);
			throw new ResourceNotFoundException(messageSource.getMessage("code.not.found", null, Locale.US) + ":" + id);
		}
		CodeDto codeDto = new CodeDto(code.getId(), code.getCode(), code.getClient().getId(), code.getClient().getName(), code.getAsset()!=null?code.getAsset().getId():null, code.getAsset()!=null?code.getAsset().getName():null, code.getSafe()!=null?code.getSafe().getSafeId():null, code.getSafe()!=null?code.getSafe().getName():null, code.getIdentifier(), code.getIdentifierType(), null);
		codeDto.setSuccess(true);
		codeDto.setStatusDetail(messageSource.getMessage("code.deleted", null, Locale.US));
		return codeDto;
	}
	
	//Delete Code by code
	@DeleteMapping("/code/{code}")
	public CodeDto deleteCode(@RequestParam("clientId") Integer clientId, @PathVariable String code) throws Exception {
		if(clientId == null) {
			throw new ResourceNotFoundException(ApiErrorCode.MISSING_QUERY_PARAM + " Missing query param clientId");
		}
		Code codeObj = codeDaoService.deleteCode(code, clientId);
		if(codeObj == null) {
			LOGGER.error("Invalid Code " +code);
			throw new ResourceNotFoundException(messageSource.getMessage("code.not.found", null, Locale.US) + ":" + code);
		}
		CodeDto codeDto = new CodeDto(codeObj.getId(), codeObj.getCode(), codeObj.getClient().getId(), codeObj.getClient().getName(), codeObj.getAsset()!=null?codeObj.getAsset().getId():null, codeObj.getAsset()!=null?codeObj.getAsset().getName():null, codeObj.getSafe()!=null?codeObj.getSafe().getSafeId():null, codeObj.getSafe()!=null?codeObj.getSafe().getName():null, codeObj.getIdentifier(), codeObj.getIdentifierType(), null);
		codeDto.setSuccess(true);
		codeDto.setStatusDetail(messageSource.getMessage("code.deleted", null, Locale.US));
		return codeDto;
	}
	
	@PostMapping("/getCompartmentData")
	public ResponseEntity<CodeDto> getCompartment(@RequestParam("clientId") Integer clientId, @RequestBody CodeDto codeDto) throws Exception {
		if(clientId == null) {
			throw new ResourceNotFoundException(ApiErrorCode.MISSING_QUERY_PARAM + " Missing query param clientId");
		}
		if(codeDto == null || codeDto.getCode() == null || codeDto.getLocationId() == null || codeDto.getSafeId() == null) {
			throw new InvalidRequestParameterException("Incorrect input data");
		}
		Code code = codeDaoService.findCodeByCode(codeDto.getCode());
		if(code == null) {
			throw new ResourceNotFoundException(messageSource.getMessage("code.not.found", null, Locale.US) + ":" + codeDto.getCode());
		}
		if(code.getLocation() != null && !code.getLocation().getLocId().equals(codeDto.getLocationId())) {
			throw new InvalidRequestParameterException("Incorrect location");
		}
		if(!validateCode(code)) {
		}
		
		if((code.getIdentifier() == null || code.getIdentifier().isEmpty()) && (code.getIdentifierType() == null || code.getIdentifierType().isEmpty()) || !code.getDataComplete()) {
			throw new InvalidRequestParameterException("Code is not assigned yet");
		}
		codeDto = setCompartmentData(code, codeDto);
		codeDto.setStatusDetail(messageSource.getMessage("code.related.info.found", null, Locale.US));
		codeDto.setSuccess(true);
		return ok(codeDto);
	}
	 
	@PutMapping("/updateStatusCodeCompartment")
	public ResponseEntity<CodeDto> updateCompartmentStausAndMarkCodeUsed(@RequestParam("clientId") Integer clientId, @RequestBody CodeDto codeDto, @RequestHeader("User-Agent") String userAgent) throws Exception {
		if(clientId == null) {
			throw new ResourceNotFoundException(ApiErrorCode.MISSING_QUERY_PARAM + " Missing query param clientId");
		}
		if (codeDto == null || codeDto.getCode() == null || codeDto.getId() == null || codeDto.getSafeId() == null
				|| codeDto.getCompartmentId() == null || codeDto.getCompartmentEmpty() == null
				|| (codeDto.getIdentifier() == null || codeDto.getIdentifier().isEmpty())
				|| codeDto.getActionType() == null 	|| codeDto.getActionType().isEmpty()) {
			throw new InvalidRequestParameterException("Incorrect input data");
		}
		codeDto.setLastUpdatedById(CommonUtils.getActionById(userAgent, clientId));
		codeDaoService.updateCompartmentStatusAndCodeUsed(clientId, codeDto.getCode(), codeDto.getSafeId(), codeDto.getCompartmentId(), codeDto.getIdentifier(), codeDto.getCompartmentEmpty(), CommonUtils.getActionById(userAgent, clientId));
		try {
			Asset asset = assetDaoService.findAssetByIdentifier(codeDto.getIdentifier());
			if(asset == null) {
				throw new InvalidRequestParameterException("Incorrect identifier");
			}
			codeDto.setAssetId(asset.getId());
			clientNotificationDaoService.sendLockNotificationToClient(clientId, codeDto);
		} catch (Exception ex) {
			LOGGER.error("Exception while sending client notification for code " + codeDto.getCode() + "::"	+ ex.getMessage());
		}
		codeDto.setStatusDetail(messageSource.getMessage("code.asset.compartment.status.updated", null, Locale.US));
		codeDto.setSuccess(true);
		codeDto.setLastUpdatedById(null);
		return ok(codeDto);
	}

	@PostMapping("/sync")
	public ResponseEntity<Map<String, Object>> getSafeData(@RequestParam("clientId") Integer clientId, @RequestBody CodeDto codeDto) throws Exception {

		List<CodeDto> codes = new ArrayList<>();
		if(clientId == null) {
			throw new ResourceNotFoundException(ApiErrorCode.MISSING_QUERY_PARAM + " Missing query param clientId");
		}
		if(codeDto == null || codeDto.getLocationId() == null) {
			throw new InvalidRequestParameterException("Incorrect input data");
		}
		Map<String, Object> model = new HashMap<>();
		List<Code> codeList = codeDaoService.getDBSyncUp(clientId, codeDto.getLocationId(), (codeDto.getId() != null ? codeDto.getId() : 0), null);
		if (codeList != null && !codeList.isEmpty()) {
			for (Code code : codeList) {
				try {
					if (validateCode(code)) {
						if (code.getIdentifier() != null || code.getIdentifierType() != null) {
							String dateFrom = code.getValidFrom() != null
									? CommonUtils.formatDate(code.getValidFrom(), Constants.DATETIME__FORMAT_yyyyMMdd)
									: null;
							String dateTill = code.getValidTill() != null
									? CommonUtils.formatDate(code.getValidTill(), Constants.DATETIME__FORMAT_yyyyMMdd)
									: null;
							CodeDto codeDto2 = new CodeDto(code.getId(), code.getCode(), clientId, code.getClient().getName(),
									(code.getAsset() != null ? code.getAsset().getId() : null),
									(code.getAsset() != null ? code.getAsset().getName() : null),
									(code.getSafe() != null ? code.getSafe().getSafeId() : null),
									(code.getSafe() != null ? code.getSafe().getName() : null),
									(code.getAsset() != null && code.getAsset().getCompartment() != null
											? code.getAsset().getCompartment().getCompartmentId() : null),
									(code.getAsset() != null && code.getAsset().getCompartment() != null
											? code.getAsset().getCompartment().getName() : null),
									code.getIdentifier(), code.getIdentifierType(), null, dateFrom, dateTill, code.getActive(), 
									CommonUtils.deriveActionByName(code.getCreatedBy(), code.getClient().getName(), code.getLocation().getName()), code.getCreatedDate(), CommonUtils.deriveActionByName(code.getLastUpdatedBy(), code.getClient().getName(), code.getLocation().getName()), code.getLastUpdatedDate());
							codeDto.setLocationId(code.getLocation()!=null?code.getLocation().getLocId():null);
							codeDto.setLocationName(code.getLocation()!=null?code.getLocation().getName():null);
//							codeDto2 = setCompartmentData(code, codeDto2);
							codes.add(codeDto2);
						}
					}
				} catch (Exception e) {
					System.out.println("Invalid code: " + code.toString());
				}
				
			}
		}
		model.put("codes", codes);
		model.put("statusDetail", codes.isEmpty() ? messageSource.getMessage("no.new.codes.found", null, Locale.US) : codes.size() + " " + messageSource.getMessage("new.codes.found", null, Locale.US));
		model.put("success", true);
		return ok(model);
	}
	
	private boolean validateCode(Code code) {
		Date currentDate = new Date();
		if(!code.getDataComplete()) {
			throw new SmartAutoSafeException(ApiErrorCode.CODE_EXPIRED, messageSource.getMessage("code.data.incomplete", null, Locale.US));
		}
		if(code.getValidFrom().after(currentDate) || code.getValidTill().before(currentDate)) {
			throw new SmartAutoSafeException(ApiErrorCode.CODE_EXPIRED, messageSource.getMessage("code.expired", null, Locale.US));
		}
		if(!code.getActive()) {	
			throw new SmartAutoSafeException(ApiErrorCode.CODE_ALREADY_USED, messageSource.getMessage("code.already.used", null, Locale.US));
		}
		return true;
	}
	
	private CodeDto setCompartmentData(Code code, CodeDto codeDto) throws Exception {
		Asset asset = null;
		if(code.getIdentifier() != null) {
			asset = code.getAsset();
			if(asset == null) {
				asset = assetDaoService.findAssetByIdentifier(code.getIdentifier());
			}
			if(!code.getLocation().getLocId().equals(codeDto.getLocationId())) {
				throw new SmartAutoSafeException(ApiErrorCode.INVALID_CODE, messageSource.getMessage("code.not.found.for.loc", null, Locale.US));
			}
			if(asset.getSafe() != null && !asset.getSafe().getSafeId().equals(codeDto.getSafeId())) {
				throw new SmartAutoSafeException(ApiErrorCode.NO_ASSET_FOR_IDENTIFIER_TYPE, "Please try on safe: " + asset.getSafe().getName());
			}
		} else if(code.getIdentifierType() != null){ 
			List<Asset> assetList = assetDaoService.findAssetBySafeAndIdentifierType(codeDto.getSafeId(), code.getIdentifierType());
			if(assetList == null || assetList.isEmpty()) {
				throw new SmartAutoSafeException(ApiErrorCode.NO_ASSET_FOR_IDENTIFIER_TYPE, "No asset found in safe for identifier type " + code.getIdentifierType() + ".Please try on another safe");
			}
			if(assetList.get(0).getCompartment() == null) {
				throw new SmartAutoSafeException(ApiErrorCode.CODE_ERROR, "Asset compartment could not be found for code " + codeDto.getCode() + " for identifier type " + codeDto.getIdentifierType() + " in safe " + codeDto.getSafeId());
			}
			asset = assetList.get(0);
		}
		
		codeDto.setId(code.getId());
		codeDto.setValidFrom(CommonUtils.formatDate(code.getValidFrom(), Constants.DATETIME__FORMAT_yyyyMMdd));	
		codeDto.setValidTill(CommonUtils.formatDate(code.getValidTill(), Constants.DATETIME__FORMAT_yyyyMMdd));	
		codeDto.setActive(code.getActive());
		codeDto.setCreatedBy(CommonUtils.deriveActionByName(code.getCreatedBy(), code.getClient().getName(), code.getLocation().getName()));
		codeDto.setCreatedOn(code.getCreatedDate());
		codeDto.setLastUpdatedBy(CommonUtils.deriveActionByName(code.getLastUpdatedBy(), code.getClient().getName(), code.getLocation().getName()));;
		codeDto.setLastUpdatedOn(code.getLastUpdatedDate());
		codeDto.setAssetId(asset.getId());
		codeDto.setIdentifier(asset.getIdentifier());
		codeDto.setIdentifierType(asset.getIdentifierType());
		if(asset.getCompartment() != null) {
			codeDto.setCompartmentId(asset.getCompartment().getCompartmentId());
			codeDto.setCompartmentName(asset.getCompartment().getName());
		}
		return codeDto;
	}
}
