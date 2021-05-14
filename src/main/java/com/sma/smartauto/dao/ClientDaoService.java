package com.sma.smartauto.dao;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import com.sma.smartauto.domain.Client;
import com.sma.smartauto.domain.ClientType;
import com.sma.smartauto.domain.Safe;
import com.sma.smartauto.exception.SmartAutoSafeException;
import com.sma.smartauto.model.ClientDto;
import com.sma.smartauto.repository.ClientRepository;
import com.sma.smartauto.repository.ClientTypeRepository;
import com.sma.smartauto.repository.SafeRepository;
import com.sma.smartauto.utils.CommonUtils;

@Component
public class ClientDaoService {
	
	private MessageSource messageSource;

	@Autowired
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	@Autowired
	ClientRepository clientRepository;
	
	@Autowired
	ClientTypeRepository clientTypeRepository;
	
	@Autowired
	SafeRepository safeRepository;
	
	public List<Client> findAll() {		
		return clientRepository.findAll();
	}
	
	public ClientDto save(ClientDto clientDto) {	
		if(clientDto.getClientTypeId() == null) {
			throw new SmartAutoSafeException(messageSource.getMessage("missing.client.type", null, Locale.US));
		}
		Optional<ClientType> clientType = clientTypeRepository.findById(clientDto.getClientTypeId());
		
		if(!clientType.isPresent()) {
			throw new SmartAutoSafeException(messageSource.getMessage("client.type.not.found", null, Locale.US) + ": " + clientDto.getClientTypeId());
		}
		Client client = null;
		if(clientDto.getClientId() != null) {
			client = findClientById(clientDto.getClientId());
			client.setLastUpdatedBy(clientDto.getLastUpdatedById());
		} else {
			client = new Client();
			client.setCreatedBy(clientDto.getLastUpdatedById());
			client.setLastUpdatedBy(clientDto.getLastUpdatedById());
		}
		
		client.setName(clientDto.getName());
		client.setType(clientType.get());
		client = clientRepository.save(client);
		
		clientDto.setClientId(client.getId());
		clientDto.setClientType(client.getType().getClientTypeDesc());
		clientDto.setCreatedBy(CommonUtils.deriveActionByName(client.getCreatedBy(), client.getName(), null));
		clientDto.setCreatedOn(client.getCreatedDate());
		clientDto.setLastUpdatedBy(CommonUtils.deriveActionByName(client.getLastUpdatedBy(), client.getName(), null));
		clientDto.setLastUpdatedOn(client.getLastUpdatedDate());
		return clientDto;
	}

	public Client findClientById(int id) {
		Optional<Client> client = clientRepository.findById(id);
		if(client.isPresent()) {
			return client.get();
		}
		return null;
	}
	
	public Client deleteClientById(int id) {
		Optional<Client> client = clientRepository.findById(id);
		if(client.isPresent()) {
			List<Safe> safes = safeRepository.findByClient(client.get());
			if(safes != null && !safes.isEmpty()) {
				throw new SmartAutoSafeException(messageSource.getMessage("client.cannot.be.deleted.safe.exist", null, Locale.US) + ": " + id);
			}                                                              
			clientRepository.delete(client.get());
			return client.get();
		}
		return null;
	}
}
