package com.webapp.tgo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.webapp.tgo.entities.Landscapes;
import com.webapp.tgo.service.LandscapesService;

@Controller
public class LandscapesController {
	@Autowired
	private LandscapesService landscapesService;
	@RequestMapping(value="/land/id/{id}", method= RequestMethod.GET)
	public String getContractrbyId(@PathVariable("id") int id,Model model) {
		Landscapes land= landscapesService.findOne(id);
		model.addAttribute("land",land);
		return "landscapes";
		
	}
}
