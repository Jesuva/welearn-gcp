package com.welearn.model;

public class Course {
	private String name;
	private String description;
	private String price;
	private String chapters;
	private String courseId;

	public Course(String id,String name,String price,String chapters,String description) {
		
		this.courseId = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.chapters = chapters;
	}
	
	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getChapters() {
		return chapters;
	}
	public void setChapters(String chapters) {
		this.chapters = chapters;
	}
	
}
