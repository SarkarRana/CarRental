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
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sma.smartauto.dao.ClientDaoService;
import com.sma.smartauto.domain.Client;
import com.sma.smartauto.exception.ResourceNotFoundException;
import com.sma.smartauto.model.ClientDto;
import com.sma.smartauto.utils.CommonUtils;

@RestController
public class ClientController {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	private MessageSource messageSource;

	@Autowired
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	@Autowired
	private ClientDaoService clientDaoService;
	
	//Get all users
	@GetMapping("/clients")
	public ResponseEntity<Map<String, Object>> getAllClients() {
		List<ClientDto> clientDtos = new ArrayList<>();
		List<Client> clients = clientDaoService.findAll();
		if(clients != null && !clients.isEmpty()) {
			for(Client client : clients) {
				ClientDto clientDto = new ClientDto(client.getId(), client.getName(), client.getType().getId(),
						client.getType().getClientTypeDesc(), CommonUtils.deriveActionByName(client.getCreatedBy(), client.getName(), null),
						client.getCreatedDate(), CommonUtils.deriveActionByName(client.getLastUpdatedBy(), client.getName(), null),
						client.getLastUpdatedDate());
				clientDtos.add(clientDto);
			}
		}
		Map<String, Object> model = new HashMap<>();
		model.put("clients", clientDtos);
		model.put("statusDetail", clientDtos.isEmpty()
						? messageSource.getMessage("no.client.found", null, Locale.US)
						: clientDtos.size() + " " + messageSource.getMessage("clients.found", null, Locale.US));
		model.put("success", true);
		return ok(model);
	}
	
	//Get client by id
	@GetMapping("/clients/{id}")
	public Resource<ClientDto> getClientById(@PathVariable int id) {
		Client client = clientDaoService.findClientById(id);
		if(client == null) {
			throw new ResourceNotFoundException(messageSource.getMessage("client.not.found", null, Locale.US) + ": " + id);
		}
		ClientDto clientDto = new ClientDto(client.getId(), client.getName(), client.getType().getId(),
				client.getType().getClientTypeDesc(), CommonUtils.deriveActionByName(client.getCreatedBy(), client.getName(), null),
				client.getCreatedDate(), CommonUtils.deriveActionByName(client.getLastUpdatedBy(), client.getName(), null),
				client.getLastUpdatedDate());
		clientDto.setStatusDetail(messageSource.getMessage("clients.found", null, Locale.US));
		clientDto.setSuccess(true);
		Resource<ClientDto> resource = new Resource<ClientDto>(clientDto);		
		return resource;
	}
	
	//save user
	@PostMapping("/clients")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<ClientDto> saveClient(@Valid @RequestBody ClientDto client, @RequestHeader("User-Agent") String userAgent) throws Exception {
		if(client.getName() == null || client.getName().isEmpty() || client.getClientTypeId() == null) {
			throw new Exception("One or more input data is missing");
		}
		client.setLastUpdatedById(CommonUtils.getActionById(userAgent, 0));
		ClientDto clientSaved = clientDaoService.save(client);
		clientSaved.setStatusDetail(messageSource.getMessage("client.created", null, Locale.US));
		clientSaved.setSuccess(true);
		clientSaved.setCreatedById(null);
		clientSaved.setLastUpdatedById(null);
//		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(clientSaved.getClientId()).toUri();
		return ResponseEntity.status(HttpStatus.CREATED).body(clientSaved);
				
	}
	
	//Update user
	@PutMapping("/clients")
	public ResponseEntity<ClientDto> updateClient(@Valid @RequestBody ClientDto client, @RequestHeader("User-Agent") String userAgent) throws Exception {
		if(client.getClientId() == null || client.getName() == null || client.getName().isEmpty() || client.getClientTypeId() == null) {
			throw new Exception("Bad Request");
		}
		if(clientDaoService.findClientById(client.getClientId()) == null) {
			throw new ResourceNotFoundException(messageSource.getMessage("client.not.found", null, Locale.US) + client.getClientId());
		}
		client.setLastUpdatedById(CommonUtils.getActionById(userAgent, 0));
		ClientDto clientSaved = clientDaoService.save(client);
		clientSaved.setStatusDetail(messageSource.getMessage("client.updated", null, Locale.US));
		clientSaved.setSuccess(true);
		clientSaved.setCreatedById(null);
		clientSaved.setLastUpdatedById(null);
//		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(clientSaved.getClientId()).toUri();		
		return ok(clientSaved);
	}
	
	//Delete user by id
	@DeleteMapping("/clients/{id}")
	public ClientDto deleteClient(@PathVariable int id) {
		Client client = clientDaoService.deleteClientById(id);
		if(client == null) {
			LOGGER.error("Client not found; " +id);
			throw new ResourceNotFoundException(messageSource.getMessage("client.not.found", null, Locale.US) + ": " + id);
		}
		ClientDto clientDto = new ClientDto(client.getId(), client.getName(), client.getType().getId(), client.getType().getClientTypeDesc());
		clientDto.setStatusDetail(messageSource.getMessage("client.deleted", null, Locale.US));
		clientDto.setSuccess(true);
		return clientDto;
	}

}
