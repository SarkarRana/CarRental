package com.sma.smartauto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sma.smartauto.domain.ClientNotification;

@Repository
public interface ClientNotificationRepository extends JpaRepository<ClientNotification, Integer> {

	ClientNotification findAllByClientIdAndType(Integer clientId, String type);	
	
}
