package com.newsbhaskar.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.newsbhaskar.model.Category;
import com.newsbhaskar.repository.CategoryRepository;

@RequestMapping(value="/editor",method=RequestMethod.GET)
@Controller
public class EditorController {
	
	private static final Logger logger=LoggerFactory.getLogger(EditorController.class);
	
	@Autowired CategoryRepository categoryRepo;
	
	
	@GetMapping("/dashboard")
	public String getEditorDashboard(Category category,Model model) {
		model.addAttribute("mod",categoryRepo.findAll());
		//this is the changed line of code
		return "dashboard";
	}
}
