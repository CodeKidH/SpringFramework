package chapter_5_ServiceAbstraction.dao;

import java.util.List;

import chapter_5_ServiceAbstraction.domain.User;

public interface UserDao {
	
	void add(User user);
	User get(String id);
	List<User> getAll();
	void deleteAll();
	int getCount();
}
