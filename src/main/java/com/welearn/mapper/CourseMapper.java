package com.welearn.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.welearn.model.Course;

public class CourseMapper implements RowMapper<Course>{
	
	public Course mapRow(ResultSet rs, int rowNum) throws SQLException{
		
		String courseId = rs.getString("courseId");
		String name = rs.getString("courseName");
		String price = rs.getString("CoursePrice");
		String chapters = rs.getString("chapters");
		String description = rs.getString("courseDescription");
		return new Course(courseId,name,price,chapters,description);
		
	}

}
