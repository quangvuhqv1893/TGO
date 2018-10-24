package com.webapp.tgo.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.webapp.tgo.entities.Location;
import com.webapp.tgo.entities.Operator;
import com.webapp.tgo.entities.Tour;
import com.webapp.tgo.repository.LanguageRepository;
import com.webapp.tgo.repository.LocationRepository;
import com.webapp.tgo.repository.OperatorRepository;
import com.webapp.tgo.repository.TourRepository;
import com.webapp.tgo.util.Constant;

@Service
public class OperatorServiceImpl implements OperatorService{
	Logger log = LoggerFactory.getLogger(OperatorServiceImpl.class);
	@Autowired
	private OperatorRepository operatorRepository;
	
	@Autowired
	private LanguageRepository languageRepository;
	
	@Autowired
	private LocationRepository locationRepository;
	
	@Autowired
	private TourRepository tourRepository;

	@Override
	public Iterable<Operator> findAll() {
		return operatorRepository.findAll();
	
	}

	@Override
	public List<Operator> search(String q) {
		return null;
	}

	@Override
	public Operator findOne(int id) {
		return operatorRepository.findOne(id);
	}

	@Override
	@Transactional
	public boolean save(Operator operator) {
		try {
			operatorRepository.save(operator);
			log.info("------------save is sucess!!");
			return true;
		} catch (Exception e) {
			log.error("---------------save is fails!!!!!!!!");
			return false;
		}
		
		
	}

	@Override
	@Transactional
	// delete operator in admin page
	public boolean delete(int id) {
		try {
		operatorRepository.delete(id);
		log.info("----------------------delete success!!!!!");
		return true;
		}catch (Exception e) {
			log.error("-------------delete fail!!!!!!");
			return false;
		}
		
	}

	@Override
	public Operator findByUserId(int id) {
		log.info("Id of operator: "+id);
		return operatorRepository.findByUserId(id);
	}

	@Override
	public Page<Operator> findAll(Pageable pageable) {
		log.info("size of list operator: "+operatorRepository.findAll(pageable).getSize());
		return operatorRepository.findAll(pageable);
	}

	@Override
	@Transactional
	public String changeInfo(Operator operator) {
		try {
		log.info("----start change infoService");
		Operator operatorNew = operatorRepository.findOne(operator.getId());
//		operatorNew.setCompanyNameViet(operator.getCompanyNameViet());
		operatorNew.getUser().setAddress(operator.getUser().getAddress());
		log.info("+++++++++++++address: "+operatorNew.getUser().getAddress());
		operatorNew.getUser().setEmail(operator.getUser().getEmail());
		log.info("+++++++++++++email: "+operatorNew.getUser().getEmail());
		operatorNew.getUser().setPhonenumber(operator.getUser().getPhonenumber());
		log.info("+++++++++++++phone: "+operatorNew.getUser().getPhonenumber());
		//add new info of operator
		operatorNew.setRepresentative(operator.getRepresentative());
		log.info("+++++++++++++representative: "+operatorNew.getRepresentative());
		operatorRepository.save(operatorNew);
		log.info("----end change infoService");
		return Constant.MESS_SUCCESS_UPDATE_INFO;
		}catch (Exception e) {
			log.error("error in changeInfo service: ",e);
			return Constant.MESS_FAIL_UPDATE_INFO;
		}
	}

//	@Override
//	public List<Guide> findGuide(String locationName, String gender, String type, String language) {
//		return guideRepository.findGuide(locationName, gender, type, language);
//		

//	}

	@Override
	public Operator findByUserName(String username) {
		return operatorRepository.findByUserName(username);
		
	}
	
//	@Override
//	public void postTour(Operator operator, String tourname, String language, String tourTime, String startDate,
//			String tourprices, String location) {
//		log.info("----start method post tour-----");
////		SimpleDateFormat fomatter = new SimpleDateFormat("dd/MM/yyyy");
//		Date stDate = new Date();
//		Tour tour = new Tour();
//		Set<Location> locationList = new HashSet<>();
//		int tourprice = (tourprices==null||"".equalsIgnoreCase(tourprices))? 0 : Integer.parseInt(tourprices);
//		try {
//			stDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);	
//		} catch (Exception e) {
//			log.error("error in Date method: ",e);
//		}
//
//			locationList.add(locationRepository.findByLocationName(location));
//			log.info("location: "+location);
//			log.info("startdate: "+ stDate);
//		tour.setCreatedDate(new Date());
//		tour.setLocations(locationList);
//		tour.setOperator(operator);
//		tour.setStatus(Constant.VALUE_CREATE_TOUR);
//		tour.setStartDate(stDate);
//		tour.setTourName(tourname);
//		tour.setTourPrice(tourprice);
//		tour.setTourTime(tourTime);
//		tourRepository.save(tour);
//		log.info("----end method post tour-----");
//	}

	@Override
	@Transactional
	public boolean postTour(Operator operator, String tourname, String language, String endDate, String startDate,
			String tourprices, List<String> locations, String requirement,String amount) {
		try {
		log.info("----start method post tour-----");
		int tourprice = (tourprices==null||"".equalsIgnoreCase(tourprices))? 0 : Integer.parseInt(tourprices);
		int amountvalue = (amount==null||"".equalsIgnoreCase(amount))? 0 : Integer.parseInt(amount);
		Date stDate=new Date(),eDate = new Date();
		try {
			stDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);	
			eDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);	
			log.info("stdate: "+startDate);
		} catch (Exception e) {
			log.error("error in Date method: "+e);
		}
		Set<Location> locationList = new HashSet<Location>();
		for (String location : locations) {
			locationList.add(locationRepository.findByLocationName(location));
			log.info("location: "+location);
		}
		Tour tour = new Tour();
		tour.setLanguage(languageRepository.findByLanguage(language));
		tour.setCreatedDate(new Date());
		tour.setLocations(locationList);
		tour.setOperator(operator);
		tour.setStatus(Constant.VALUE_APPROVAL_TOUR);
		tour.setStartDate(stDate);
		tour.setTourName(tourname);
		tour.setTourPrice(tourprice);
		tour.setAmount(amountvalue);
		tour.setEndDate(eDate);
		tour.setRequirement(requirement);
		tourRepository.save(tour);
		log.info("----end method post tour-----");
		return true;
	}catch (Exception e) {
		log.error("----------------false post tour!!!!!",e);
		return false;
	}
	}

	@Override
	public List<Operator> findOperatorNeedCheck() {
		try {
			return operatorRepository.findOperatorNeedCheck(Constant.VALUE_CREATE_TOUR);
		} catch (Exception e) {
			log.error("error in find operator need check!!",e);
			return null;
			
		}

	}

//	@Override
//	public List<Tour> findTourWaitting(int id) {
//		return tourRepository.findTourWaiting(id);
//	}
	
}
