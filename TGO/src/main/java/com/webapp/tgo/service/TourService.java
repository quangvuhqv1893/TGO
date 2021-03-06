package com.webapp.tgo.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.webapp.tgo.entities.Guide;
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
    boolean acceptRequest(int tourGuidexrefId);
    
    List<Tour> findByTourName(String tourName);
//    List<Tour> findTour(Guide guide,String tourName,String operatorName,String location, String day);
    Page<Tour> findTour(int guideId, String tourName,String operatorName,String location, String day, Pageable pageable);

	List<Tour> TourRecomend(Guide guide);

	List<Tour> findTourNeedCheck();

	String approveTour(int tourid);

	String cancelApproveTour(int tourid);
    
}
