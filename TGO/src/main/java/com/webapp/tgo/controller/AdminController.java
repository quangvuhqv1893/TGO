package com.webapp.tgo.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.hibernate.Session;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.webapp.tgo.entities.Guide;
import com.webapp.tgo.entities.Language;
import com.webapp.tgo.entities.Location;
import com.webapp.tgo.entities.Operator;
import com.webapp.tgo.entities.Pagination;
import com.webapp.tgo.entities.Tour;
import com.webapp.tgo.service.GuideService;
import com.webapp.tgo.service.LanguageService;
import com.webapp.tgo.service.LocationService;
import com.webapp.tgo.service.OperatorService;
import com.webapp.tgo.service.TourService;
import com.webapp.tgo.service.UserDetailService;
import com.webapp.tgo.util.Constant;

@Controller
public class AdminController {
	Logger log = LoggerFactory.getLogger(OperatorController.class);
	
	@Autowired
	private UserDetailService UserDetailService;
	
	@Autowired
	private TourService tourService;
	
	@Autowired
	private GuideService guideService;
	
	@Autowired
	private OperatorService operatorService;
	
	@Autowired
	private LocationService locationService;
	
	@Autowired
	private LanguageService languageService;
	
	@GetMapping("/admin")
	public String adminCheckTour(HttpServletRequest request, Model model) {
		try {
			if (request.isUserInRole(Constant.ROLE_ADMIN)) {
				log.info("----------------start redirect to admin page");
				model.addAttribute(Constant.ENTITY_ADMIN,UserDetailService.findByUsername(request.getUserPrincipal().getName()));
				model.addAttribute(Constant.ENTITY_LIST_TOUR, tourService.findTourNeedCheck());
				log.info("----------------end redirect to admin page");
				return Constant.VIEW_ADMIN;
			}
			return Constant.VIEW_403;
		} catch (Exception e) {
			log.error("redirect to admin page error: " + e);
			model.addAttribute(Constant.MESS_EXCEPTION, e);
			return Constant.VIEW_ERROR;
		}
	}
	
	@GetMapping("/admin/userApproval")
	public String adminCheckUser(HttpServletRequest request, Model model) {
		try {
			if (request.isUserInRole(Constant.ROLE_ADMIN)) {
				log.info("----------------start redirect to admin check user page");
				model.addAttribute(Constant.ENTITY_ADMIN,UserDetailService.findByUsername(request.getUserPrincipal().getName()));
				model.addAttribute(Constant.ENTITY_LIST_GUIDE, guideService.findGuideNeedCheck());
				log.info("----------------end redirect to admin check user page");
				return Constant.VIEW_ADMIN_CHECK_USER;
			}
			return Constant.VIEW_403;
		} catch (Exception e) {
			log.error("redirect to operator page error: " + e);
			model.addAttribute(Constant.MESS_EXCEPTION, e);
			return Constant.VIEW_ERROR;
		}
	}
	
	
	@GetMapping("/admin/operatorApproval")
	public String adminCheckOperator(HttpServletRequest request, Model model) {
		try {
			if (request.isUserInRole(Constant.ROLE_ADMIN)) {
				log.info("----------------start redirect to admin check user page");
				model.addAttribute(Constant.ENTITY_ADMIN,UserDetailService.findByUsername(request.getUserPrincipal().getName()));
				List<Operator> list = (List<Operator>) operatorService.findOperatorNeedCheck();
				model.addAttribute(Constant.ENTITY_LIST_OPERATOR, list);
				log.info("list operator: "+list.size());
				log.info("----------------end redirect to admin check user page");
				return Constant.VIEW_ADMIN_CHECK_OPERATOR;
			}
			return Constant.VIEW_403;
		} catch (Exception e) {
			log.error("redirect to operator page error: " + e);
			model.addAttribute(Constant.MESS_EXCEPTION, e);
			return Constant.VIEW_ERROR;
		}
	}
	
	
	@ResponseBody
	@PostMapping("/admin/cancelApproveUser")
	public String cancelApproveUser(@Param("userid") int userid,@Param("role") int role, HttpServletRequest request, Model model) {
		try {
			if(request.isUserInRole(Constant.ROLE_ADMIN)) {
				return UserDetailService.cancelApproveUser(userid,role);
			}else {
				return Constant.VIEW_403;
			}
			
		} catch (Exception e) {
			log.error("error in approve tour ", e);
			return Constant.MESS_FAIL;
		}
	}
	@ResponseBody
	@PostMapping("/tour/acceptApproveUser")
	public String acceptApproveUser(@Param("userid") int userid,@Param("role") int role, HttpServletRequest request, Model model) {
		try {
			if(request.isUserInRole(Constant.ROLE_ADMIN)) {
				return UserDetailService.acceptApproveUser(userid,role);
			}else {
				return Constant.VIEW_403;
			}
			
		} catch (Exception e) {
			log.error("error in approve tour ", e);
			return Constant.MESS_FAIL;
		}
	}
	
	/**
	 * description: go to manage tour of admin
	 * @param request
	 * @param model
	 * @return
	 */
	@GetMapping("/admin/manageTour")
	public String manageTour(@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page,HttpServletRequest request, Model model) {
		try {
			if(!request.isUserInRole(Constant.ROLE_ADMIN)) {
				return Constant.VIEW_403;
			}
			log.info("----------------start redirect to admin mangage tour page");
			model.addAttribute(Constant.ENTITY_ADMIN,UserDetailService.findByUsername(request.getUserPrincipal().getName()));
			//get parameter
			String idTour = request.getParameter(Constant.PARAMETER_ID_TOUR);
			String tourName = request.getParameter(Constant.PARAMETER_TOUR_NAME);
			String startDate = request.getParameter(Constant.PARAMETER_START_DATE);
			String status = request.getParameter(Constant.STATUS);
			String tourPrice = request.getParameter(Constant.PARAMETER_TOUR_PRICE);
			log.info("idTour: "+idTour);
			log.info("tourName: "+tourName);
			log.info("startDate: "+startDate);
			log.info("status: "+status);
			log.info("tourPrice: "+tourPrice);
			//paging list tour
			// Evaluate page size. If requested parameter is null, return initial
			// page size
			int evalPageSize = pageSize.orElse(Constant.INITIAL_PAGE_SIZE);
			// Evaluate page. If requested parameter is null or less than 0 (to
			// prevent exception), return initial size. Otherwise, return value of
			// param. decreased by 1.
			int evalPage = (page.orElse(0) < 1) ? Constant.INITIAL_PAGE : page.get() - 1;
			Page<Tour> tours = tourService.manageTour(idTour,tourName,startDate,status,tourPrice,new PageRequest(evalPage, evalPageSize));
			Pagination pagination = new Pagination(tours.getTotalPages(), tours.getNumber(),
					Constant.BUTTON_TO_SHOW);
			
			model.addAttribute(Constant.ENTITY_LIST_TOUR,tours);
			model.addAttribute(Constant.SELECTED_PAGE_SIZE, evalPageSize);
			model.addAttribute(Constant.PAGE_SIZES_OBJIECT, Constant.PAGE_SIZES);
			model.addAttribute(Constant.PAGINATION, pagination);
			//add value of search
			model.addAttribute(Constant.PARAMETER_ID_TOUR, idTour);
			model.addAttribute(Constant.PARAMETER_TOUR_NAME, tourName);
			model.addAttribute(Constant.PARAMETER_START_DATE, startDate);
			model.addAttribute(Constant.STATUS, status);
			model.addAttribute(Constant.PARAMETER_TOUR_PRICE, tourPrice);
			model.addAttribute(Constant.ENTITY_LIST_LOCATION,  (List<Location>) locationService.findAll());
			log.info("----------------list tour size: " + tours.getTotalElements());
			
			return Constant.VIEW_ADMIN_MANAGE_INFO;
		}catch (Exception e) {
			log.error("error in manage tour ", e);
			model.addAttribute(Constant.MESS_EXCEPTION, e);
			return Constant.VIEW_ERROR;
		}
	}
	
	/**
	 * description: show all guide
	 * @param request
	 * @param model
	 * @param pageSize
	 * @param page
	 * @return
	 */
	@GetMapping("/admin/manageGuide")
	public String manageGuide(HttpServletRequest request, Model model, @RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page) {
		log.info("start manage guide!");
		try {
			if(!request.isUserInRole(Constant.ROLE_ADMIN)) {
				return Constant.VIEW_403;
			}
			model.addAttribute(Constant.ENTITY_ADMIN,UserDetailService.findByUsername(request.getUserPrincipal().getName()));
			//get parameter
			String guideId = request.getParameter(Constant.PARAMETER_ID_GUIDE);
			String fullName = request.getParameter(Constant.PARAMETER_FULLNAME);
			String email = request.getParameter(Constant.PARAMETER_EMAIL);
			String location = request.getParameter(Constant.PARAMETER_LOCATION);
			String language = request.getParameter(Constant.PARAMETER_LANGUAGE);
			log.info("guideId: "+guideId);
			log.info("fullName: "+fullName);
			log.info("email: "+email);
			log.info("location: "+location);
			log.info("language: "+language);
			//paging list tour
			// Evaluate page size. If requested parameter is null, return initial
			// page size
			int evalPageSize = pageSize.orElse(Constant.INITIAL_PAGE_SIZE);
			// Evaluate page. If requested parameter is null or less than 0 (to
			// prevent exception), return initial size. Otherwise, return value of
			// param. decreased by 1.
			int evalPage = (page.orElse(0) < 1) ? Constant.INITIAL_PAGE : page.get() - 1;
			Page<Guide> listGuide = guideService.manageGuide(guideId,fullName,email,location,language,new PageRequest(evalPage, evalPageSize));
			Pagination pagination = new Pagination(listGuide.getTotalPages(), listGuide.getNumber(),
					Constant.BUTTON_TO_SHOW);
//			
			model.addAttribute(Constant.ENTITY_LIST_GUIDE, listGuide);
			model.addAttribute(Constant.SELECTED_PAGE_SIZE, evalPageSize);
			model.addAttribute(Constant.PAGE_SIZES_OBJIECT, Constant.PAGE_SIZES);
			model.addAttribute(Constant.PAGINATION, pagination);
//			//add value of search
			model.addAttribute(Constant.PARAMETER_ID_GUIDE, guideId);
			model.addAttribute(Constant.PARAMETER_FULLNAME, fullName);
			model.addAttribute(Constant.PARAMETER_EMAIL, email);
			model.addAttribute(Constant.PARAMETER_LOCATION, location);
			model.addAttribute(Constant.PARAMETER_LANGUAGE, language);
//			model.addAttribute(Constant.ENTITY_LIST_LOCATION,  (List<Location>) locationService.findAll());
			log.info("----------------list tour size: " + listGuide.getTotalElements());
			return Constant.VIEW_ADMIN_MANAGE_INFO_GUIDE;
		} catch (Exception e) {
			log.error("error in manage guide ", e);
			model.addAttribute(Constant.MESS_EXCEPTION, e);
			return Constant.VIEW_ERROR;
		}
		
		
	}
	
	/**
	 * @param request
	 * @param model
	 * @param pageSize
	 * @param page
	 * @return
	 */
	@GetMapping("/admin/manageOperator")
	public String manageOperator(HttpServletRequest request, Model model, @RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page) {
		log.info("start manage operator!");
		try {
			if(!request.isUserInRole(Constant.ROLE_ADMIN)) {
				return Constant.VIEW_403;
			}
			model.addAttribute(Constant.ENTITY_ADMIN,UserDetailService.findByUsername(request.getUserPrincipal().getName()));
			//get parameter
			String operatorId = request.getParameter(Constant.PARAMETER_ID_OPERATOR);
			String fullName = request.getParameter(Constant.PARAMETER_FULLNAME);
			String username = request.getParameter(Constant.PARAMETER_USERNAME);
			String taxcode = request.getParameter(Constant.PARAMETER_TAX_CODE);
			String busRegCode = request.getParameter(Constant.PARAMETER_BUSINESS_REGISTRATION_CODE);
			log.info("operatorId: "+operatorId);
			log.info("fullName: "+fullName);
			log.info("username: "+username);
			log.info("busRegCode: "+busRegCode);
			log.info("taxcode: "+taxcode);
			//paging list tour
			// Evaluate page size. If requested parameter is null, return initial
			// page size
			int evalPageSize = pageSize.orElse(Constant.INITIAL_PAGE_SIZE);
			// Evaluate page. If requested parameter is null or less than 0 (to
			// prevent exception), return initial size. Otherwise, return value of
			// param. decreased by 1.
			int evalPage = (page.orElse(0) < 1) ? Constant.INITIAL_PAGE : page.get() - 1;
			Page<Operator> listOperator = operatorService.manageOperator(operatorId,fullName,username,busRegCode,taxcode,new PageRequest(evalPage, evalPageSize));
			Pagination pagination = new Pagination(listOperator.getTotalPages(), listOperator.getNumber(),
					Constant.BUTTON_TO_SHOW);
//			
			model.addAttribute(Constant.ENTITY_LIST_OPERATOR, listOperator);
			model.addAttribute(Constant.SELECTED_PAGE_SIZE, evalPageSize);
			model.addAttribute(Constant.PAGE_SIZES_OBJIECT, Constant.PAGE_SIZES);
			model.addAttribute(Constant.PAGINATION, pagination);
//			//add value of search
			model.addAttribute(Constant.PARAMETER_ID_OPERATOR, operatorId);
			model.addAttribute(Constant.PARAMETER_FULLNAME, fullName);
			model.addAttribute(Constant.PARAMETER_USERNAME, username);
			model.addAttribute(Constant.PARAMETER_TAX_CODE, taxcode);
			model.addAttribute(Constant.PARAMETER_BUSINESS_REGISTRATION_CODE, busRegCode);
//			model.addAttribute(Constant.ENTITY_LIST_LOCATION,  (List<Location>) locationService.findAll());
			log.info("----------------list tour size: " + listOperator.getTotalElements());
			return Constant.VIEW_ADMIN_MANAGE_INFO_OPERATOR;
		} catch (Exception e) {
			log.error("error in manage operator ", e);
			model.addAttribute(Constant.MESS_EXCEPTION, e);
			return Constant.VIEW_ERROR;
		}
		
		
	}
	
	@GetMapping("/admin/tourInGuide")
	public String showTourInGuide(HttpServletRequest request, Model model, int guideId) {
		try {
			if (request.isUserInRole(Constant.ROLE_ADMIN)) {
				log.info("start go to tour in guide page");
				model.addAttribute(Constant.ENTITY_ADMIN,UserDetailService.findByUsername(request.getUserPrincipal().getName()));
				model.addAttribute(Constant.ENTITY_LIST_TOUR, tourService.getTourofGuide(guideId));
				model.addAttribute(Constant.ENTITY_GUIDE, guideId);
				log.info("end go to tour in guide page");
				return Constant.VIEW_ADMIN_GUIDE_INFO_TOUR;
			}else {
				return Constant.VIEW_403;
			}
		} catch (Exception e) {
			log.error("error in show guide in tour ", e);
			model.addAttribute(Constant.MESS_EXCEPTION, e);
			return Constant.VIEW_ERROR;
		}
	}
	
	
	@GetMapping("/admin/tourInOperator")
	public String showTourInOperator(@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page,HttpServletRequest request, Model model) {
		try {
			if(!request.isUserInRole(Constant.ROLE_ADMIN)) {
				return Constant.VIEW_403;
			}
			log.info("----------------start redirect to admin mangage tour page");
			model.addAttribute(Constant.ENTITY_ADMIN,UserDetailService.findByUsername(request.getUserPrincipal().getName()));
			//get parameter
			String idTour = request.getParameter(Constant.PARAMETER_ID_TOUR);
			String tourName = request.getParameter(Constant.PARAMETER_TOUR_NAME);
			String startDate = request.getParameter(Constant.PARAMETER_START_DATE);
			String status = request.getParameter(Constant.STATUS);
			String operatorId = request.getParameter(Constant.PARAMETER_ID_OPERATOR);
			String tourPrice = request.getParameter(Constant.PARAMETER_TOUR_PRICE);
			log.info("idTour: "+idTour);
			log.info("tourName: "+tourName);
			log.info("startDate: "+startDate);
			log.info("status: "+status);
			log.info("tourPrice: "+tourPrice);
			//paging list tour
			// Evaluate page size. If requested parameter is null, return initial
			// page size
			int evalPageSize = pageSize.orElse(Constant.INITIAL_PAGE_SIZE);
			// Evaluate page. If requested parameter is null or less than 0 (to
			// prevent exception), return initial size. Otherwise, return value of
			// param. decreased by 1.
			int evalPage = (page.orElse(0) < 1) ? Constant.INITIAL_PAGE : page.get() - 1;
			Page<Tour> tours = tourService.manageTourinOpearator(Integer.parseInt(operatorId), idTour,tourName,startDate,status,tourPrice,new PageRequest(evalPage, evalPageSize));
			Pagination pagination = new Pagination(tours.getTotalPages(), tours.getNumber(),
					Constant.BUTTON_TO_SHOW);
			
			model.addAttribute(Constant.ENTITY_LIST_TOUR,tours);
			model.addAttribute(Constant.SELECTED_PAGE_SIZE, evalPageSize);
			model.addAttribute(Constant.PAGE_SIZES_OBJIECT, Constant.PAGE_SIZES);
			model.addAttribute(Constant.PAGINATION, pagination);
			//add value of search
			model.addAttribute(Constant.PARAMETER_ID_TOUR, idTour);
			model.addAttribute(Constant.PARAMETER_TOUR_NAME, tourName);
			model.addAttribute(Constant.PARAMETER_START_DATE, startDate);
			model.addAttribute(Constant.STATUS, status);
			model.addAttribute(Constant.PARAMETER_TOUR_PRICE, tourPrice);
			model.addAttribute(Constant.PARAMETER_ID_OPERATOR, operatorId);
			log.info("----------------list tour size: " + tours.getTotalElements());
				return Constant.VIEW_ADMIN_OPERATOR_INFO_TOUR;
		} catch (Exception e) {
			log.error("error in show guide in tour ", e);
			model.addAttribute(Constant.MESS_EXCEPTION, e);
			return Constant.VIEW_ERROR;
		}
	}
	
	/**
	 * @description: change info of guide
	 * @param request
	 * @param model
	 * @return
	 */
	@PostMapping("/admin/changeinfoGuide")
	public String updateInfoGuide(HttpServletRequest request, Model model, String username) {
		try {
			if (request.isUserInRole(Constant.ROLE_ADMIN)) {
				log.info("-----------start chảng info guide!!!!");
				String guidename = request.getParameter(Constant.PARAMETER_GUIDE_NAME);
				String address = request.getParameter(Constant.PARAMETER_ADDRESS);
				String phonenumber = request.getParameter(Constant.PARAMETER_PHONE);
				String cardnumber = request.getParameter(Constant.PARAMETER_CARD_NUMBER);
				String email = request.getParameter(Constant.PARAMETER_EMAIL);
				String gender = request.getParameter(Constant.PARAMETER_GENDER);
				log.info("guidename: " + guidename);
				log.info("address: " + address);
				log.info("phonenumber: " + phonenumber);
				log.info("cardnumber: " + cardnumber);
				log.info("email: "+email);
				log.info("gender: " + gender);
				int countlocation = Integer.parseInt(request.getParameter(Constant.PARAMETER_COUNT_LOCATION));// "count-location"
				int countlanguage = Integer.parseInt(request.getParameter(Constant.PARAMETER_COUNT_LANGUAGE));
				Guide guide = guideService.findbyUserName(username);
				guide.getUser().setFullname(guidename);
				guide.getUser().setAddress(address);
				guide.getUser().setPhonenumber(phonenumber);
				guide.setCardnumber(cardnumber);
				guide.getUser().setEmail(email);
				guide.setGender(gender);
				if (countlocation > 0) {
					Set<Location> setl = new HashSet<>();
					for (int i = 1; i <= countlocation; i++) {
						try {
							setl.add(locationService
									.findByLocationName(request.getParameter(Constant.PARAMETER_LOCATION + i)));
						} catch (Exception e) {
							log.error("error in set location!!!");
						}
					}
					guide.setLocations(setl);
				}
				if (countlanguage > 0) {
					Set<Language> setl = new HashSet<>();
					for (int i = 1; i <= countlanguage; i++) {
						try {
							setl.add(languageService
									.findByLanguage(request.getParameter(Constant.PARAMETER_LANGUAGE + i)));
						} catch (Exception e) {
							log.error("error in set language!!!");
						}
					}
					guide.setLanguages(setl);
				}
				model.addAttribute(Constant.MESS, guideService.saveGuide(guide));
				model.addAttribute(Constant.ENTITY_GUIDE, guide);
				model.addAttribute(Constant.ENTITY_ADMIN, UserDetailService.findByUsername(request.getUserPrincipal().getName()));
				log.info("-----------end change info guide!!!!");
				return Constant.VIEW_REDIRECT_DETAIL_GUIDE_INFO+guide.getId();
				
			} else {
				return Constant.VIEW_403;
			}
		} catch (Exception e) {
			log.error("exception in method changeInfo: ", e);
			model.addAttribute(Constant.MESS_ERROR, e);
			return Constant.VIEW_ERROR;
		}
	}

	
	/**
	 * @description method change info of operator
	 * @param operator
	 * @param model
	 * @return view OPERATOR_INFO
	 */
	@PostMapping("/admin/changeinfoOperator")
	public String changeInfo(@Valid Operator operator, Model model, HttpServletRequest request) {
		try {
			if (request.isUserInRole(Constant.ROLE_ADMIN)) {
				log.info("----------------------redirect to changeInfo page");
				//change info
				model.addAttribute(Constant.MESS,operatorService.changeInfo(operator));
				model.addAttribute(Constant.MESS_ERROR, Constant.MESS_ChANGE_INFO_OPERATOR);
				model.addAttribute(Constant.ENTITY_ADMIN, UserDetailService.findByUsername(request.getUserPrincipal().getName()));
				model.addAttribute(Constant.ENTITY_OPERATOR, operatorService.findOne(operator.getId()));
				log.info("----------------------end redirect to changeInfo page");
				return Constant.VIEW_REDIRECT_DETAIL_OPERATOR_INFO+operator.getId();
			}
			return Constant.VIEW_403;
		} catch (Exception e) {
			log.error("exception in method changeInfo: " + e);
			model.addAttribute(Constant.MESS_ERROR, e);
			return Constant.VIEW_ERROR;
		}
	}
	
	
	/*
	 * @description: operator delete tour waiting or cancel tour is running
	 * @param tourid
	 * @param request
	 * @param model
	 * @return
	 */
	@ResponseBody
	@PostMapping("/admin/delete")
	public String deleteTour( int tourid, HttpServletRequest request, Model model) {
		try {
			if (request.isUserInRole(Constant.ROLE_ADMIN)) {
				return tourService.cancelTour(tourid)? Constant.VIEW_REDIRECT_ADMIN : Constant.VIEW_ERROR;
			} else
				return Constant.VIEW_403;
		} catch (Exception e) {
			log.error("redirect to operator page error: " + e);
			model.addAttribute(Constant.MESS_EXCEPTION, e);
			return Constant.VIEW_ERROR;
		}
	}
}
