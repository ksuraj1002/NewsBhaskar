package com.newsbhaskar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newsbhaskar.model.Reader;

public interface ReaderRepository extends JpaRepository<Reader, Integer> {
	Reader findByEmail(String email);
	Reader findByMobile(String mobile);
}
