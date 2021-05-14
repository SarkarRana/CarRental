package com.sma.smartauto.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sma.smartauto.domain.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {
	
	Optional<Location> findByName(String name);
	
	Location findByLocId(Integer id);

}
