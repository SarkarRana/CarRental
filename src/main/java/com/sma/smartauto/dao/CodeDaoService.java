package com.sma.smartauto.dao;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sma.smartauto.domain.Asset;
import com.sma.smartauto.domain.Client;
import com.sma.smartauto.domain.Code;
import com.sma.smartauto.domain.Compartment;
import com.sma.smartauto.domain.Location;
import com.sma.smartauto.domain.Safe;
import com.sma.smartauto.exception.ApiErrorCode;
import com.sma.smartauto.exception.ResourceNotFoundException;
import com.sma.smartauto.exception.SmartAutoSafeException;
import com.sma.smartauto.model.CodeDto;
import com.sma.smartauto.model.CompartmentDto;
import com.sma.smartauto.repository.AssetRepository;
import com.sma.smartauto.repository.ClientRepository;
import com.sma.smartauto.repository.CodeRepository;
import com.sma.smartauto.repository.LocationRepository;
import com.sma.smartauto.repository.SafeRepository;
import com.sma.smartauto.utils.CommonUtils;
import com.sma.smartauto.utils.Constants;

@Component
public class CodeDaoService {
	
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
	SafeRepository safeRepository;
	
	@Autowired
	LocationRepository locationRepository;
	
	@Autowired
	CodeRepository codeRepository;
	
	@Autowired
	CompartmentDaoService compartmentDaoService;
	
	
	public List<Code> findAll() {		
		return codeRepository.findAll();
	}
	
	public Code findCodeByCode(String code) {
		Optional<Code> newCode = codeRepository.findByCode(code);
		if(newCode.isPresent()) {
			return newCode.get();
		}
		return null;
	}
	
	public List<Code> findCodeByClient(Integer clientId) {
		Optional<Client> client = clientRepository.findById(clientId);
		if(!client.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("client.not.found", null, Locale.US) + ": " + clientId);
		}
		return codeRepository.findCodeByClient(client.get());
	}
	
	public List<Code> findCodeBySafe(Integer safeId) {
		Optional<Safe> safe = safeRepository.findById(safeId);
		if(!safe.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("safe.not.found", null, Locale.US) + ": " + safe);
		}
		return codeRepository.findCodeBySafe(safe.get());
	}
	
	public List<Code> findCodeByIdentifierType(String identifierType) {
		return codeRepository.findByIdentifierType(identifierType);
	}
	
	public Code findCodeByIdentifier(String identifier) {
		Optional<Code> code = codeRepository.findByIdentifier(identifier);
		if(code.isPresent()) {
			return code.get();
		} 
		return null;
	}
	
	public CodeDto save(Integer clientId, CodeDto codeDto) throws Exception {
		Optional<Client> client = clientRepository.findById(clientId);
		Integer safeId = codeDto.getSafeId();
		if(!client.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("client.not.found", null, Locale.US) + ": " + clientId);
		}
		if(codeDto == null || codeDto.getSafeId() == null || codeDto.getCode() == null || codeDto.getValidFrom() == null || codeDto.getValidTill() == null || (codeDto.getIdentifier() == null && codeDto.getIdentifierType() == null)) {
			throw new SmartAutoSafeException(ApiErrorCode.CODE_ERROR,"Bad input data");
		}
		
		Code code = null;
		Optional<Code> existingCode = codeRepository.findByCode(codeDto.getCode());
		if(existingCode.isPresent() && codeDto.getId() == null) {
			throw new SmartAutoSafeException(ApiErrorCode.CODE_ERROR, messageSource.getMessage("code.already.exist", null, Locale.US) + ": " + existingCode.get().getCode());
		}
		if(codeDto.getId() == null) {
			code = new Code();
			if (existingCode.isPresent() && existingCode.get().getSafe().getSafeId().equals(codeDto.getSafeId()) && existingCode.get().getClient().getId().equals(clientId)) {
				throw new SmartAutoSafeException(ApiErrorCode.CODE_ERROR, messageSource.getMessage("code.already.exist.for.client.safe", null, Locale.US));
			}
			code.setCreatedBy(codeDto.getLastUpdatedById());
			code.setLastUpdatedBy(codeDto.getLastUpdatedById());
		} else {
			if(!codeRepository.findById(codeDto.getId()).isPresent() || !existingCode.isPresent()) {
				throw new SmartAutoSafeException(ApiErrorCode.CODE_ERROR, messageSource.getMessage("code.not.found", null, Locale.US) + ": " + codeDto.getCode());
			}
			code = existingCode.get();
			if (!existingCode.get().getDataComplete()) {
				throw new SmartAutoSafeException(ApiErrorCode.CODE_ERROR, messageSource.getMessage("unassigned.data.code.cannot.be.updated", null, Locale.US) + ": " + codeDto.getCode());
			}
			if (!existingCode.get().getId().equals(codeDto.getId())) {
				throw new SmartAutoSafeException(ApiErrorCode.CODE_ERROR, messageSource.getMessage("code.already.exist", null, Locale.US) + ": " + code.getCode());
			}
			code.setLastUpdatedBy(codeDto.getLastUpdatedById());
		}
		if(codeDto.getIdentifier() != null) {
			Code existingCodeForAsset = codeRepository.getActiveAndUnassignedCodesByIdentifier(codeDto.getIdentifier());
			if(existingCodeForAsset != null && !codeDto.getCode().equals(existingCodeForAsset.getCode()) && CommonUtils.isCodeActive(existingCodeForAsset) && (codeDto.getId() == null || !codeDto.getId().equals(existingCodeForAsset.getId()))) {
				throw new SmartAutoSafeException(ApiErrorCode.CODE_ERROR, messageSource.getMessage("asset.already.associated.with.another.code", null, Locale.US) + ": " + codeDto.getCode());
			}
		}
		code.setCode(codeDto.getCode());
		code.setClient(client.get());
		code.setIdentifierType(codeDto.getIdentifierType());
		code.setValidFrom(CommonUtils.formatDate(codeDto.getValidFrom(), Constants.DATETIME__FORMAT_yyyyMMdd));
		code.setValidTill(CommonUtils.formatDate(codeDto.getValidTill(), Constants.DATETIME__FORMAT_yyyyMMdd));
		code.setActive(codeDto.getActive());
		if(codeDto.getIdentifier() != null) {
			Optional<Asset> asset = assetRepository.findByIdentifier(codeDto.getIdentifier());
			if(!asset.isPresent()) {
				throw new ResourceNotFoundException(messageSource.getMessage("asset.not.found", null, Locale.US) + ": " + codeDto.getIdentifier());
			} else {
				code.setIdentifier(asset.get().getIdentifier());
				code.setAsset(asset.get());
				if(safeId == null) {
					safeId = asset.get().getSafe().getSafeId();
				}
			}
		}		
		code.setIdentifierType(code.getIdentifierType());
		Optional<Safe> safe = safeRepository.findById(safeId);
		if(!safe.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("safe.not.found", null, Locale.US) + ": " + codeDto.getSafeId());
		}
		if(client.get().getId().intValue() != safe.get().getClient().getId().intValue()) {
			throw new InputMismatchException(messageSource.getMessage("safe.not.found.for.client", null, Locale.US) + ": " + codeDto.getClientId());
		}
		code.setSafe(safe.get());
		code.setLocation(safe.get().getLocation());
		code.setDataComplete(true);
		Code savedCode = codeRepository.save(code);
		codeDto.setId(savedCode.getId());
		codeDto.setCreatedBy(CommonUtils.deriveActionByName(code.getCreatedBy(), code.getClient().getName(), code.getSafe().getLocation().getName()));
		codeDto.setCreatedOn(code.getCreatedDate());
		codeDto.setLastUpdatedBy(CommonUtils.deriveActionByName(code.getLastUpdatedBy(), code.getClient().getName(), code.getSafe().getLocation().getName()));
		codeDto.setLastUpdatedOn(code.getLastUpdatedDate());
		codeDto.setLastUpdatedById(null);
		return codeDto;
	}

	public CodeDto saveCodeByLocation(Integer clientId, CodeDto codeDto) throws Exception {
		Optional<Client> client = clientRepository.findById(clientId);
		Integer safeId = codeDto.getSafeId();
		if(!client.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("client.not.found", null, Locale.US) + ": " + clientId);
		}
		if(codeDto == null || codeDto.getCode() == null || (codeDto.getIdentifier() == null && codeDto.getIdentifierType() == null)) {
			throw new Exception("Bad input data");
		}
		
		Code code = null;
		Optional<Code> existingCode = codeRepository.findByCode(codeDto.getCode());
		if(existingCode.isPresent() && codeDto.getId() == null) {
			throw new SmartAutoSafeException(ApiErrorCode.CODE_ERROR, messageSource.getMessage("code.already.exist", null, Locale.US) + ": " + existingCode.get().getId());
		}
		if(!existingCode.isPresent() && codeDto.getId() != null) {
			throw new SmartAutoSafeException(ApiErrorCode.CODE_ERROR, messageSource.getMessage("code.not.found", null, Locale.US) + ": " + codeDto.getCode());
		}
		if(codeDto.getId() == null) {
			code = new Code();
			code.setCreatedBy(codeDto.getLastUpdatedById());
			code.setLastUpdatedBy(codeDto.getLastUpdatedById());
		} else {
			if(!codeRepository.findById(codeDto.getId()).isPresent()) {
				throw new SmartAutoSafeException(ApiErrorCode.CODE_ERROR, messageSource.getMessage("code.not.found", null, Locale.US) + ": " + codeDto.getCode());
			}
			code = existingCode.get();
			if (!existingCode.get().getId().equals(codeDto.getId())) {
				throw new SmartAutoSafeException(ApiErrorCode.CODE_ERROR, messageSource.getMessage("code.id.mismatch", null, Locale.US) + ": " + existingCode.get().getId());
			}
			/*if (existingCode.get().getDataComplete()) {
				throw new SmartAutoSafeException(ApiErrorCode.CODE_ERROR, messageSource.getMessage("assigned.data.code.cannot.be.updated", null, Locale.US) + ": " + codeDto.getCode());
			}*/
			code.setLastUpdatedBy(codeDto.getLastUpdatedById());
		}
		if(codeDto.getIdentifier() != null) {
			Code existingCodeForAsset = codeRepository.getActiveAndUnassignedCodesByIdentifier(codeDto.getIdentifier());
			if(existingCodeForAsset != null && !codeDto.getCode().equals(existingCodeForAsset.getCode()) && CommonUtils.isCodeActive(existingCodeForAsset) && (codeDto.getId() == null || !codeDto.getId().equals(existingCodeForAsset.getId()))) {
				throw new SmartAutoSafeException(ApiErrorCode.CODE_ERROR, messageSource.getMessage("asset.already.associated.with.another.code", null, Locale.US) + ": " + existingCodeForAsset.getCode());
			}
		}
		if(codeDto.getLocationId() != null) {
			Location loc = locationRepository.findByLocId(codeDto.getLocationId());
			if(loc == null) {
				throw new SmartAutoSafeException(ApiErrorCode.RESOURCE_NOT_FOUND, "Location does not exist:" + codeDto.getLocationId());
			}
			code.setLocation(loc);
		}
		code.setCode(codeDto.getCode());
		code.setClient(client.get());
		code.setIdentifierType(codeDto.getIdentifierType());
		code.setValidFrom(CommonUtils.formatDate(codeDto.getValidFrom(), Constants.DATETIME__FORMAT_yyyyMMdd));
		code.setValidTill(CommonUtils.formatDate(codeDto.getValidTill(), Constants.DATETIME__FORMAT_yyyyMMdd));
		code.setActive(codeDto.getActive());
		if(codeDto.getIdentifier() != null) {
			Optional<Asset> asset = assetRepository.findByIdentifier(codeDto.getIdentifier());
			
			if(!asset.isPresent()) {
				throw new ResourceNotFoundException(messageSource.getMessage("asset.not.found", null, Locale.US) + ": " + codeDto.getIdentifier());
			} else {
				code.setIdentifier(asset.get().getIdentifier());
				code.setIdentifierType(asset.get().getIdentifierType());
				code.setAsset(asset.get());
				if(safeId == null && asset.get().getSafe() != null) {
					safeId = asset.get().getSafe().getSafeId();
				}
			}
		}		
		code.setIdentifierType(code.getIdentifierType());
		if(safeId != null) {
			Optional<Safe> safe = safeRepository.findById(safeId);
			if(!safe.isPresent()) {
				throw new ResourceNotFoundException(messageSource.getMessage("safe.not.found", null, Locale.US) + ": " + codeDto.getSafeId());
			}
			if(client.get().getId().intValue() != safe.get().getClient().getId().intValue()) {
				throw new InputMismatchException(messageSource.getMessage("safe.not.found.for.client", null, Locale.US) + ": " + codeDto.getClientId());
			}
			if(code.getLocation() != null && safe.get().getLocation().getLocId() != code.getLocation().getLocId()) {
				throw new InputMismatchException("Location specified in request is not same as location of safe" + ": " + safe.get().getLocation().getLocId());
			}
			code.setSafe(safe.get());
			code.setLocation(safe.get().getLocation());
		}		
		code.setDataComplete(CommonUtils.isCodeAssigned(code));
		Code savedCode = codeRepository.save(code);
		codeDto.setId(savedCode.getId());
		codeDto.setSafeId(safeId);
		codeDto.setCreatedBy(CommonUtils.deriveActionByName(code.getCreatedBy(), code.getClient().getName(), (code.getSafe()!=null && code.getSafe().getLocation()!=null)?code.getSafe().getLocation().getName():null));
		codeDto.setCreatedOn(code.getCreatedDate());
		codeDto.setLastUpdatedBy(CommonUtils.deriveActionByName(code.getLastUpdatedBy(), code.getClient().getName(), (code.getSafe()!=null && code.getSafe().getLocation()!=null)?code.getSafe().getLocation().getName():null));
		codeDto.setLastUpdatedOn(code.getLastUpdatedDate());
		codeDto.setLastUpdatedById(null);
		codeDto.setActive(savedCode.getActive());
		return codeDto;
	}
	
	public Code deleteCodeById(int id, Integer clientId) throws Exception {
		Optional<Client> client = clientRepository.findById(clientId);		
		if(!client.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("client.not.found", null, Locale.US) + ": " + clientId);
		}
		Optional<Code> code = codeRepository.findById(id);
		if(code.isPresent()) {
			if(code.get().getClient().getId().equals(clientId)) {
			codeRepository.delete(code.get());
			return code.get();
			} else {
				throw new ResourceNotFoundException(messageSource.getMessage("code.not.found.for.client", null, Locale.US));
			}
		}
		return null;
	}
	
	public Code deleteCode(String code, Integer clientId) throws Exception {
		Optional<Client> client = clientRepository.findById(clientId);		
		if(!client.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("client.not.found", null, Locale.US) + ": " + clientId);
		}
		Optional<Code> codeObj = codeRepository.findByCode(code);
		if(codeObj.isPresent()) {
			if(codeObj.get().getClient().getId().equals(clientId)) {
			codeRepository.delete(codeObj.get());
			return codeObj.get();
			} else {
				throw new ResourceNotFoundException(messageSource.getMessage("code.not.found.for.client", null, Locale.US));
			}
		}
		return null;
	}
	
	 @Transactional
	public void updateCompartmentStatusAndCodeUsed(Integer clientId, String code, Integer safeId, Integer compartmentId, String identifier, boolean empty, Integer updatedBy) throws Exception {
		Optional<Client> client = clientRepository.findById(clientId);		
		if(!client.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("client.not.found", null, Locale.US) + ": " + clientId);
		}		
		Code codeObj = findCodeByCode(code);
		if(code == null || !codeObj.getClient().getId().equals(clientId)){
			throw new ResourceNotFoundException(messageSource.getMessage("code.not.found", null, Locale.US) + ": " + code);
		}
		Compartment compartment = compartmentDaoService.findCompartmentById(clientId, compartmentId, safeId);
		if(compartment == null) {
			throw new ResourceNotFoundException(messageSource.getMessage("compartment.not.found", null, Locale.US) + ": " + compartmentId);
		}
		if((codeObj.getSafe() != null && !codeObj.getSafe().getSafeId().equals(safeId)) || !compartment.getSafe().getSafeId().equals(safeId)) {
			throw new ResourceNotFoundException(messageSource.getMessage("code.compartment.safe.mismatch", null, Locale.US));
		}
		if(codeObj.getAsset() != null && !codeObj.getAsset().getIdentifier().equals(identifier)) {
			throw new ResourceNotFoundException(messageSource.getMessage("code.asset.mismatch", null, Locale.US));
		}
		codeObj.setLastUpdatedBy(updatedBy);
		codeObj.setActive(false);
		codeRepository.save(codeObj);		
		
		compartment.setEmpty(empty);
		CompartmentDto compartmentDto = new CompartmentDto(compartment.getId(), compartment.getCompartmentId(),
				compartment.getName(), clientId, client.get().getName(), safeId, null, empty,
				CommonUtils.deriveActionByName(compartment.getCreatedBy(), client.get().getName(),
						compartment.getSafe().getLocation().getName()),
				compartment.getCreatedDate(), CommonUtils.deriveActionByName(compartment.getLastUpdatedBy(),
						client.get().getName(), compartment.getSafe().getLocation().getName()),
				compartment.getLastUpdatedDate());
		compartmentDto.setCompartmentNum(compartment.getName());
		compartmentDto.setLastUpdatedById(updatedBy);
		compartmentDaoService.save(clientId, compartmentDto);
		
		assetRepository.updateCompartmentData(clientId, (empty ? null : safeId), (empty ? null : compartmentId), identifier, updatedBy);		
	}
	
	public List<Code> getDBSyncUp(Integer clientId, Integer locId, Integer maxSyncedId, Integer locationId) {
		Optional<Client> client = clientRepository.findById(clientId);
		if(!client.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("client.not.found", null, Locale.US) + ": " + clientId);
		}
		Location loc = locationRepository.findByLocId(locId);
		if(loc == null || loc.getClient().getId() != clientId) {
			throw new ResourceNotFoundException(messageSource.getMessage("location.not.found.for.client", null, Locale.US) + ": " + clientId);
		}
		List<Code> codes = codeRepository.getAllActiveCodesForLocation(locId, maxSyncedId, clientId);
		return codes;
	}
	
}
