package com.welearn.services;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.welearn.dao.CourseInterface;
import com.welearn.mapper.CourseMapper;
import com.welearn.mapper.LoginMapper;
import com.welearn.model.Course;
import com.welearn.model.Login;

@Service
public class CourseServices extends JdbcDaoSupport implements CourseInterface {
	private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	DatastoreService datastore =  DatastoreServiceFactory.getDatastoreService();

	@Autowired
    public CourseServices (DataSource datasource) {
        this.setDataSource(datasource);
    }
	
	public List<Course> getAllCourses() {
		try {
			String sql = "SELECT * FROM courses;";
			Object[] params = new Object[] {  };
	        CourseMapper mapper = new CourseMapper();
	        List<Course> courseInfo = this.getJdbcTemplate().query(sql, params, mapper);
            return courseInfo;
			
		}
		catch(Exception e) {
			logger.warning(e.getMessage());
		}
		return null;
	}
	
	public List<Entity> getAllAvailableCourses(String email){
		try {
			MessageDigest sha2 = MessageDigest.getInstance("SHA-256");
			sha2.update(email.getBytes());
			BigInteger hash = new BigInteger(1,sha2.digest());
			String userId = hash.toString(16);
			List<Long> enrolledCoursesId = new ArrayList<Long>();
			List<Long> availableCoursesId = new ArrayList<Long>();
			
			Query q = new Query("CourseEnrollment").addFilter("userId",FilterOperator.EQUAL,userId)
					.addFilter("isActive", FilterOperator.EQUAL, true);
			List<Entity> course = 
                   datastore.prepare(q).asList(FetchOptions.Builder.withLimit(10));
			
			
			for(Entity e:course) {
				enrolledCoursesId.add((long) e.getProperty("courseId"));
			}
			
			
			Query query = new Query("Course");
			List<Entity> courses = 
                   datastore.prepare(query).asList(FetchOptions.Builder.withLimit(10));
			
			
			List<Entity> availableCourses = new ArrayList<Entity>();
			
			for(Entity ac: courses) {
				if(enrolledCoursesId.contains((long) ac.getKey().getId())) {
					
				}
				else {
					availableCourses.add(ac);
				}
				
			}
			
            return availableCourses;
			
		}
		catch(Exception e) {
			e.printStackTrace();
			logger.warning(e.getMessage());
		}
		return null;
	}
	
	public List<Entity> getAllEnrolledCourses(String email){
		try {
			MessageDigest sha2 = MessageDigest.getInstance("SHA-256");
			sha2.update(email.getBytes());
			BigInteger hash = new BigInteger(1,sha2.digest());
			String userId = hash.toString(16);
			
			Query query = new Query("CourseEnrollment").addFilter("userId",FilterOperator.EQUAL,userId)
					.addFilter("isActive", FilterOperator.EQUAL, true);
			List<Entity> courses = 
                   datastore.prepare(query).asList(FetchOptions.Builder.withLimit(10));
			List<Entity> coursesInfo = new ArrayList<Entity>();
			for(Entity e: courses) {
				Entity thisEntity = getCourseDetails((long) e.getProperty("courseId"));
				coursesInfo.add(thisEntity);
				thisEntity = null;
			}
			return coursesInfo;
			
			
		}
		catch(Exception e) {
			logger.warning(e.getMessage());
		}
		return null;
		
	}
	public boolean addCourse(String name,String price,String description ,String chapters, String email) {
		try {
			MessageDigest sha2 = MessageDigest.getInstance("SHA-256");
			sha2.update(email.getBytes());
			BigInteger hash = new BigInteger(1,sha2.digest());
			String userId = hash.toString(16);
			Entity course = new Entity("Course");
			course.setProperty("courseName", name);
			course.setProperty("description", description);
			course.setProperty("chapters", chapters);
			course.setProperty("price", price);
			course.setProperty("created_by", userId);
			datastore.put(course);
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			logger.warning(e.getMessage());
			return false;
		}
	}
	
	public boolean updateCourse(String name,String price,String description ,String chapters,long courseId,String email) {
		try {
			MessageDigest sha2 = MessageDigest.getInstance("SHA-256");
			sha2.update(email.getBytes());
			BigInteger hash = new BigInteger(1,sha2.digest());
			String userId = hash.toString(16);
			Entity course = new Entity("Course",courseId);
			course.setProperty("courseName", name);
			course.setProperty("description", description);
			course.setProperty("chapters", chapters);
			course.setProperty("price", price);
			course.setProperty("created_by", userId);
			datastore.put(course);
			return true;
		}
		catch(Exception e) {
			
			logger.warning(e.getMessage());
			return false;
		}
	}
	public boolean checkCourseName(String name) {
		Query validCourseName = new Query("Course").
			    setFilter(new Query.FilterPredicate("courseName", 
			                  Query.FilterOperator.EQUAL,
			                  name)
			    ).setKeysOnly();
			Entity courseEntity = datastore.prepare(validCourseName).asSingleEntity();
		if (courseEntity==null) {
			System.out.print("null");
			return true;
		}
		else {
			System.out.print("not null");
			return false;
		}
		
	}

	public Entity getCourseDetails(long courseId) {
		Key courseKey = KeyFactory.createKey("Course", courseId);
		Query validuserquery = new Query("Course").
			    setFilter(new Query.FilterPredicate(Entity.KEY_RESERVED_PROPERTY, 
			                  Query.FilterOperator.EQUAL,
			                  courseKey)
			    );
			Entity anyentity = datastore.prepare(validuserquery).asSingleEntity();
		
		return anyentity;
		
	}
	
	public List<Entity> getCourseCreatedByUser(String name){
		try {
			MessageDigest sha2 = MessageDigest.getInstance("SHA-256");
			sha2.update(name.getBytes());
			BigInteger hash = new BigInteger(1,sha2.digest());
			String userId = hash.toString(16);
			
			Query query = new Query("Course").addFilter("created_by",FilterOperator.EQUAL,userId);
			List<Entity> courses = 
                   datastore.prepare(query).asList(FetchOptions.Builder.withLimit(10));
			
			return courses;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public Entity enrollCourse(String email,long courseId) {
		try {
			MessageDigest sha2 = MessageDigest.getInstance("SHA-256");
			sha2.update(email.getBytes());
			BigInteger hash = new BigInteger(1,sha2.digest());
			String userId = hash.toString(16);
			Entity enroll = new Entity("CourseEnrollment");
			enroll.setProperty("courseId", courseId);
			enroll.setProperty("userId", userId);
			enroll.setProperty("isActive", true);
			datastore.put(enroll);
			Entity enrollDetails = getCourseDetails(courseId);
			return enrollDetails;
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Entity unenrollCourse(String email, long courseId) {
		try {
			
			MessageDigest sha2 = MessageDigest.getInstance("SHA-256");
			sha2.update(email.getBytes());
			BigInteger hash = new BigInteger(1,sha2.digest());
			String userId = hash.toString(16);
			Query q =
			      new Query("CourseEnrollment")
			          .setFilter(
			              CompositeFilterOperator.and(
			                  new Query.FilterPredicate("userId", FilterOperator.EQUAL, userId),
			                  new Query.FilterPredicate("courseId", FilterOperator.EQUAL, courseId)));
			Entity result = datastore.prepare(q).asSingleEntity();
			Entity unenroll = new Entity("CourseEnrollment",result.getKey().getId());
			unenroll.setProperty("userId", userId);
			unenroll.setProperty("courseId", courseId);
			unenroll.setProperty("isActive", false);
			datastore.put(unenroll);
			Entity unenrollCourseInfo = getCourseDetails(courseId);
			return unenrollCourseInfo;
		}
		catch(Exception e) {
			e.printStackTrace();
			logger.warning(e.getMessage());
			return null;
		}
	}
}
