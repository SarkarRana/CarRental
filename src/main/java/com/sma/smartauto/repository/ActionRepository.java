package com.sma.smartauto.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sma.smartauto.domain.Action;
import com.sma.smartauto.domain.Asset;
import com.sma.smartauto.domain.Client;
import com.sma.smartauto.domain.Code;
import com.sma.smartauto.domain.Compartment;
import com.sma.smartauto.domain.Safe;
import com.sma.smartauto.domain.User;

@Repository
public interface ActionRepository extends JpaRepository<Action, Integer> {

	List<Action> findAllByClient(Client client);
	
	List<Action> findAllBySafe(Safe safe);
	
	List<Action> findAllByCompartment(Compartment compartment);

	List<Action> findAllByAsset(Asset asset);
	
	List<Action> findAllByCode(Code code);

	List<Action> findAllByUser(User user);
	
	@Query(value = "SELECT * FROM actions t WHERE t.client_id = :clientId AND t.action_type_id = :actionTypeId", nativeQuery = true)
	List<Action> findAllByActionType(@Param("clientId") Integer clientId, @Param("actionTypeId") Integer actionTypeId);
	
	@Query(value = "SELECT * FROM actions t WHERE t.client_id = :clientId AND t.safe_id = :safeId AND t.compartment_id = :compartment_id", nativeQuery = true)
	List<Action> findByClientSafeCompartment(@Param("clientId") Integer clientId, @Param("safeId") Integer safeId, @Param("compartment_id") Integer compId);

	@Query(value = "SELECT * FROM actions t WHERE t.client_id = :clientId AND (t.date_time > :date_time_start AND  t.date_time < :date_time_end)", nativeQuery = true)
	List<Action> findAllByDateTime(@Param("clientId") Integer clientId, @Param("date_time_start") Date dateStart, @Param("date_time_end") Date dateEnd);
	
	@Query(value = "SELECT * FROM actions t WHERE t.client_id = :clientId AND t.safe_id = :safeId AND (t.date_time > :date_time_start AND  t.date_time < :date_time_end)", nativeQuery = true)
	List<Action> findByClientSafeDate(@Param("clientId") Integer clientId, @Param("safeId") Integer safeId, @Param("date_time_start") Date dateStart, @Param("date_time_end") Date dateEnd);

}
