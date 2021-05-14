package com.sma.smartauto.dao;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import com.sma.smartauto.domain.ClientNotification;
import com.sma.smartauto.domain.ClientNotificationHistory;
import com.sma.smartauto.exception.ApiErrorCode;
import com.sma.smartauto.exception.SmartAutoSafeException;
import com.sma.smartauto.model.CodeDto;
import com.sma.smartauto.repository.ClientNotificationHistoryRepository;
import com.sma.smartauto.repository.ClientNotificationRepository;

@Component
public class ClientNotificationDaoService {
	
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	private MessageSource messageSource;

	@Autowired
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	@Autowired
	ClientNotificationRepository clientNotificationRepository;
	
	@Autowired
	ClientNotificationHistoryRepository clientNotificationHistoryRepository;
	
	Calendar cal = Calendar.getInstance();
	
	public void sendLockNotificationToClient(Integer clientId, CodeDto codeDto) {
		try {
			ClientNotification clientNotification = clientNotificationRepository.findAllByClientIdAndType(clientId, "lock_notification");
			if (clientNotification == null) {
				throw new SmartAutoSafeException(ApiErrorCode.RESOURCE_NOT_FOUND + "client notification could not be found for client id: " + clientId);

			}
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			parameterMap.put("Code", codeDto.getCode());
			parameterMap.put("CodeId", codeDto.getId());
			parameterMap.put("LocationId", codeDto.getLocationId());
			parameterMap.put("SafeId", codeDto.getSafeId());
			parameterMap.put("CompartmentId", codeDto.getCompartmentId());
			parameterMap.put("AssetId", codeDto.getAssetId());
			parameterMap.put("Identifier", codeDto.getIdentifier());
			parameterMap.put("ActionType", codeDto.getActionType());

			String response = sendLockNotification(clientNotification.getApiEndpoint(), parameterMap,	clientNotification.getAuthToken());
			String desc = response != null && response.contains("::") ? response.split("::")[1] : null;
			String status = response != null && response.contains("::") ? response.split("::")[0] : response;
			codeDto.setClientId(clientId);
			saveClientNotificationHistory(codeDto, status, desc);
		} catch (Exception ex) {
			LOGGER.error("Exception occured while sending lock notification to client : " + clientId + " :: " + ex.getMessage());
			LOGGER.debug(codeDto.toString());
		}
	}
	
	public String sendLockNotification(String url, Map<String, Object> parameterMap, String token) {
		String statusStr = "SUCCESS";
		try {
			String jsonRequest = new ObjectMapper().writeValueAsString(parameterMap) ;
			
			CloseableHttpClient httpClient = HttpClients.createDefault() ;
			HttpPost httppost = new HttpPost(url);

			httppost.addHeader("Content-Type", "application/json");
			httppost.addHeader("authorization", "Basic " + token);
			
			
			httppost.setEntity(new StringEntity(jsonRequest, ContentType.APPLICATION_JSON));
			
			ResponseHandler< String > responseHandler = response -> {
			    int status = response.getStatusLine().getStatusCode();
			    if (status >= 200 && status < 300) {
			        HttpEntity entity = (HttpEntity) response.getEntity();
			        return entity != null ? EntityUtils.toString(entity) : null;
			    } else {
			        throw new ClientProtocolException("Unexpected response status: " + status);
			    }
			};
			
			String responseBody = httpClient.execute(httppost, responseHandler);
			
			if(responseBody.toString().contains("\"Success\":true")) {
				LOGGER.info("Client Notification send successfully. Response: " + responseBody.toString());
			} else {
				LOGGER.error("Client Notification failed : " + responseBody.toString() + "\n Original Request: " + jsonRequest);
				statusStr = "ERROR::"+ responseBody;
			}
		} catch (Exception ex) {
			LOGGER.error("Error while sending client notification", ex);
			statusStr = "ERROR::"+ ex.getMessage();
		}
		return statusStr;
	}
	
	public boolean saveClientNotificationHistory(CodeDto codeDto, String status, String desc) {
		try {
			ClientNotificationHistory history = new ClientNotificationHistory();
			history.setClientId(codeDto.getClientId());
			history.setCode(codeDto.getCode());
			history.setCodeId(codeDto.getId());
			history.setSafeId(codeDto.getSafeId());
			history.setCompartmentId(codeDto.getCompartmentId());
			history.setIdentifier(codeDto.getIdentifier());
			history.setAction(codeDto.getActionType());
			history.setStatus(status);
			if(desc != null) {
				history.setResponseDesc(desc);
			}
			return clientNotificationHistoryRepository.save(history) == null;
		} catch (Exception ex) {
			LOGGER.error("error while saving notification history: " + ex.getMessage());
			return false;
		}
	}
}
