package com.webapp.tgo.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.webapp.tgo.entities.Tour_Guide_Xref;
import com.webapp.tgo.util.Constant;

public interface Tour_Guide_Xref_Repository extends CrudRepository<Tour_Guide_Xref, Integer>{
	@Modifying
	@Query(value=Constant.DELETE_TGX_BY_TOURID)
	void deleteTGXbyTourId(@Param("tourid") int tourId);
	
	@Modifying
	@Query(value=Constant.DELETE_TGX_BY_TGXID)
	void deleteTGXbyTgxId(@Param("tgxid") int tgxId);
	
}
