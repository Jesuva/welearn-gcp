package com.welearn.dao;

import java.util.List;

import com.google.appengine.api.datastore.Entity;
import com.welearn.model.Course;

public interface CourseInterface {
	
	public List<Course> getAllCourses();
	
	public List<Entity> getAllAvailableCourses(String email);
	
	public List<Entity> getAllEnrolledCourses(String email);
	
	public boolean checkCourseName(String name);
	
	public boolean addCourse(String name,String price,String description,String chapters,String email);
	
	public boolean updateCourse(String name,String price,String description,String chapters,long courseId,String email);
	
	public Entity getCourseDetails(long courseId);
	
	public List<Entity> getCourseCreatedByUser(String name);
	
	public Entity enrollCourse(String name, long courseId);
	
	public Entity unenrollCourse(String email, long courseId);
	
	
}