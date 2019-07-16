package com.newsbhaskar.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newsbhaskar.model.Category;
import com.newsbhaskar.model.News;

public interface NewsRepository extends JpaRepository<News, Integer> {
	//public List<News> findAllByOrderByIdDesc();
	public int countByCategories(Category category);
	public List<News> findAllByCategoriesOrderByIdDesc(Category category);
	public List<News> findFirst10ByOrderByVisitingNumDesc();
	public List<News> findFirst10ByCategoriesOrderByVisitingNumDesc(Category category);
	public News findByCategoriesAndIdAndHeadline(Category category, Integer id, String headlines);
	public List<News> findFirst20ByOrderByIdDesc();
	public List<News> findFirst30ByOrderByIdDesc();
}
