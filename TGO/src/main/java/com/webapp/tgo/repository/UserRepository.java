package com.webapp.tgo.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.webapp.tgo.entities.User;
import com.webapp.tgo.util.Constant;

public interface UserRepository extends CrudRepository<User, Integer> {
	
//	Page<User> findAll(Pageable pageable);
	
    User findByUsername(String username);

    @Modifying
    @Query(value=Constant.QUERY_CHANGE_STATUS_USER)
	void changeApproveUser(@Param("userid") int userid,@Param("status")  int status, @Param("role")  int role);
}
