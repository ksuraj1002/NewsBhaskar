package com.newsbhaskar.controller;

import com.newsbhaskar.model.Editor;
import com.newsbhaskar.service.EditorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.newsbhaskar.model.Category;
import com.newsbhaskar.repository.CategoryRepository;

@Controller
@RequestMapping(value="/admin",method=RequestMethod.GET)
public class AdminController {
	private static final Logger logger=LoggerFactory.getLogger(AdminController.class);
	@Autowired CategoryRepository categoryRepo;
	@Autowired 	EditorService editorService;
	
	@PostMapping("/addcategory")
	public String addCategory(@RequestParam("category") String paracategory) {
		categoryRepo.save(new Category(paracategory));
		logger.warn("no "+paracategory);
		return "admin/dashboard";
	}
	
	@GetMapping("/deletecategory/{id}")
	@ResponseBody
	public String deleteCategory(@PathVariable("id") int id) {
		categoryRepo.deleteById(id);
		return "deleted ";
	}
	
	@GetMapping("/dashboard")
	public String getAdminDashboard() {
		return "admin/dashboard";
	}

	@GetMapping("/newapplicants")
	public String getApplicant(Model model){
		model.addAttribute("newApplicant",editorService.findAllNewApplicant("tobeapproved"));
		return "admin/newapplicant";
	}

	@GetMapping("/{path}/{id}")
	public String getParticularApplicantDetails(Model model,@PathVariable("path") String path,@PathVariable("id") Integer id,@RequestParam("details") String details){
		model.addAttribute("detailsof",editorService.findEditorById(id));
		model.addAttribute("msg",details);
		return "admin/pageofdetails";
	}

	@GetMapping("/downloadfile/{id}")
	public ResponseEntity downloadFile(@PathVariable("id") Integer id) {
		// Load file from database
		Editor dbFile = editorService.getBiodata(id);

		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(dbFile.getFileType()))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dbFile.getFileName() + "\"")
				.body(new ByteArrayResource(dbFile.getBioData()));
	}

	@GetMapping("/newapplicants/{id}/acknowledge")
	@ResponseBody
	public String sendAck(@PathVariable("id") Integer id){
		editorService.sendAck(id);
		return "done";
	}

	@GetMapping("/{path}")
	public String getPendingApplicant(@PathVariable("path") String path,@RequestParam("param") String param,Model model){
		model.addAttribute("msg",param);
		model.addAttribute("list",editorService.findAllApplicantAndEditor());
		return "admin/applicant_and_editor";
	}

	@GetMapping("/newslist")
	public String getNewsList(){
		return "admin/newslist";
	}

}
