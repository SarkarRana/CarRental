package com.sma.smartauto.dao;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import com.sma.smartauto.domain.Asset;
import com.sma.smartauto.domain.Client;
import com.sma.smartauto.domain.Compartment;
import com.sma.smartauto.domain.Safe;
import com.sma.smartauto.exception.InvalidRequestParameterException;
import com.sma.smartauto.exception.ResourceNotFoundException;
import com.sma.smartauto.exception.SmartAutoSafeException;
import com.sma.smartauto.model.CompartmentDto;
import com.sma.smartauto.repository.AssetRepository;
import com.sma.smartauto.repository.ClientRepository;
import com.sma.smartauto.repository.CompartmentRepository;
import com.sma.smartauto.repository.SafeRepository;
import com.sma.smartauto.utils.CommonUtils;

@Component
public class CompartmentDaoService {
	
	private MessageSource messageSource;

	@Autowired
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	@Autowired
	ClientRepository clientRepository;
	
	@Autowired
	CompartmentRepository compartmentRepository;
	
	@Autowired
	SafeRepository safeRepository;
	
	@Autowired
	AssetRepository assetRepository;
	
	public List<Compartment> findAllCompartmentOfSafe(Integer clientId, Integer safeId) {
		Optional<Client> client = clientRepository.findById(clientId);
		if(!client.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("client.not.found", null, Locale.US) + ":" + clientId);
		}
		Safe safe = safeRepository.findSafeByIdAndClient(safeId, clientId);
		if(safe == null) {
			throw new ResourceNotFoundException(messageSource.getMessage("safe.not.found.for.client", null, Locale.US) +": "+ clientId);
		}
		return compartmentRepository.findBySafe(safe);		
	}
	
	public Compartment findCompartmentByName(Integer clientId, String compartmentName, Integer safeId) throws Exception {
		Optional<Client> client = clientRepository.findById(clientId);
		if(!client.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("client.not.found", null, Locale.US) + clientId);
		}
		
		Compartment compartment = compartmentRepository.findCompartmentBySafeAndName(safeId, compartmentName);
	
		if(compartment != null && safeId != null && safeId.equals(compartment.getSafe().getSafeId()) && compartment.getClient().getId().equals(clientId)) {
			return compartment;
		}
		return null;
	}
	
	public Compartment findCompartmentById(Integer clientId, Integer compartmentId, Integer safeId) throws Exception {
		Optional<Client> client = clientRepository.findById(clientId);
		if(!client.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("client.not.found", null, Locale.US) + ": " + clientId);
		}
		Optional<Compartment> compartment = compartmentRepository.findByCompartmentId(compartmentId);
		if(compartment.isPresent()) {
			if(safeId != null && !safeId.equals(compartment.get().getSafe().getSafeId())) {
				throw new Exception(messageSource.getMessage("compartment.not.found.for.safe", null, Locale.US) + ": " + safeId);
			}
			if(!compartment.get().getClient().getId().equals(clientId)) {
				throw new InvalidRequestParameterException(messageSource.getMessage("compartment.not.found.for.client", null, Locale.US) + ": " + clientId);
			}
			return compartment.get();
		}
		return null;
	}
	
	public Compartment findEmptyCompartment(Integer clientId, Integer safeId) throws Exception {
		Optional<Client> client = clientRepository.findById(clientId);
		if(!client.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("client.not.found", null, Locale.US) + ": " + clientId);
		}
		Safe safe = safeRepository.findSafeByIdAndClient(safeId, clientId);
		if(safe == null) {
			throw new ResourceNotFoundException(messageSource.getMessage("safe.not.found.for.client", null, Locale.US) + ": " + clientId);
		}
		
		List<Compartment> compartments = compartmentRepository.findBySafe(safe);
		if(compartments != null && !compartments.isEmpty()) {
			for(Compartment compartment : compartments) {
				if(compartment.getEmpty() ) {
					return compartment;
				}
			}
		}
		return null;
	}
	
	public CompartmentDto save(Integer clientId, CompartmentDto compartmentDto) throws Exception {
		Optional<Client> client = clientRepository.findById(clientId);
		if(!client.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("client.not.found", null, Locale.US) + ": " + clientId);
		}
		if(compartmentDto == null || (compartmentDto.getId() == null && (compartmentDto.getCompartmentNum() == null || compartmentDto.getName() == null)) 
				|| (compartmentDto.getId() != null && compartmentDto.getCompartmentId() == null)
				|| compartmentDto.getSafeId() == null) {
			throw new InvalidRequestParameterException("Bad input data");
		}
		Optional<Safe> safe = safeRepository.findById(compartmentDto.getSafeId());
		if(!safe.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("safe.not.found", null, Locale.US) + ": " + compartmentDto.getSafeId());
		}
		
		Compartment compartment = new Compartment();
		if(compartmentDto.getId() != null || compartmentDto.getCompartmentId() != null) {
			Optional<Compartment> compOpt = null;
			if(compartmentDto.getId() != null) {
				compOpt = compartmentRepository.findById(compartmentDto.getId());
				if(!compOpt.isPresent()) {
					throw new ResourceNotFoundException(messageSource.getMessage("compartment.not.found", null, Locale.US) + ": " + compartmentDto.getId());
				}
				if(compartmentDto.getCompartmentId() != null && !compOpt.get().getCompartmentId().equals(compartmentDto.getCompartmentId()) 
						|| !compOpt.get().getName().equals(compartmentDto.getName())) {
					throw new InvalidRequestParameterException(messageSource.getMessage("compartment.data.mismatch", null, Locale.US) + ": " + clientId);
				}
			}
			compartment = compOpt.get();
			compartment.setLastUpdatedBy(compartmentDto.getLastUpdatedById());
		} else {
			Integer compartmentId = generateCompartmentId(safe.get().getSafeId(), compartmentDto.getCompartmentNum());
			compartment.setCompartmentId(compartmentId);
			compartment.setName(compartmentDto.getName());
			compartment.setClient(client.get());
			compartment.setSafe(safe.get());
			compartment.setCreatedBy(compartmentDto.getLastUpdatedById());
			compartment.setLastUpdatedBy(compartmentDto.getLastUpdatedById());
		}
		compartment.setEmpty(compartmentDto.getEmpty());
		 
		Compartment savedCompartment = compartmentRepository.save(compartment);
		String compartmentNumStr = compartment.getCompartmentId().toString();
		String compartmentNum = compartmentNumStr.substring(compartmentNumStr.length()-2, compartmentNumStr.length());
		CompartmentDto savedCompartmentDto = new CompartmentDto(savedCompartment.getId(),
				savedCompartment.getCompartmentId(), compartmentNum, savedCompartment.getName(),
				compartment.getClient().getId(), savedCompartment.getClient().getName(),
				savedCompartment.getSafe().getSafeId(), savedCompartment.getSafe().getName(),
				savedCompartment.getEmpty(),
				CommonUtils.deriveActionByName(savedCompartment.getCreatedBy(), savedCompartment.getClient().getName(),
						savedCompartment.getSafe().getLocation().getName()),
				savedCompartment.getCreatedDate(),
				CommonUtils.deriveActionByName(savedCompartment.getLastUpdatedBy(),
						savedCompartment.getClient().getName(), savedCompartment.getSafe().getLocation().getName()),
				savedCompartment.getLastUpdatedDate());
		return savedCompartmentDto;
	}

	public Compartment findCompartmentById(int id) {
		Optional<Compartment> compartment = compartmentRepository.findById(id);
		if(compartment.isPresent()) {
			return compartment.get();
		}
		return null;
	}
	
	public Asset getAssetByCompartment(Compartment compartment) {
		if(compartment == null) {
			return null;
		}
		return assetRepository.findByCompartment(compartment);
	}
	
	public Compartment deleteCompartment(Integer clientId, Integer safeId, Integer compartmentId) throws Exception {
		Compartment compartment = findCompartmentById(clientId, compartmentId, safeId);
		if(compartment != null) {
			Asset asset = assetRepository.findByCompartment(compartment);
			if(asset != null) {
				throw new SmartAutoSafeException(messageSource.getMessage("compartment.cannot.be.deleted.asset.associated", null, Locale.US) + ": " + asset.getIdentifier());
			}
			compartmentRepository.delete(compartment);
			return compartment;
		}
		return null;
	}
	
	public Integer generateCompartmentId(Integer safeId, String compartmentNum) {
		if(compartmentNum.length() == 1) {
			compartmentNum = "0" + compartmentNum;
		}
		return Integer.parseInt(safeId + compartmentNum);
	}
	
	public static void main(String[] a) {
		Integer x = 1000789;
		String y = x.toString();
		
				
		System.out.println(y.substring((y.length()-2), y.length()));
	}
}
