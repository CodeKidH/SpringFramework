package chapter_5_ServiceAbstraction.service;

import java.util.List;

import chapter_5_ServiceAbstraction.dao.UserDao;
import chapter_5_ServiceAbstraction.domain.User;
import chapter_5_ServiceAbstraction.domain.User.Level;

public class UserService {
	
	UserDao userDao;
	
	public void setUserDao(UserDao userDao){
		this.userDao = userDao;
	}
	
	public void upgradeLevels(){
		
		List<User> users = userDao.getAll();
		for(User user : users){
			
			Boolean changed = null;
			if(user.getLevel() == Level.BASIC && user.getLogin() >= 50){
				user.setLevel(Level.SILVER);
				changed = true;
			}else if(user.getLevel() == Level.SILVER && user.getRecommend() >= 30){
				user.setLevel(Level.GOLD);
				changed = true;
			}else if(user.getLevel() == Level.GOLD){
				changed = false;
			}else{
				changed = false;
				
			}
			
			if(changed){
				userDao.update(user);
			}
			
		}
		
	}

}
