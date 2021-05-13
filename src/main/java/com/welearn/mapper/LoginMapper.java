package com.welearn.mapper;


import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

import com.welearn.model.Login;
import com.welearn.model.User;
 
public class LoginMapper implements RowMapper<Login> {
 
    public Login mapRow(ResultSet rs, int rowNum) throws SQLException {
    	String name = rs.getString("userName");
        String email = rs.getString("email");
        String password = rs.getString("Password");
 
        return new Login(name,email, password);
    }
 
}
