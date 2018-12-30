package com.webapp.tgo.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.webapp.tgo.entities.Guide;

public interface GuideService {
	Page<Guide> findAll(Pageable pageable);
	Iterable<Guide> findAll();
	
    List<Guide> search(String q);

    Guide findOne(int id);

    String saveGuide(Guide guide);

    void delete(int id);
    Guide findByUserId(int  id);
    Page<Guide> findGuide(String locationName, String gender, String type, String language,Pageable pageable);
//    List<Guide> findGuide(String guideName,Tour tour,String location,boolean isMale,boolean isFeMale,boolean isNoExpGuide,boolean isExpGuide);
    Guide findbyUserName(String username);
	List<Guide> findGuideNeedCheck();
	Page<Guide> manageGuide(String guideId, String fullName, String email, String location, String language,
			Pageable pageRequest);

    
}
