package com.sma.smartauto.dao;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import com.sma.smartauto.domain.Asset;
import com.sma.smartauto.domain.AssetType;
import com.sma.smartauto.domain.Client;
import com.sma.smartauto.domain.Code;
import com.sma.smartauto.domain.Compartment;
import com.sma.smartauto.domain.Safe;
import com.sma.smartauto.exception.ApiErrorCode;
import com.sma.smartauto.exception.ResourceNotFoundException;
import com.sma.smartauto.exception.SmartAutoSafeException;
import com.sma.smartauto.model.AssetDto;
import com.sma.smartauto.repository.AssetRepository;
import com.sma.smartauto.repository.AssetTypeRepository;
import com.sma.smartauto.repository.ClientRepository;
import com.sma.smartauto.repository.CodeRepository;
import com.sma.smartauto.repository.CompartmentRepository;
import com.sma.smartauto.repository.SafeRepository;
import com.sma.smartauto.utils.CommonUtils;
import com.sma.smartauto.utils.Constants;

@Component
public class AssetDaoService {
	
	private MessageSource messageSource;

	@Autowired
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	@Autowired
	ClientRepository clientRepository;
	
	@Autowired
	AssetRepository assetRepository;
	
	@Autowired
	AssetTypeRepository assetTypeRepository;
	
	@Autowired
	CompartmentRepository compartmentRepository;
	
	@Autowired
	SafeRepository safeRepository;
	
	@Autowired
	CodeRepository codeRepository;
	
	public List<Asset> findAll() {		
		return assetRepository.findAll();
	}
	
	public Asset findAssetById(Integer clientId, Integer id) {		
		 Optional<Asset> asset = assetRepository.findById(id);
		 if(asset.isPresent()) {
			 if(asset.get().getClient().getId().equals(clientId)) {
				 return asset.get();
			 } else {
				 throw new SmartAutoSafeException(ApiErrorCode.ACCESS_DENIED, messageSource.getMessage("asset.not.found.for.client", null, Locale.US) + ": " + clientId);
			 }
		 }
		 return null;
	}
	
	public List<Asset> findAllAssetByClient(Integer clientId) {
		Optional<Client> client = clientRepository.findById(clientId);
		if(!client.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("client.not.found", null, Locale.US) + ": " + clientId);
		}
		return assetRepository.findAllByClient(client.get());
	}
	
	public Asset findAssetByCompartmentId(Integer compartmentId) {
		Optional<Compartment> compartment = compartmentRepository.findByCompartmentId(compartmentId);
		if(!compartment.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("compartment.not.found", null, Locale.US) + ": " + compartmentId);
		}
		return assetRepository.findByCompartment(compartment.get());
	}
	
	public List<Asset> findAllAssetBySafe(Integer safeId) {
		Optional<Safe> safe = safeRepository.findById(safeId);
		if(!safe.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("safe.not.found", null, Locale.US) + ": " + safeId);
		}
		return assetRepository.findAllBySafe(safe.get());
	}
	
	public List<Asset> findAssetByIdentifierType(String identifierType) {
		return assetRepository.findByIdentifierType(identifierType);
	}
	
	public Asset findAssetByIdentifier(String identifier) {
		Optional<Asset> asset = assetRepository.findByIdentifier(identifier);
		if(asset.isPresent()) {
			return asset.get();
		} 
		return null;
	}
	
	public AssetDto save(Integer clientId, AssetDto assetDto) throws Exception {
		Optional<Client> client = clientRepository.findById(clientId);
		if(!client.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("client.not.found", null, Locale.US) + ": " + clientId);
		}
		if(assetDto == null || assetDto.getIdentifier() == null || assetDto.getIdentifierType() == null || assetDto.getAssetTypeId() == null
				|| (assetDto.getSafeId() != null && assetDto.getCompartmentId() == null) || (assetDto.getSafeId() == null && assetDto.getCompartmentId() != null)) {
			throw new Exception("Bad input data");
		}
		Optional<AssetType> assetType = assetTypeRepository.findById(assetDto.getAssetTypeId());
		if(!assetType.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("asset.type.not.found", null, Locale.US) + ": " + assetDto.getAssetTypeId());
		}
		Optional<Asset> existingAsset = assetRepository.findByIdentifier(assetDto.getIdentifier());
		if(existingAsset.isPresent() && existingAsset.get().getClient().getId().equals(clientId) && assetDto.getAssetId() == null) {
			throw new SmartAutoSafeException(messageSource.getMessage("asset.with.identifier.exists.for.client", null, Locale.US) + ": " + clientId);
			
		}
		Asset asset = null;
		if(assetDto.getAssetId() != null) {
			Optional<Asset> assetOpt = assetRepository.findById(assetDto.getAssetId());
			if(!assetOpt.isPresent()) {
				throw new SmartAutoSafeException(messageSource.getMessage("asset.not.found.for.client", null, Locale.US) + ": " + clientId);
			} else {
				asset = assetOpt.get();
				asset.setLastUpdatedBy(assetDto.getLastUpdatedById());
			}
		} else {
			asset = new Asset();
			asset.setCreatedBy(assetDto.getLastUpdatedById());
			asset.setLastUpdatedBy(assetDto.getLastUpdatedById());
		}
		asset.setName(assetDto.getName());
		asset.setAssetType(assetType.get());
		asset.setClient(client.get());
		asset.setIdentifier(assetDto.getIdentifier());
		asset.setIdentifierType(assetDto.getIdentifierType());
		asset.setActive(assetDto.getActive());


		Optional<Compartment> compartment = null;

		if(assetDto.getAssetId() != null && asset.getCompartment() != null) {
			if(assetDto.getCompartmentId() == null || !asset.getCompartment().getCompartmentId().equals(assetDto.getCompartmentId())) {
				Compartment oldCompartment = asset.getCompartment();
				oldCompartment.setEmpty(true);
				compartmentRepository.save(oldCompartment);
			}
		}
		if(assetDto.getSafeId() != null) {
			Optional<Safe> safe = safeRepository.findById(assetDto.getSafeId());
			if(!safe.isPresent()) {
				throw new ResourceNotFoundException(messageSource.getMessage("safe.not.found", null, Locale.US) + ": " + assetDto.getSafeId());
			} 
			compartment = compartmentRepository.findByCompartmentId(assetDto.getCompartmentId());
			if(!compartment.isPresent()) {
				throw new ResourceNotFoundException(messageSource.getMessage("compartment.not.found", null, Locale.US) + ": " + assetDto.getCompartmentId());
			}
			if(!compartment.get().getEmpty()) {
				throw new ResourceNotFoundException(messageSource.getMessage("compartment.not.empty", null, Locale.US) + ": " + assetDto.getCompartmentId());
			}
			if(!compartment.get().getSafe().getSafeId().equals(safe.get().getSafeId())) {
				throw new SmartAutoSafeException(ApiErrorCode.INVALID_INPUT, messageSource.getMessage("compartment.not.found.for.safe", null, Locale.US) + ": " + assetDto.getSafeId());
			} 
			if(!compartment.get().getClient().getId().equals(clientId) || !safe.get().getClient().getId().equals(clientId)) {
				throw new SmartAutoSafeException(ApiErrorCode.INVALID_INPUT, messageSource.getMessage("compartment.safe.not.found.for.client", null, Locale.US) + ": " + clientId);
			} else {
				asset.setSafe(safe.get());
				asset.setCompartment(compartment.get());
			}
		}
		Asset savedAsset = assetRepository.save(asset);
		if(compartment != null && compartment.isPresent()) {
			compartment.get().setEmpty(false);
			compartmentRepository.save(compartment.get());
		}
		assetDto.setAssetId(savedAsset.getId());
		String createdLocName = asset.getClient().getName();
		if(!asset.getCreatedBy().toString().startsWith(CommonUtils.API_ID_PREFIX.toString())) {
			Safe safe = safeRepository.findSafeByIdAndClient(asset.getCreatedBy(), asset.getClient().getId());
			if(safe != null) {
				createdLocName = safe.getLocation().getName();
			}
		}String updatedLocName = asset.getClient().getName();
		if(!asset.getLastUpdatedBy().toString().startsWith(CommonUtils.API_ID_PREFIX.toString())) {
			Safe safe = safeRepository.findSafeByIdAndClient(asset.getLastUpdatedBy(), asset.getClient().getId());
			if(safe != null) {
				createdLocName = safe.getLocation().getName();
			}
		}
		assetDto.setCreatedBy(CommonUtils.deriveActionByName(asset.getCreatedBy(), asset.getClient().getName(), createdLocName));
		assetDto.setCreatedOn(asset.getCreatedDate());
		assetDto.setLastUpdatedBy(CommonUtils.deriveActionByName(asset.getLastUpdatedBy(), asset.getClient().getName(), updatedLocName));
		assetDto.setLastUpdatedOn(asset.getLastUpdatedDate());
		return assetDto;
	}
	
	public AssetDto updateAssetCompartmentMapping(Integer clientId, Asset asset, Integer compartmentId, String action, Integer actionById) {
		Optional<Client> client = clientRepository.findById(clientId);
		if(!client.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("client.not.found", null, Locale.US) + ": " + clientId);
		}
		Optional<Compartment> compartmentOpt = compartmentRepository.findByCompartmentId(compartmentId);
		if(!compartmentOpt.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("compartment.not.found", null, Locale.US) + ": " + compartmentId);
		}
		if(!asset.getClient().getId().equals(clientId) || !compartmentOpt.get().getClient().getId().equals(clientId)) {
			throw new SmartAutoSafeException(ApiErrorCode.INVALID_INPUT, messageSource.getMessage("asset.compartment.not.found.for.client", null, Locale.US) + ": " + clientId + clientId);
		}
		try {
			Compartment compartment = compartmentOpt.get();
			if(action.equals(Constants.ADD_ASSET_TO_COMP_ACTION)) {
				asset.setCompartment(compartment);
				asset.setSafe(compartment.getSafe());
				
				compartment.setEmpty(false);
			} else {
				asset.setCompartment(null);
				asset.setSafe(null);
				
				compartment.setEmpty(true);
			}
			asset.setLastUpdatedBy(actionById);
			compartment.setLastUpdatedBy(actionById);
			assetRepository.save(asset);	
			compartmentRepository.save(compartment);
			String createAssetLoc = asset.getClient().getName();
			if(!asset.getCreatedBy().toString().startsWith(CommonUtils.API_ID_PREFIX.toString())) {
				Safe safe = safeRepository.findSafeByIdAndClient(asset.getCreatedBy(), asset.getClient().getId());
				if(safe != null) {
					createAssetLoc = safe.getLocation().getName();
				}
			}
			String updateAssetLoc = asset.getClient().getName();
			if(!asset.getLastUpdatedBy().toString().startsWith(CommonUtils.API_ID_PREFIX.toString())) {
				Safe safe = safeRepository.findSafeByIdAndClient(asset.getLastUpdatedBy(), asset.getClient().getId());
				if(safe != null) {
					updateAssetLoc = safe.getLocation().getName();
				}
			}
			return new AssetDto(asset.getId(), asset.getClient().getName(), asset.getClient().getId(),
					(asset.getSafe() != null ? asset.getSafe().getName() : null),
					(asset.getSafe() != null ? asset.getSafe().getSafeId() : null),
					(asset.getCompartment() != null ? asset.getCompartment().getName() : null),
					(asset.getCompartment() != null ? asset.getCompartment().getCompartmentId() : null),
					asset.getName(), asset.getIdentifier(), asset.getIdentifierType(),
					asset.getAssetType().getAssetTypeDesc(), asset.getAssetType().getAssetTypeId(), CommonUtils.deriveActionByName(asset.getCreatedBy(), asset.getClient().getName(), createAssetLoc), asset.getCreatedDate(), CommonUtils.deriveActionByName(asset.getLastUpdatedBy(), asset.getClient().getName(), updateAssetLoc), asset.getLastUpdatedDate());
		} catch (Exception e) {
			throw new SmartAutoSafeException("Error occured while saving asset/comp data: " + e.getMessage());
		}
	}
	
	public Asset deleteAsset(Integer clientId, int id) {
		Optional<Client> client = clientRepository.findById(clientId);
		if(!client.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("client.not.found", null, Locale.US) + ": " + clientId);
		}
		Optional<Asset> asset = assetRepository.findById(id);
		if(asset.isPresent()) {
			if(!asset.get().getClient().getId().equals(clientId)) {
				throw new SmartAutoSafeException(ApiErrorCode.ACCESS_DENIED, messageSource.getMessage("asset.not.found.for.client", null, Locale.US) + ": " + clientId);
			}
			Code code = codeRepository.getActiveCodesByIdentifier(asset.get().getIdentifier());
			if(code != null) {
				throw new SmartAutoSafeException(messageSource.getMessage("asset.cannot.be.deleted.active.code", null, Locale.US) + ": " + clientId+ code.getCode());
			}
			assetRepository.delete(asset.get());
			Compartment compartment = asset.get().getCompartment();
			if(compartment != null) {
				compartment.setEmpty(true);
				compartmentRepository.save(compartment);
			}
			return asset.get();
		}
		return null;
	}	
	
	public List<Asset> findAssetBySafeAndIdentifierType(Integer safeId, String identifierType) {
		return assetRepository.findAssetBySafeAndIdentifierType(safeId, identifierType);
	}
}
