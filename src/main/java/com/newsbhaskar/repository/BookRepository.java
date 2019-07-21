package com.newsbhaskar.repository;

import com.newsbhaskar.model.Books;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Books,Integer> {
}
