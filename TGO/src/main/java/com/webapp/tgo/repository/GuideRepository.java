package com.webapp.tgo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.webapp.tgo.entities.Guide;
import com.webapp.tgo.util.Constant;

public interface GuideRepository extends CrudRepository<Guide, Integer> {

	
	@Query(value= Constant.QUERY_FIND_GUIDE_NEED_CHECK)
	List<Guide> findGuideNeedCheck(@Param("status") int status);

	Page<Guide> findAll(Pageable pageable);

	Guide findByUserId(int id);

	Guide findOne(int id);

//	 @Query(value = Constant.QUERRY_FIND_GUIDE)
//	 List<Guide> findGuide(@Param("locationName") String locationName, @Param("gender") String gender,
//				@Param("type") String type, @Param("language") String language);
	@Query(Constant.QUERRY_FIND_GUIDE)
	Page<Guide> findGuide(@Param("locationName") String locationName, @Param("gender") String gender,
			@Param("type") String type, @Param("language") String language, Pageable pageable);
	
	@Query(value = Constant.QUERRY_FIND_GUIDE_BY_USERNAME)
	Guide findbyUserName(@Param("username")String username);
	
	@Query(value = Constant.QUERRY_MANAGE_GUIDE)
	Page<Guide> manageGuide(@Param("guideId") String guideId,@Param("fullName") String fullName,@Param("email") String email,@Param("location") String location,@Param("language") String language,
			Pageable pageRequest);

}
