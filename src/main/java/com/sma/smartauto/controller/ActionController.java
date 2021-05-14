package com.sma.smartauto.controller;

import static org.springframework.http.ResponseEntity.ok;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sma.smartauto.dao.ActionDaoService;
import com.sma.smartauto.domain.Action;
import com.sma.smartauto.exception.ApiErrorCode;
import com.sma.smartauto.exception.InvalidRequestParameterException;
import com.sma.smartauto.exception.ResourceNotFoundException;
import com.sma.smartauto.model.ActionDto;
import com.sma.smartauto.utils.Constants;

@RequestMapping("/action")
@RestController
public class ActionController {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	private MessageSource messageSource;

	@Autowired
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	@Autowired
	private ActionDaoService actionDaoService;
	
	@GetMapping("/")
	public ResponseEntity<Map<String, Object>> getAllActionsOfClient(@RequestParam("clientId") Integer clientId) throws Exception {
		List<ActionDto> actionDtos = new ArrayList<>();
		if(clientId == null) {
			throw new ResourceNotFoundException(ApiErrorCode.MISSING_QUERY_PARAM + " Missing query param clientId");
		}
		List<Action> actionList = actionDaoService.findAllActionByClient(clientId);
		if(actionList != null && !actionList.isEmpty()) {
			for(Action action : actionList) {
				ActionDto actionDto = new ActionDto(action.getId(), action.getActionType().getActionTypeDesc(), action.getActionType().getId(), action.getAsset().getId(), action.getAsset().getIdentifier(), action.getClient().getName(), action.getClient().getId(), action.getSafe().getName(), action.getSafe().getSafeId(), action.getCompartment().getName(), action.getCompartment().getCompartmentId(), (action.getCode()!=null?action.getCode().getCode():null), (action.getUser() != null ? action.getUser().getUserId() : null), (action.getUser() != null ? action.getUser().getUsername() : null), (action.getUser()!=null?action.getUser().getStaffCardCode():null), action.getDateTime());
				actionDtos.add(actionDto);
			}
		}
		Map<String, Object> model = new HashMap<>();
		model.put("actions", actionDtos);
		model.put("statusDetail", actionDtos.isEmpty() ? messageSource.getMessage("no.action.found", null, Locale.US) : messageSource.getMessage("total.action.found", null, Locale.US).replace("x", actionDtos.size()+""));
		model.put("success", true);
		return ok(model);
	}
	
	//Get actions for assetId
	@GetMapping("/forAsset/{assetId}")
	public ResponseEntity<Map<String, Object>> getActionsForAsset(@RequestParam("clientId") Integer clientId, @PathVariable int assetId) throws Exception {
		if(clientId == null) {
			throw new ResourceNotFoundException(ApiErrorCode.MISSING_QUERY_PARAM + " Missing query param clientId");
		}

		List<ActionDto> actionDtos = getActionsByFilterName("Asset", assetId, clientId);
		Map<String, Object> map = new HashMap<>();
		map.put("actions", actionDtos);
		map.put("statusDetail", actionDtos.isEmpty() ? messageSource.getMessage("no.action.found", null, Locale.US) : messageSource.getMessage("total.action.found", null, Locale.US).replace("x", actionDtos.size()+""));
		map.put("success", true);
		return ok(map);
	}
	
	//Get actions for safeId
	@GetMapping("/forSafe/{safeId}")
	public ResponseEntity<Map<String, Object>> getActionsForSafe(@RequestParam("clientId") Integer clientId, @PathVariable int safeId) throws Exception {
		if(clientId == null) {
			throw new ResourceNotFoundException(ApiErrorCode.MISSING_QUERY_PARAM + " Missing query param clientId");
		}
		List<ActionDto> actionDtos = getActionsByFilterName("Safe", safeId, clientId);
		
		Map<String, Object> map = new HashMap<>();
		map.put("actions", actionDtos);
		map.put("statusDetail", actionDtos.isEmpty() ? messageSource.getMessage("no.action.found", null, Locale.US) : messageSource.getMessage("total.action.found", null, Locale.US).replace("x", actionDtos.size()+""));
		map.put("success", true);
		return ok(map);
	}
	
	//Get actions for safeId
	@GetMapping("/forCompartment/{compartmentId}")
	public ResponseEntity<Map<String, Object>> getActionsForCompartment(@RequestParam("clientId") Integer clientId, @PathVariable int compartmentId) throws Exception {
		if(clientId == null) {
			throw new ResourceNotFoundException(ApiErrorCode.MISSING_QUERY_PARAM + " Missing query param clientId");
		}
		List<ActionDto> actionDtos = getActionsByFilterName("Compartment", compartmentId, clientId);
		
		Map<String, Object> map = new HashMap<>();
		map.put("actions", actionDtos);
		map.put("statusDetail", actionDtos.isEmpty() ? messageSource.getMessage("no.action.found", null, Locale.US) : messageSource.getMessage("total.action.found", null, Locale.US).replace("x", actionDtos.size()+""));
		map.put("success", true);
		return ok(map);
	}
	
	//Get actions for userId
	@GetMapping("/forUser/{userId}")
	public ResponseEntity<Map<String, Object>> getActionsForUser(@RequestParam("clientId") Integer clientId, @PathVariable int userId) throws Exception {
		if(clientId == null) {
			throw new ResourceNotFoundException(ApiErrorCode.MISSING_QUERY_PARAM + " Missing query param clientId");
		}
		List<ActionDto> actionDtos = getActionsByFilterName("User", userId, clientId);
		
		Map<String, Object> map = new HashMap<>();
		map.put("actions", actionDtos);
		map.put("statusDetail", actionDtos.isEmpty() ? messageSource.getMessage("no.action.found", null, Locale.US) : messageSource.getMessage("total.action.found", null, Locale.US).replace("x", actionDtos.size()+""));
		map.put("success", true);
		return ok(map);
	}
	
	//Get actions for Action Type
	@GetMapping("/forActionType/{actionTypeId}")
	public ResponseEntity<Map<String, Object>> getActionsForActionType(@RequestParam("clientId") Integer clientId, @PathVariable int actionTypeId) throws Exception {
		if(clientId == null) {
			throw new ResourceNotFoundException(ApiErrorCode.MISSING_QUERY_PARAM + " Missing query param clientId");
		}
		List<ActionDto> actionDtos = getActionsByFilterName("actionType", actionTypeId, clientId);
		
		Map<String, Object> map = new HashMap<>();
		map.put("actions", actionDtos);
		map.put("statusDetail", actionDtos.isEmpty() ? messageSource.getMessage("no.action.found", null, Locale.US) : messageSource.getMessage("total.action.found", null, Locale.US).replace("x", actionDtos.size()+""));
		map.put("success", true);
		return ok(map);
	}
	
	//Get actions for date
	@GetMapping("/forDate/{date}")
	public ResponseEntity<Map<String, Object>> getActionsForDate(@RequestParam("clientId") Integer clientId, @PathVariable String date) throws Exception {
		if(clientId == null) {
			throw new ResourceNotFoundException(ApiErrorCode.MISSING_QUERY_PARAM + " Missing query param clientId");
		}
		List<ActionDto> actionDtos = getActionsByFilterName("Date", date, clientId);
		Map<String, Object> map = new HashMap<>();
		map.put("actions", actionDtos);
		map.put("statusDetail", actionDtos.isEmpty() ? messageSource.getMessage("no.action.found", null, Locale.US) : messageSource.getMessage("total.action.found", null, Locale.US).replace("x", actionDtos.size()+""));
		map.put("success", true);
		return ok(map);
	}
	
	//Get actions for safeId
	@GetMapping("/forCode/{code}")
	public ResponseEntity<Map<String, Object>> getActionsForCode(@RequestParam("clientId") Integer clientId, @PathVariable String code) throws Exception {
		if(clientId == null) {
			throw new ResourceNotFoundException(ApiErrorCode.MISSING_QUERY_PARAM + " Missing query param clientId");
		}
		List<ActionDto> actionDtos = getActionsByFilterName("Code", code, clientId);		
		Map<String, Object> map = new HashMap<>();
		map.put("actions", actionDtos);
		map.put("statusDetail", actionDtos.isEmpty() ? messageSource.getMessage("no.action.found", null, Locale.US) : messageSource.getMessage("total.action.found", null, Locale.US).replace("x", actionDtos.size()+""));
		map.put("success", true);
		return ok(map);
	}
	
	//Get actions for safeId
	@GetMapping("/forClientSafeDate/{safeId}/{date}")
	public ResponseEntity<Map<String, Object>> getActionsForClientSafeDate(@RequestParam("clientId") Integer clientId, @PathVariable int safeId, @PathVariable String date) throws Exception {
		if(clientId == null) {
			throw new ResourceNotFoundException(ApiErrorCode.MISSING_QUERY_PARAM + " Missing query param clientId");
		}
		Date dateObj = sdf.parse(date);
		List<Action> actionList = actionDaoService.findByClientSafeDate(clientId, safeId, dateObj);
		if(actionList == null || actionList.isEmpty()) {
			throw new ResourceNotFoundException(messageSource.getMessage("action.not.found", null, Locale.US));
		}			
		if(!actionList.get(0).getClient().getId().equals(clientId)) {
			throw new InvalidRequestParameterException("Incorrect input data");
		}
		List<ActionDto> actionDtos = new ArrayList<>();
		for(Action action : actionList) {
			ActionDto actionDto = new ActionDto(action.getId(), action.getActionType().getActionTypeDesc(), action.getActionType().getId(), action.getAsset().getId(), action.getAsset().getIdentifier(), action.getClient().getName(), action.getClient().getId(), action.getSafe().getName(), action.getSafe().getSafeId(), action.getCompartment().getName(), action.getCompartment().getCompartmentId(), (action.getCode()!=null?action.getCode().getCode():null), (action.getUser() != null ? action.getUser().getUserId() : null), (action.getUser() != null ? action.getUser().getUsername() : null), (action.getUser()!=null?action.getUser().getStaffCardCode():null), action.getDateTime());
			actionDtos.add(actionDto);
		}
		Map<String, Object> map = new HashMap<>();
		map.put("actions", actionDtos);
		map.put("statusDetail", actionDtos.isEmpty() ? messageSource.getMessage("no.action.found", null, Locale.US) : messageSource.getMessage("total.action.found", null, Locale.US).replace("x", actionDtos.size()+""));
		map.put("success", true);
		return ok(map);
	}
	
	//Save Action
	@PostMapping("/")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<ActionDto> saveAction(@RequestParam("clientId") Integer clientId, @RequestBody ActionDto actionDto) throws Exception {
		if(clientId == null) {
			throw new ResourceNotFoundException(ApiErrorCode.MISSING_QUERY_PARAM + " Missing query param clientId");
		}
		if(actionDto == null || actionDto.getActionType() == null || actionDto.getActionType().isEmpty() 
				|| actionDto.getIdentifier() == null || actionDto.getIdentifier().isEmpty() 
				|| actionDto.getCode() == null || actionDto.getSafeId() == null
				|| actionDto.getCompartmentId() == null) {
			throw new InvalidRequestParameterException("Incorrect input data");
		}

		ActionDto savedAction = actionDaoService.save(clientId, actionDto);
		savedAction.setStatusDetail(messageSource.getMessage("action.created", null, Locale.US));
		savedAction.setSuccess(true);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedAction);
	}
	
	//Save Action
		@PostMapping("/saveStaffAction")
		@ResponseStatus(HttpStatus.CREATED)
		public ResponseEntity<ActionDto> saveStaffAction(@RequestParam("clientId") Integer clientId, @RequestBody ActionDto actionDto) throws Exception {
			if(clientId == null) {
				throw new ResourceNotFoundException(ApiErrorCode.MISSING_QUERY_PARAM + " Missing query param clientId");
			}
			if(actionDto == null || actionDto.getActionType() == null || actionDto.getActionType().isEmpty() 
					|| actionDto.getIdentifier() == null || actionDto.getIdentifier().isEmpty() 
					|| actionDto.getStaffCardCode() == null || actionDto.getStaffCardCode().isEmpty()
					|| actionDto.getSafeId() == null || actionDto.getCompartmentId() == null) {
				throw new InvalidRequestParameterException("Incorrect input data");
			}
			
			ActionDto savedAction = actionDaoService.save(clientId, actionDto);
			savedAction.setStatusDetail(messageSource.getMessage("action.created", null, Locale.US));
			savedAction.setSuccess(true);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedAction);
		}
	

	//Delete asset by id
	@DeleteMapping("/{id}")
//	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ActionDto deleteAction(@RequestParam("clientId") Integer clientId, @PathVariable int id) throws Exception {
		if(clientId == null) {
			throw new ResourceNotFoundException(ApiErrorCode.MISSING_QUERY_PARAM + " Missing query param clientId");
		}
		Action action = actionDaoService.deleteAction(clientId, id);
		if(action == null) {
			LOGGER.error("Invalid action " +id);
			throw new ResourceNotFoundException(messageSource.getMessage("action.not.found", null, Locale.US) + ":" + id);
		}
		ActionDto actionDto = new ActionDto(action.getId(), action.getActionType().getActionTypeDesc(), action.getActionType().getId(), action.getAsset().getId(), action.getAsset().getIdentifier(), action.getClient().getName(), action.getClient().getId(), action.getSafe().getName(), action.getSafe().getSafeId(), action.getCompartment().getName(), action.getCompartment().getCompartmentId(), (action.getCode()!=null?action.getCode().getCode():null), (action.getUser() != null ? action.getUser().getUserId() : null), (action.getUser() != null ? action.getUser().getUsername() : null), (action.getUser()!=null?action.getUser().getStaffCardCode():null), action.getDateTime());
		actionDto.setStatusDetail(messageSource.getMessage("action.deleted", null, Locale.US));
		actionDto.setSuccess(true);
		return actionDto;
	}
	
	private List<ActionDto> getActionsByFilterName(String filterName, Object id, Integer clientId) throws Exception {
		List<ActionDto> actionDtos = null;
		List<Action> actionList = null;
		switch (filterName) {
		case Constants.COMPARTMENT:
			actionList = actionDaoService.findAllActionByCompartment(clientId, (Integer)id);
			break;
		case Constants.ASSET:
			actionList = actionDaoService.findAllActionByAsset(clientId, (Integer)id);
			break;
		case Constants.SAFE:
			actionList = actionDaoService.findAllActionBySafe(clientId, (Integer)id);
			break;
		case Constants.CODE:
			actionList = actionDaoService.findAllActionByCode(clientId, (String)id);
			break;
		case Constants.ACTION_TYPE:
			actionList = actionDaoService.findAllActionByType(clientId, (Integer)id);
			break;
		case Constants.USER:
			actionList = actionDaoService.findAllActionByUser(clientId, (Integer)id);
			break;
		case Constants.DATE:
			Date date = sdf.parse(id.toString());
			actionList = actionDaoService.findAllActionByDate(clientId, date);
			break;

		default:
			break;
		}		
		if(actionList == null || actionList.isEmpty()) {
			throw new ResourceNotFoundException(messageSource.getMessage("no.action.found", null, Locale.US) + filterName + ": " + id);
		}
		if(actionList.get(0).getClient().getId() != clientId) {
			throw new InvalidRequestParameterException("Action does not belong to the client: " + clientId);
		}
		actionDtos = new ArrayList<>();
		for(Action action : actionList) {
			ActionDto actionDto = new ActionDto(action.getId(), action.getActionType().getActionTypeDesc(), action.getActionType().getId(), action.getAsset().getId(), action.getAsset().getIdentifier(), action.getClient().getName(), action.getClient().getId(), action.getSafe().getName(), action.getSafe().getSafeId(), action.getCompartment().getName(), action.getCompartment().getCompartmentId(), (action.getCode() != null ? action.getCode().getCode() : null), (action.getUser()!=null?action.getUser().getUserId():null), (action.getUser()!=null?action.getUser().getUsername():null), (action.getUser()!=null?action.getUser().getStaffCardCode():null), action.getDateTime());
			actionDtos.add(actionDto);
		}
		return actionDtos;
	}
	
	

}
