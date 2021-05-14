package com.sma.smartauto.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sma.smartauto.domain.Asset;
import com.sma.smartauto.domain.Client;
import com.sma.smartauto.domain.Compartment;
import com.sma.smartauto.domain.Safe;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Integer> {

	Optional<Asset> findByName(String name);

	Optional<Asset> findByIdentifier(String identifier);

	List<Asset> findByIdentifierType(String identifierType);
	
	List<Asset> findAllByClient(Client client);
	
	List<Asset> findAllBySafe(Safe safe);
	
	Asset findByCompartment(Compartment compartment);
	
	@Query(value = "SELECT * FROM assets t WHERE t.safe_id = :safeId AND t.identifier_type = :identifierType", nativeQuery = true)
	List<Asset> findAssetBySafeAndIdentifierType(@Param("safeId") Integer safeId, @Param("identifierType") String identifierType);
	
	@Modifying(clearAutomatically = true)
	@Query(value = "Update assets set compartment_id = :compartmentId, safe_id = :safeId, last_updated_by = :lastUpdateBy, last_updated_datetime = GETDATE() WHERE client_id = :clientId AND identifier = :identifier", nativeQuery = true)
	int updateCompartmentData(@Param("clientId") Integer clientId, @Param("safeId") Integer safeId, @Param("compartmentId") Integer compartmentId, @Param("identifier") String identifier, @Param("lastUpdateBy") Integer lastUpdateBy);
}
