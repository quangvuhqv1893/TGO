package com.webapp.tgo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.webapp.tgo.entities.Landscapes;

public interface LandscapesRepositoy extends CrudRepository<Landscapes, Integer>{
	Page<Landscapes> findAll(Pageable pageable);
}
