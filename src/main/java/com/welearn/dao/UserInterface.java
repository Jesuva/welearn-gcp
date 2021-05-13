package com.welearn.dao;

import com.welearn.model.Login;
import com.welearn.model.User;

public interface UserInterface {
	public Login findUser(String email,String password);
	void addUser(String name,String email,String password);
	public boolean checkUserMail(String email);
}
