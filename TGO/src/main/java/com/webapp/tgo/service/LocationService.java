package com.webapp.tgo.service;

import com.webapp.tgo.entities.Location;

public interface LocationService {

	Iterable<Location> findAll();

    void save(Location location);

    void delete(int id);

	Location findByLocationName(String location);

	Location findOne(int id);
}
