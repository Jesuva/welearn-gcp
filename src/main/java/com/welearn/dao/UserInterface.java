package com.welearn.dao;

import com.google.appengine.api.datastore.Entity;
import com.welearn.model.Login;
import com.welearn.model.User;

public interface UserInterface {
	public boolean findUser(String email,String password);
	void addUser(String name,String email,String password);
	public boolean checkUserMail(String email);
}
