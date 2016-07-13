package chapter_1_ObjectAndDependency.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class NConnectionMaker implements ConnectionMaker{
	public Connection makeConnection() throws ClassNotFoundException, SQLException{
		Class.forName("com.mysql.jdbc.Driver");
		Connection c = DriverManager.getConnection("jdbc:mysql://localhost/test","root","1111");
		return c;
	}
}
