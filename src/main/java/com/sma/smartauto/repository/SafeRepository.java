package com.sma.smartauto.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sma.smartauto.domain.Client;
import com.sma.smartauto.domain.Safe;

@Repository
public interface SafeRepository extends JpaRepository<Safe, Integer> {

	Optional<Safe> findBySafeId(Integer safeId);
	
	Optional<Safe> findByName(String name);

	List<Safe> findByClient(Client client);
	
	@Query(value = "SELECT * FROM safes t WHERE t.safe_id = :safeId AND t.client_id = :clientId", nativeQuery = true)
	Safe findSafeByIdAndClient(@Param("safeId") Integer safeId, @Param("clientId") Integer clientId);
	
	@Query(value = "SELECT * FROM safes t WHERE t.safe_name = :name AND t.client_id = :clientId", nativeQuery = true)
	Safe findSafeByNameAndClient(@Param("name") String name, @Param("clientId") Integer clientId);

}
