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
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Transaction;
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
	 
	 public boolean findUser(String email,String password) {
		 	try {
				MessageDigest sha2 = MessageDigest.getInstance("SHA-256");
				sha2.update(password.getBytes());
				BigInteger hash = new BigInteger(1,sha2.digest());
				String HashPassword = hash.toString(16);
				Query query = new Query("User").addFilter("email",FilterOperator.EQUAL,email)
						.addFilter("password", FilterOperator.EQUAL, HashPassword);
				Entity user = 
	                   datastore.prepare(query).asSingleEntity();
				
				if(user!=null) {
					return true;
				}
				else {
					return false;
				}
			} 
		 			 	
		 	catch (Exception e) {
				logger.warning(e.getMessage());
				return false;
			}
		 	 
	    }
	 
	 public void addUser(String name,String email,String password) {
		 Transaction transaction = datastore.beginTransaction();
		 try {
				MessageDigest sha2 = MessageDigest.getInstance("SHA-256");
				sha2.update(password.getBytes());
				BigInteger hash = new BigInteger(1,sha2.digest());
				String HashPassword = hash.toString(16);
			 	sha2.update(email.getBytes());
			 	BigInteger mailHash = new BigInteger(1,sha2.digest());
			 	String userId = mailHash.toString(16);
			 	Entity user = new Entity("User",userId);
			    user.setProperty("userId", userId);
			    user.setProperty("name",name);
			    user.setProperty("email", email);
			    user.setProperty("password", HashPassword);
			    datastore.put(transaction,user);
			    transaction.commit();
		 }
		 catch(Exception e) {
			 logger.warning(e.getMessage());
		 }
		 finally {
			 if(transaction.isActive()) {
				 transaction.rollback();
				 logger.warning("Add user function rolled back.");
			 }
		 }
		   
	 }

	public boolean checkUserMail(String email) {
		
		try {
			Query validCourseName = new Query("User").
				    setFilter(new Query.FilterPredicate("email", 
				                  Query.FilterOperator.EQUAL,
				                  email)
				    ).setKeysOnly();
				Entity emailTaken = datastore.prepare(validCourseName).asSingleEntity();
            if(emailTaken==null) {
            	return true;
            }
			
		}
		catch(Exception e) {
			logger.warning(e.getMessage());
		}
		
		return false;
	}
	
	
	
	
}
