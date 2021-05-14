package com.sma.smartauto.dao;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import com.sma.smartauto.domain.Client;
import com.sma.smartauto.domain.Compartment;
import com.sma.smartauto.domain.Location;
import com.sma.smartauto.domain.Safe;
import com.sma.smartauto.exception.InvalidRequestParameterException;
import com.sma.smartauto.exception.ResourceNotFoundException;
import com.sma.smartauto.exception.SmartAutoSafeException;
import com.sma.smartauto.model.SafeDto;
import com.sma.smartauto.repository.ClientRepository;
import com.sma.smartauto.repository.CompartmentRepository;
import com.sma.smartauto.repository.LocationRepository;
import com.sma.smartauto.repository.SafeRepository;
import com.sma.smartauto.utils.CommonUtils;

@Component
public class SafeDaoService {
	
	private MessageSource messageSource;

	@Autowired
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	@Autowired
	ClientRepository clientRepository;
	
	@Autowired
	LocationRepository locationRepository;
	
	@Autowired
	SafeRepository safeRepository;
	
	@Autowired
	CompartmentRepository compartmentRepository;
	
	public List<Safe> findAll() {		
		return safeRepository.findAll();
	}
	
	public List<Safe> findAllSafeForClient(Integer clientId) {		
		Optional<Client> client = clientRepository.findById(clientId);
		if(!client.isPresent()) {

			throw new ResourceNotFoundException(messageSource.getMessage("client.not.found", null, Locale.US) + ": " + clientId);
		}
		return safeRepository.findByClient(client.get());
	}
	
	public Safe findByName(Integer clientId, String name) {		
		Optional<Client> client = clientRepository.findById(clientId);
		if(!client.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("client.not.found", null, Locale.US) + ": " + clientId);
		}
		return safeRepository.findSafeByNameAndClient(name, clientId);
	}
	
	public Safe find(Integer clientId, Integer safeId) {		
		Optional<Client> client = clientRepository.findById(clientId);
		if(!client.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("client.not.found", null, Locale.US) + ": " + clientId);
		}
		return safeRepository.findSafeByIdAndClient(safeId, clientId);
	}
	
	public SafeDto save(Integer clientId, SafeDto safeDto) throws Exception {
		Optional<Client> client = clientRepository.findById(clientId);
		if(!client.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("client.not.found", null, Locale.US) + ": " + clientId);
		}
		if(safeDto == null || safeDto.getName() == null || safeDto.getLocationId() == null) {
			throw new Exception("Bad input data");
		}
		Optional<Location> location = locationRepository.findById(safeDto.getLocationId());
		if(!location.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("location.not.found", null, Locale.US) + ": " + safeDto.getLocationId());
		}
		Safe safe = null;
		if(safeDto.getSafeId() == null) {
			safe = new Safe();
			safe.setCreatedBy(safeDto.getLastUpdatedById());
			safe.setLastUpdatedBy(safeDto.getLastUpdatedById());
		} else {
			safe = findSafeById(safeDto.getSafeId());
			safe.setLastUpdatedBy(safeDto.getLastUpdatedById());
		}
		safe.setName(safeDto.getName());
		safe.setClient(client.get());
		safe.setLocation(location.get());
		 
		safeRepository.save(safe);
		
		SafeDto savedSafe = new SafeDto(safe.getSafeId(), safe.getName(), clientId, safe.getClient().getName(),
				safe.getLocation().getLocId(), safe.getLocation().getName(),
				CommonUtils.deriveActionByName(safe.getCreatedBy(), client.get().getName(), safe.getLocation().getName()), safe.getCreatedDate(),
				CommonUtils.deriveActionByName(safe.getLastUpdatedBy(), client.get().getName(), safe.getLocation().getName()), safe.getLastUpdatedDate());
		return savedSafe;
	}

	public Safe findSafeById(int id) {
		Optional<Safe> safe = safeRepository.findById(id);
		if(safe.isPresent()) {
			return safe.get();
		}
		return null;
	}
	
	public Safe deleteSafe(int clientId, int id) {
		Optional<Client> client = clientRepository.findById(clientId);
		if(!client.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("client.not.found", null, Locale.US) + ": " + clientId);
		}
		Optional<Safe> safe = safeRepository.findById(id);
		if(safe.isPresent()) {
			if(safe.get().getClient().getId().intValue() !=  clientId) {
				throw new InvalidRequestParameterException(messageSource.getMessage("safe.not.found.for.client", null, Locale.US) + ": " + clientId);
			}
			List<Compartment> compartments = compartmentRepository.findBySafe(safe.get());
			if(compartments != null && !compartments.isEmpty()) {
				throw new SmartAutoSafeException(messageSource.getMessage("safe.cannot.be.deleted.compartment.exist", null, Locale.US));
			}
			safeRepository.delete(safe.get());
			return safe.get();
		} else {
			throw new ResourceNotFoundException(messageSource.getMessage("safe.not.found", null, Locale.US) + ": " + id);
		}
	}
}
