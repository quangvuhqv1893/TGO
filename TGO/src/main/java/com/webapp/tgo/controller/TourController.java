	package com.webapp.tgo.controller;

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

import com.webapp.tgo.entities.Tour;
import com.webapp.tgo.service.TourService;
import com.webapp.tgo.service.Tour_Guide_Xref_Service;
import com.webapp.tgo.util.Constant;

@Controller
public class TourController {
	Logger log = LoggerFactory.getLogger(TourController.class);
	@Autowired
	private TourService tourService;
	@Autowired
	private Tour_Guide_Xref_Service tour_Guide_Xref_Service;

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
			return tourService.acceptRequest(tourxrefid)? Constant.MESS_SUCCESS : Constant.MESS_FAIL;
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
			if(request.isUserInRole(Constant.ROLE_OPERATOR)|| request.isUserInRole(Constant.ROLE_GUIDE)) {
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
