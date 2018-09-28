package com.webapp.tgo.service;

import com.webapp.tgo.entities.Guide;

public interface Tour_Guide_Xref_Service {
//	Iterable<Tour_Guide_Xref> findAll();
//
//    List<Tour_Guide_Xref> search(String q);
//
//    Tour_Guide_Xref findOne(int id);
////
//    void save(Tour_Guide_Xref tourguidexref);
//
	String guideSendRequest(Guide guide, int tourId);
    String  deleteGuideInTour(int id);
//    
//    int recommendByLocation(int tourid,int locationid,int languageid);
//    void operatorRequest(int tourid,int guideid);
}
