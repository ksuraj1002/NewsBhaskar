package com.newsbhaskar;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.newsbhaskar.model.Category;
import com.newsbhaskar.repository.CategoryRepository;

@Component
public class CategoryLoader implements ApplicationListener<ContextRefreshedEvent>{
	private static final Logger logger=LoggerFactory.getLogger(CategoryLoader.class);

	@Autowired CategoryRepository categoryRepo;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		/*System.out.println("Data Loading.......");*/
		logger.debug("Data is Loading.......");
		updateCategory();
	}

	private void updateCategory() {
		List<Category> category=categoryRepo.findAll();
		if(category.isEmpty()) {
			category.add(new Category("politics"));
			category.add(new Category("education"));
			category.add(new Category("crime"));
			category.add(new Category("entertainment"));
			category.add(new Category("weather"));
			category.add(new Category("cricket"));
			categoryRepo.saveAll(category);
		}
		else {
			logger.debug("Data are already existed.......");
		}
	}

}
