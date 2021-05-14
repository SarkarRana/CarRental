package com.sma.smartauto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sma.smartauto.domain.ClientNotificationHistory;

@Repository
public interface ClientNotificationHistoryRepository extends JpaRepository<ClientNotificationHistory, Integer> {

	
}
