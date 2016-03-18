package chapter_1_ObjectAndDependency.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import chapter_1_ObjectAndDependency.domain.User;

public class UserDao {
	
	private ConnectionMaker connectionMaker; //We don't need to know specific Class name
	
	public UserDao(){
		connectionMaker = new NConnectionMaker(); //but, We have to fix the problem
	}
	
	public void add(User user)throws ClassNotFoundException, SQLException{
		
		Connection c = connectionMaker.makeConnection(); //If the class will be changed, We don't care 
		
		PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?,?,?)");
		ps.setString(1,user.getId());
		ps.setString(2,user.getName());
		ps.setString(3,user.getPassword());
		
		ps.executeUpdate();
		
		ps.close();
		c.close();
	}
	
	public User get(String id)throws ClassNotFoundException, SQLException{
		
		
		Connection c = connectionMaker.makeConnection();
		
		PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
		ps.setString(1,id);
		
		ResultSet rs = ps.executeQuery();
		rs.next();
		User user = new User();
		user.setId(rs.getString("id"));
		user.setName(rs.getString("name"));
		user.setPassword(rs.getString("password"));
		
		rs.close();
		ps.close();
		c.close();
		
		return user;
		
	}
	
	
	public static void main(String[]args)throws ClassNotFoundException, SQLException{
			
			UserDao dao = new UserDao();
			
			User user = new User();
			user.setId("Kyle2");
			user.setName("Hee2");
			user.setPassword("11112");
			
			dao.add(user);
			
			User user2 = dao.get(user.getId());
			
			System.out.println(user2.getName());
		
	}
}

