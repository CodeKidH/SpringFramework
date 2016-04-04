package chapter_2_Test.dao;

import java.sql.Connection;
import java.sql.SQLException;

public class CountingConnectionMaker implements ConnectionMaker{
	
	int count = 0;
	private ConnectionMaker realConnectionMaker;
	
	public void setCountingConnectionMaker(ConnectionMaker realConnectionMaker){
		this.realConnectionMaker = realConnectionMaker;
	}
	
	public Connection makeConnection()throws ClassNotFoundException, SQLException{
		this.count++;
		return realConnectionMaker.makeConnection();
	}
	
	public int getCount(){
		return this.count;
	}
}
