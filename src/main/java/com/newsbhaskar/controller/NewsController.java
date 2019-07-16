package com.newsbhaskar.controller;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.newsbhaskar.repository.CategoryRepository;
import com.newsbhaskar.service.NewsService;

@Controller
public class NewsController {
	@Autowired CategoryRepository categoryRepo;
	@Autowired NewsService newsService;
	
	@RequestMapping(value="/publish",method=RequestMethod.POST, consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	public  ResponseEntity<Object> publishNews(@RequestParam(required=true, value="fileimage") MultipartFile fileimage, @RequestParam(required=false, value="jsondata")String jsondata) throws IOException{
		newsService.publishNews(fileimage, jsondata);
		return  new ResponseEntity<>("File is uploaded successfully", HttpStatus.OK);
	}
	
	@GetMapping("/capture")
	public ResponseEntity<Object> Capture(@RequestParam(value="urlresource") String urlresource) {
		newsService.captureNews(urlresource);
		return  new ResponseEntity<>("Data is captured successfully", HttpStatus.OK);
	}
	
	@GetMapping("/newscapturing")
	public String NewsCapturing() {
		return "capturingnews";
	}
	
	@GetMapping("/deletenews/{id}")
	@ResponseBody
	public void deleteNews(@PathVariable("id") Integer id) {
		newsService.deleteNewsById(id);
	}
	
	@GetMapping("/deletefeedback/{id}")
	@ResponseBody
	public void deleteFeedback(@PathVariable("id") Integer id) {
		newsService.deleteFeedbackById(id);
	}
	
}
