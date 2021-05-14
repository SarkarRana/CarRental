package com.sma.smartauto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sma.smartauto.domain.ClientType;

@Repository
public interface ClientTypeRepository extends JpaRepository<ClientType, Integer> {
	
//	Optional<ActionType> findByActionTypeDesc(String actionTypeDesc);

}

