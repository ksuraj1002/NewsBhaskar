package com.newsbhaskar.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.json.simple.parser.ParseException;
import org.springframework.web.multipart.MultipartFile;

import com.newsbhaskar.model.Category;
import com.newsbhaskar.model.Feedback;
import com.newsbhaskar.model.News;
import com.newsbhaskar.model.Reader;

public interface NewsService {
	void publishNews(MultipartFile fileimage, String jsondata) throws FileNotFoundException, IOException;
	//News findNewsById(Integer id);
	void captureNews(String urlresource);
	List<News> findByCategories(Category category);
	List<News> findPopularNewsByCategory(Category category);
	News findNewsByNatureIdHeadlines(Category category, Integer id, String headlines);
	void submitCommentsAndReaderDetails(String commentObj) throws ParseException;
	void deleteNewsById(Integer id);
	void deleteFeedbackById(Integer id);

	News downloadImageFile(Integer id);
}
