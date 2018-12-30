package com.webapp.tgo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import com.webapp.tgo.util.Constant;
import com.webapp.tgo.entities.Guide;
import com.webapp.tgo.entities.Operator;

public interface OperatorRepository extends CrudRepository<Operator, Integer>{
	Page<Operator> findAll(Pageable pageable);
	
	Operator findByUserId(int id);
	
	@Query(value = Constant.QUERRY_FIND_OPERATOR_BY_USERNAME)
	Operator findByUserName(@Param("username") String username);

	@Query(value = Constant.QUERRY_FIND_OPERATOR_NEED_CHECK)
	List<Operator> findOperatorNeedCheck(@Param("status") int status);
	
	@Query(value = Constant.QUERRY_MANAGE_OPERATOR)
	Page<Operator> manageOperator(@Param("operatorId") String operatorId,@Param("fullName") String fullName,@Param("username") String username,
			@Param("busRegCode") String busRegCode,@Param("taxcode") String taxcode,Pageable pageRequest);
}
