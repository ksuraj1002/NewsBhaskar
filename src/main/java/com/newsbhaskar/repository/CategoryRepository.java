package com.newsbhaskar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newsbhaskar.model.Category;
public interface CategoryRepository extends JpaRepository<Category, Integer> {
	Category findByCategory(String string);
	Category findByCategoryAndId(String nature, Integer id);
}
