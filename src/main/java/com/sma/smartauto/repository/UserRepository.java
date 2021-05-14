package com.sma.smartauto.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sma.smartauto.domain.Client;
import com.sma.smartauto.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);
    
    Optional<User> findByStaffCardCode(String staffCardCode);
    
    List<User> findByClient(Client client);

    @Query(value = "SELECT * FROM users t WHERE t.location_id = :locationId AND t.client_id = :clientId", nativeQuery = true)
    List<User> findSafeByClientAndLocation(@Param("locationId") Integer locationId, @Param("clientId") Integer clientId);
}
