package com.webapp.tgo.service;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.webapp.tgo.entities.Guide;
import com.webapp.tgo.entities.Location;
import com.webapp.tgo.entities.Tour;

public interface TourService {
	Page<Tour> findAll(Pageable pageable);

    List<Tour> search(String q);

    Tour findOne(int id);

    boolean save(Tour tour);
    boolean delete(int id);
    boolean cancelTour(int id);
    String runTour(int id);
    boolean finishTour(int id);
    String acceptRequest(int tourGuidexrefId);
    
    List<Tour> findByTourName(String tourName);
//    List<Tour> findTour(Guide guide,String tourName,String operatorName,String location, String day);
    Page<Tour> findTour(int guideId, String tourName,String operatorName,String location, String day, Pageable pageable);

	List<Tour> TourRecomend(Guide guide);

	List<Tour> findTourNeedCheck();

	String approveTour(int tourid);

	String cancelApproveTour(int tourid);

	Page<Tour> manageTour(String idTour, String tourName, String startDate, String status, String tourPrice,
			Pageable pageRequest);

	boolean updateTour(int tourid,String tourname, String language, String endDate, String startDate, String tourprice,
			Set<Location> locations, String requirement, String amount);

	List<Tour> getTourofGuide(int guideId);

	Page<Tour> manageTourinOpearator(int operatorId, String idTour, String tourName, String startDate, String status, String tourPrice,
			Pageable pageRequest);

}
