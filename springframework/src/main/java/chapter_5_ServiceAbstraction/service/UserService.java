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
			
			if(canUpgradeLevel(user)){
				upgradeLevel(user);
			}
			
		}
		
	}
	
	public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
	public static final int MIN_RECCOMEND_FOR_SILVER = 30;
	
	private boolean canUpgradeLevel(User user){
		
		Level currentLevel = user.getLevel();
		
		switch(currentLevel){
			case BASIC : return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
			case SILVER : return (user.getRecommend() >= MIN_RECCOMEND_FOR_SILVER);
			case GOLD : return false;
			default : throw new IllegalArgumentException("Unknown level:"+ currentLevel);
		}
	}
	
	private void upgradeLevel(User user){
		user.upgradeLevel();
		userDao.update(user);
	}
	
	public void add(User user){
		
		if(user.getLevel() == null)user.setLevel(Level.BASIC);
		
			userDao.add(user);
		
	}

}
