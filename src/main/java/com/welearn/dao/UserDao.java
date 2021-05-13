package com.welearn.dao;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import javax.sql.DataSource;
import javax.swing.tree.RowMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.welearn.mapper.LoginMapper;
import com.welearn.mapper.UserMapper;
import com.welearn.model.Login;
import com.welearn.model.User;

@Service
public class UserDao extends JdbcDaoSupport implements UserInterface {
	private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	DatastoreService datastore =  DatastoreServiceFactory.getDatastoreService();


	 @Autowired
	    public UserDao(DataSource datasource) {
	        this.setDataSource(datasource);
	    }
	 
	 public Login findUser(String email,String password) {
		 	try {
				MessageDigest sha2 = MessageDigest.getInstance("SHA-256");
				sha2.update(password.getBytes());
				BigInteger hash = new BigInteger(1,sha2.digest());
				String HashPassword = hash.toString(16);
				
		        String sql = "Select u.userName,u.email,u.password "//
		                + " from users u where u.email = ? and u.password = ? ";
		 
		        Object[] params = new Object[] { email,HashPassword };
		        LoginMapper mapper = new LoginMapper();
		        Login userInfo = this.getJdbcTemplate().queryForObject(sql, params, mapper);
		       
	            return userInfo;
			} 
		 			 	
		 	catch (Exception e) {
				logger.warning(e.getMessage());
				return null;
			}
		 	 
	    }
	 
	 public void addUser(String name,String email,String password) {
		 
		 try {
				MessageDigest sha2 = MessageDigest.getInstance("SHA-256");
				sha2.update(password.getBytes());
				BigInteger hash = new BigInteger(1,sha2.digest());
				String HashPassword = hash.toString(16);
			 	sha2.update(email.getBytes());
			 	BigInteger mailHash = new BigInteger(1,sha2.digest());
			 	String userId = mailHash.toString(16);
			 	Entity user = new Entity("User",email);
			    user.setProperty("userId", email);
			    user.setProperty("name",name);
			    user.setProperty("email", email);
			    user.setProperty("password", HashPassword);
			    datastore.put(user);
			 String sql = "insert into users (`userId`, `userName`, `email`, `password`) VALUES (?,?,?,?)";
			 Object[] params = new Object[] {userId,name,email,HashPassword};
			 int userInfo = this.getJdbcTemplate().update(sql, params);
		 }
		 catch(Exception e) {
			 logger.warning(e.getMessage());
		 }
		   
	 }

	public boolean checkUserMail(String email) {
		
		try {
			String sql = "Select count(*) "//
	                + " from users u where u.email = ?";
	 
	        Object[] params = new Object[] { email};
	        int userCount = this.getJdbcTemplate().queryForObject(sql, params, Integer.class);
            if(userCount==0) {
            	return true;
            }
			
		}
		catch(Exception e) {
			logger.warning(e.getMessage());
		}
		
		return false;
	}
	
	
	
	
}
