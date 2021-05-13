package com.welearn.services;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
			Query query = new Query("Course");
			List<Entity> courses = 
                   datastore.prepare(query).asList(FetchOptions.Builder.withLimit(10));
//			String sql = "SELECT * FROM courses where courseId Not in (select course_id from course_enrollment where user_id=?);";
//			Object[] params = new Object[] { userId };
//	        CourseMapper mapper = new CourseMapper();
//	        List<Course> courseInfo = this.getJdbcTemplate().query(sql, params, mapper);
			
            return courses;
			
		}
		catch(Exception e) {
			logger.warning(e.getMessage());
		}
		return null;
	}
	
	public List<Course> getAllEnrolledCourses(String email){
		try {
			MessageDigest sha2 = MessageDigest.getInstance("SHA-256");
			sha2.update(email.getBytes());
			BigInteger hash = new BigInteger(1,sha2.digest());
			String userId = hash.toString(16);
			String sql = "SELECT * FROM courses where courseId IN (select course_id from course_enrollment where user_id=?);";
			Object[] params = new Object[] { userId };
	        CourseMapper mapper = new CourseMapper();
	        List<Course> courseInfo = this.getJdbcTemplate().query(sql, params, mapper);
            return courseInfo;
			
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
			String sql = "INSERT INTO `welearn`.`courses` (`courseName`, `courseDescription`, `chapters`, `coursePrice`, `created_by`) VALUES (?,?,?,?,?);\r\n"
					+ ";";
			Object[] params = new Object[] {name,description,chapters,price,userId};
			int addCourse = this.getJdbcTemplate().update(sql, params);
			if(addCourse!=0) {
				return true;
			}
			return false;
		}
		catch(Exception e) {
			e.printStackTrace();
			logger.warning(e.getMessage());
			return false;
		}
	}
	
	public boolean updateCourse(String name,String price,String description ,String chapters,int courseId) {
		try {
			
			String sql = "UPDATE `welearn`.`courses` SET `courseName` = ?,`coursePrice` = ?,`chapters`=?,`courseDescription`=? WHERE (`courseId` = ?);";
			int updateCourse = this.getJdbcTemplate().update(sql,name,price,chapters,description,courseId);
			if(updateCourse!=0) {
				return true;
			}
			else {
				return false;
			}
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
		}
		else {
			System.out.print("not null");
		}
		String sql ="SELECT count(*) FROM welearn.courses where courseName=?;";
		Object[] params = new Object[] {name};
		int courseCount = this.getJdbcTemplate().queryForObject(sql, params,Integer.class);
		if(courseCount==0) {
			return true;
		}
		
		return false;
	}

	public Entity getCourseDetails(long courseId) {
		Key courseKey = KeyFactory.createKey("Course", courseId);
		Query validuserquery = new Query("Course").
			    setFilter(new Query.FilterPredicate(Entity.KEY_RESERVED_PROPERTY, 
			                  Query.FilterOperator.EQUAL,
			                  courseKey)
			    );
			Entity anyentity = datastore.prepare(validuserquery).asSingleEntity();
		System.out.println(anyentity);
		return anyentity;
		
	}
	
	public List<Course> getCourseCreatedByUser(String name){
		try {
			MessageDigest sha2 = MessageDigest.getInstance("SHA-256");
			sha2.update(name.getBytes());
			BigInteger hash = new BigInteger(1,sha2.digest());
			String userId = hash.toString(16);
			String sql = "select * from courses where created_by=?;";
			Object[] params = new Object[] {userId};
			CourseMapper mapper = new CourseMapper();
			List<Course> courses = this.getJdbcTemplate().query(sql, params,mapper);
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
	
	public Course unenrollCourse(String email, int courseId) {
		try {
			MessageDigest sha2 = MessageDigest.getInstance("SHA-256");
			sha2.update(email.getBytes());
			BigInteger hash = new BigInteger(1,sha2.digest());
			String userId = hash.toString(16);
			String sql = "INSERT INTO `welearn`.`course_unenrollment` (`user_id`,`course_id`) VALUES (?, ?);\r\n"
					+ "";
			Object[] params = new Object[] {userId,courseId};
			int isUnenrolled = this.getJdbcTemplate().update(sql,params);
			if (isUnenrolled!=0) {
				String sql3 = "DELETE FROM `course_enrollment` WHERE (`user_id` = ? and `course_id`=?);";
				Object[] params3 = new Object[] {userId,courseId};
				this.getJdbcTemplate().update(sql3,params3);
				String sql2 = "select * from `courses` where `courseId`=?;";
				Object[] thisParams = new Object[] {courseId};
				CourseMapper mapper = new CourseMapper();
				Course course = this.getJdbcTemplate().queryForObject(sql2, thisParams,mapper);
				return course;
			}
			else {
				logger.warning("update query not working");
				return null;
			}
		}
		catch(Exception e) {
			logger.warning(e.getMessage());
			return null;
		}
	}
}
