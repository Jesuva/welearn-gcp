package com.welearn.mapper;


import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

import com.welearn.model.User;
 
public class UserMapper implements RowMapper<User> {
 
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
 
        String email = rs.getString("email");
        String password = rs.getString("Password");
        String name = rs.getString("userName");
 
        return new User(name,email, password);
    }
 
}
