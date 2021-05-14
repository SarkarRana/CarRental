package com.sma.smartauto.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sma.smartauto.domain.RestClient;

@Repository
public interface RestClientRepository extends JpaRepository<RestClient, Long> {

    Optional<RestClient> findByRestClientName(String restClientName);

}
