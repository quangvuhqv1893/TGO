package com.webapp.tgo.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.webapp.tgo.entities.Contract;
import com.webapp.tgo.util.Constant;

public interface ContractRepository extends CrudRepository<Contract, Integer>{
	Page<Contract> findAll(Pageable pageable);
	@Modifying
	@Query(value=Constant.QUERY_DELETE_CONTRACT_BY_TOURID)
	void deleteContractByTourId(@Param("tourid") int tourId);
	
	@Modifying
	@Query(value = Constant.QUERY_UPDATE_FINISH_DATE_OF_CONTRACT_BY_TOURID)
	void updateFinishDatebyTourId(@Param("tourid") int tourId, @Param("finishday") Date finishday);
	
	@Query(value = Constant.QUERRY_FIND_GUIDE_IN_CONTRACT)
	List<Integer> findGuide(@Param("tourid") int tourid);
	
	@Modifying
	@Query(value=Constant.QUERRY_DELETE_GUIDE_IN_CONTRACT)
	void deleteGuideInContract(@Param("tgxid") int tgxid);
}
