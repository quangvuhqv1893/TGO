package com.webapp.tgo.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.webapp.tgo.entities.Language;
import com.webapp.tgo.entities.Location;
import com.webapp.tgo.entities.User;
import com.webapp.tgo.service.LanguageService;
import com.webapp.tgo.service.LocationService;
import com.webapp.tgo.service.UserDetailService;
import com.webapp.tgo.util.Constant;

@Controller
public class UserController {

	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	@Autowired
	private UserDetailService userDetailService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private LocationService locationService;
	
	@Autowired
	private LanguageService languageService;

	@GetMapping({"/","/index","/login"})
	public String index(Model model) {
		model.addAttribute(Constant.ENTITY_LIST_LANGUAE, (List<Language>) languageService.findAll());
		model.addAttribute(Constant.ENTITY_LIST_LOCATION, (List<Location>) locationService.findAll());
		return "index";
	}
	@GetMapping("/default")
	public String defaultAfterLogin(HttpServletRequest request) {
		if (request.isUserInRole("ROLE_ADMIN")) {
			log.info("------login admin successfull!!!");
			return "redirect:/admin";
		}
		if (request.isUserInRole("ROLE_GUIDE")) {
			log.info("------login guide successfull!!!");
			return "redirect:/guide";
		}
		log.info("------login operator successfull!!!");
		return "redirect:/operator";
	}

	@GetMapping("/home")
	public String home(HttpServletRequest request) {
		if (request.isUserInRole("ROLE_ADMIN")) {
			return "redirect:/admin";
		}
		if (request.isUserInRole("ROLE_GUIDE")) {
			return "redirect:/guide";
		}
		if (request.isUserInRole("ROLE_OPERATOR"))
			return "redirect:/operator";
		return "redirect:/index";
	}

	@GetMapping("/403")
	public String accessDenied() {
		return "403";
	}
	
	@ResponseBody
	@PostMapping("/signin")
	public String signIn(@Param("username") String username, @Param("password") String password) {
		try {
			User user = userDetailService.findByUsername(username);
			if(user==null) {
				log.error("user not exist!!!");
				return  Constant.MESS_NOT_EXIST;
			}
			if(!BCrypt.checkpw(password, user.getPassword())) {
				log.error("invaild username or password!!!");
				return Constant.MESS_FAIL;
			}
			return user.getStatus()==1? Constant.MESS_SUCCESS : Constant.MESS_NOT_APPROVAL;
		} catch (Exception e) {
			log.error("invaild username or password!!!");
			return Constant.MESS_FAIL;
		}
	}
	/**
	 * @description: go logout method
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request, HttpServletResponse response, Model model ) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			new SecurityContextLogoutHandler().logout(request, response, authentication);
		}
		model.addAttribute(Constant.ENTITY_LIST_LANGUAE, (List<Language>) languageService.findAll());
		model.addAttribute(Constant.ENTITY_LIST_LOCATION, (List<Location>) locationService.findAll());
		return "index";
	}
	/**
	 * @description: signup method, first check username if user not exist then
	 *               check password and repasword, if true then update in table.
	 * @param request
	 * @param model
	 * @return
	 */
	//ko return ve view ma return ve string cho js bat
	@ResponseBody
	@PostMapping("/signup")
	public String signUp(HttpServletRequest request) {
		try {
			log.info("-------signup page");
		String username = request.getParameter(Constant.PARAMETER_USERNAME);
		if (userDetailService.findByUsername(username)!=null) {
			log.error("----------username is exist!!");
			return Constant.MESS_EXIST_USER;
		}
		log.info("----------username is not exist!!");
		User user = new User();
		user.setUsername(username);
		user.setPassword(passwordEncoder.encode(request.getParameter(Constant.PARAMETER_PASSWORD)));
		user.setEmail(request.getParameter(Constant.PARAMETER_EMAIL));
		user.setPhonenumber(request.getParameter(Constant.PARAMETER_PHONE)) ;
		user.setAddress(request.getParameter(Constant.PARAMETER_ADDRESS));
		user.setFullname(request.getParameter(Constant.PARAMETER_FULLNAME));
		user.setStatus(Constant.VALUE_APPROVAL_USER);
		String accountType = request.getParameter(Constant.PARAMETER_ACC_TYPE);
		log.info("username: "+username);
		log.info("password: "+user.getPassword());
		log.info("email: "+user.getEmail());
		log.info("phone: "+user.getPhonenumber());
		log.info("setAddress: "+user.getAddress());
		log.info("setFullname: "+user.getFullname());
		if (Constant.ENTITY_OPERATOR.equalsIgnoreCase(accountType)) {
			String companyName = request.getParameter(Constant.PARAMETER_NAME_VIET);
			String representative = request.getParameter(Constant.PARAMETER_NAME_REPRESENTATIVE);
			String businessRegistrationCode = request.getParameter(Constant.PARAMETER_BUSINESS_REGISTRATION_CODE);
			String taxCode = request.getParameter(Constant.PARAMETER_TAX_CODE);
			log.info("companyName: "+companyName);
			log.info("representative: "+representative);
			log.info("businessRegistrationCode: "+businessRegistrationCode);
			log.info("taxCode: "+taxCode);
			log.info("-------end signup operator page");
			return userDetailService.createNewOperator(user, companyName,representative,businessRegistrationCode,taxCode);
		}else {
		String birthDate = request.getParameter(Constant.PARAMETER_BIRTH_DATE);
		String language = request.getParameter(Constant.PARAMETER_LANGUAGE);
		String location = request.getParameter(Constant.PARAMETER_LOCATION);
		String cardNumber = request.getParameter(Constant.PARAMETER_CARD_NUMBER);
		String cardType = request.getParameter(Constant.PARAMETER_CARD_TYPE);
		String expirationDate = request.getParameter(Constant.PARAMETER_EXPIRATION_DATE);
		String year = request.getParameter(Constant.PARAMETER_YEAR);
		String month = request.getParameter(Constant.PARAMETER_MONTH);
		String day = request.getParameter(Constant.PARAMETER_DAY);
		StringBuffer exp = new StringBuffer( "".equalsIgnoreCase(year)?"0":year);
		exp.append(Constant.PARAMETER_NAM);
		exp.append("".equalsIgnoreCase(month)?"0":month);
		exp.append(Constant.PARAMETER_THANG);
		exp.append("".equalsIgnoreCase(day)?"0":day);
		exp.append(Constant.PARAMETER_NGAY);
		String gender = request.getParameter(Constant.PARAMETER_GENDER);
		log.info("birthDate: "+birthDate);
		log.info("language: "+language);
		log.info("location: "+location);
		log.info("cardNumber: "+cardNumber);
		log.info("cardType: "+cardType);
		log.info("expirationDate: "+expirationDate);
		log.info("exp: "+exp);
		log.info("gender: "+gender);
		log.info("-------end signup guide page");
		return userDetailService.createNewGuide(user,birthDate,language,location,cardNumber,cardType,expirationDate,exp,gender);
		
		}
		}catch (Exception e) {
			log.error("sign up has been error: ",e);
			return Constant.MESS_FAIL;
		}
	}
	/**
	 * @description: change passwpord
	 * @param request
	 * @param model
	 * @return
	 */
	@PostMapping("/changepassword")
	public String changePass(HttpServletRequest request,RedirectAttributes reAttributes) {
		try {
		if(request.isUserInRole(Constant.ROLE_OPERATOR)||request.isUserInRole(Constant.ROLE_GUIDE)) {
			String oldPassword = request.getParameter(Constant.PARAMETER_OLD_PASS);
			String newPassword = request.getParameter(Constant.PARAMETER_NEW_PASS);
			String mess = null;
			User user = userDetailService.findByUsername(request.getUserPrincipal().getName());
//			model.addAttribute(Constant.STATUS,userDetailService.changePassword(user, oldPassword, newPassword));
			if(BCrypt.checkpw(oldPassword, user.getPassword())) {
				user.setPassword(passwordEncoder.encode(newPassword));
				mess= userDetailService.save(user);
				log.info(mess);
				log.info(Constant.MESS_CHANGE_PASSWORD_SUCCESS);
			}else {
				log.info(Constant.MESS_CHANGE_PASS_IS_FAIL);
				mess= Constant.MESS_CHANGE_PASSWORD_FAIL;
			}
			reAttributes.addFlashAttribute(Constant.STATUS,mess);
			return request.isUserInRole(Constant.ROLE_OPERATOR)? Constant.VIEW_REDIRECT_OPERATOR_INFO : Constant.VIEW_REDIRECT_GUIDE_INFO;
		}
			return Constant.VIEW_403;
	}catch (Exception e) {
		log.info("----------reset password fail!");
		log.error("exception in method changeInfo: " + e);
		reAttributes.addFlashAttribute(Constant.MESS_ERROR, e);
		return Constant.VIEW_ERROR; 
	}
	}

}
