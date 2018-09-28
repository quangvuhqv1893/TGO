package com.webapp.tgo.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.webapp.tgo.entities.Operator;
import com.webapp.tgo.service.GuideService;
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
			log.info("error in approve tour ", e);
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
			log.info("error in approve tour ", e);
			return Constant.MESS_FAIL;
		}
	}
}
