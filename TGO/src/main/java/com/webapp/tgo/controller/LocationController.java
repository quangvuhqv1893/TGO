package com.webapp.tgo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.webapp.tgo.entities.Location;
import com.webapp.tgo.service.LocationService;

@Controller
public class LocationController {
	@Autowired
	private LocationService locationService;
	
	@RequestMapping(value="/location/id/{id}", method= RequestMethod.GET)
	public String getLocationbyId(@PathVariable("id") int id,Model model) {
		Location location= locationService.findOne(id);
		model.addAttribute("location",location);
		return "location";
		
	}
}
