package com.newsbhaskar.controller;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.newsbhaskar.model.*;
import com.newsbhaskar.repository.EditorRepository;
import com.newsbhaskar.repository.ResumeRepository;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsbhaskar.repository.CategoryRepository;
import com.newsbhaskar.repository.NewsRepository;
import com.newsbhaskar.service.EditorService;
import com.newsbhaskar.service.NewsService;

@Controller
public class RequestHandler {
	@Autowired NewsRepository newsRepo;
	@Autowired CategoryRepository cateRepo;
	@Autowired EditorService editorService;
	@Autowired NewsService newsService;
	@Autowired
	EditorRepository editorRepository;

	ObjectMapper objectMapper = new ObjectMapper();
	
	@GetMapping("/career")
	public String getCareer(Model model) {
		return "career";
	}
	
	/*@PostMapping("/applyprofile")
	public String applyProfile(Editor editor,Address address) {
		editorService.applyProfile(editor, address);
		return "redirect:/career";
	}*/
	
	@GetMapping("/")
	public String getHome(Model model) {
		List<News> news=newsRepo.findFirst20ByOrderByIdDesc();
		
		List<News> topstories=newsRepo.findFirst30ByOrderByIdDesc();
		System.out.println(topstories.get(1).getHeadline());
		
		List<News> mostPopular=newsRepo.findFirst10ByOrderByVisitingNumDesc();
	
		List<News> tops=new ArrayList<News>(topstories.subList(topstories.size()-10, topstories.size()));
	
		model.addAttribute("newsData",news);
		model.addAttribute("mostPopular",mostPopular);
		model.addAttribute("catData",cateRepo.findAll());
		
		model.addAttribute("latest",tops);
		
		return "index";
	}
	
	@GetMapping("redirectsto/{nature}/{id}/{headlines}")
	public String getNewsBody(@PathVariable String nature,@PathVariable Integer id, @PathVariable String headlines,Model model) {
		Category category=cateRepo.findByCategory(nature);
		/*model.addAttribute("reader", new Reader());
		model.addAttribute("feedback", new FeedbackRepository());*/
		try{
			model.addAttribute("newsBody", newsService.findNewsByNatureIdHeadlines(category,id,headlines));
		}catch(Exception e) {
			return "404";
		}
		return "newsbody";
	}
	
	@GetMapping("redirectsto-captured/{nature}/{id}/{headlines}")
	public String getCapturedNewsBody(@PathVariable String nature,@PathVariable Integer id, @PathVariable String headlines,Model model) {
		Category category=cateRepo.findByCategory(nature);
		try{
			model.addAttribute("newsBody", newsService.findNewsByNatureIdHeadlines(category,id,headlines));
		}catch(Exception e) {
			System.out.println(e.getMessage());
			return "404";
		}
		return "captured";
	}
	
	@GetMapping("newsof/{nature}/{id}")
	public String getCategorizedNews(@PathVariable String nature,@PathVariable Integer id,Model model) {
		int listsize;
		Category category=cateRepo.findByCategoryAndId(nature,id);
		if(category==null) {
			return "404";
		}
		List<News> categorizedNews=newsService.findByCategories(category);
		
		List<News> mostPopular=newsService.findPopularNewsByCategory(category);
	
		if(categorizedNews.size()>6) {
			listsize=6;
		}
		else {
			listsize=categorizedNews.size();
		}
		
		//List<News> latestList=new ArrayList<News>();
		
		model.addAttribute("newsData",new ArrayList<News>(categorizedNews.subList(0, listsize)));
		
		model.addAttribute("mostPopular",mostPopular);
		model.addAttribute("catData",cateRepo.findAll());
		
		/*for(int i=0; i<categorizedNews.size(); i++) {
			if(categorizedNews.get(i).getDatetime()!=null) {
				if(LocalDateTime.now().isBefore(categorizedNews.get(i).getDatetime())) {
					latestList.add(categorizedNews.get(i));
				}
			}
		}*/
		//model.addAttribute("latest",latestList);
		return "newsbycategory";
	}
	
	
	
	@RequestMapping(value="/applyprofile",method=RequestMethod.POST, consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	public  ResponseEntity<Object> applyProfile(@RequestParam(required=true, value="biodata") MultipartFile biodata, @RequestParam(required=false, value="jsondata")String jsondata) throws IOException, ParseException, java.text.ParseException{
		editorService.applyProfile(biodata, jsondata);
		return  new ResponseEntity<>("File is uploaded successfully", HttpStatus.OK);
	}
	
	@PostMapping("/submitcomment")
	public String submitComments(@RequestParam(required=false, value="jsonData")String jsonData) throws ParseException {
		newsService.submitCommentsAndReaderDetails(jsonData);
		return "video-post";
	}

}
