package com.sma.smartauto.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sma.smartauto.domain.Client;
import com.sma.smartauto.domain.Code;
import com.sma.smartauto.domain.Safe;

@Repository
public interface CodeRepository extends JpaRepository<Code, Integer> {
	List<Code> findCodeByClient(Client client);
	
	List<Code> findCodeBySafe(Safe safe);
	
	Optional<Code> findByCode(String code);

	Optional<Code> findByIdentifier(String identifier);

	List<Code> findByIdentifierType(String identifierType);
	
	@Query(value = "SELECT * FROM code t WHERE t.safe_id = :safeId AND t.id > :id AND (t.identifier_type IS NOT NULL OR t.identifier IS NOT NULL) AND t.active=1 AND t.data_complete=1", nativeQuery = true)
	List<Code> getAllActiveCodesForSafe(@Param("safeId") Integer safeId, @Param("id") Integer id);
	
	@Query(value = "SELECT * FROM code t WHERE t.client_id = :clientId AND t.location_id = :locId AND t.id > :id AND (t.identifier_type IS NOT NULL OR t.identifier IS NOT NULL) AND t.active=1 AND t.data_complete=1", nativeQuery = true)
	List<Code> getAllActiveCodesForLocation(@Param("locId") Integer locId, @Param("id") Integer id, @Param("clientId") Integer clientId);
	
	@Query(value = "SELECT * FROM code t WHERE t.identifier = :identifier AND (t.date_time_from <=  GETDATE() AND t.date_time_to >=  GETDATE()) AND t.active=1 AND t.data_complete=1", nativeQuery = true)
	Code getActiveCodesByIdentifier(@Param("identifier") String identifier);
	
	@Query(value = "SELECT * FROM code t WHERE t.identifier = :identifier AND (t.date_time_from <=  GETDATE() AND t.date_time_to >=  GETDATE()) AND t.active=1", nativeQuery = true)
	Code getActiveAndUnassignedCodesByIdentifier(@Param("identifier") String identifier);
}
