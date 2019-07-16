package com.newsbhaskar.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.util.List;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsbhaskar.model.Category;
import com.newsbhaskar.model.Feedback;
import com.newsbhaskar.model.News;
import com.newsbhaskar.model.Reader;
import com.newsbhaskar.repository.CategoryRepository;
import com.newsbhaskar.repository.FeedbackRepository;
import com.newsbhaskar.repository.NewsRepository;
import com.newsbhaskar.repository.ReaderRepository;

@Service
public class NewsServiceImpl implements NewsService {
	ObjectMapper mapper = new ObjectMapper();
	
	@Autowired NewsRepository newsRepo;
	@Autowired CategoryRepository categoryRepo;
	@Autowired ReaderRepository readerRepo;
	@Autowired FeedbackRepository feedbackRepo;
	
	int visitingNum=0;
	
	@Override
	public void publishNews(MultipartFile fileimage, String jsondata) throws FileNotFoundException, IOException {
		File convertFile = new File("C:/xampp/htdocs/mydownloads/"+fileimage.getOriginalFilename());
		convertFile.createNewFile();
		FileOutputStream fout = new FileOutputStream(convertFile);
		fout.write(fileimage.getBytes());
		fout.close();
	
		News news=mapper.readValue(jsondata, News.class);
		
		Category category=categoryRepo.getOne(Integer.parseInt(news.getCategories().getCategory())); 
		
		//System.out.println("categoryies "+newsRepo.findAllByNewscategory(category.getCategory()).size());
		//int num=newsRepo.findAllByNewscategory(category.getCategory()).size();
		
		int num=newsRepo.countByCategories(category);
		category.setNumOfnews(++num);
		
		System.out.println("number of records "+num+" abd categirt "+category);
		
		news.setFilename(fileimage.getOriginalFilename().toString());
		news.setCategories(category);
		news.setDatetime(LocalDateTime.now().plusHours(2));
		newsRepo.save(news);
	}

	
	/*@Override
	public News findNewsById(Integer id) {
		News news=newsRepo.getOne(id);
		    visitingNum=news.getVisitingNum();
			if(visitingNum>=0) {
				visitingNum++;
				news.setVisitingNum(visitingNum);
				newsRepo.save(news);
			}
		return newsRepo.getOne(id);
	}*/


	@Override
	public void captureNews(String urlresource) {
		try {
			URL url = new URL(urlresource);
			URLConnection urlcon = url.openConnection();

			InputStream stream = urlcon.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(stream));
			String line=null;
			String lnkpage=null;
			while ((line = br.readLine()) != null) {
				lnkpage+=line;
			}

			String headline=null,leadparagraph=null,filename=null,body=null,imgCaption=null,categories=null,editedby=null;
			
			if(urlresource.contains("https://sports.ndtv.com/")) {
				System.out.println("hello");
				categories=urlresource.substring(urlresource.indexOf("https://sports.ndtv.com/")+"https://sports.ndtv.com/".length(),urlresource.lastIndexOf("/"));
				if(categories.equalsIgnoreCase("world-cup-2019")) {
					categories="cricket";
				}
				System.out.println(categories);
				String headings=lnkpage.substring(lnkpage.indexOf("<h1 itemprop=\"headline\">"),lnkpage.indexOf("<div class=\"nd-right date-top\">"));
				headline=headings.substring(headings.indexOf("<h1 itemprop=\"headline\">")+"<h1 itemprop=\"headline\">".length(),headings.indexOf(" </h1></div>"));
				System.out.println(headline);

				leadparagraph=lnkpage.substring(lnkpage.indexOf("itemprop=\"description\"><h2>")+"itemprop=\"description\"><h2>".length(),lnkpage.indexOf("</h2></div>"));
				System.out.println(leadparagraph);
				
				String editor=lnkpage.substring(lnkpage.indexOf("<div class=\"post-meta\""),lnkpage.indexOf("<div class=\"nd-right\"><div class=\"content-wrap\""));
				editedby=editor.substring(editor.indexOf("<span itemprop=\"name\">")+"<span itemprop=\"name\">".length(),editor.indexOf("</span>"));
				System.out.println(editedby);
				
				imgCaption=lnkpage.substring(lnkpage.indexOf(" <div itemprop=\"image\""),lnkpage.indexOf("<meta itemprop=\"url\""));
				filename=imgCaption.substring(imgCaption.indexOf("'><img src='")+"'><img src='".length(),imgCaption.indexOf("' class='caption'"));
				System.out.println(filename);
				
				body=lnkpage.substring(lnkpage.indexOf("<div itemprop=\"articleBody\">"),lnkpage.indexOf(" <meta itemprop=\"datePublished\""));
			
				News news=new News(headline,leadparagraph,filename,body,editedby);
				Category category=categoryRepo.findByCategory(categories);
			
				System.out.println("is id asdf asdf asdf sdf "+category.getId());
				int num=newsRepo.countByCategories(category);
				category.setNumOfnews(++num);
				
				news.setDatetime(LocalDateTime.now().plusHours(2));
				news.setCategories(category);
				newsRepo.save(news);
			}

			else if(urlresource.contains("india-news")||urlresource.contains("delhi-news")||urlresource.contains("andhra-pradesh-news")){
						categories="politics";

					String headings=lnkpage.substring(lnkpage.indexOf("<div class=\"ins_headline\">")+"<div class=\"ins_headline\">".length(),lnkpage.indexOf("<span itemprop=\"headline\""));
					headline=headings.substring(headings.indexOf("<h1><span>")+"<h1><span>".length(),headings.indexOf("</span></h1>"));
					System.out.println(headline);

					leadparagraph=headings.substring(headings.indexOf("<h2 class=\"ins_descp\"> ")+"<h2 class=\"ins_descp\"> ".length(),headings.indexOf(" </h2>"));
					System.out.println(leadparagraph);

					imgCaption=lnkpage.substring(lnkpage.indexOf("<img title=\""),lnkpage.indexOf("<p class=\"mainimage_caption\">"));
					filename=imgCaption.substring(imgCaption.indexOf("src=\"")+"src=\"".length(),imgCaption.indexOf("\" /></div>"));
					System.out.println(filename);

					String editor=lnkpage.substring(lnkpage.indexOf("<div class=\"ins_dateline\">"),lnkpage.indexOf("<span itemprop=\"dateModified\""));
					editedby=editor.substring(editor.lastIndexOf("<span itemprop=\"name\">")+"<span itemprop=\"name\">".length(),editor.indexOf("</span></span></a> "));

					body=lnkpage.substring(lnkpage.indexOf("<div itemprop=\"articleBody\""),lnkpage.indexOf("<div class=\"comments_slide dismiss\">"));
						News news=new News(headline,leadparagraph,filename,body,editedby);
						Category category=categoryRepo.findByCategory(categories);
						
						int num=newsRepo.countByCategories(category);
						category.setNumOfnews(++num);
						
						news.setDatetime(LocalDateTime.now().plusHours(2));
						news.setCategories(category);
						newsRepo.save(news);
					}
				else{
					
					//for science,education,entertainment,worldnews
					categories=urlresource.substring(urlresource.indexOf("https://www.ndtv.com/")+"https://www.ndtv.com/".length(), urlresource.lastIndexOf("/"));
					
					String headings=lnkpage.substring(lnkpage.indexOf("<div class=\"ins_headline\">")+"<div class=\"ins_headline\">".length(),lnkpage.indexOf("<span itemprop=\"headline\""));
					headline=headings.substring(headings.indexOf("<h1><span>")+"<h1><span>".length(),headings.indexOf("</span></h1>"));
					System.out.println(headline);

					leadparagraph=headings.substring(headings.indexOf("<h2 class=\"ins_descp\"> ")+"<h2 class=\"ins_descp\"> ".length(),headings.indexOf(" </h2>"));
					System.out.println(leadparagraph);

					imgCaption=lnkpage.substring(lnkpage.indexOf("<img title=\""),lnkpage.indexOf("<p class=\"mainimage_caption\">"));
					filename=imgCaption.substring(imgCaption.indexOf("src=\"")+"src=\"".length(),imgCaption.indexOf("\" /></div>"));
					System.out.println(filename);

					String editor=lnkpage.substring(lnkpage.indexOf("<div class=\"ins_dateline\">"),lnkpage.indexOf("<span itemprop=\"dateModified\""));
					editedby=editor.substring(editor.lastIndexOf("<span itemprop=\"name\">")+"<span itemprop=\"name\">".length(),editor.indexOf("</span></span></a> "));

					body=lnkpage.substring(lnkpage.indexOf("<div itemprop=\"articleBody\""),lnkpage.indexOf("<div class=\"comments_slide dismiss\">"));
						News news=new News(headline,leadparagraph,filename,body,editedby);
						Category category=categoryRepo.findByCategory(categories);
						
						int num=newsRepo.countByCategories(category);
						category.setNumOfnews(++num);
						
						news.setDatetime(LocalDateTime.now().plusHours(2));
						news.setCategories(category);
						newsRepo.save(news);
				}

		} catch (Exception e) {
			System.out.println("in end "+e.getMessage());
		}
	}

	@Override
	public List<News> findByCategories(Category category) {
		return newsRepo.findAllByCategoriesOrderByIdDesc(category);
	}


	@Override
	public List<News> findPopularNewsByCategory(Category category) {
		return newsRepo.findFirst10ByCategoriesOrderByVisitingNumDesc(category);
	}


	@Override
	public News findNewsByNatureIdHeadlines(Category category, Integer id, String headlines) {
		News news=newsRepo.findByCategoriesAndIdAndHeadline(category,id,headlines);
		  visitingNum=news.getVisitingNum();
			if(visitingNum>=0) {
				visitingNum++;
				news.setVisitingNum(visitingNum);
				newsRepo.save(news);
			}
			return news;
	}
	
	@Override
	public void submitCommentsAndReaderDetails(String commentObj) throws ParseException {
		JSONParser parser = new JSONParser();
		org.json.simple.JSONObject json=(org.json.simple.JSONObject) parser.parse(commentObj);
		Reader reader = null;
		String mobileoremail=(String)json.get("emailorphone");
		
		String email=null;
		String mobile = null;
		
		if(checkMobile(mobileoremail)) {
			 mobile=mobileoremail;
		}
		else if(checkEmail(mobileoremail)) {
			email=mobileoremail;
		}
		
		News news=newsRepo.getOne(Integer.parseInt((String)json.get("newsid")));
		Feedback feedback=new Feedback((String)json.get("comments"));
		
		if((email!=null)) {
			try {
				reader=readerRepo.findByEmail(email);
			}catch(Exception e) {
				System.out.print("here is exception "+e.getMessage());
			}
			 if(reader!=null) {
				 feedback.setReader(reader);
			 }
			 else {
				 reader=new Reader((String)json.get("name"),mobile,email);
				 feedback.setReader(reader);
			 }
		}
		
		else if((mobile!=null)) {
			try {
				reader=readerRepo.findByMobile(mobile);
			}catch(Exception e) {
				System.out.print("here is exception "+e.getMessage());
			}
			 if(reader!=null) {
				 feedback.setReader(reader);
			 }
			 else {
				 reader=new Reader((String)json.get("name"),mobile,email);
				 feedback.setReader(reader);
			 }
		}
		
		
		feedback.setNews(news);
		
		feedbackRepo.save(feedback);
	}
	
	public boolean checkMobile(String mobileoremail) {
		for(int i=0;i<mobileoremail.length(); i++) {
			if(!(Character.isDigit(mobileoremail.charAt(i)))) {
				return false;
			}
		}
		return true;
	}
	
	public boolean checkEmail(String mobileoremail) {
		if(mobileoremail.contains("@")) {
			return true;
		}
		return false;
	}


	@Override
	public void deleteNewsById(Integer id) {
		newsRepo.deleteById(id);
	}


	@Override
	public void deleteFeedbackById(Integer id) {
		feedbackRepo.deleteById(id);
	}

	@Override
	public News downloadImageFile(Integer id) {

		return newsRepo.getOne(id);
	}

}
