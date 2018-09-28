package com.webapp.tgo.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.webapp.tgo.entities.Guide;
import com.webapp.tgo.entities.Tour;
import com.webapp.tgo.entities.Tour_Guide_Xref;
import com.webapp.tgo.repository.ContractRepository;
import com.webapp.tgo.repository.TourRepository;
import com.webapp.tgo.repository.Tour_Guide_Xref_Repository;
import com.webapp.tgo.util.Constant;

@Service
public class Tour_Guide_Xref_Impl implements Tour_Guide_Xref_Service{
	
	Logger log = LoggerFactory.getLogger(Tour_Guide_Xref_Impl.class);
	
	@Autowired
	private Tour_Guide_Xref_Repository tour_Guide_Repository;
	
	@Autowired
	private TourRepository tourRepository;
	
	@Autowired
	private ContractRepository contractRepository;
	
//	@Override
//	public Iterable<Tour_Guide_Xref> findAll() {
//		// TODO Auto-generated method stub
//		return tour_Guide_Repository.findAll();
//	}

//	@Override
//	public List<Tour_Guide_Xref> search(String q) {
//		// TODO Auto-generated method stub
//		return null;
//	}

//	@Override
//	public Tour_Guide_Xref findOne(int id) {
//		// TODO Auto-generated method stub
//		return tour_Guide_Repository.findOne(id);
//	}

//	@Override
//	public void save(Tour_Guide_Xref tourguidexref) {
//		tour_Guide_Repository.save(tourguidexref);
//		
//	}

	/**
	 * @description: delete row tgx table with tgx id, this method can use for operator reject guide or guide reject tour
	 * 
	 */
	@Override
	@Transactional
	//delete row tgx table with tgx id, this method can use for operator reject guide or guide reject tour
	public String deleteGuideInTour(int tgxid) {
		try {
			//update currentamount-1
			Tour_Guide_Xref tgx = tour_Guide_Repository.findOne(tgxid);
			tgx.getTour().setCurrentAmount(tgx.getTour().getCurrentAmount()-1);
			//delete trong bang contract neu status của bảng tgx =2
			contractRepository.deleteGuideInContract(tgxid);
			//delete trong bang tgx vs tgxid
			tour_Guide_Repository.deleteTGXbyTgxId(tgxid);
			log.info("-----------------xóa thành công!!");
			return Constant.MESS_SUCCESS;
			}catch (Exception e) {
			log.error("----------------delete tgx is error",e);
			return Constant.MESS_FAIL;
	}
	}

	@Override
	@Transactional
	//create new row in tgx table with status=0
	public String guideSendRequest(Guide guide, int tourId) {
		try {
		Tour tour = tourRepository.findOne(tourId);
		Tour_Guide_Xref tgx = new Tour_Guide_Xref();
		tgx.setGuide(guide);
		tgx.setTour(tour);
		tgx.setCreatedDate(new Date());
		tgx.setStatus(Constant.VALUE_CREATE_TOUR);
		log.info("tour id: "+tourId);
		tour_Guide_Repository.save(tgx);
		return Constant.MESS_GUIDE_SEND_REQUEST_SUCCESS; //Constant.MESS_GUIDE_SEND_REQUEST_SUCCESS
		}catch (Exception e) {
			log.error("----------send request is fail!!",e);
			return Constant.MESS_GUIDE_SEND_REQUEST_FAIL; //Constant.MESS_GUIDE_SEND_REQUEST_FAIL
		}
	}
	
//	@Override
//	public int recommendByLocation(int tourid, int locationid,int languageid) {
//		String query="insert into tour_guide_xref (tourid,guideid,status) "
//				+ " select "+ tourid+",glo.guideid,3 from guide_location_xref glo,guide_language_xref glang where  glo.guideid=glang.guideid and glo.locationid="+locationid+" and glang.languageid="+languageid+
//				" and glo.guideid not in ( select guideid from tour_guide_xref where tourid="+tourid+")"+";";
//		try {
//			
//			stm.executeUpdate(query);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return 0; 
//	}

//	@Override
//	public void operatorRequest(int tourid, int guideid) {
//		String query = "insert into tour_guide_xref(tourid,guideid,status) values ("+tourid+","+guideid+",2);";
//		
//		try {
//			stm.executeUpdate(query);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

}
