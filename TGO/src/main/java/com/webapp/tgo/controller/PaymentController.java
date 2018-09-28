package com.webapp.tgo.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import com.webapp.tgo.service.PaymentService;

@Controller
public class PaymentController {
	Logger log = LoggerFactory.getLogger(PaymentController.class);
	@Autowired
	PaymentService paymentService;
	
	@PostMapping("operator/payment")
	public String operatorPayToSystem(HttpServletRequest request) {
		
		return null;
	}

}
