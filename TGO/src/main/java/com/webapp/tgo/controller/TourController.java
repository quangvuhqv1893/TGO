	package com.webapp.tgo.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.webapp.tgo.entities.Language;
import com.webapp.tgo.entities.Location;
import com.webapp.tgo.entities.Operator;
import com.webapp.tgo.entities.Tour;
import com.webapp.tgo.service.LanguageService;
import com.webapp.tgo.service.LocationService;
import com.webapp.tgo.service.TourService;
import com.webapp.tgo.service.Tour_Guide_Xref_Service;
import com.webapp.tgo.service.UserDetailService;
import com.webapp.tgo.util.Constant;

@Controller
public class TourController {
	Logger log = LoggerFactory.getLogger(TourController.class);
	@Autowired
	private TourService tourService;
	@Autowired
	private Tour_Guide_Xref_Service tour_Guide_Xref_Service;
	@Autowired
	private LocationService locationService;
	@Autowired
	private LanguageService languageService;
	@Autowired
	private UserDetailService userDetailService;

	/*
	 * @description: operator delete tour waiting or cancel tour is running
	 * @param tourid
	 * @param request
	 * @param model
	 * @return
	 */
	@ResponseBody
	@PostMapping("/tour/delete/{tourid}")
	public String deleteTour(@PathVariable("tourid") int tourid, HttpServletRequest request, Model model) {
		try {
			if (request.isUserInRole(Constant.ROLE_OPERATOR)) {
				return tourService.cancelTour(tourid)? Constant.VIEW_REDIRECT_OPERATOR : Constant.VIEW_ERROR;
			} else
				return Constant.VIEW_403;
		} catch (Exception e) {
			log.error("redirect to operator page error: " + e);
			model.addAttribute(Constant.MESS_EXCEPTION, e);
			return Constant.VIEW_ERROR;
		}
	}

	/**
	 * @description: operator run tour
	 * @param tourid
	 * @param request
	 * @param model
	 * @return
	 */
	@ResponseBody
	@PostMapping("tour/run/{tourid}")
	public String runTour(@PathVariable("tourid") int tourid, HttpServletRequest request) {
		try {
			if (request.isUserInRole(Constant.ROLE_OPERATOR)) {
				//runtour
				return tourService.runTour(tourid);
			} else
				return Constant.VIEW_403;
		} catch (Exception e) {
			log.error("redirect to operator page error: " , e);
			return Constant.MESS_FAIL;
		}
	}

	/**
	 * @description: operator finish tour
	 * @param tourid
	 * @param request
	 * @param model
	 * @return
	 */
	@PostMapping("tour/finish/{tourid}")
	public String finishTour(@PathVariable("tourid") int tourid, HttpServletRequest request, Model model) {
		try {
			if (request.isUserInRole(Constant.ROLE_OPERATOR)) {
				if (tourService.finishTour(tourid)) {
					log.info("---------------- finish tour is completed!!!");
				} else {
					log.error("----------------can't finish tour!!!");
					model.addAttribute(Constant.MESS_EXCEPTION, Constant.MESS_SET_FINISH_FAILS);
					return Constant.VIEW_ERROR;
				}
				return Constant.VIEW_REDIRECT_OPERATOR_RUNNING_TOUR;
			} else
				return Constant.VIEW_403;
		} catch (Exception e) {
			log.error("redirect to operator page error: " + e);
			model.addAttribute(Constant.MESS_EXCEPTION, e);
			return Constant.VIEW_ERROR;
		}
	}

	/**
	 * @description: guide accept tour or operator accept guide
	 * @param tourxrefid
	 * @param request
	 * @param model
	 * @return view
	 */
	@ResponseBody
	@GetMapping("/tour/accept/{tourxrefid}")
	public String accept(@PathVariable("tourxrefid") int tourxrefid, HttpServletRequest request, Model model) {
		try {
			if(request.isUserInRole(Constant.ROLE_OPERATOR)||request.isUserInRole(Constant.ROLE_GUIDE)) {
			return tourService.acceptRequest(tourxrefid);
			} else {
				return Constant.VIEW_403;
			}
		} catch (Exception e) {
			log.error("redirect to operator page error: " + e);
			model.addAttribute(Constant.MESS_EXCEPTION, e);
			return Constant.VIEW_ERROR;
		}
	}

	/**
	 * @description: operator delete guide of tour or guide delete tour invited them.
	 * @param tourxrefid
	 * @param request
	 * @param model
	 * @return
	 */
	@ResponseBody
	@GetMapping("/tour/cancelGuide")
	public String cancelGuide(@Param("tourxrefid") int tourxrefid, HttpServletRequest request, Model model) {
		try {
			if(request.isUserInRole(Constant.ROLE_OPERATOR)|| request.isUserInRole(Constant.ROLE_GUIDE)||request.isUserInRole(Constant.ROLE_ADMIN)) {
			 return tour_Guide_Xref_Service.deleteGuideInTour(tourxrefid);
			} else
				return Constant.VIEW_403;
		} catch (Exception e) {
			log.error("redirect to operator page error: " + e);
			model.addAttribute(Constant.MESS_EXCEPTION, e);
			return Constant.VIEW_ERROR;
		}
	}

//	@PostMapping("/tour/guiderequest/{id}")
//	public String guideSendRequest(@PathVariable("id") int id, HttpServletRequest request, Model model) {
//		if (request.isUserInRole("ROLE_GUIDE")) {
//			Tour_Guide_Xref tgxref = tour_Guide_Xref_Service.findOne(id);
//			tgxref.setStatus(0);
//			tour_Guide_Xref_Service.save(tgxref);
//			Principal principal = request.getUserPrincipal();
//			User user = userRepository.findByUsername(principal.getName());
//			Guide guide = guideService.findByUserId(user.getId());
//			model.addAttribute("guide", guide);
//			return "redirect:/guide";
//
//		} else
//			return "index";
//	}
	@ResponseBody
	@PostMapping("/tour/approveTour")
	public String approveTour(@Param("tourid") int tourid, HttpServletRequest request, Model model) {
		try {
			if(request.isUserInRole(Constant.ROLE_ADMIN)) {
				return tourService.approveTour(tourid);
			}else {
				return Constant.VIEW_403;
			}
			
		} catch (Exception e) {
			log.info("error in approve tour ", e);
			return Constant.MESS_FAIL;
		}
	}
	
	
	@ResponseBody
	@PostMapping("/tour/cancelApproveTour")
	public String cancelApproveTour(@Param("tourid") int tourid, HttpServletRequest request, Model model) {
		try {
			if(request.isUserInRole(Constant.ROLE_ADMIN)) {
				return tourService.cancelApproveTour(tourid);
			}else {
				return Constant.VIEW_403;
			}
			
		} catch (Exception e) {
			log.info("error in approve tour ", e);
			return Constant.MESS_FAIL;
		}
	}
	
	
	/**
	 * @description: create new tour in operator
	 * @param request
	 * @param model
	 * @return view
	 */
	@ResponseBody
	@PostMapping("/tour/updateTour")
	public String updateTour(HttpServletRequest request) {
		try {
			log.info("------------------start postour");
			String language = null;
			String tourname = request.getParameter(Constant.PARAMETER_TOUR_NAME);
//			String day = request.getParameter(Constant.PARAMETER_DAY);
//			String night = request.getParameter(Constant.PARAMETER_NIGHT);
//			String tourTime = Constant.VALUE_TIME_TOUR_NOTHING;
//			if(!"".equals(day)||!"".equals(night)) {
//				 tourTime = day + Constant.PARAMETER_NGAY
//						+ night + Constant.PARAMETER_DEM;
//			}
			int tourid =0;	
			if(request.getParameter(Constant.PARAMETER_TOUR_ID)!=null) {
				tourid =Integer.parseInt(request.getParameter(Constant.PARAMETER_TOUR_ID));	
			}
			String startDate = request.getParameter(Constant.PARAMETER_DATE);
			String endDate = request.getParameter(Constant.PARAMETER_END_DATE);
			String tourprice = request.getParameter(Constant.PARAMETER_TOUR_PRICES);
			String amount = request.getParameter(Constant.PARAMETER_AMOUNT);
//			String location = request.getParameter(Constant.PARAMETER_LOCATION);
			String countlocation = request.getParameter(Constant.PARAMETER_COUNT_LOCATION);
			String countlanguage = request.getParameter(Constant.PARAMETER_COUNT_LANGUAGE);
			String requirement =request.getParameter(Constant.PARAMETER_REQUIREMENT);
			int countLocation =0;
			if(!"".equals(countlocation)&&!"null".equalsIgnoreCase(countlocation)&& countlocation!=null) {
				countLocation = Integer.parseInt(countlocation);
			}
			int countLanguage =0;
			if(!"".equals(countlanguage)&&!"null".equalsIgnoreCase(countlanguage)&& countlanguage!=null) {
				countLanguage = Integer.parseInt(countlanguage);
			}
			log.info("tourid: " + tourid);
			log.info("tourname: " + tourname);
			log.info("startDate: " + startDate);
			log.info("endDate: " + endDate);
			log.info("tourprice: " + tourprice);
			log.info("amount: " + amount);
			log.info("coucountLocationnt: " + countLocation);
			log.info("countLanguage: " + countLanguage);
			log.info("requirement: " + requirement);
			Set<Location> locations = new HashSet<>();
			if (countLocation>0) {
				for (int i = 1; i <= countLocation; i++) {
					log.info(request.getParameter(Constant.PARAMETER_LOCATION + i));
					locations.add(locationService
							.findByLocationName(request.getParameter(Constant.PARAMETER_LOCATION + i)));
				}
			}
			if (countLanguage>0) {
				for (int i = 1; i <= countLanguage; i++) {
					log.info(request.getParameter(Constant.PARAMETER_LANGUAGE + i));
					language = request.getParameter(Constant.PARAMETER_LANGUAGE+ i);
					log.info("language: " + language);
//					locations.add(locationService
//							.findByLocationName(request.getParameter(Constant.PARAMETER_LOCATION + i)));
				}
			}
			if (tourService.updateTour(tourid,tourname, language, endDate, startDate, tourprice, locations, requirement,amount)) {
				log.info("----------------post tour is success!!!!!");
			}else {
				log.error("---------------------fasle post tour!!!!!!!!!!");
//				model.addAttribute(Constant.MESS_ERROR, Constant.MESS_POST_TOUR_FAILS);
				return Constant.MESS_FAIL;
			}
			log.info("------------------end postour");
			return Constant.MESS_SUCCESS;
		} catch (Exception e) {
			log.error("error: "+e);
//			model.addAttribute(Constant.MESS_ERROR, e);
			return Constant.MESS_FAIL;
		}
	}
	
	
	@GetMapping("/danhsachtour")
	public String adminConTour(HttpServletRequest request, Model model) {
		if (request.isUserInRole("ROLE_ADMIN")) {
			Page<Tour> tours = tourService.findAll(new PageRequest(0, 5));
			model.addAttribute("tours", tours);
			model.addAttribute("page", 1);
			model.addAttribute("previouspage", 0);
			model.addAttribute("nextpage", 2);
			return "danhsachtour";
		}
		if (request.isUserInRole("ROLE_GUIDE")) {
			return "ERROR";
		}
		return "ERROR";
	}
	@GetMapping("/tour/detailTour")
	public String detailTour(HttpServletRequest request, Model model, int tourId) {
		try {
			if (request!=null && request.isUserInRole(Constant.ROLE_ADMIN)) {
				log.info("----------------start redirect to info tour page");
				model.addAttribute(Constant.ENTITY_ADMIN,userDetailService.findByUsername(request.getUserPrincipal().getName()));
				model.addAttribute(Constant.ENTITY_TOUR, tourService.findOne(tourId));
				model.addAttribute(Constant.ENTITY_LIST_LANGUAE, (List<Language>) languageService.findAll());
				model.addAttribute(Constant.ENTITY_LIST_LOCATION, (List<Location>) locationService.findAll());
				log.info("----------------end redirect to info tour page");
				return Constant.VIEW_ADMIN_TOUR_INFO;
			}else{
				return Constant.VIEW_403;
			}
		} catch (Exception e) {
			log.error("error in detail tour ", e);
			model.addAttribute(Constant.MESS_EXCEPTION, e);
			return Constant.VIEW_ERROR;
		}
	}
	
	/**
	 * description: go to guide in tour page
	 * @param request
	 * @param model
	 * @param tourId
	 * @return
	 */
	@GetMapping("/admin/guideInTour")
	public String guideInTour(HttpServletRequest request, Model model, int tourId) {
		try {
			if(!request.isUserInRole(Constant.ROLE_ADMIN)) {
				return Constant.VIEW_403;
			}
			log.info("----------------start redirect to info guide of tour page");
			model.addAttribute(Constant.ENTITY_ADMIN,userDetailService.findByUsername(request.getUserPrincipal().getName()));
			model.addAttribute(Constant.ENTITY_TOUR, tourService.findOne(tourId));
			log.info("----------------end redirect to info guide of tour page");
			return Constant.VIEW_GUIDE_IN_TOUR;
		} catch (Exception e) {
			log.error("error in show guide in tour ", e);
			model.addAttribute(Constant.MESS_EXCEPTION, e);
			return Constant.VIEW_ERROR;
		}
	}
	
	@GetMapping("/danhsachtour/page/{id}")
	public String adminConTour1(@PathVariable("id") int id, HttpServletRequest request, Model model) {
		if (request.isUserInRole("ROLE_ADMIN")) {
			if (id > 0) {
				Page<Tour> tours = tourService.findAll(new PageRequest(id - 1, 5));
				model.addAttribute("tours", tours);
				model.addAttribute("page", id);
				model.addAttribute("previouspage", id - 1);
				model.addAttribute("nextpage", id + 1);
				return "danhsachtour";
			} else {
				Page<Tour> tours = tourService.findAll(new PageRequest(0, 5));
				model.addAttribute("tours", tours);
				model.addAttribute("page", 1);
				model.addAttribute("previouspage", 0);
				model.addAttribute("nextpage", 2);
				return "danhsachtour";
			}
		}
		if (request.isUserInRole("ROLE_GUIDE")) {
			return "ERROR";
		}
		return "ERROR";
	}

	@GetMapping(value = "/tour/delete/{page}/{id}")
	public String deleteTour(@PathVariable("page") int page, @PathVariable("id") int id, Model model) {
		tourService.delete(id);
		Page<Tour> tours = tourService.findAll(new PageRequest(page - 1, 5));
		model.addAttribute("tours", tours);
		model.addAttribute("page", page);
		model.addAttribute("previouspage", page - 1);
		model.addAttribute("nextpage", page + 1);
		return "danhsachtour";
	}

//	@PostMapping(value = "/tour/operatorrequest/{tourid}/{guideid}")
//	public String requestTour(@PathVariable("tourid") int tourid, @PathVariable("guideid") int guideid,
//			HttpServletRequest request, Model model) {
//		if (request.isUserInRole("ROLE_OPERATOR")) {
//			tour_Guide_Xref_Service.operatorRequest(tourid, guideid);
//			Principal principal = request.getUserPrincipal();
//			User user = userRepository.findByUsername(principal.getName());
//			Operator operator = operatorService.findByUserId(user.getId());
//			model.addAttribute("operator", operator);
//			return "find-guider";
//
//		} else
//			return "index";
//	}
}
