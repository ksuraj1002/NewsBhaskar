package com.newsbhaskar.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Feedback {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String comments;
	
	@JoinColumn(name="readerid",nullable = true)
	@ManyToOne(fetch = FetchType.LAZY, optional = false,cascade=CascadeType.PERSIST)
	private Reader reader;
	
	@JoinColumn(name="newsid",nullable = true)
	@ManyToOne(fetch = FetchType.LAZY, optional = false, cascade=CascadeType.PERSIST)
	private News news;
	
	public Feedback() {
		
	}
	
	public Feedback(String comments) {
	this.comments=comments;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public Reader getReader() {
		return reader;
	}
	public void setReader(Reader reader) {
		this.reader = reader;
	}
	public News getNews() {
		return news;
	}
	public void setNews(News news) {
		this.news = news;
	}
	
}
