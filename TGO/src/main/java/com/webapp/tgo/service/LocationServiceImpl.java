package com.webapp.tgo.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webapp.tgo.entities.Location;
import com.webapp.tgo.repository.LocationRepository;

@Service
public class LocationServiceImpl implements LocationService{
	Logger log = LoggerFactory.getLogger(LocationServiceImpl.class);
	@Autowired
	private LocationRepository locationRepository;

	/* 
	 * get all location
	 */
	@Override
	public Iterable<Location> findAll() {
		return locationRepository.findAll();
	}

	@Override
	public void save(Location location) {
		locationRepository.save(location);
		
	}

	@Override
	public void delete(int id) {
		locationRepository.delete(id);
		
	}

	@Override
	public Location findByLocationName(String location) {
		log.info("find location: "+locationRepository.findByLocationName(location));
		return locationRepository.findByLocationName(location);
	}

	@Override
	public Location findOne(int id) {
		return locationRepository.findOne(id);
	}
	
}
