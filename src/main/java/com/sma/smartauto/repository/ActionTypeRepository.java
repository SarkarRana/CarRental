package com.sma.smartauto.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sma.smartauto.domain.ActionType;

@Repository
public interface ActionTypeRepository extends JpaRepository<ActionType, Integer> {
	
	Optional<ActionType> findByActionTypeDesc(String actionTypeDesc);

}

