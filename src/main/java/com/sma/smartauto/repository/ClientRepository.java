package com.sma.smartauto.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sma.smartauto.domain.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
	
	Optional<Client> findByName(String name);

}
