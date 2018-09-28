package com.webapp.tgo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.webapp.tgo.entities.Contract;
import com.webapp.tgo.service.ContractService;

@Controller
public class ContractController {
	@Autowired
	private ContractService contractService;
	
	@RequestMapping(value="/contract/id/{id}", method= RequestMethod.GET)
	public String getContractrbyId(@PathVariable("id") int id,Model model) {
		Contract contract= contractService.findOne(id);
		model.addAttribute("contract",contract);
		return "contract";
		
	}
}
