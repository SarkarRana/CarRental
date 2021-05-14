package com.sma.smartauto.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sma.smartauto.domain.Compartment;
import com.sma.smartauto.domain.Safe;

@Repository
public interface CompartmentRepository extends JpaRepository<Compartment, Integer> {
	
	Optional<Compartment> findByName(String name);
	
	Optional<Compartment> findByCompartmentId(Integer compartmentId);
	
	List<Compartment> findBySafe(Safe safe);
	
	@Query(value = "SELECT * FROM compartment t WHERE t.safe_id = :safeId AND t.compartment_name = :compartment_name", nativeQuery = true)
	Compartment findCompartmentBySafeAndName(@Param("safeId") Integer safeId, @Param("compartment_name") String compartment_name);

}
