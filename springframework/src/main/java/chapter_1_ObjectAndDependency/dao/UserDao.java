package chapter_1_ObjectAndDependency.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import chapter_1_ObjectAndDependency.domain.User;

public abstract class UserDao {
	
	public void add(User user)throws ClassNotFoundException, SQLException{
		
		Connection c = getConnection();
		
		PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?,?,?)");
		ps.setString(1,user.getId());
		ps.setString(2,user.getName());
		ps.setString(3,user.getPassword());
		
		ps.executeUpdate();
		
		ps.close();
		c.close();
	}
	
	public User get(String id)throws ClassNotFoundException, SQLException{
		
		
		Connection c = getConnection();
		
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
	
	public abstract Connection getConnection() throws ClassNotFoundException, SQLException;  
	
	public static void main(String[]args)throws ClassNotFoundException, SQLException{
			
			UserDao dao = new NUserDao();
			
			User user = new User();
			user.setId("Kyle2");
			user.setName("Hee2");
			user.setPassword("11112");
			
			dao.add(user);
			
			User user2 = dao.get(user.getId());
			
			System.out.println(user2.getName());
		
	}
}

