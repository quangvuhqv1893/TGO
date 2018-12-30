package com.webapp.tgo.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.webapp.tgo.entities.Guide;
import com.webapp.tgo.repository.GuideRepository;
import com.webapp.tgo.util.Constant;


@Service
public class GuideServiceImpl implements GuideService{
	Logger log = LoggerFactory.getLogger(GuideServiceImpl.class);
	
	@Autowired
	private GuideRepository guideRepository;
	
	@Override
	public Page<Guide> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return guideRepository.findAll(pageable);
	}

	@Override
	public List<Guide> search(String q) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Guide findOne(int id) {
		// TODO Auto-generated method stub
		return guideRepository.findOne(id);
	}

	@Override
	@Transactional
	public String saveGuide(Guide guide) {
		try {
		guideRepository.save(guide);
		return Constant.MESS_SUCCESS_UPDATE_INFO;
		}catch (Exception e) {
			log.error("-----fails to save guide!!!!!");
			return Constant.MESS_FAIL_UPDATE_INFO;
		}
		
	}

	@Override
	public void delete(int id) {
		guideRepository.delete(id);
		
	}



	@Override
	public Guide findByUserId(int id) {
		// TODO Auto-generated method stub
		return guideRepository.findByUserId(id);
	}

	@Override
	public Iterable<Guide> findAll() {
		// TODO Auto-generated method stub
		return guideRepository.findAll();
	}

	@Override
	@Transactional(readOnly=true)
	public Page<Guide> findGuide(String locationName, String gender, String type, String language,Pageable pageable) {
		Page<Guide> listGuide = guideRepository.findGuide(locationName, gender, type, language, pageable);
		return listGuide;
	}

	@Override
	public Guide findbyUserName(String username) {
		return guideRepository.findbyUserName(username);

	}

	@Override
	public List<Guide> findGuideNeedCheck() {
		try {
			return guideRepository.findGuideNeedCheck(Constant.VALUE_CREATE_TOUR);
		} catch (Exception e) {
			log.error("error in find guide need check!!!",e);
			return null;
		}

	}

	@Override
	public Page<Guide> manageGuide(String guideId, String fullName, String email, String location, String language,
			Pageable pageRequest) {
		
		return guideRepository.manageGuide(guideId, fullName, email, location, language, pageRequest);
	}

//	@Override
//	public String guideSendRequest(Guide guide, int tourId) {
//		try {
//			Tour tour = tourRepository.findOne(tourId);
//			
//		}catch (Exception e) {
//			
//		}
//		return null;
//	}
	
//	public List<Guide> findGuide(String guidename,Tour tour,String location,boolean isMale,boolean isFeMale,boolean isNoExpGuide,boolean isExpGuide){
//		List<Guide> guides = new LinkedList<>();
//		try {
//			String gender,exp,name;
//			if(guidename==null||guidename.equals("")) {
//				name="";
//				
//			}
//			else {
//				name=" and fullname like '%"+guidename+"%'";
//			}
//			if(isMale) {
//				gender=" and gender='male'";
//			}
//			else if(isFeMale) {
//				gender=" and gender='female'";
//			}
//			else {
//				gender ="";
//			}
//			if(isExpGuide) {
//				exp=" and  cardnumber is not null";
//			}
//			else if(isNoExpGuide){
//				exp=" and  cardnumber is null";
//			}
//			else {
//				exp="";
//			}
//			String query ="select guide.id as gid from user,guide,guide_location_xref gl,location where user.id=guide.userid and guide.id=gl.guideid and location.id=gl.locationid and location.LocationName='"+location+"'"
//					+gender+exp+name+";";
//			System.out.println(query);
//			ResultSet rs=Tour_Guide_Xref_Impl.stm.executeQuery(query);
//			
//			while(rs.next()) {
//				guides.add(guideRepository.findOne(rs.getInt("gid")));
//			}
//			for(int i=0;i<guides.size();i++) {
//				
//				for(int j=0;j<tour.getTourGuideXref().size();j++) {
//					if(guides.get(i).getId()==tour.getTourGuideXref().get(j).getGuide().getId()) {
//						guides.remove(i);
//						i--;
//						break;
//					}
//				}
//				
//			}
//			
//			
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return guides;
//	}
	
	

}
