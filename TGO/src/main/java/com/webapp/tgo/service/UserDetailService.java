package com.webapp.tgo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.webapp.tgo.entities.User;

public interface UserDetailService extends UserDetailsService {

	User findByUsername(String username);

	String save(User user);

	UserDetails loadUserByUsername(String s) throws UsernameNotFoundException;

	String changePassword(User user, String oldPassword, String newPassword);


	String createNewOperator(User user, String companyName, String representative, String businessRegistrationCode,
			String taxCode);

	String createNewGuide(User user, String birthDate, String language, String location, String cardNumber,
			String cardType, String expirationDate, StringBuffer exp, String gender);

	String cancelApproveUser(int userid, int role);

	String acceptApproveUser(int userid, int role);

}