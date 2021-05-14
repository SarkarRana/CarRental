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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sma.smartauto.dao.AssetDaoService;
import com.sma.smartauto.dao.ClientDaoService;
import com.sma.smartauto.dao.ClientNotificationDaoService;
import com.sma.smartauto.dao.SafeDaoService;
import com.sma.smartauto.dao.UserDaoService;
import com.sma.smartauto.domain.Asset;
import com.sma.smartauto.domain.Client;
import com.sma.smartauto.domain.Safe;
import com.sma.smartauto.domain.User;
import com.sma.smartauto.exception.ApiErrorCode;
import com.sma.smartauto.exception.InvalidRequestParameterException;
import com.sma.smartauto.exception.ResourceNotFoundException;
import com.sma.smartauto.exception.SmartAutoSafeException;
import com.sma.smartauto.model.AssetDto;
import com.sma.smartauto.model.CodeDto;
import com.sma.smartauto.utils.AssetIdentiferType;
import com.sma.smartauto.utils.CommonUtils;
import com.sma.smartauto.utils.Constants;

@RequestMapping("/asset")
@RestController
public class AssetController {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	private MessageSource messageSource;

	@Autowired
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	@Autowired
	private ClientDaoService clientDaoService;
	
	@Autowired
	private AssetDaoService assetDaoService;
	
	@Autowired
	private SafeDaoService safeDaoService;
	
	@Autowired
	private UserDaoService userDaoService;
	
	@Autowired
	private ClientNotificationDaoService clientNotificationDaoService;
	
	//Get all assets
	@GetMapping("/all")
	public ResponseEntity<Map<String, Object>> allAssetsOfClient(@RequestParam("clientId") Integer clientId) throws Exception {
		List<AssetDto> assets = new ArrayList<>();
		if(clientId == null) {
			throw new ResourceNotFoundException(ApiErrorCode.MISSING_QUERY_PARAM + " Missing query param clientId");
		}
		Client client = clientDaoService.findClientById(clientId);
		if(client == null) {
			throw new ResourceNotFoundException(messageSource.getMessage("client.not.found", null, Locale.US) + ":" + clientId);
		}
		List<Asset> assetList = assetDaoService.findAllAssetByClient(clientId);
		if(assetList != null && !assetList.isEmpty()) {
			for(Asset asset : assetList) {
				String createLocName = asset.getClient().getName();
				if(!asset.getLastUpdatedBy().toString().startsWith(CommonUtils.API_ID_PREFIX.toString())) {
					Safe safe = safeDaoService.findSafeById(asset.getLastUpdatedBy());
					if(safe != null) {
						createLocName = safe.getLocation().getName();
					}
				}
				String updateLocName = asset.getClient().getName();
				if(!asset.getLastUpdatedBy().toString().startsWith(CommonUtils.API_ID_PREFIX.toString())) {
					Safe safe = safeDaoService.findSafeById(asset.getLastUpdatedBy());
					if(safe != null) {
						createLocName = safe.getLocation().getName();
					}
				}
				AssetDto assetDto = new AssetDto(asset.getId(), asset.getClient().getName(), asset.getClient().getId(),
						asset.getSafe()!=null ? asset.getSafe().getName():null, asset.getSafe()!=null ? asset.getSafe().getSafeId():null, 
						asset.getCompartment()!=null ? asset.getCompartment().getName():null, asset.getCompartment()!=null ? asset.getCompartment().getCompartmentId():null, 
						asset.getName(), asset.getIdentifier(), asset.getIdentifierType(),
						asset.getAssetType().getAssetTypeDesc(), asset.getAssetType().getAssetTypeId(),
						CommonUtils.deriveActionByName(asset.getCreatedBy(), asset.getClient().getName(), createLocName), asset.getCreatedDate(),
						CommonUtils.deriveActionByName(asset.getLastUpdatedBy(), asset.getClient().getName(), updateLocName), asset.getLastUpdatedDate());
				assets.add(assetDto);
			}
		}
		Map<String, Object> model = new HashMap<>();
		model.put("assets", assets);
		model.put("statusDetail", assets.isEmpty() ? messageSource.getMessage("no.asset.found", null, Locale.US) : messageSource.getMessage("total.asset.found", null, Locale.US).replace("x", assets.size()+""));
		model.put("success", true);
		return ok(model);
	}
	
	//Get asset by id
	@GetMapping("/{assetId}")
	public ResponseEntity<AssetDto> getAssetById(@RequestParam("clientId") Integer clientId, @PathVariable int assetId) throws Exception {
		if(clientId == null) {
			throw new ResourceNotFoundException(ApiErrorCode.MISSING_QUERY_PARAM + " Missing query param clientId");
		}

		Client client = clientDaoService.findClientById(clientId);
		if(client == null) {
			throw new ResourceNotFoundException(messageSource.getMessage("client.not.found", null, Locale.US) + ":" + clientId);
		}
		Asset asset = assetDaoService.findAssetById(clientId, assetId);
		if(asset == null) {
			throw new ResourceNotFoundException(messageSource.getMessage("asset.not.found", null, Locale.US) + ":" + assetId);
		}
		if(!asset.getClient().getId().equals(clientId)) {
			throw new InvalidRequestParameterException("invalid client Id");
		}
		String safeLocName = asset.getClient().getName();
		if(!asset.getLastUpdatedBy().toString().startsWith(CommonUtils.API_ID_PREFIX.toString())) {
			Safe safe = safeDaoService.findSafeById(asset.getLastUpdatedBy());
			if(safe != null) {
				safeLocName = safe.getLocation().getName();
			}
		}
		AssetDto assetDto = new AssetDto(asset.getId(), asset.getClient().getName(), asset.getClient().getId(),
				asset.getSafe()!=null ? asset.getSafe().getName():null, asset.getSafe()!=null ? asset.getSafe().getSafeId():null, 
				asset.getCompartment()!=null ? asset.getCompartment().getName():null, asset.getCompartment()!=null ? asset.getCompartment().getCompartmentId():null, 
				asset.getName(), asset.getIdentifier(), asset.getIdentifierType(), asset.getAssetType().getAssetTypeDesc(), asset.getAssetType().getAssetTypeId(),
				CommonUtils.deriveActionByName(asset.getCreatedBy(), asset.getClient().getName(), safeLocName), asset.getCreatedDate(),
				CommonUtils.deriveActionByName(asset.getLastUpdatedBy(), asset.getClient().getName(), safeLocName), asset.getLastUpdatedDate());
		if(asset.getSafe() != null) {
			assetDto.setSafeId(asset.getSafe().getSafeId());
			assetDto.setSafeName(asset.getSafe().getName());
		}
		if(asset.getCompartment() != null) {
			assetDto.setCompartmentId(asset.getCompartment().getCompartmentId());
			assetDto.setCompartmentName(asset.getCompartment().getName());
		}
		assetDto.setStatusDetail(messageSource.getMessage("asset.found", null, Locale.US));
		assetDto.setSuccess(true);
		return ok(assetDto);
	}
	
	//Save asset
	@PostMapping("/")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<AssetDto> saveAsset(@RequestParam("clientId") Integer clientId, @RequestBody AssetDto asset, @RequestHeader("User-Agent") String userAgent) throws Exception {
		if(clientId == null) {
			throw new ResourceNotFoundException(ApiErrorCode.MISSING_QUERY_PARAM + " Missing query param clientId");
		}

		Client client = clientDaoService.findClientById(clientId);
		if(client == null) {
			throw new ResourceNotFoundException(messageSource.getMessage("client.not.found", null, Locale.US) + ":" + clientId);
		}
		if(asset == null || asset.getAssetTypeId() == null || asset.getName() == null || asset.getName().trim().isEmpty() 
				|| asset.getIdentifier() == null || asset.getIdentifier().trim().isEmpty() || asset.getIdentifierType() == null || asset.getIdentifierType().trim().isEmpty()
				|| AssetIdentiferType.valueOf(asset.getIdentifierType()) == null
				|| (asset.getSafeId() != null && asset.getCompartmentId() == null) || (asset.getSafeId() == null && asset.getCompartmentId() != null)) {
			throw new InvalidRequestParameterException("Bad input data");
		}
		if((asset.getSafeId() != null && asset.getCompartmentId() == null) || (asset.getSafeId() == null && asset.getCompartmentId() != null)) {
			throw new InvalidRequestParameterException("Either provide safe id and compartment id both or none");
		}

		Asset assetWithSameIdentifier = assetDaoService.findAssetByIdentifier(asset.getIdentifier());
		if(assetWithSameIdentifier != null && assetWithSameIdentifier.getClient().getId().equals(clientId)) {
			throw new SmartAutoSafeException(ApiErrorCode.ASSET_IDENTIFIER_ALREADY_EXISTS, messageSource.getMessage("asset.with.identifier.exists.for.client", null, Locale.US));
		}
		asset.setLastUpdatedById(CommonUtils.getActionById(userAgent, clientId));
		AssetDto savedAsset = assetDaoService.save(clientId, asset);
		savedAsset.setStatusDetail(messageSource.getMessage("asset.created", null, Locale.US));
		savedAsset.setSuccess(true);		
		return ResponseEntity.status(HttpStatus.CREATED).body(savedAsset);
	}
	
	//Update asset
	@PutMapping("/")
	public ResponseEntity<AssetDto> updateAsset(@RequestParam("clientId") Integer clientId, @RequestBody AssetDto assetDto, @RequestHeader("User-Agent") String userAgent) throws Exception {
		if(clientId == null) {
			throw new ResourceNotFoundException(ApiErrorCode.MISSING_QUERY_PARAM + " Missing query param clientId");
		}
		if(assetDto == null || assetDto.getAssetTypeId() == null || assetDto.getAssetId() == null || assetDto.getName() == null  || assetDto.getName().trim().isEmpty()
				|| assetDto.getIdentifier() == null || assetDto.getIdentifier().trim().isEmpty() || assetDto.getIdentifierType() == null || assetDto.getIdentifierType().trim().isEmpty()
				|| AssetIdentiferType.valueOf(assetDto.getIdentifierType()) == null
				|| (assetDto.getSafeId() != null && assetDto.getCompartmentId() == null) || (assetDto.getSafeId() == null && assetDto.getCompartmentId() != null)) {
			throw new InvalidRequestParameterException("Bad input data");
		}

		Client client = clientDaoService.findClientById(clientId);
		if(client == null) {
			throw new ResourceNotFoundException(messageSource.getMessage("client.not.found", null, Locale.US) + ":" + clientId);
		}
		if((assetDto.getSafeId() != null && assetDto.getCompartmentId() == null) || (assetDto.getSafeId() == null && assetDto.getCompartmentId() != null)) {
			throw new InvalidRequestParameterException("Either provide safe id and compartment id both or none");
		}
		
		Asset asset = assetDaoService.findAssetById(clientId, assetDto.getAssetId());
		if(asset == null) {
			throw new ResourceNotFoundException("Asset does not exist");
		}
		Asset assetWithSameIdentifier = assetDaoService.findAssetByIdentifier(asset.getIdentifier());
		if(assetWithSameIdentifier != null && assetWithSameIdentifier.getClient().getId().equals(clientId) && !assetWithSameIdentifier.getId().equals(assetDto.getAssetId())) {
			throw new SmartAutoSafeException(ApiErrorCode.ASSET_IDENTIFIER_ALREADY_EXISTS, messageSource.getMessage("asset.with.identifier.exists.for.client", null, Locale.US));
		}
		assetDto.setLastUpdatedById(CommonUtils.getActionById(userAgent, clientId));
		AssetDto savedAsset = assetDaoService.save(clientId, assetDto);
		savedAsset.setStatusDetail(messageSource.getMessage("asset.updated", null, Locale.US));
		savedAsset.setSuccess(true);
		return ok(savedAsset);
	}
	
	//Delete asset by id
	@DeleteMapping("/{id}")
	public AssetDto deleteAsset(@RequestParam("clientId") Integer clientId, @PathVariable int id) throws Exception {
		if(clientId == null) {
			throw new ResourceNotFoundException(ApiErrorCode.MISSING_QUERY_PARAM + " Missing query param clientId");
		}

		Client client = clientDaoService.findClientById(clientId);
		if(client == null) {
			throw new ResourceNotFoundException(messageSource.getMessage("client.not.found", null, Locale.US) + ":" + clientId);
		}
		Asset asset = assetDaoService.deleteAsset(clientId, id);
		if(asset == null) {
			LOGGER.error("Invalid Code " +id);
			throw new ResourceNotFoundException("id:" + id);
		}
		AssetDto assetDto = new AssetDto(asset.getId(), asset.getClient().getName(), asset.getClient().getId(), null, null, null, null, asset.getName(), asset.getIdentifier(), asset.getIdentifierType(), asset.getAssetType().getAssetTypeDesc(), asset.getAssetType().getAssetTypeId());
		assetDto.setStatusDetail(messageSource.getMessage("asset.deleted", null, Locale.US));
		return assetDto;
	}
	
	@PutMapping("/addAssetToCompartment")
	public ResponseEntity<AssetDto> addAssetToCompartment(@RequestParam("clientId") Integer clientId, @RequestBody AssetDto assetDto, @RequestHeader("User-Agent") String userAgent) throws Exception {
		if(clientId == null) {
			throw new ResourceNotFoundException(ApiErrorCode.MISSING_QUERY_PARAM + " Missing query param clientId");
		}

		Client client = clientDaoService.findClientById(clientId);
		if(client == null) {
			throw new ResourceNotFoundException(messageSource.getMessage("client.not.found", null, Locale.US) + ":" + clientId);
		}
		if(assetDto == null || (assetDto.getAssetId() == null && (assetDto.getIdentifier() == null || assetDto.getIdentifier().isEmpty())) || assetDto.getCompartmentId() == null) {
			throw new InvalidRequestParameterException("Bad input data");
		}
		User user = null;
		if((assetDto.getStaffCardCode() != null || assetDto.getUserId() != null)) {
			if(assetDto.getSafeId() == null) {
				throw new InvalidRequestParameterException("safe id missing");				
			} else if(!assetDto.getSafeId().equals(CommonUtils.getSafeIdFromCompartmentId(assetDto.getCompartmentId()))) {
				throw new InvalidRequestParameterException("Incorrect safe id");
			}
			user = assetDto.getUserId() != null ? userDaoService.findUserById(clientId, assetDto.getUserId())
					: userDaoService.findClientUserByStaffCardCode(clientId, assetDto.getStaffCardCode());
			if(assetDto.getUserId() != null && !assetDto.getUserId().equals(user.getUserId())) {
				throw new InvalidRequestParameterException("Incorrect user id or staff card code");
			}
			if(assetDto.getStaffCardCode() != null && !assetDto.getStaffCardCode().equals(user.getStaffCardCode())) {
				throw new InvalidRequestParameterException("Incorrect user id or staff card code");
			}
		}
		Asset asset = null;
		if(assetDto.getAssetId() != null) {
			asset = assetDaoService.findAssetById(clientId, assetDto.getAssetId());
		} else {
			asset = assetDaoService.findAssetByIdentifier(assetDto.getIdentifier());
		}
		if(asset == null) {
			throw new ResourceNotFoundException("Asset does not exist");
		}
		if(asset.getCompartment() != null) {
			if(asset.getCompartment().getCompartmentId().equals(assetDto.getCompartmentId())) {
				throw new InvalidRequestParameterException(messageSource.getMessage("asset.already.associated.with.compartment", null, Locale.US));
			} else if(!asset.getCompartment().getCompartmentId().equals(assetDto.getCompartmentId())) {
				throw new InvalidRequestParameterException(messageSource.getMessage("asset.already.associated.with.another.compartment", null, Locale.US));
			}
		}
		Asset assetComp = assetDaoService.findAssetByCompartmentId(assetDto.getCompartmentId());
		if(assetComp != null) {
			throw new InvalidRequestParameterException(messageSource.getMessage("compartment.already.assigned", null, Locale.US));
		}
		assetDto.setLastUpdatedById(CommonUtils.getActionById(userAgent, clientId));
		AssetDto savedAssetDto = assetDaoService.updateAssetCompartmentMapping(clientId, asset, assetDto.getCompartmentId(), Constants.ADD_ASSET_TO_COMP_ACTION, CommonUtils.getActionById(userAgent, clientId));

		if (user != null) {			
			try {
				CodeDto codeDto = new CodeDto(user.getUserId(), user.getStaffCardCode(), clientId, null,
						assetDto.getAssetId(), null, assetDto.getSafeId(), null, assetDto.getCompartmentId(), null,
						assetDto.getIdentifier(), null, null, false, null, null, true);
				codeDto.setActionType(Constants.ADD_ASSET_TO_COMP_ACTION_BY_STAFF);
				clientNotificationDaoService.sendLockNotificationToClient(clientId, codeDto);
			} catch (Exception ex) {
				LOGGER.error("Exception while sending key movement notification to client: " + clientId + "::" + ex.getMessage());
			}

		}
		savedAssetDto.setStatusDetail(messageSource.getMessage("asset.add.compartment", null, Locale.US));
		savedAssetDto.setSuccess(true);
		return ok(savedAssetDto);
	}
	
	@PutMapping("/removeAssetFromCompartment")
	public ResponseEntity<AssetDto> removeAssetFromCompartment(@RequestParam("clientId") Integer clientId, @RequestBody AssetDto assetDto, @RequestHeader("User-Agent") String userAgent) throws Exception {
		if(clientId == null) {
			throw new ResourceNotFoundException(ApiErrorCode.MISSING_QUERY_PARAM + " Missing query param clientId");
		}

		Client client = clientDaoService.findClientById(clientId);
		if(client == null) {
			throw new ResourceNotFoundException(messageSource.getMessage("client.not.found", null, Locale.US) + ":" + clientId);
		}
		if(assetDto == null || (assetDto.getAssetId() == null && (assetDto.getIdentifier() == null || assetDto.getIdentifier().isEmpty())) || assetDto.getCompartmentId() == null) {
			throw new InvalidRequestParameterException("Bad input data");
		}
		User user = null;
		if((assetDto.getStaffCardCode() != null || assetDto.getUserId() != null)) {
			if(assetDto.getSafeId() == null) {
				throw new InvalidRequestParameterException("safe id missing");				
			} else if(!assetDto.getSafeId().equals(CommonUtils.getSafeIdFromCompartmentId(assetDto.getCompartmentId()))) {
				throw new InvalidRequestParameterException("incorrect safe id");
			}
			user = assetDto.getUserId() != null ? userDaoService.findUserById(clientId, assetDto.getUserId())
					: userDaoService.findClientUserByStaffCardCode(clientId, assetDto.getStaffCardCode());
			if(assetDto.getUserId() != null && !assetDto.getUserId().equals(user.getUserId())) {
				throw new InvalidRequestParameterException("incorrect user id ");
			}
			if(assetDto.getStaffCardCode() != null && !assetDto.getStaffCardCode().equals(user.getStaffCardCode())) {
				throw new InvalidRequestParameterException("incorrect staff card code ");
			}
		}
		Asset asset = null;
		if(assetDto.getAssetId() != null) {
			asset = assetDaoService.findAssetById(clientId, assetDto.getAssetId());
		} else {
			asset = assetDaoService.findAssetByIdentifier(assetDto.getIdentifier());
		}
		if(asset == null) {
			throw new ResourceNotFoundException(messageSource.getMessage("asset.not.found.for.client", null, Locale.US));
		}
		if(asset.getCompartment() == null || !asset.getCompartment().getCompartmentId().equals(assetDto.getCompartmentId())) {
			throw new InvalidRequestParameterException(messageSource.getMessage("asset.cannot.be.removed.compartment", null, Locale.US));
		}
		assetDto.setLastUpdatedById(CommonUtils.getActionById(userAgent, clientId));
		AssetDto savedAssetDto = assetDaoService.updateAssetCompartmentMapping(clientId, asset, assetDto.getCompartmentId(), Constants.REMOVE_ASSET_FROM_COMP_ACTION, CommonUtils.getActionById(userAgent, clientId));
		
		if(user != null) {
			try {
			CodeDto codeDto = new CodeDto(user.getUserId(), user.getStaffCardCode(), clientId,
					null, assetDto.getAssetId(), null, assetDto.getSafeId(), null, assetDto.getCompartmentId(), null,
					assetDto.getIdentifier(), null, null, false, null, null, true);
			codeDto.setActionType(Constants.REMOVE_ASSET_FROM_COMP_ACTION_BY_STAFF);
			clientNotificationDaoService.sendLockNotificationToClient(clientId, codeDto);
			} catch (Exception ex) {
				LOGGER.error("Exception while sending key movement notification to client: " + clientId + "::" + ex.getMessage());
			}
			
		}
		savedAssetDto.setStatusDetail(messageSource.getMessage("asset.remove.compartment", null, Locale.US));
		savedAssetDto.setSuccess(true);
		return ok(savedAssetDto);
	}
	
}
