package com.sma.smartauto.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sma.smartauto.domain.Code;
import com.sma.smartauto.exception.SmartAutoSafeException;

public class CommonUtils {
	
	public static final String RASPBERRY_PI_ = "RASPBERRY_PI_";
	public static final Integer API_ID_PREFIX = 99999;
	private static final Logger LOGGER = LoggerFactory.getLogger("CommounUtils");

	public static Date formatDate(String strDate, String format) {
		if(null != strDate && !strDate.isEmpty()) {
			try {
				return new SimpleDateFormat(format).parse(strDate);
			} catch (ParseException ex) {
//				logger.debug("Error while parsing date: " + strDate, ex);
				System.out.println("Error while parsing date: " + strDate+ ex.getMessage());
			}
		}
		return null;
	}
	
	public static String formatDate(Date date, String format) {
		if (null != date) {
			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat(format);
				String sqlDate = dateFormat.format(date);
				return sqlDate;
			} catch (Exception ex) {
//				logger.debug("Error while parsing date: " + date, ex);
				System.out.println("Error while parsing date: " + date+ ex.getMessage());
			}
		}
		return null;
	}
	
	public static String getMd5(String input) 
    { 
        try { 
  
            // Static getInstance method is called with hashing MD5 
            MessageDigest md = MessageDigest.getInstance("MD5"); 
  
            // digest() method is called to calculate message digest 
            //  of an input digest() return array of byte 
            byte[] messageDigest = md.digest(input.getBytes()); 
  
            // Convert byte array into signum representation 
            BigInteger no = new BigInteger(1, messageDigest); 
  
            // Convert message digest into hex value 
            String hashtext = no.toString(16); 
            while (hashtext.length() < 32) { 
                hashtext = "0" + hashtext; 
            } 
            return hashtext; 
        }  
  
        // For specifying wrong message digest algorithms 
        catch (NoSuchAlgorithmException e) { 
            throw new RuntimeException(e); 
        } 
    } 
	
	public static boolean isCodeActive(Code code) {
		boolean isActive = true;
		if(code != null) {
			Date date = new Date();
			if((!code.getActive() || (code.getValidFrom().after(date) || code.getValidTill().before(date)))) {
				isActive = false;
			}
		}
		return isActive;
	}
	
	public static int getActionById(String userAgent, int clientId) {
		int actionById = 0;
		if(userAgent != null && !userAgent.isEmpty() && userAgent.startsWith(RASPBERRY_PI_)) {
			userAgent = userAgent.replace(RASPBERRY_PI_, "");
			actionById = Integer.parseInt(userAgent);
		} else {
			try {
				actionById = Integer.parseInt(API_ID_PREFIX.toString() + clientId);
			} catch (Exception e) {
				LOGGER.error("Invalid actionBy Id:" + API_ID_PREFIX.toString() + clientId + " : " + clientId);
				throw new SmartAutoSafeException("Invalid value for clientId: " + clientId);
			}
		}		
		return actionById;
	}
	
	public static Integer getSafeIdFromCompartmentId(Integer compartmentId) {
		String compartmentIdStr = compartmentId.toString();
		return Integer.parseInt(compartmentIdStr.substring(0, compartmentIdStr.length()-2));
	}
	
	public static boolean isCodeAssigned(Code code) {
		return (code.getLocation() != null && code.getCode() != null && !code.getCode().isEmpty() 
				&& ((code.getIdentifierType() != null && !code.getIdentifierType().isEmpty()) || (code.getIdentifier() != null && !code.getIdentifier().isEmpty()))
				&& code.getValidFrom() != null && code.getValidTill() != null);
	}
	
	public static String deriveActionByName(Integer id, String clientName, String locName) {
		String actionByName = "";
		if (id.toString().startsWith(API_ID_PREFIX.toString())) {
			int clientId = Integer.parseInt(id.toString().replace(API_ID_PREFIX.toString(), ""));
			if (clientId == 0) {
				actionByName = "api_admin";
			} else {
				actionByName = "api_" + clientName;
			}
		} else {
			actionByName = "safe" + id + "_" + locName;
		}		
		return actionByName;
	}

	public static void main(String[] a) {
		System.out.println(getSafeIdFromCompartmentId(206215));
	}
}
