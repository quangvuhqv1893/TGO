package com.webapp.tgo.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.webapp.tgo.entities.Contract;
import com.webapp.tgo.entities.Guide;
import com.webapp.tgo.entities.Tour;
import com.webapp.tgo.entities.Tour_Guide_Xref;
import com.webapp.tgo.repository.ContractRepository;
import com.webapp.tgo.repository.TourRepository;
import com.webapp.tgo.repository.Tour_Guide_Xref_Repository;
import com.webapp.tgo.util.Constant;

@Service
public class TourServiceImpl implements TourService {
	Logger log = LoggerFactory.getLogger(TourServiceImpl.class);
	@Autowired
	private TourRepository tourRepository;
	
	@Autowired
	private Tour_Guide_Xref_Repository tgxRepository;
	
	@Autowired
	private ContractRepository contractRepository;

	@Override
	public Page<Tour> findAll(Pageable pageable) {
		return tourRepository.findAll(pageable);
	}

	@Override
	public List<Tour> search(String q) {
		return null;
	}

	@Override
	public Tour findOne(int id) {
		return tourRepository.findOne(id);
	}

	@Override
	@Transactional
	public boolean save(Tour tour) {
		try {
			tourRepository.save(tour);
			return true;
		} catch (Exception e) {
			log.error("----------------false to save tour!!! ",e);
			return false;
		}

	}

	@Override
	public boolean delete(int id) {

//		try {
//			String query = "delete from tour_guide_xref where tourid=" + id + ";";
//			Tour_Guide_Xref_Impl.stm.executeUpdate(query);
//			query = "delete from tour_location_xref where tourid=" + id + ";";
//			Tour_Guide_Xref_Impl.stm.executeUpdate(query);
//			query = "delete from operator_tour_xref where tourid=" + id + ";";
//			Tour_Guide_Xref_Impl.stm.executeUpdate(query);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//
//		tourRepository.delete(id);
		return true;
	}

	@Override
	public List<Tour> findByTourName(String tourName) {
		// TODO Auto-generated method stub
		return tourRepository.findByTourName(tourName);
	}

	@Override
	@Transactional
	public boolean cancelTour(int id) {
		//update status in tour is '-1', delete all row of contract table and tour_guide_xref with idTour
		try {
			log.info("----------------start service to cancel tour!!!");
			tourRepository.updateTourByStatusAndTourID(id, Constant.VALUE_DELETE_TOUR, new Date());
			tgxRepository.deleteTGXbyTourId(id);
			contractRepository.deleteContractByTourId(id);
			return true;
		} catch (Exception e) {
			log.error("----------------false to cancel tour!!! ",e);
			return false;
		}
		
	}

	@Override
	@Transactional
	public String runTour(int tourid) {
		// update status of Tour table = 2 and delete all row of tour_guide_xref in tourId = id
		try {
			log.info("----------------start service to run tour!!!");
			//check tour have guide or not? run tour : return false
			List<Integer> listguide = contractRepository.findGuide(tourid);
			log.info("-----------------------list guide: "+listguide.size());
			if(listguide!=null && (listguide.size() > 0)) {
			tourRepository.updateTourByStatusAndTourID(tourid, Constant.VALUE_RUN_TOUR,new Date());
			tgxRepository.deleteTGXbyTourId(tourid);
			return Constant.MESS_SUCCESS;
			}return Constant.MESS_GUIDE_NOT_EXIST;
		} catch (Exception e) {
			log.error("----------------false to run tour!!!",e);
			return Constant.MESS_FAIL;
		}
		
	}

	@Override
	@Transactional
	public boolean finishTour(int id) {
		// update status of Tour table = 1 and update finish date of table contract
		try {
			log.info("----------------start service to finish tour!!!");
			tourRepository.updateTourByStatusAndTourID(id, Constant.VALUE_FINISH_TOUR, new Date());
			contractRepository.updateFinishDatebyTourId(id, new Date());
			return true;
		} catch (Exception e) {
			log.error("----------------false to finish tour!!!",e);
			return false;
		}
		
	}

	@Override
	@Transactional
	public String acceptRequest(int tourGuidexrefId) {
		// insert new row in contract table, update status of tgx is 2
		try {
			log.info("----------------start service to accept request!!!");
			Tour_Guide_Xref tXref = tgxRepository.findOne(tourGuidexrefId);
			//check amount of tour before add tour
			int currentAmount = tXref.getTour().getCurrentAmount();
			if(currentAmount>=tXref.getTour().getAmount()) {
				return Constant.MESS_ERROR_AMONUNT;
			}
			tXref.setStatus(Constant.VALUE_ACCEPT_REQUEST);
			tXref.setUpdatedDate(new Date());
			tXref.getTour().setCurrentAmount(currentAmount+1);
			tgxRepository.save(tXref);
			Contract contract = new Contract();
			contract.setCreatedDate(new Date());
			contract.setDepartureDay(tXref.getTour().getStartDate());
			contract.setGuide(tXref.getGuide());
			contract.setOperator(tXref.getTour().getOperator());
			contract.setTour(tXref.getTour());
			contract.setStatus(Constant.VALUE_CREATE_CONTRACT);
			contract.setSalary(tXref.getTour().getTourPrice());
			contractRepository.save(contract);
		return Constant.MESS_SUCCESS;
		}catch (Exception e) {
			log.error("----------------false to accept request!!!",e);
			return Constant.MESS_FAIL;
		}
		
	}

	@Override
	//find tour with condition is param input and start date not exsist
	public Page<Tour> findTour(int guideId, String tourName, String operatorName, String location, String day, Pageable pageable) {
		try {
		return tourRepository.findTour(guideId, location, tourName, operatorName, day, pageable);
	}catch (Exception e) {
		log.error("-----------error in find tour method!!!!!!!!!",e);
		return null;
	}
	}

	@Override
	// get all tour if status = 0, location mapping with location of guide and not start in date guide have tour is running
	public List<Tour> TourRecomend(Guide guide) {
		return  tourRepository.TourRecomend(guide.getId());
	}

	@Override
	public List<Tour> findTourNeedCheck() {
		List<Tour>  list = tourRepository.findTourNeedCheck();
		log.info("----list tour:",list);
		return list;
	}

	@Override
	@Transactional
	public String approveTour(int tourid) {
		try {
		tourRepository.updateTourByStatusAndTourID(tourid, Constant.VALUE_CREATE_TOUR, new Date());
		return Constant.MESS_SUCCESS;
		}catch (Exception e) {
			return Constant.MESS_FAIL;
		}
	}

	@Override
	@Transactional
	public String cancelApproveTour(int tourid) {
		try {
			tourRepository.cancelApproveTour(tourid);
			return Constant.MESS_SUCCESS;
		} catch (Exception e) {
			log.error("fail cancel approve tour!",e);
			return Constant.MESS_FAIL;
		}
	}

}
