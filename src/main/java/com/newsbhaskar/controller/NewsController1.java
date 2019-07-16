/*package com.newsbhaskar.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsbhaskar.model.Category;
import com.newsbhaskar.model.News;
import com.newsbhaskar.repository.CategoryRepository;
import com.newsbhaskar.service.NewsService;

@Controller
public class NewsController1 {
	ObjectMapper mapper = new ObjectMapper();
	
	@Autowired NewsService newsService;
	@Autowired CategoryRepository categoryRepo;

	@RequestMapping(value="/publishs",method=RequestMethod.POST, consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	public  ResponseEntity<Object> publishNews(@RequestParam(required=true, value="fileimage") MultipartFile file, @RequestParam(required=false, value="jsondata")String jsondata) throws IOException{

		File convertFile = new File("c://mydownloads//"+file.getOriginalFilename());
		convertFile.createNewFile();
		FileOutputStream fout = new FileOutputStream(convertFile);
		fout.write(file.getBytes());
		fout.close();
		
		News news=mapper.readValue(jsondata, News.class);
		
		System.out.println("category id "+news.getCategories().getCategory());
		Category category=categoryRepo.getOne(Integer.parseInt(news.getCategories().getCategory()));
		//news.setCategories(news.getCategories().getCategory());
		news.setFilename(file.getOriginalFilename().toString());
		news.setCategories(category);
		newsService.publishNews(news);
		return  new ResponseEntity<>("File is uploaded successfully", HttpStatus.OK);
	}
	
}
*/