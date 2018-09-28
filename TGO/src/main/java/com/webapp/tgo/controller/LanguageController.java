package com.webapp.tgo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.webapp.tgo.entities.Language;
import com.webapp.tgo.service.LanguageService;

@Controller
public class LanguageController {
	@Autowired
	private LanguageService languageService;
	
	@RequestMapping(value="/language/id/{id}", method= RequestMethod.GET)
	public String getLanguagebyId(@PathVariable("id") int id,Model model) {
		Language language= languageService.findOne(id);
		model.addAttribute("language",language);
		return "language";
		
	}
}
