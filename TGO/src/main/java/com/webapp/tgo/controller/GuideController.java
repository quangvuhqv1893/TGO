package com.webapp.tgo.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

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
import com.webapp.tgo.entities.Pagination;
import com.webapp.tgo.entities.Tour;
import com.webapp.tgo.service.GuideService;
import com.webapp.tgo.service.LanguageService;
import com.webapp.tgo.service.LocationService;
import com.webapp.tgo.service.OperatorService;
import com.webapp.tgo.service.TourService;
import com.webapp.tgo.service.Tour_Guide_Xref_Service;
import com.webapp.tgo.util.Constant;

@Controller
public class GuideController {
	@Autowired
	private GuideService guideService;
	@Autowired
	private TourService tourService;
	@Autowired
	private OperatorService operatorService;
	@Autowired
	private LocationService locationService;
	@Autowired
	private LanguageService languageService;
	@Autowired
	private Tour_Guide_Xref_Service tgXref_Service;

	Logger log = LoggerFactory.getLogger(GuideController.class);

	/**
	 * @description: show guide page
	 * @param request
	 * @param model
	 * @return
	 */
	@GetMapping("/guide")
	public String gotoGuidePage(HttpServletRequest request, Model model) {
		try {
			if (request.isUserInRole(Constant.ROLE_GUIDE)) {
				log.info("----------------start redirect to guide page");
				log.info("username: " + request.getUserPrincipal().getName());
				Guide guide = guideService.findbyUserName(request.getUserPrincipal().getName());
				//show recommend tour for guide
				List<Tour> tours = tourService.TourRecomend(guide);
				log.info("list of tour: " + tours.size());
				model.addAttribute(Constant.PARAMETER_TOURS, tours);
				model.addAttribute(Constant.ENTITY_GUIDE, guide);
				log.info("----------------end redirect to guide page");
				return Constant.VIEW_GUIDE;
			}
			return Constant.VIEW_403;

		} catch (Exception e) {
			log.error("redirect to guide page error: " , e);
			model.addAttribute(Constant.MESS_EXCEPTION, e);
			return Constant.VIEW_ERROR;
		}
	}

	/**
	 * @description: change to change info guide page
	 * @param request
	 * @param model
	 * @return
	 */
	@GetMapping("/guide/info")
	public String info(HttpServletRequest request, Model model) {
		try {
			if (request.isUserInRole(Constant.ROLE_GUIDE)) {
				log.info("--------start change to guide info page");
				model.addAttribute(Constant.ENTITY_GUIDE,
						guideService.findbyUserName(request.getUserPrincipal().getName()));
				log.info("--------end change to guide info page");
				return Constant.VIEW_GUIDE_INFO;
			} else {
				log.error("-------access dinied!");
				return Constant.VIEW_403;
			}

		} catch (Exception e) {
			log.error("redirect to info guide page error: ", e);
			model.addAttribute(Constant.MESS_ERROR, e);
			return Constant.VIEW_ERROR;
		}
	}

	/**
	 * @description: change info of guide
	 * @param request
	 * @param model
	 * @return
	 */
	@PostMapping("/guide/changeinfo")
	public String updateInfo(HttpServletRequest request, Model model) {
		try {
			if (request.isUserInRole(Constant.ROLE_GUIDE)) {
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
				Guide guide = guideService.findbyUserName(request.getUserPrincipal().getName());
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
				log.info("-----------end change info guide!!!!");
				return Constant.VIEW_REDIRECT_GUIDE_INFO;
				
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
	 * @description: redirect to find tour page
	 * @param request
	 * @param model
	 * @return
	 */
	@GetMapping("/guide/findtour")
	public String goToFindTourPage(HttpServletRequest request, Model model) {
		try {
			if (request.isUserInRole(Constant.ROLE_GUIDE)) {
				Guide guide = guideService.findbyUserName(request.getUserPrincipal().getName());
				log.info("username: " + guide);
				model.addAttribute(Constant.ENTITY_GUIDE, guide);
				model.addAttribute(Constant.ENTITY_LIST_LOCATION, (List<Location>) locationService.findAll());
				log.info("------success redirect to find tour!!!!");
				return Constant.VIEW_FIND_TOUR;
			} else
				return Constant.VIEW_403;
		} catch (Exception e) {
			log.error("redirect to find tour page error: " + e);
			model.addAttribute(Constant.MESS_ERROR, e);
			return Constant.VIEW_ERROR;
		}
	}

	/**
	 * @description: search all tour if tour is wating and guide have't join tour and start date is not start date of tour in guide
	 * @param pageSize
	 * @param page
	 * @param request
	 * @param attributes
	 * @return view
	 */
	@GetMapping("/guide/search")
	public String searchTour(@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page, HttpServletRequest request, RedirectAttributes attributes) {
		try {
			if (request.isUserInRole(Constant.ROLE_GUIDE)) {
				Guide guide = guideService.findbyUserName(request.getUserPrincipal().getName());
				String location = request.getParameter(Constant.PARAMETER_LOCATION);
				String tourName = request.getParameter(Constant.PARAMETER_TOUR_NAME);
				String operatorName = request.getParameter(Constant.PARAMETER_OPERATOR_NAME);
				String day = request.getParameter(Constant.PARAMETER_DAY);
				log.info("----------------- location: "+location);
				log.info("----------------- tour name: "+tourName);
				log.info("-----------------operator name: "+operatorName);
				log.info("-----------------start date: "+day);
				// Evaluate page size. If requested parameter is null, return initial
				// page size
				int evalPageSize = pageSize.orElse(Constant.INITIAL_PAGE_SIZE);
				// Evaluate page. If requested parameter is null or less than 0 (to
				// prevent exception), return initial size. Otherwise, return value of
				// param. decreased by 1.
				int evalPage = (page.orElse(0) < 1) ? Constant.INITIAL_PAGE : page.get() - 1;
				Page<Tour> tours = tourService.findTour(guide.getId(), tourName, operatorName, location, day,
						new PageRequest(evalPage, evalPageSize));
				Pagination pagination = new Pagination(tours.getTotalPages(), tours.getNumber(),
						Constant.BUTTON_TO_SHOW);
				log.info("----------------list tour size: " + tours.getSize());
				attributes.addFlashAttribute(Constant.PARAMETER_TOURS, tours);
				attributes.addFlashAttribute(Constant.SELECTED_PAGE_SIZE, evalPageSize);
				attributes.addFlashAttribute(Constant.PAGE_SIZES_OBJIECT, Constant.PAGE_SIZES);
				attributes.addFlashAttribute(Constant.PAGINATION, pagination);
				attributes.addFlashAttribute(Constant.PARAMETER_LOCATION, location);
				attributes.addFlashAttribute(Constant.PARAMETER_TOUR_NAME, tourName);
				attributes.addFlashAttribute(Constant.PARAMETER_OPERATOR_NAME, operatorName);
				attributes.addFlashAttribute(Constant.PARAMETER_DAY, day);
				return Constant.VIEW_REDIRECT_GUIDE_FIND_TOUR;
			} else
				return Constant.VIEW_403;
		} catch (Exception e) {
			log.error("redirect to find tour error: ", e);
			attributes.addAttribute(Constant.MESS_ERROR, e);
			return Constant.VIEW_ERROR;
		}

	}
	
	/**
	 * @description: show info of operator in search tour page of guide
	 * @param operatorid
	 * @param request
	 * @param model
	 * @return view
	 */
	@GetMapping("/guide/showOperator")
	public String showOperatorInfo(@RequestParam("operatorid") int operatorid, HttpServletRequest request,
			Model model) {
		try {
		if (request.isUserInRole(Constant.ROLE_GUIDE)) {
			model.addAttribute("operator", operatorService.findOne(operatorid));
			model.addAttribute("guide", guideService.findbyUserName(request.getUserPrincipal().getName()));
			return Constant.VIEW_SHOW_OPERATOR_INFO;
		} else
			return Constant.VIEW_403;
	}catch (Exception e) {
		log.error("----------show operator info is error: " + e);
		model.addAttribute(Constant.MESS_ERROR, e);
		return Constant.VIEW_ERROR;
	}
	}
	
	/**
	 * @description: guide send request to operator
	 * @param tourid
	 * @param request
	 * @param model
	 * @return
	 */
	@ResponseBody
	@GetMapping("/guide/guideSendrequest")
	public String requestTour(@RequestParam("tourid") int tourid, HttpServletRequest request, Model model) {
		try {
		if (request.isUserInRole(Constant.ROLE_GUIDE)) {
			return tgXref_Service.guideSendRequest(guideService.findbyUserName(request.getUserPrincipal().getName()), tourid);
		} else
			return Constant.VIEW_403;
		}catch (Exception e) {
			log.error("----------guide send request is error: " + e);
			model.addAttribute(Constant.MESS_ERROR, e);
			return Constant.VIEW_ERROR; 
		}
	}

	/**
	 * @description: go to accepted tour page
	 * @param request
	 * @param model
	 * @return
	 */
	@GetMapping("/guide/showAcceptedTour")
	public String goToAcceptedTourPage(HttpServletRequest request, Model model) {
		try {
			if (request.isUserInRole(Constant.ROLE_GUIDE)) {
			model.addAttribute(Constant.ENTITY_GUIDE, guideService.findbyUserName(request.getUserPrincipal().getName()));
			log.info("---------------user name"+request.getUserPrincipal().getName());
			return Constant.VIEW_GUIDE_ACCEPTED;
			}
			return Constant.VIEW_403;
		}catch (Exception e) {
			log.error("----------go to show accepted page is error: ", e);
			model.addAttribute(Constant.MESS_ERROR, e);
			return Constant.VIEW_ERROR;
		}
	}
	
	/**
	 * @description: go to tour running of guide
	 * @param request
	 * @param model
	 * @return
	 */
	@GetMapping("/guide/RunningTour")
	public String goToRunningTourPage(HttpServletRequest request, Model model) {
		try {
			if (request.isUserInRole(Constant.ROLE_GUIDE)) {
				model.addAttribute(Constant.ENTITY_GUIDE, guideService.findbyUserName(request.getUserPrincipal().getName()));
			return Constant.VIEW_GUIDE_RUNNING;
			}
			return Constant.VIEW_403;
		}catch (Exception e) {
			log.error("----------guide running tour is error: " + e);
			model.addAttribute(Constant.MESS_ERROR, e);
			return Constant.VIEW_ERROR;
		}
	}
	
	@GetMapping("/guide/showCompletedTour")
	public String goToCompletedTourPage(HttpServletRequest request, Model model) {
		try {
			if (request.isUserInRole(Constant.ROLE_GUIDE)) {
				model.addAttribute(Constant.ENTITY_GUIDE, guideService.findbyUserName(request.getUserPrincipal().getName()));
			return Constant.VIEW_GUIDE_COMPLETED;
		}
		return Constant.VIEW_403;
		}catch (Exception e) {
			log.error("----------guide send request is error: " + e);
			model.addAttribute(Constant.MESS_ERROR, e);
			return Constant.VIEW_ERROR;
		}
	}
	
	@GetMapping("/guide/showRequestTour")
	public String goToRequestTouPage(HttpServletRequest request, Model model) {
		try {
			if (request.isUserInRole(Constant.ROLE_GUIDE)) {
				model.addAttribute(Constant.ENTITY_GUIDE, guideService.findbyUserName(request.getUserPrincipal().getName()));
			return Constant.VIEW_GUIDE_REQUEST;
			}
			return Constant.VIEW_403;
		}catch (Exception e) {
			log.error("----------guide send request is error: " + e);
			model.addAttribute(Constant.MESS_ERROR, e);
			return Constant.VIEW_ERROR;
		}
	}
	
	@GetMapping("/guide/showInvitedTour")
	public String goToInvitedTourPage(HttpServletRequest request, Model model) {
		try {
			if (request.isUserInRole(Constant.ROLE_GUIDE)) {
				model.addAttribute(Constant.ENTITY_GUIDE, guideService.findbyUserName(request.getUserPrincipal().getName()));
			return Constant.VIEW_GUIDE_INVITED;
			}
			return Constant.VIEW_403;
		}catch (Exception e) {
			log.error("----------guide send request is error: " + e);
			model.addAttribute(Constant.MESS_ERROR, e);
			return Constant.VIEW_ERROR;
		}
	}
	
	
	
	
	
	
	@GetMapping("/danhsachhuongdanvien")
	public String adminCon(HttpServletRequest request, Model model) {
		if (request.isUserInRole("ROLE_ADMIN")) {
			Page<Guide> guides = guideService.findAll(new PageRequest(0, 5));
			model.addAttribute("guides", guides);
			model.addAttribute("page", 1);
			model.addAttribute("previouspage", 0);
			model.addAttribute("nextpage", 2);
			return "danhsachhuongdanvien";
		}
		if (request.isUserInRole("ROLE_GUIDE")) {
			return "ERROR";
		}
		return "ERROR";
	}

	@GetMapping("/danhsachhuongdanvien/page/{id}")
	public String adminCon1(@PathVariable("id") int id, HttpServletRequest request, Model model) {
		if (request.isUserInRole("ROLE_ADMIN")) {
			if (id > 0) {
				Page<Guide> guides = guideService.findAll(new PageRequest(id - 1, 5));
				model.addAttribute("guides", guides);
				model.addAttribute("page", id);
				model.addAttribute("previouspage", id - 1);
				model.addAttribute("nextpage", id + 1);
				return "danhsachhuongdanvien";
			} else {
				Page<Guide> guides = guideService.findAll(new PageRequest(0, 5));
				model.addAttribute("guides", guides);
				model.addAttribute("page", 1);
				model.addAttribute("previouspage", 0);
				model.addAttribute("nextpage", 2);
				return "danhsachhuongdanvien";
			}
		}
		if (request.isUserInRole("ROLE_GUIDE")) {
			return "ERROR";
		}
		return "ERROR";
	}

	@GetMapping(value = "/guide/delete/{page}/{id}")
	public String delete(@PathVariable("page") int page, @PathVariable("id") int id, Model model) {
		guideService.delete(id);
		Page<Guide> guides = guideService.findAll(new PageRequest(page - 1, 5));
		model.addAttribute("guides", guides);
		model.addAttribute("page", page);
		model.addAttribute("previouspage", page - 1);
		model.addAttribute("nextpage", page + 1);
		return "danhsachhuongdanvien";
	}

///*	@PostMapping("/guide/changepassword")
//	public String changePass(HttpServletRequest request, Model model) {
//		if (request.isUserInRole("ROLE_GUIDE")) {
//			String oldpassword = request.getParameter("old-passwd");
//			String newpassword = request.getParameter("new-passwd");
//			Principal principal = request.getUserPrincipal();
//			User user = userDetailService.findByUsername(principal.getName());
//			Guide guide = guideService.findByUserId(user.getId());
//			if (BCrypt.checkpw(oldpassword, user.getPassword())) {
//				guide.getUser().setPassword(passwordEncoder.encode(newpassword));
//				model.addAttribute("status", "Ä�á»•i password thÃ nh cÃ´ng");
//			} else {
//				model.addAttribute("status", "Ä�á»•i password tháº¥t báº¡i do sai máº­t kháº©u cÅ©");
//			}
//			guideService.save(guide);
//			model.addAttribute("guide", guide);
//			return "guide-info";
//		} else {
//			return "index";
//		}
//	}*/

	
}
