package com.webapp.tgo.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.webapp.tgo.entities.Operator;

public interface OperatorService {
	String changeInfo(Operator operator);
	
	Iterable<Operator> findAll();
	
	Page<Operator> findAll(Pageable pageable);
	
	List<Operator> search(String q);

	Operator findOne(int id);

	Operator findByUserId(int id);
	
	Operator findByUserName(String username);

	boolean save(Operator operator);

	boolean delete(int id);

	boolean postTour(Operator operator, String tourname, String language, String tourTime, String startDate,
			String tourprice, List<String> locations, String requirement,String amount);
//	void postTour(Operator operator, String tourname, String language, String tourTime, String startDate,
//			String tourprice, String location);

	Object findOperatorNeedCheck();

//	List<Tour> findTourWaitting(int id);
	
//	void postTour(String tourName, String );
//	List<Guide> findGuide(String locationName, String gender, String type, String language);
	
	
}
