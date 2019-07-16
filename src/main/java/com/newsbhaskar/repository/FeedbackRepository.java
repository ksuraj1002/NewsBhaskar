package com.newsbhaskar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newsbhaskar.model.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {

}
