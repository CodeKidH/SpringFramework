#Template

    We will review the OCP
    
    Template : The part that is not changed distances itself from the part that is changed freely 

## 1. Review of stupid DAO
        
        UserDao has a problem of Exception 


#### 1_1. UserDao has a Exception

* Modify Exception
    
~~~java
    public void deleteAll() throws SQLException{
		
		
		Connection c = null;
		PreparedStatement ps = null;
				
		try{

			// This part might be a error possibility
			c = dataSource.getConnection();
			ps = c.prepareStatement("delete from users");
			ps.executeUpdate();
			
		}catch(SQLException e){
			throw e;
			
		}finally{ // It must be started 
			if(ps != null){
				try{
					ps.close(); // This part also might be a exception
				}catch(SQLException e){
					
				}
			}
			if(c!=null){
				try{
					c.close();
				}catch(SQLException e){
				}
			}
		}
	}
~~~

* Retreive Exception

~~~java
public int getCount() throws SQLException{
		
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try{
			c= dataSource.getConnection();
			ps = c.prepareStatement("select count(*) from users");
			rs = ps.executeQuery();
			rs.next();
			return rs.getInt(1);
		}catch(SQLException e){
			throw e;
		}finally{
			if(rs != null){
				try{
					rs.close();
				}catch(SQLException e){
					
				}
			}
			if(ps!=null){
				try{
					ps.close();
				}catch(SQLException e){
					
				}
			}
			if(c != null){
				try{
					c.close();
				}catch(SQLException e){
					
				}
			}
		}
		
	}
~~~

## 2. Either It was changed or not

#### 2_1. Problem's JDBC try/catch/finally

	 try/catch/finally was overlaped in code

#### 2_2. Design Pattern for reuse and separation

	1. We find two parts, changed part and not changed part
	
    
* Template Method Pattern

	We can use a extended function through a extend 
	
	- UserDaoDeleteAll.class
	
	~~~java
	public class UserDaoDeleteAll extends UserDao{

		protected PreparedStatement makeStatement(Connection c)throws SQLException{
			PreparedStatement ps = c.prepareStatement("delete from users");
			return ps;
			}
		}
	~~~
	- pros
		1. If UserDao want to extend its functions, It will do that easily
		2. UserDao will not be changed unnecessarily
	
	- cons
		1. There is a limit
		2. We have to make a new class's logic one by one


* Apply a Strategy Pattern
	


