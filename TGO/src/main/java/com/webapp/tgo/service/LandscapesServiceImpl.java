package com.webapp.tgo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webapp.tgo.entities.Landscapes;
import com.webapp.tgo.repository.LandscapesRepositoy;

@Service
public class LandscapesServiceImpl implements LandscapesService{
	@Autowired
	private LandscapesRepositoy landscapesRepositoy;

	@Override
	public Iterable<Landscapes> findAll() {
		// TODO Auto-generated method stub
		return landscapesRepositoy.findAll();
	}

	@Override
	public List<Landscapes> search(String q) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Landscapes findOne(int id) {
		// TODO Auto-generated method stub
		return landscapesRepositoy.findOne(id);
	}

	@Override
	public void save(Landscapes landscapes) {
		// TODO Auto-generated method stub
		landscapesRepositoy.save(landscapes);
	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		landscapesRepositoy.delete(id);
	}


	
	
}
