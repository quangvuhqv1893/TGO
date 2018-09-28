package com.webapp.tgo.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.webapp.tgo.entities.Tour;
import com.webapp.tgo.util.Constant;

public interface TourRepository extends CrudRepository<Tour, Integer> {
	Page<Tour> findAll(Pageable pageable);

	List<Tour> findByTourName(String tourname);

	@Modifying
	@Query(value = Constant.QUERY_UPDATE_TOUR_BY_STATUS_AND_TOURID)
	void updateTourByStatusAndTourID(@Param("tourid") int tourid, @Param("status") int status,
			@Param("updatedDate") Date updatedDate);

	@Query(value = Constant.QUERY_FIND_TOUR)
	Page<Tour> findTour(@Param("guideid") int guideId, @Param("location") String location,
			@Param("tourname") String tourName, @Param("operatorname") String operatorName,
			@Param("startdate") String startDate, Pageable pageable);
	
	@Query(value = Constant.QUERY_RECOMEND_TOUR)
	List<Tour> TourRecomend(@Param("guideid") int guideId);

	@Query(value=Constant.QUERY_FIND_TOUR_NEED_CHECK)
	List<Tour> findTourNeedCheck();

	
	@Modifying
	@Query(value = Constant.QUERY_CANCEL_APPROVE_TOUR)
	void cancelApproveTour(@Param("tourid") int tourid);

//	@Query(value = Constant.QUERY_FIND_TOUR_WAITING)
//	List<Tour> findTourWaiting(int id);

}
