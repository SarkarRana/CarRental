package com.sma.smartauto.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import com.sma.smartauto.domain.Action;
import com.sma.smartauto.domain.ActionType;
import com.sma.smartauto.domain.Asset;
import com.sma.smartauto.domain.Client;
import com.sma.smartauto.domain.Code;
import com.sma.smartauto.domain.Compartment;
import com.sma.smartauto.domain.Safe;
import com.sma.smartauto.domain.User;
import com.sma.smartauto.exception.ApiErrorCode;
import com.sma.smartauto.exception.InvalidRequestParameterException;
import com.sma.smartauto.exception.ResourceNotFoundException;
import com.sma.smartauto.exception.SmartAutoSafeException;
import com.sma.smartauto.model.ActionDto;
import com.sma.smartauto.repository.ActionRepository;
import com.sma.smartauto.repository.ActionTypeRepository;
import com.sma.smartauto.repository.AssetRepository;
import com.sma.smartauto.repository.ClientRepository;
import com.sma.smartauto.repository.CodeRepository;
import com.sma.smartauto.repository.CompartmentRepository;
import com.sma.smartauto.repository.SafeRepository;
import com.sma.smartauto.repository.UserRepository;

@Component
public class ActionDaoService {
	
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
	UserRepository userRepository;
	
	@Autowired
	ActionTypeRepository actionTypeRepository;
	
	@Autowired
	CodeRepository codeRepository;
	
	@Autowired
	CompartmentRepository compartmentRepository;
	
	@Autowired
	SafeRepository safeRepository;
	
	@Autowired
	ActionRepository actionRepository;
	
	Calendar cal = Calendar.getInstance();
	
	public List<Action> findAll() {		
		return actionRepository.findAll();
	}
	
	public Action findActionById(Integer clientId, Integer id) {
		Optional<Client> client = clientRepository.findById(clientId);
		if(!client.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("client.not.found", null, Locale.US) + ": " + clientId);
		}
		 Optional<Action> action = actionRepository.findById(id);
		 if(action.isPresent()) {
			 if(action.get().getClient().getId().equals(clientId)) {
				 return action.get();
			 } else {
				 throw new SmartAutoSafeException(ApiErrorCode.ACCESS_DENIED, "Requested action does not belong to client with id " + clientId + ". Access denied");
			 }
		 }
		 return null;
	}
	
	public List<Action> findAllActionByClient(Integer clientId) {
		Optional<Client> client = clientRepository.findById(clientId);
		if(!client.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("client.not.found", null, Locale.US) + ": " + clientId);
		}
		try {
			return actionRepository.findAllByClient(client.get());
		} catch (Exception e) {
			throw new SmartAutoSafeException("Error while fertching actions for client: " + clientId + " :: " + e.getMessage());
		}
	}
	
	public List<Action> findAllActionBySafe(Integer clientId, Integer safeId) {
		Optional<Client> client = clientRepository.findById(clientId);
		if(!client.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("client.not.found", null, Locale.US) + ": " + clientId);
		}
		Optional<Safe> safe = safeRepository.findById(safeId);
		if(!safe.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("safe.not.found", null, Locale.US) + ": " + safeId);
		} else if(!safe.get().getClient().getId().equals(clientId)) {
			throw new InvalidRequestParameterException("Safe does not belong to client: " + clientId);
		}
		return actionRepository.findAllBySafe(safe.get());
	}
	
	public List<Action> findAllActionByCompartment(Integer clientId, Integer compId) {
		Optional<Client> client = clientRepository.findById(clientId);
		if(!client.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("client.not.found", null, Locale.US) + ": " + clientId);
		}
		Optional<Compartment> compartment = compartmentRepository.findByCompartmentId(compId);
		if(!compartment.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("compartment.not.found", null, Locale.US) + ": " + compId);
		} else if(!compartment.get().getClient().getId().equals(clientId)) {
			throw new InvalidRequestParameterException("Compartment does not belong to client: " + clientId);
		}
		return actionRepository.findAllByCompartment(compartment.get());
	}
	
	public List<Action> findAllActionByAsset(Integer clientId, Integer assetId) {
		Optional<Client> client = clientRepository.findById(clientId);
		if(!client.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("client.not.found", null, Locale.US) + ": " + clientId);
		}
		Optional<Asset> asset = assetRepository.findById(assetId);
		if(!asset.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("asset.not.found", null, Locale.US) + ": " + assetId);
		} else if(!asset.get().getClient().getId().equals(clientId)) {
			throw new InvalidRequestParameterException("Asset does not belong to client: " + clientId);
		}
		return actionRepository.findAllByAsset(asset.get());
	}
	
	public List<Action> findAllActionByCode(Integer clientId, String code) {
		Optional<Client> client = clientRepository.findById(clientId);
		if(!client.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("client.not.found", null, Locale.US) + ": " + clientId);
		}
		Optional<Code> codeObj = codeRepository.findByCode(code);
		if(!codeObj.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("code.not.found", null, Locale.US) + ": " + code);
		}
		return actionRepository.findAllByCode(codeObj.get());
	}
	
	public List<Action> findAllActionByUser(Integer clientId,Integer userId) {
		Optional<Client> client = clientRepository.findById(clientId);
		if(!client.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("client.not.found", null, Locale.US) + ": " + clientId);
		}
		Optional<User> user = userRepository.findById(userId);
		if(!user.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("user.not.found", null, Locale.US) + ": " + userId);
		} else if(!user.get().getClient().getId().equals(clientId)) {
			throw new InvalidRequestParameterException("User does not belong to client: " + clientId);
		}
		return actionRepository.findAllByUser(user.get());
	}
	
	public List<Action> findAllActionByType(Integer clientId, Integer actionTypeId) {
		Optional<Client> client = clientRepository.findById(clientId);
		if(!client.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("client.not.found", null, Locale.US) + ": " + clientId);
		}
		Optional<ActionType> type = actionTypeRepository.findById(actionTypeId);
		if(!type.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("action.type.not.found", null, Locale.US) + ": " + actionTypeId);
		} 
		return actionRepository.findAllByActionType(clientId, actionTypeId);
	}
	
	public List<Action> findAllActionByDate(Integer clientId, Date date) {
		Optional<Client> client = clientRepository.findById(clientId);
		if(!client.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("client.not.found", null, Locale.US) + ": " + clientId);
		}
		cal.setTime(date);
		cal.add(Calendar.DATE, 1);
		return actionRepository.findAllByDateTime(clientId, date, cal.getTime());
	}
	
	public List<Action> findByClientSafeCompartment(Integer clientId, Integer safeId, Integer compId) {		
		return actionRepository.findByClientSafeCompartment(clientId, safeId, compId);
	}
	
	public List<Action> findByClientSafeDate(Integer clientId, Integer safeId, Date date) {
		Optional<Client> client = clientRepository.findById(clientId);
		if(!client.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("client.not.found", null, Locale.US) + ": " + clientId);
		}
		Optional<Safe> safe = safeRepository.findById(safeId);
		if(!safe.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("safe.not.found", null, Locale.US) + ": " + safeId);
		}
		cal.setTime(date);
		cal.add(Calendar.DATE, 1);
		return actionRepository.findByClientSafeDate(clientId, safeId, date, cal.getTime());
	}
	
	public ActionDto save(Integer clientId, ActionDto actionDto) throws Exception {
		Optional<Client> client = clientRepository.findById(clientId);
		if(!client.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("client.not.found", null, Locale.US) + ": " + clientId);
		}
		if(actionDto == null || actionDto.getActionType() == null || actionDto.getIdentifier() == null 
				|| (actionDto.getCode() == null && (actionDto.getStaffCardCode() == null || actionDto.getStaffCardCode().isEmpty()))
				|| actionDto.getSafeId() == null || actionDto.getCompartmentId() == null) {
			throw new Exception("Bad input data");
		}
		
		Action action = new Action();
		Optional<ActionType> actionType = actionTypeRepository.findByActionTypeDesc(actionDto.getActionType());
		if(!actionType.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("action.type.not.found", null, Locale.US) + ": " + actionDto.getActionType());
		}
		action.setActionType(actionType.get());
		action.setClient(client.get());
		action.setDateTime(actionDto.getDate() == null ? new Date() : actionDto.getDate());
		if(actionDto.getStaffCardCode() != null && !actionDto.getStaffCardCode().isEmpty()) {
			Optional<User> user = userRepository.findByStaffCardCode(actionDto.getStaffCardCode());
			if(!user.isPresent()) {
				throw new ResourceNotFoundException(messageSource.getMessage("user.not.found", null, Locale.US) + ": " + actionDto.getStaffCardCode());
			} else {
				if(!user.get().getClient().getId().equals(clientId)) {
					throw new InvalidRequestParameterException("User does not belong to client: " + clientId);
				}
				action.setUser(user.get());
			}
		}
		Optional<Safe> safe = safeRepository.findById(actionDto.getSafeId());
		if(!safe.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("safe.not.found", null, Locale.US) + ": " + actionDto.getSafeId());
		} else {
			action.setSafe(safe.get());
		}
		Optional<Compartment> compartment = compartmentRepository.findByCompartmentId(actionDto.getCompartmentId());
		if(!compartment.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("compartment.not.found", null, Locale.US) + ": " + actionDto.getCompartmentId());
		} else {
			action.setCompartment(compartment.get());
		} 
		if (actionDto.getCode() != null) {
			Optional<Code> code = codeRepository.findByCode(actionDto.getCode());
			if (!code.isPresent()) {
				throw new ResourceNotFoundException(
						messageSource.getMessage("code.not.found", null, Locale.US) + ": " + actionDto.getCode());
			} else {
				action.setCode(code.get());
			}
		}
		Optional<Asset> asset = assetRepository.findByIdentifier(actionDto.getIdentifier());
		if(!asset.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("asset.not.found", null, Locale.US) + ": " + actionDto.getIdentifier());
		} else {
			action.setAsset(asset.get());
		}
		if(asset.get().getClient().getId().equals(clientId)) {
		Action savedAction = actionRepository.save(action);
		actionDto.setActionId(savedAction.getId());
		actionDto.setSuccess(true);
		} else {
			throw new InvalidRequestParameterException("Mismatch in mapping among code, asset, compartment and safe");
		}
		return actionDto;
	}
	
	public Action deleteAction(Integer clientId, int id) {
		Optional<Client> client = clientRepository.findById(clientId);
		if(!client.isPresent()) {
			throw new ResourceNotFoundException(messageSource.getMessage("client.not.found", null, Locale.US) + ": " + clientId);
		}
		Optional<Action> action = actionRepository.findById(id);
		if(action.isPresent()) {
			if(!action.get().getClient().getId().equals(clientId)) {
				throw new SmartAutoSafeException(ApiErrorCode.ACCESS_DENIED, messageSource.getMessage("action.not.found.for.client", null, Locale.US));
			} else if(!action.get().getSafe().getClient().getId().equals(clientId)) {
				throw new SmartAutoSafeException(ApiErrorCode.INVALID_INPUT, messageSource.getMessage("safe.not.found.for.client", null, Locale.US));
			}
			actionRepository.delete(action.get());
			return action.get();
		}
		return null;
	}
}
