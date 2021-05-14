package com.sma.smartauto.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sma.smartauto.domain.Location;
import com.sma.smartauto.repository.LocationRepository;

@Component
public class LocationDaoService {
	
	@Autowired
	LocationRepository locationRepository;
	
	public List<Location> findAll() {		
		return locationRepository.findAll();
	}
	
	public Location save(Location location) {
		Location newLoc = locationRepository.save(location);
		return newLoc;
	}

	public Location findLocationById(int id) {
		Optional<Location> loc = locationRepository.findById(id);
		if(loc.isPresent()) {
			return loc.get();
		}
		return null;
	}
	
	public Location deleteLocationById(int id) {
		Optional<Location> loc = locationRepository.findById(id);
		if(loc.isPresent()) {
			locationRepository.delete(loc.get());
			return loc.get();
		}
		return null;
	}
}
