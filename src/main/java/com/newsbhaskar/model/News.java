package com.newsbhaskar.model;


import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;



@Entity
public class News {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private int visitingNum;
	private String headline;
	private LocalDateTime datetime;
	private String leadparagraph;
	private String editedby;
	@Column(length=50000)
	private String body;
	@Column(length=50000)
	private String filename;
	
	@JoinColumn(name="categoryid",nullable = true)
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private Category categories;
	
	@OneToMany(fetch=FetchType.LAZY,cascade=CascadeType.ALL,mappedBy="news")
	private List<Feedback> feedback;
	
	public News() {
		
	}
	
	public News(String headline, String leadparagraph, String filename, String body,String editedby) {
		this.headline=headline;
		this.leadparagraph=leadparagraph;
		this.filename=filename;
		this.body=body;
		this.editedby=editedby;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getHeadline() {
		return headline;
	}

	public void setHeadline(String headline) {
		this.headline = headline;
	}

	public void setDatetime(LocalDateTime datetime) {
		this.datetime = datetime;
	}

	public LocalDateTime getDatetime() {
		return datetime;
	}

	public String getLeadparagraph() {
		return leadparagraph;
	}

	public void setLeadparagraph(String leadparagraph) {
		this.leadparagraph = leadparagraph;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Category getCategories() {
		return categories;
	}

	public void setCategories(Category categories) {
		this.categories = categories;
	}
	
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public int getVisitingNum() {
		return visitingNum;
	}

	public void setVisitingNum(int visitingNum) {
		this.visitingNum = visitingNum;
	}

	public List<Feedback> getFeedback() {
		return feedback;
	}

	public void setFeedback(List<Feedback> feedback) {
		this.feedback = feedback;
	}

	public String getEditedby() {
		return editedby;
	}

	public void setEditedby(String editedby) {
		this.editedby = editedby;
	}
}
