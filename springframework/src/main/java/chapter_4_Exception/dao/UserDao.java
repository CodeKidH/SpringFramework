package chapter_4_Exception.dao;

import java.util.List;

import chapter_4_Exception.domain.User;

public interface UserDao {
	
	void add(User user);
	User get(String id);
	List<User> getAll();
	void deleteAll();
	int getCount();
}
