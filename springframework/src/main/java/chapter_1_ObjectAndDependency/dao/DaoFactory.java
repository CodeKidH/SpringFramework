package chapter_1_ObjectAndDependency.dao;

public class DaoFactory {
	
	public UserDao userDao(){
		return new UserDao(connectionMaker()); 
	}
	
	public AccountDao accountDao(){
		return new accountDao(connectionMaker());
	}
	
	public MessageDao messageDao(){
		return new messageDao(connectionMaker());
	}
	
	public ConnectionMaker connectionMaker(){
		
		return new NConnectionMaker();
	}

}
