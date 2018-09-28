package com.webapp.tgo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.webapp.tgo.entities.Location;

public interface LocationRepository extends CrudRepository<Location, Integer>{
	Page<Location> findAll(Pageable pageable);
	Location findByLocationName (String name);
}
