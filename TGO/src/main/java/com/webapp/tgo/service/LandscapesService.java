package com.webapp.tgo.service;

import java.util.List;

import com.webapp.tgo.entities.Landscapes;

public interface LandscapesService {
	Iterable<Landscapes> findAll();

    List<Landscapes> search(String q);

    Landscapes findOne(int id);

    void save(Landscapes landscapes);

    void delete(int id);
}
