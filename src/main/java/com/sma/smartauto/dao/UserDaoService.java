package com.sma.smartauto.dao;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.sql.rowset.CachedRowSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import com.sma.smartauto.domain.Client;
import com.sma.smartauto.domain.Location;
import com.sma.smartauto.domain.Role;
import com.sma.smartauto.domain.User;
import com.sma.smartauto.exception.InvalidRequestParameterException;
import com.sma.smartauto.exception.ResourceNotFoundException;
import com.sma.smartauto.model.UserDto;
import com.sma.smartauto.repository.ClientRepository;
import com.sma.smartauto.repository.LocationRepository;
import com.sma.smartauto.repository.RoleRepository;
import com.sma.smartauto.repository.UserRepository;
import com.sma.smartauto.utils.CommonUtils;

@Component
public class UserDaoService {
	
	private MessageSource messageSource;

	@Autowired
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ClientRepository clientRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	LocationRepository locationRepository;
	
	public List<User> findAll() {		
		return userRepository.findAll();
	}
	
	public User findUserByUsername(String username) {
		Optional<User> user =  userRepository.findByUsername(username);
		if(user.isPresent()) {
			return user.get();
		}
		return null;
	}
	
	public User findClientUserByUsername(Integer clientId, String username) {
		Optional<Client> client = clientRepository.findById(clientId);
		if(!client.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("client.not.found", null, Locale.US) + ": " + clientId);
		}
		Optional<User> user =  userRepository.findByUsername(username);
		if(!user.isPresent()) {
			return null;
		} else {
			if(!user.get().getClient().getId().equals(clientId)) {
				throw new InvalidRequestParameterException(messageSource.getMessage("user.not.found.for.client", null, Locale.US) + ": " + clientId);				
			} else {
				return user.get();
			}
		}
	}
	
	public User findClientUserByStaffCardCode(Integer clientId, String staffCardCode) {
		Optional<Client> client = clientRepository.findById(clientId);
		if(!client.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("client.not.found", null, Locale.US) + ": " + clientId);
		}
		Optional<User> user =  userRepository.findByStaffCardCode(staffCardCode);
		if(!user.isPresent()) {
			return null;
		} else {
			if(!user.get().getClient().getId().equals(clientId)) {
				throw new InvalidRequestParameterException(messageSource.getMessage("user.not.found.for.client", null, Locale.US) + ": " + clientId);				
			} else {
				return user.get();
			}
		}
	}

	public User findUserById(Integer clientId, Integer id) {
		Optional<Client> client = clientRepository.findById(clientId);
		if(!client.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("client.not.found", null, Locale.US) + ": " + clientId);
		}
		Optional<User> user = userRepository.findById(id);
		if(!user.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("user.not.found", null, Locale.US) + ": " + id);
		} else {
			if(!user.get().getClient().getId().equals(clientId)) {
				throw new InvalidRequestParameterException(messageSource.getMessage("user.not.found.for.client", null, Locale.US) + ": " + clientId);				
			} else {
				return user.get();
			}
		}
	}

	public List<User> findAllUserByClient(Integer clientId) {
		Optional<Client> client = clientRepository.findById(clientId);
		if(!client.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("client.not.found", null, Locale.US) + ": " + clientId);
		}
		return userRepository.findByClient(client.get());
	}
	
	public List<User> findUserByClientAndClient(Integer clientId, Integer locationId) {
		Optional<Client> client = clientRepository.findById(clientId);
		if(!client.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("client.not.found", null, Locale.US) + ": " + clientId);
		}
		Optional<Location> location = locationRepository.findById(locationId);
		if(!location.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("location.not.found", null, Locale.US) + ": " + locationId);
		}
		return userRepository.findSafeByClientAndLocation(locationId, clientId);
	}
	
	public User save(User user) {
		User newUser = userRepository.save(user);
		return newUser;
	}
	
	public UserDto save(Integer clientId, UserDto userDto) throws Exception {
		Optional<Client> client = clientRepository.findById(clientId);
		if(!client.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("client.not.found", null, Locale.US) + ": " + clientId);
		}
		if(userDto == null || userDto.getStaffCardCode() == null || userDto.getAppPassword() == null || userDto.getRole() == null) {
			throw new Exception("Bad input data");
		}
		Role role = roleRepository.findByName(userDto.getRole());
		if (role == null) {
			throw new ResourceNotFoundException(messageSource.getMessage("role.not.found", null, Locale.US) + ": " + userDto.getRole());
		}
		Location loc = null;
		if (userDto.getLocationId() != null) {
			Optional<Location> location = locationRepository.findById(userDto.getLocationId());
			if (!location.isPresent()) {
				throw new ResourceNotFoundException(messageSource.getMessage("location.not.found", null, Locale.US)
						+ ": " + userDto.getLocationId());
			} else {

				if(!location.get().getClient().getId().equals(client.get().getId())) {
					throw new Exception("Wrong location for specified client");
				}
				loc = location.get();
			}
		}
		User user = new User();
		user.setClient(client.get());
		user.setStaffCardCode(userDto.getStaffCardCode());
		user.setUsername(userDto.getUsername());
		if(userDto.getAppPassword() != null) {
			user.setTempPassword(userDto.getAppPassword());
		}
//		user.setTempPassword(userDto.getAppPassword());
		user.setRole(role);
		user.setLocation(loc);
		user.setCreatedBy(userDto.getLastUpdatedById());
		user.setLastUpdatedBy(userDto.getLastUpdatedById());
		 
		userRepository.save(user);
		
		UserDto savedUserDto = new UserDto(user.getUserId(), null, null, user.getStaffCardCode(),
				user.getRole().getName(), (user.getLocation() != null ? user.getLocation().getLocId() : null),
				(user.getLocation() != null ? user.getLocation().getName() : null), clientId, client.get().getName(),
				CommonUtils.deriveActionByName(user.getCreatedBy(), client.get().getName(), null),
				user.getCreatedDate(),
				CommonUtils.deriveActionByName(user.getLastUpdatedBy(), client.get().getName(), null),
				user.getLastUpdatedDate());
		return savedUserDto;
	}
	
	public User deleteUserById(Integer clientId, Integer id) {
		Optional<Client> client = clientRepository.findById(clientId);
		if(!client.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("client.not.found", null, Locale.US) + ": " + clientId);
		}
		Optional<User> user = userRepository.findById(id);
		if(!user.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("user.not.found", null, Locale.US) + ": " + id);
		} else {
			if(!user.get().getClient().getId().equals(clientId)) {
				throw new InvalidRequestParameterException(messageSource.getMessage("user.not.found.for.client", null, Locale.US) + ": " + clientId);				
			} else  {
				userRepository.delete(user.get());
				return user.get();
			}
		}
	}
	
	public User deleteUserByUserName(String username, Integer clientId) {
		User user = findClientUserByUsername(clientId, username);
		if(user == null) {
			throw new ResourceNotFoundException(messageSource.getMessage("user.not.found", null, Locale.US) + ": " + username);
		} else {
			if(!user.getClient().getId().equals(clientId)) {
				throw new InvalidRequestParameterException("User does not belong to client User name: " + username + " client Id: " + clientId);				
			} else  {
				userRepository.delete(user);
				return user;
			}
		}
	}
}
