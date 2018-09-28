package com.webapp.tgo.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.webapp.tgo.entities.Guide;
import com.webapp.tgo.entities.Language;
import com.webapp.tgo.entities.Location;
import com.webapp.tgo.entities.Operator;
import com.webapp.tgo.entities.Pagination;
import com.webapp.tgo.entities.Tour;
import com.webapp.tgo.entities.User;
import com.webapp.tgo.service.GuideService;
import com.webapp.tgo.service.LanguageService;
import com.webapp.tgo.service.LocationService;
import com.webapp.tgo.service.OperatorService;
import com.webapp.tgo.service.TourService;
import com.webapp.tgo.service.UserDetailService;
import com.webapp.tgo.util.Constant;

/**
 * @author Quang
 *
 */
@Controller
public class OperatorController {
	Logger log = LoggerFactory.getLogger(OperatorController.class);
	@Autowired
	private OperatorService operatorService;
	@Autowired
	private LanguageService languageService;
	@Autowired
	private UserDetailService userDetailService;
	@Autowired
	private TourService tourService;
	@Autowired
	private LocationService locationService;
	@Autowired
	private GuideService guideService;

	/**
	 * @description: show operator page.
	 * @param request
	 * @param model
	 * @return view
	 */
	@GetMapping("/operator")
	public String operator(HttpServletRequest request, Model model) {
		try {
			if (request.isUserInRole(Constant.ROLE_OPERATOR)) {
				log.info("----------------start redirect to operator page");
				model.addAttribute(Constant.ENTITY_OPERATOR,
						operatorService.findByUserName(request.getUserPrincipal().getName()));
				model.addAttribute(Constant.ENTITY_LIST_LANGUAE, (List<Language>) languageService.findAll());
				model.addAttribute(Constant.ENTITY_LIST_LOCATION, (List<Location>) locationService.findAll());
				log.info("----------------end redirect to operator page");
				return Constant.VIEW_OPERATOR;
			}
			return Constant.VIEW_403;
		} catch (Exception e) {
			log.error("redirect to operator page error: " + e);
			model.addAttribute(Constant.MESS_EXCEPTION, e);
			return Constant.VIEW_ERROR;
		}
	}

	/**
	 * @description: go to info operator page
	 * @param request
	 * @param model
	 * @return view OPERATOR_INFO
	 */
	@GetMapping("/operator/info")
	public String operatorInfo(HttpServletRequest request, Model model) {

		try {
			if (request.isUserInRole(Constant.ROLE_OPERATOR)) {
				log.info("----------------start redirect to info operator page");
				model.addAttribute(Constant.ENTITY_OPERATOR, operatorService.findByUserName(request.getUserPrincipal().getName()));
				model.addAttribute(Constant.ENTITY_LIST_LANGUAE, (List<Language>) languageService.findAll());
				model.addAttribute(Constant.ENTITY_LIST_LOCATION, (List<Location>) locationService.findAll());
				log.info("----------------end redirect to info operator page");
				return Constant.VIEW_OPERATOR_INFO;
			} else {
				return Constant.VIEW_403;
			}
		} catch (Exception e) {
			log.error("redirect to info operator page error: " + e);
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
	@PostMapping("/operator/changeinfo")
	public String changeInfo(@Valid Operator operator, Model model) {
		try {
			if (Constant.ROLE_OPERATOR.equalsIgnoreCase(operator.getUser().getRoles().getName())) {
				log.info("----------------------redirect to changeInfo page");
				//change info
				model.addAttribute(Constant.MESS,operatorService.changeInfo(operator));
				model.addAttribute(Constant.MESS_ERROR, Constant.MESS_ChANGE_INFO_OPERATOR);
				log.info("----------------------end redirect to changeInfo page");
				return Constant.VIEW_REDIRECT_OPERATOR_INFO;
			}
			return Constant.VIEW_403;
		} catch (Exception e) {
			log.error("exception in method changeInfo: " + e);
			model.addAttribute(Constant.MESS_ERROR, e);
			return Constant.VIEW_ERROR;
		}
	}

	/**
	 * @description: go to form search guide
	 * @param request
	 * @param model
	 * @return view
	 */
	@GetMapping("/operator/searchguide")
	public String findGuide(HttpServletRequest request, Model model) {
		try {
			if (request.isUserInRole(Constant.ROLE_OPERATOR)) {
				log.info("------------redirect to searchguide page");
				model.addAttribute(Constant.ENTITY_OPERATOR,
						operatorService.findByUserName(request.getUserPrincipal().getName()));
				model.addAttribute(Constant.ENTITY_LIST_LANGUAE, (List<Language>) languageService.findAll());
				model.addAttribute(Constant.ENTITY_LIST_LOCATION, (List<Location>) locationService.findAll());
				log.info("------------end redirect to searchguide page");
				return Constant.VIEW_FIND_GUIDE;
			} else
				return Constant.VIEW_403;
		} catch (Exception e) {
			log.error("error go to page searchguide: " + e);
			model.addAttribute(Constant.MESS_ERROR, e);
			return Constant.VIEW_ERROR;
		}
	}
	
	/**
	 * @description method search guide of operator
	 * @param pageSize
	 * @param page
	 * @param request
	 * @param attributes
	 * @return view
	 */
	
	
	@GetMapping("/operator/search")
	public String searchGuide(@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page, HttpServletRequest request, RedirectAttributes attributes) {
		try {
			if (request.isUserInRole(Constant.ROLE_OPERATOR)) {
				log.info("------------start search");
				log.info("page size: " + pageSize);
				log.info("page: " + page);
				String location = request.getParameter(Constant.PARAMETER_LOCATION);
				String language = request.getParameter(Constant.PARAMETER_LANGUAGE);
				String gender = request.getParameter(Constant.PARAMETER_GENDER);
				String type = request.getParameter(Constant.PARAMETER_TYPE);
				log.info("location: " + location);
				log.info("language: " + language);
				log.info("gender: " + gender);
				log.info("type: " + type);
				// Evaluate page size. If requested parameter is null, return initial
				// page size
				int evalPageSize = pageSize.orElse(Constant.INITIAL_PAGE_SIZE);
				// Evaluate page. If requested parameter is null or less than 0 (to
				// prevent exception), return initial size. Otherwise, return value of
				// param. decreased by 1.
				int evalPage = (page.orElse(0) < 1) ? Constant.INITIAL_PAGE : page.get() - 1;
				Page<Guide> listGuide = guideService.findGuide(location, gender, type, language,
						new PageRequest(evalPage, evalPageSize));
				Pagination pagination = new Pagination(listGuide.getTotalPages(), listGuide.getNumber(),
						Constant.BUTTON_TO_SHOW);
				attributes.addFlashAttribute(Constant.ENTITY_LIST_GUIDE, listGuide);
				attributes.addFlashAttribute(Constant.SELECTED_PAGE_SIZE, evalPageSize);
				attributes.addFlashAttribute(Constant.PAGE_SIZES_OBJIECT, Constant.PAGE_SIZES);
				attributes.addFlashAttribute(Constant.PAGINATION, pagination);
				log.info("size list guide search: " + listGuide.getSize());
				attributes.addFlashAttribute(Constant.PARAMETER_LANGUAGE, language);
				attributes.addFlashAttribute(Constant.PARAMETER_LOCATION, location);
				attributes.addFlashAttribute(Constant.PARAMETER_GENDER, gender);
				attributes.addFlashAttribute(Constant.PARAMETER_TYPE, type);
				log.info("evaluate page: " + evalPage);
				log.info("evaluate page size: " + evalPageSize);
				log.info("-------------end search");
				return Constant.VIEW_REDIRECT_FIND_GUIDE;
			} else
				return Constant.VIEW_403;
		} catch (Exception e) {
			log.error("error search: " + e);
			attributes.addFlashAttribute(Constant.MESS_ERROR, e);
			return Constant.VIEW_ERROR;
		}
	}


	/**
	 * @description: go to waiting tour page
	 * @param request
	 * @param model
	 * @return
	 */
	@GetMapping("/operator/waitingTour")
	public String showWaitingTour(HttpServletRequest request, Model model) {
		try {
			log.info("----go to waiting tour");
			if (request.isUserInRole(Constant.ROLE_OPERATOR)) {
				Operator operator = operatorService.findByUserName(request.getUserPrincipal().getName());
//				List<Tour> tourList = operatorService.findTourWaitting(operator.getId());
//				model.addAttribute(Constant.ENTITY_LIST_TOUR, tourList);
				model.addAttribute(Constant.ENTITY_OPERATOR, operator);
				model.addAttribute(Constant.ENTITY_LIST_LANGUAE, (List<Language>) languageService.findAll());
				model.addAttribute(Constant.ENTITY_LIST_LOCATION, (List<Location>) locationService.findAll());
				log.info("--- finis go to waiting tour");
				return Constant.VIEW_WATING_TOUR;
			} else {
				return Constant.VIEW_403;
			}

		} catch (Exception e) {
			log.error("redirect to info operator page error: " + e);
			model.addAttribute(Constant.MESS_ERROR, e);
			return Constant.VIEW_ERROR;
		}
	}

	/**
	 * @description: go to running tour page
	 * @param request
	 * @param model
	 * @return
	 */
	@GetMapping("/operator/runningTour")
	public String showRunningTour(HttpServletRequest request, Model model) {
		try {
			log.info("----go to running tour");
			if (request.isUserInRole(Constant.ROLE_OPERATOR)) {
				model.addAttribute(Constant.ENTITY_OPERATOR,
						operatorService.findByUserName(request.getUserPrincipal().getName()));
				model.addAttribute(Constant.ENTITY_LIST_LANGUAE, (List<Language>) languageService.findAll());
				model.addAttribute(Constant.ENTITY_LIST_LOCATION, (List<Location>) locationService.findAll());
				log.info("--- finis go to running tour");
				return Constant.VIEW_RUNNING_TOUR;
			} else {
				return Constant.VIEW_403;
			}

		} catch (Exception e) {
			log.error("redirect to info operator page error: " + e);
			model.addAttribute(Constant.MESS_ERROR, e);
			return Constant.VIEW_ERROR;
		}
	}

	/**
	 * @description: go to finish tour page
	 * @param request
	 * @param model
	 * @return
	 */
	@GetMapping("/operator/finishTour")
	public String showFinishTour(HttpServletRequest request, Model model) {
		try {
			log.info("----go to finish tour");
			if (request.isUserInRole(Constant.ROLE_OPERATOR)) {
				model.addAttribute(Constant.ENTITY_OPERATOR,
						operatorService.findByUserName(request.getUserPrincipal().getName()));
				model.addAttribute(Constant.ENTITY_LIST_LANGUAE, (List<Language>) languageService.findAll());
				model.addAttribute(Constant.ENTITY_LIST_LOCATION, (List<Location>) locationService.findAll());
				log.info("--- finis go to finish tour");
				return Constant.VIEW_FINISH_TOUR;
			} else {
				return Constant.VIEW_403;
			}

		} catch (Exception e) {
			log.error("redirect to info operator page error: " + e);
			model.addAttribute(Constant.MESS_ERROR, e);
			return Constant.VIEW_ERROR;
		}
	}

	
	

	/**
	 * @description: create new tour in operator
	 * @param request
	 * @param model
	 * @return view
	 */
	@ResponseBody
	@GetMapping("/operator/posttour")
	public String posttour(HttpServletRequest request) {
		try {

			if (!request.isUserInRole(Constant.ROLE_OPERATOR))
				return Constant.VIEW_403;
			log.info("------------------start postour");
			Operator operator = operatorService.findByUserName(request.getUserPrincipal().getName());
			String tourname = request.getParameter(Constant.PARAMETER_TOUR_NAME);
			String language = request.getParameter(Constant.PARAMETER_LANGUAGE);
			String day = request.getParameter(Constant.PARAMETER_DAY);
			String night = request.getParameter(Constant.PARAMETER_NIGHT);
			String tourTime = Constant.VALUE_TIME_TOUR_NOTHING;
			if(!"".equals(day)||!"".equals(night)) {
				 tourTime = day + Constant.PARAMETER_NGAY
						+ night + Constant.PARAMETER_DEM;
			}
			String startDate = request.getParameter(Constant.PARAMETER_DATE);
			String tourprice = request.getParameter(Constant.PARAMETER_TOUR_PRICE);
			String amount = request.getParameter(Constant.PARAMETER_AMOUNT);
			String location = request.getParameter(Constant.PARAMETER_LOCATION);
			String countlocation = request.getParameter(Constant.PARAMETER_COUNT_LOCATION);
			String requirement =request.getParameter(Constant.PARAMETER_REQUIREMENT);
			int count =0;
			if(!"".equals(countlocation)&&!"null".equalsIgnoreCase(countlocation)) {
				count = Integer.parseInt(countlocation);
			}
			log.info("tourname: " + tourname);
			log.info("language: " + language);
			log.info("tourTime: " + tourTime);
			log.info("startDate: " + startDate);
			log.info("tourprice: " + tourprice);
			log.info("amount: " + amount);
			log.info("count: " + count);
			log.info("requirement: " + requirement);
			List<String> locations = new ArrayList<String>();
			locations.add(location);
			for (int i = 1; i <= count; i++) {
				log.info(request.getParameter(Constant.PARAMETER_LOCATION + i));
				locations.add(request.getParameter(Constant.PARAMETER_LOCATION + i));
				log.info("location " + i + " : " + locations.get(i));
			}
			if (operatorService.postTour(operator, tourname, language, tourTime, startDate, tourprice, locations, requirement,amount)) {
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

	/**
	 * description: change info of operator
	 * 
	 * @param operator
	 * @param model
	 * @return view
	 */

	@GetMapping("/operator/showGuide")
	public String showGuideInfo(@RequestParam("guideid") int guideid, HttpServletRequest request, Model model) {
		try {
			if (request.isUserInRole(Constant.ROLE_OPERATOR)) {
				Operator operator = operatorService.findByUserName(request.getUserPrincipal().getName());
				model.addAttribute(Constant.ENTITY_GUIDE, guideService.findOne(guideid));
				model.addAttribute(Constant.ENTITY_OPERATOR, operator);
				return Constant.VIEW_SHOWING_INFO_GUIDE;
			} else
				return Constant.VIEW_403;
		} catch (Exception e) {
			model.addAttribute(Constant.MESS_ERROR, e);
			return Constant.VIEW_OPERATOR;
		}
	}

	
	
	
	
	
	
	
	/*-_-------------------------------------------------------*/
	
	@GetMapping("/danhsachcongtyluhanh")
	public String adminCon(HttpServletRequest request, Model model) {
		if (request.isUserInRole("ROLE_ADMIN")) {
			Page<Operator> operators = operatorService.findAll(new PageRequest(0, 5));
			model.addAttribute("operators", operators);
			model.addAttribute("page", 1);
			model.addAttribute("previouspage", 0);
			model.addAttribute("nextpage", 2);
			return "danhsachcongtyluhanh";
		}
		if (request.isUserInRole("ROLE_GUIDE")) {
			return "ERROR";
		}
		return "ERROR";
	}

	@GetMapping("/danhsachcongtyluhanh/page/{id}")
	public String adminCompany(@PathVariable("id") int id, HttpServletRequest request, Model model) {
		if (request.isUserInRole("ROLE_ADMIN")) {
			if (id > 0) {
				Page<Operator> operators = operatorService.findAll(new PageRequest(id - 1, 5));
				model.addAttribute("operators", operators);
				model.addAttribute("page", id);
				model.addAttribute("previouspage", id - 1);
				model.addAttribute("nextpage", id + 1);
			} else {
				Page<Operator> operators = operatorService.findAll(new PageRequest(0, 5));
				model.addAttribute("operators", operators);
				model.addAttribute("page", 1);
				model.addAttribute("previouspage", 0);
				model.addAttribute("nextpage", 2);
				return "danhsachcongtyluhanh";
			}
			return "danhsachcongtyluhanh";
		}
		if (request.isUserInRole("ROLE_GUIDE")) {
			return "ERROR";
		}
		return "ERROR";
	}

	@GetMapping(value = "/operator/delete/{page}/{id}")
	public String delete(@PathVariable("page") int page, @PathVariable("id") int id, Model model) {
		operatorService.delete(id);
		Page<Operator> operators = operatorService.findAll(new PageRequest(page - 1, 5));
		model.addAttribute("operators", operators);
		model.addAttribute("page", page);
		model.addAttribute("previouspage", page - 1);
		model.addAttribute("nextpage", page + 1);
		return "danhsachcongtyluhanh";
	}

	@PostMapping("/operator/update/{id}")
	public String updateTour(@PathVariable("id") int id, HttpServletRequest request, Model model) {
		Tour tour = tourService.findOne(id);
		String tourName = request.getParameter("tourName");
		String tourPrice = request.getParameter("tourPrice");
		String tourTime = request.getParameter(Constant.PARAMETER_DAY) + " ngày "
				+ request.getParameter(Constant.PARAMETER_NIGHT) + " đêm ";
		tour.setTourName(tourName);
		tour.setTourPrice(Integer.parseInt(tourPrice));
		tour.setTourTime(tourTime);
		tourService.save(tour);
		Principal principal = request.getUserPrincipal();
		User user = userDetailService.findByUsername(principal.getName());
		Operator operator = operatorService.findByUserId(user.getId());
		model.addAttribute("operator", operator);
		return "TourOperator";
	}

}