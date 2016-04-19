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

		Some object separate from other object 	
		and Classes are connected by interface

	~~~java
		Context ---------------> Strategy <<-------------Astrategy
					                       <<-------------Bstrategy
					          
	1. Context has a fixed operation
	2. Specific function is extended by using Strategy
	~~~
	
		Context of deleteAll()
		1. Get a DBConnection
		2. Call external function to create PreparedStatement << Strategy
		3. Execute PreparedStatement
		4. Exception
		5. Close


	- StatementStrategy.class
	~~~java
	public interface StatementStrategy {
	
		PreparedStatement makePreparedStatement(Connection c)throws SQLException;
	}
	~~~
	
	- DeleteAllStatement.class
	~~~java
	public class DeleteAllStatement implements StatementStrategy{
	
		public PreparedStatement makePreparedStatement(Connection c)throws SQLException{
			PreparedStatement ps = c.prepareStatement("delete from users");
			return ps;
		}
	}
	~~~
	
	- UserDao.class
	~~~java
	public void deleteAll() throws SQLException{
		
		
		Connection c = null;
		PreparedStatement ps = null;
				
		try{
			
			c = dataSource.getConnection();
			
			StatementStrategy strategy = new DeleteAllStatement();
			ps = strategy.makePreparedStatement(c);
			
			ps.executeUpdate();
			
		}catch(SQLException e){
			throw e;
			
		}finally{
			if(ps != null){
				try{
					ps.close();
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

* Separation of Client/Context to apply DI

	- UserDao.class(jdbcContextWithStatementStrategy)
	~~~java
	public void jdbcContextWithStatementStrategy(StatementStrategy stmt)throws SQLException{
		
		Connection c = null;
		PreparedStatement ps = null;
		
		try{
			
			c = dataSource.getConnection();
			ps = stmt.makePreparedStatement(c);
			ps.executeUpdate();
			
		}catch(SQLException e){
			throw e;
		}finally{
			if(ps != null){
				try{
					ps.close();
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
	
	- deleteAll()
	~~~java
	public void deleteAll() throws SQLException{
		
		StatementStrategy st = new DeleteAllStatement();
		jdbcContextWithStatementStrategy(st);
		
	}
	~~~

## 3. Optimization of JDBC StrategyPattern

#### 3_1. Additional information

* AddStatement.class

~~~java
	public class AddStatement implements StatementStrategy{
	
		User user;
		
		public AddStatement(User user){
			this.user = user;
		}
		
		public PreparedStatement makePreparedStatement(Connection c)throws SQLException{
			
			PreparedStatement ps = c.prepareStatement("insert into users(id,name,password) values(?,?,?)");
			ps.setString(1, user.getId());
			ps.setString(2, user.getName());
			ps.setString(3, user.getPassword());
			
			return ps;
		}
	}
~~~

* UserDao.class -> add()

~~~java
	public void add(User user)throws ClassNotFoundException, SQLException{
		
		StatementStrategy st = new AddStatement(user);
		jdbcContextWithStatementStrategy(st);
	}
~~~

#### 3_2. Strategy live with a Client

	There are some problems
	
	1. We have to make a StatementStrategy class each DAO Method
	2. If there is a additional information, like a User, We will have to make a Constructor or Value to get it 
	
* Local Class
	
		We would be able to solve Num.1 problem


	- add()
	~~~java
	public void add(final User user)throws ClassNotFoundException, SQLException{
		
		class AddStatement implements StatementStrategy{
			
			public PreparedStatement makePreparedStatement(Connection c)throws SQLException{
				
				PreparedStatement ps = c.prepareStatement("insert into users(id,name,password) values(?,?,?)");
				ps.setString(1, user.getId());
				ps.setString(2, user.getName());
				ps.setString(3, user.getPassword());
				
				return ps;
			}
		}
		
		StatementStrategy st = new AddStatement();
		jdbcContextWithStatementStrategy(st);
	}
	~~~
	
	- Nested class
	~~~java
		- static class
		- inner class - member inner class
			         - local class
			         - anonymous inner class
	~~~

	- pros
		1. good readability
		2. Local class can access info it need easily
	
	- cons
		1. When Local class use a external parameter, the parameter must defines a final

* Anonymous inner class

		1. Defines a class + create a object
		2. Anonymous inner class doesn't have a own type
		
		new Interface's name(){class context};

	- add()
	~~~java
	public void add(final User user)throws ClassNotFoundException, SQLException{
		
		StatementStrategy st = new StatementStrategy(){
			
			public PreparedStatement makePreparedStatement(Connection c)throws SQLException{
				
				PreparedStatement ps = c.prepareStatement("insert into users(id,name,password) values(?,?,?)");
				ps.setString(1, user.getId());
				ps.setString(2, user.getName());
				ps.setString(3, user.getPassword());
				
				return ps;
			}
		};
		
		jdbcContextWithStatementStrategy(st);
		
	}
	~~~
	
	- add()_2
	~~~java
	public void add(final User user)throws ClassNotFoundException, SQLException{
		jdbcContextWithStatementStrategy(
			 new StatementStrategy(){
				
				public PreparedStatement makePreparedStatement(Connection c)throws SQLException{
					
					PreparedStatement ps = c.prepareStatement("insert into users(id,name,password) values(?,?,?)");
					ps.setString(1, user.getId());
					ps.setString(2, user.getName());
					ps.setString(3, user.getPassword());
					
					return ps;
				}
			}
		
		);
	}	
	~~~
	
	- deleteAll()
	~~~java
	public void deleteAll() throws SQLException{
		
		jdbcContextWithStatementStrategy(
				
				new StatementStrategy(){
						public PreparedStatement makePreparedStatement(Connection c)throws SQLException{
							return c.prepareStatement("delete from users");
						}
							
				}
		);
		
	}
	~~~

## 4. Context and DI

#### 4_1. Separation of JdbcContext
	
	Other DAO also use a jdbcContextWithStatementStrategy()

* Separation of class

	- JdbcContext.class
	~~~java
	public class JdbcContext {
		
		private DataSource dataSource;
		
		public void setDataSource(DataSource dataSource){
			this.dataSource = dataSource;
		}
		
		public void workWithStatementStrategy(StatementStrategy stmt)throws SQLException{
			
			Connection c = null;
			PreparedStatement ps = null;
			
			try{
				
				c = dataSource.getConnection();
				ps = stmt.makePreparedStatement(c);
				ps.executeUpdate();
				
			}catch(SQLException e){
				throw e;
			}finally{
				if(ps != null){
					try{
						ps.close();
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
		
		
	}
	~~~
	
	- UserDao.class
	~~~java
	private DataSource dataSource;
	private Connection c;
	private User user;
	private JdbcContext jdbcContext;
	
	public void setDataSource(DataSource dataSource){
		this.dataSource = dataSource;
	}
	
	public void setJdbcContext(JdbcContext jdbcContext){
		this.jdbcContext = jdbcContext;
	}
	
	public void add(final User user)throws ClassNotFoundException, SQLException{
		this.jdbcContext.workWithStatementStrategy(
			 new StatementStrategy(){
				
				public PreparedStatement makePreparedStatement(Connection c)throws SQLException{
					
					PreparedStatement ps = c.prepareStatement("insert into users(id,name,password) values(?,?,?)");
					ps.setString(1, user.getId());
					ps.setString(2, user.getName());
					ps.setString(3, user.getPassword());
					
					return ps;
				}
			}
		
		);
	}
		public void deleteAll() throws SQLException{
		
		this.jdbcContext.workWithStatementStrategy(
				
				new StatementStrategy(){
						public PreparedStatement makePreparedStatement(Connection c)throws SQLException{
							return c.prepareStatement("delete from users");
						}
							
				}
		);
		
	}
	
	~~~

* Bean dependency

	- applicationContext.xml
	~~~xml
	<?xml version="1.0" encoding="UTF-8"?>
	<beans xmlns="http://www.springframework.org/schema/beans"
			xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			xsi:schemaLocation="http://www.springframework.org/schema/beans
									http://www.springframework.org/schema/beans/spring-beans-3.0.xsd">
		
		<bean id="dataSource" class="org.springframework.jdbc.datasource.SimpleDriverDataSource">
			<property name="driverClass" value="com.mysql.jdbc.Driver"/>
			<property name="url" value="jdbc:mysql://localhost/test"/>
			<property name="username" value="root"/>
			<property name="password" value="1111"/>
		</bean>
		
		<bean id="userDao" class="chapter_3_Template.dao.UserDao">
			<property name="dataSource" ref="dataSource"/>
			<property name="jdbcContext" ref="jdbcContext"/>
		</bean>
		
		<bean id="jdbcContext" class="chapter_3_Template.dao.JdbcContext">
			<property name="dataSource" ref="dataSource"/>
		</bean>
		
		
	</beans>
	~~~
	
	- UserDaoTest.class
	~~~java
	@RunWith(SpringJUnit4ClassRunner.class)
	@ContextConfiguration(locations="/chapter_3_Template/dao/applicationContext.xml")
	///@DirtiesContext
	public class UserDaoTest {
		
		@Autowired
		private ApplicationContext context;
		
		private UserDao dao;
		private User user1;
		private User user2;
		private User user3;
		
		@Before
		public void setUp(){
			this.dao = this.context.getBean("userDao", UserDao.class);
			
			this.user3 = new User("x","x","x");
			this.user1 = new User("v","v","v");
			this.user2 = new User("z","z","z");
		}
		
		@Test
		public void addAndGet() throws SQLException, ClassNotFoundException{	
			
			
			dao.deleteAll();
			assertThat(dao.getCount(),is(0));
			
			dao.add(user1);
			dao.add(user2);
			assertThat(dao.getCount(),is(2));
			
			User userget1 = dao.get(user1.getId());
			assertThat(userget1.getId(),is(user1.getId()));
			
			User userget2 = dao.get(user2.getId());
			assertThat(userget2.getId(),is(user2.getId()));
		}
		
		@Test
		public void count() throws SQLException, ClassNotFoundException{
			
			
			dao.deleteAll();
			assertThat(dao.getCount(),is(0));
			
			dao.add(user1);
			assertThat(dao.getCount(),is(1));
			
			dao.add(user2);
			assertThat(dao.getCount(),is(2));
			
	
			dao.add(user3);
			assertThat(dao.getCount(),is(3));
		}
		
		public static void main(String[]args)throws ClassNotFoundException, SQLException{
			
			JUnitCore.main("chapter_3_Template.test.UserDaoTest");
		}
		
		@Test(expected=EmptyResultDataAccessException.class)
		public void getUserFailure() throws SQLException, ClassNotFoundException{
			
			
			dao.deleteAll();
			assertThat(dao.getCount(),is(0));
			
			dao.get("unknown");
		}
	}
	~~~


