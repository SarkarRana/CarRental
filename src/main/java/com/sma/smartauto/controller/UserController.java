package com.sma.smartauto.controller;
//TODO: Write update user API and allow all the fields to be editable
import static org.springframework.http.ResponseEntity.ok;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sma.smartauto.dao.UserDaoService;
import com.sma.smartauto.domain.User;
import com.sma.smartauto.exception.ApiErrorCode;
import com.sma.smartauto.exception.InvalidRequestParameterException;
import com.sma.smartauto.exception.ResourceNotFoundException;
import com.sma.smartauto.exception.SmartAutoSafeException;
import com.sma.smartauto.model.UserDto;
import com.sma.smartauto.repository.UserRepository;
import com.sma.smartauto.utils.CommonUtils;

@RestController()
@RequestMapping("/user")
public class UserController {
	
	private MessageSource messageSource;

	@Autowired
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

    @Autowired
    UserRepository users;
    
    @Autowired
    UserDaoService userDaoService;
    
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @PostMapping("/authenticateUser")
    public ResponseEntity<UserDto> authenticateUser(@RequestParam("clientId") Integer clientId, @RequestBody UserDto data) {
    	if(clientId == null) {
			throw new ResourceNotFoundException(ApiErrorCode.MISSING_QUERY_PARAM + " Missing query param clientId");
		}
    	if(data == null || data.getStaffCardCode() == null || data.getStaffCardCode().isEmpty() || data.getAppPassword() == null || data.getAppPassword().isEmpty() || data.getLocationId() == null) {
			throw new InvalidRequestParameterException("One or more input values are either null or empty");
		}
    	UserDto responseDto = new UserDto();
        try {
        	Optional<User> userOpt = null;
        	if(data.getUsername() != null && !data.getUsername().isEmpty()) { 
	            userOpt = this.users.findByUsername(data.getUsername());
        	} else if (data.getStaffCardCode() != null && !data.getStaffCardCode().isEmpty()) {
        		userOpt = this.users.findByStaffCardCode(data.getStaffCardCode());
        	} else {
        		throw new SmartAutoSafeException(ApiErrorCode.INVALID_INPUT, messageSource.getMessage("username.staffCardCode.not.found", null, Locale.US));
        	}
        	        	
            if(!userOpt.isPresent()) {
            	throw new UsernameNotFoundException(messageSource.getMessage("username.does.not.exist", null, Locale.US));
            }
            //Changed auth from password to temp password
            /*if(!userOpt.get().getPassword().equalsIgnoreCase(data.getPassword())) {
            	throw new UsernameNotFoundException(messageSource.getMessage("password.does.not.match", null, Locale.US));
            }*/
            if(!userOpt.get().getTempPassword().equalsIgnoreCase(data.getAppPassword())) {
            	throw new UsernameNotFoundException(messageSource.getMessage("password.does.not.match", null, Locale.US));
            }
            if(userOpt.get().getLocation() == null || !userOpt.get().getLocation().getLocId().equals(data.getLocationId())) {
            	throw new UsernameNotFoundException(messageSource.getMessage("user.location.does.not.match", null, Locale.US));
            }
            
            List<String> roleList = new ArrayList<>();
            roleList.add(userOpt.get().getRole().getName());
            
//            responseDto.setUsername(userOpt.get().getUsername());
            responseDto.setStaffCardCode(userOpt.get().getStaffCardCode());
            responseDto.setUserId(userOpt.get().getUserId());
            if(userOpt.get().getLocation() != null) {
            	responseDto.setLocationId(userOpt.get().getLocation().getLocId());
            	responseDto.setLocationName(userOpt.get().getLocation().getName());
            }
            responseDto.setClientId(userOpt.get().getClient().getId());
            responseDto.setClientName(userOpt.get().getClient().getName());
            responseDto.setRoleList(roleList);
            responseDto.setStatusDetail(messageSource.getMessage("user.authorization.successful", null, Locale.US));
            responseDto.setSuccess(true);
            return ok(responseDto);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password.");
        }
    }
    
  //Get all users of client
    @GetMapping("/")
	public ResponseEntity<Map<String, Object>> getAllUsersForClient(@RequestParam("clientId") Integer clientId) throws Exception {
		List<UserDto> users = new ArrayList<>();
		if(clientId == null) {
			throw new ResourceNotFoundException(ApiErrorCode.MISSING_QUERY_PARAM + " Missing query param clientId");
		}
		List<User> userList = userDaoService.findAllUserByClient(clientId);
		if(userList != null && !userList.isEmpty()) {
			users = userList.stream().map(this::toDto).collect(Collectors.toList());
			/*for (User user : userList) {
				UserDto userDto = new UserDto(user.getUserId(), user.getUsername(), null, user.getStaffCardCode(), user.getRole().getName(),
						user.getClient().getId(), user.getClient().getName(),
						CommonUtils.deriveActionByName(user.getCreatedBy(), user.getClient().getName(), null),
						user.getCreatedDate(),
						CommonUtils.deriveActionByName(user.getLastUpdatedBy(), user.getClient().getName(), null),
						user.getLastUpdatedDate());
				users.add(userDto);
			}*/
		}
		Map<String, Object> model = new HashMap<>();
		model.put("users", users);
		model.put("statusDetail", users.isEmpty() ? messageSource.getMessage("no.user.found", null, Locale.US) : messageSource.getMessage("total.user.found", null, Locale.US).replace("x", users.size()+""));
		model.put("success", true);
		return ok(model);
	}
  	
    //Get all users of client
    @GetMapping("/byLocation/{locationId}")
	public ResponseEntity<Map<String, Object>> getLocationUsersForClient(@RequestParam("clientId") Integer clientId, @PathVariable Integer locationId) throws Exception {
		List<UserDto> users = new ArrayList<>();
		if(clientId == null) {
			throw new ResourceNotFoundException(ApiErrorCode.MISSING_QUERY_PARAM + " Missing query param clientId");
		}
		List<User> userList = userDaoService.findAllUserByClient(clientId);
		if(userList != null && !userList.isEmpty()) {
			users = userList.stream().map(this::toDto).collect(Collectors.toList());
		}
		Map<String, Object> model = new HashMap<>();
		model.put("users", users);
		model.put("statusDetail", users.isEmpty() ? messageSource.getMessage("no.user.found", null, Locale.US) : messageSource.getMessage("total.user.found", null, Locale.US).replace("x", users.size()+""));
		model.put("success", true);
		return ok(model);
	}
    
  	//Get user by id
    @GetMapping("/byId/{userId}")
	public ResponseEntity<UserDto> getUserById(@RequestParam("clientId") Integer clientId, @PathVariable Integer userId) {
    	if(clientId == null) {
			throw new ResourceNotFoundException(ApiErrorCode.MISSING_QUERY_PARAM + " Missing query param clientId");
		}
		User user = userDaoService.findUserById(clientId, userId);
		if(user == null) {
			throw new ResourceNotFoundException(userId.toString());
		}
		UserDto userDto = toDto(user);
		userDto.setStatusDetail(messageSource.getMessage("user.found", null, Locale.US));
		userDto.setSuccess(true);
		return ok(userDto);
	}
    
  //Get user by username
//    @GetMapping("/byName/{username}")
	public ResponseEntity<UserDto> getUserByUsername(@RequestParam("clientId") Integer clientId, @PathVariable String username) {
    	if(clientId == null) {
			throw new ResourceNotFoundException(ApiErrorCode.MISSING_QUERY_PARAM + " Missing query param clientId");
		}
		User user = userDaoService.findClientUserByUsername(clientId, username);
		if(user == null) {
			throw new ResourceNotFoundException(messageSource.getMessage("user.not.found", null, Locale.US) + ":"+username.toString());
		}
		UserDto userDto = toDto(user);
		userDto.setStatusDetail(messageSource.getMessage("user.found", null, Locale.US));
		userDto.setSuccess(true);
		return ok(userDto);
	}
	
	//Save user
	@PostMapping("/")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<UserDto> saveUser(@RequestParam("clientId") Integer clientId, @Valid @RequestBody UserDto userDto, @RequestHeader("User-Agent") String userAgent) throws Exception {
		if(clientId == null) {
			throw new ResourceNotFoundException(ApiErrorCode.MISSING_QUERY_PARAM + " Missing query param clientId");
		}
		if(userDto == null || userDto.getStaffCardCode() == null || userDto.getStaffCardCode().isEmpty() || userDto.getAppPassword() == null || userDto.getAppPassword().isEmpty() || userDto.getLocationId() == null || userDto.getRole() == null || userDto.getRole().isEmpty()) {
			throw new InvalidRequestParameterException("One or more input values are either null or empty");
		}
		/*User existingUser = userDaoService.findClientUserByUsername(clientId, userDto.getUsername());
		if(existingUser != null && existingUser.getClient().getId().equals(clientId)) {
			throw new SmartAutoSafeException(ApiErrorCode.INVALID_INPUT, messageSource.getMessage("username.already.exist", null, Locale.US));
		}*/
		User existingStaffCardUser = userDaoService.findClientUserByStaffCardCode(clientId, userDto.getStaffCardCode());
		if(existingStaffCardUser != null && existingStaffCardUser.getClient().getId().equals(clientId)) {
			throw new SmartAutoSafeException(ApiErrorCode.INVALID_INPUT, messageSource.getMessage("staffcardcode.already.assigned", null, Locale.US));
		}
		userDto.setLastUpdatedById(CommonUtils.getActionById(userAgent, clientId));
		UserDto savedUser = userDaoService.save(clientId, userDto);
		savedUser.setStatusDetail(messageSource.getMessage("user.created", null, Locale.US));
		savedUser.setSuccess(true);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
	}
			
	//Delete user by id
	@DeleteMapping("/byId/{userId}")
	public UserDto deleteUser(@RequestParam("clientId") Integer clientId, @PathVariable int userId) throws Exception {
		if(clientId == null) {
			throw new ResourceNotFoundException(ApiErrorCode.MISSING_QUERY_PARAM + " Missing query param clientId");
		}
		User user = userDaoService.deleteUserById(clientId, userId);		
		if(user == null) {
			LOGGER.error("Invalid User " + userId);
			throw new ResourceNotFoundException(messageSource.getMessage("user.not.found", null, Locale.US)+":" + userId);
		}

		UserDto userDto = toDto(user);
		userDto.setStatusDetail(messageSource.getMessage("user.deleted", null, Locale.US));
		userDto.setSuccess(true);
		return userDto;
	}
	
	//Delete user by name
//	@DeleteMapping("/byUsername/{username}")
	public UserDto deleteUserByName(@RequestParam("clientId") Integer clientId, @PathVariable String username) throws Exception {
		if(clientId == null) {
			throw new ResourceNotFoundException(ApiErrorCode.MISSING_QUERY_PARAM + "Missing query param clientId");
		}
		User user = userDaoService.deleteUserByUserName(username, clientId);
		if(user == null) {
			LOGGER.error("Invalid User " + username);
			throw new ResourceNotFoundException(messageSource.getMessage("user.not.found", null, Locale.US)+":" + username);
		}
		UserDto userDto = new UserDto(user.getUserId(), null, null, user.getStaffCardCode(),
				user.getRole().getName(), (user.getLocation() != null ? user.getLocation().getLocId() : null),
				(user.getLocation() != null ? user.getLocation().getName() : null), user.getClient().getId(),
				user.getClient().getName());
		userDto.setStatusDetail(messageSource.getMessage("user.deleted", null, Locale.US));
		userDto.setSuccess(true);
		return userDto;
	}
	
	private UserDto toDto(User user) {
		UserDto userDto = new UserDto(user.getUserId(), null, null, user.getStaffCardCode(),
				user.getRole().getName(), (user.getLocation() != null ? user.getLocation().getLocId() : null),
				(user.getLocation() != null ? user.getLocation().getName() : null), user.getClient().getId(),
				user.getClient().getName(),
				CommonUtils.deriveActionByName(user.getCreatedBy(), user.getClient().getName(), null),
				user.getCreatedDate(),
				CommonUtils.deriveActionByName(user.getLastUpdatedBy(), user.getClient().getName(), null),
				user.getLastUpdatedDate());
		return userDto;
	}
}
