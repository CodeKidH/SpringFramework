# Exception

## 1. Exception disappear

    
* UserDao.class
~~~java
//Before
public void deleteAll()throws SQLException{
	
  	this.jdbcContext.executeSql("delete from users");
  	
  }
  
  //After
  public void deleteAll(){
	
  	this.jdbcTemplate.update("delete from users");
  	
  }
~~~
      - Where is a Exception?
      
#### 1_1. Stupid Exception

* Exception Blackhole

    - Stupid 1
    ~~~java
    try{

    }catch(SQLException e){
    
    }
    
    // We don't have to do that
    // This exception will pass a error when Exception occur
    ~~~
    
    - Stupid 2
    ~~~java
    
    }catch(SQLException e){
    	System.out.println(e);
    }
    
    }catch(SQLException e){
    	e.printStackTrace();
    }
    ~~~
    
    - Better than that
    
    ~~~java
    }catch(SQLException e){
    	e.printStackTrace();
    	System.exit(1);
    }
    ~~~
* irresponsibility throws

		Use the throws Exception
	
	- cons
		1. I can't get a proper information about method
		2. I miss a opportunity that might be handled 

	
#### 1_2. Kinds of Exception
	
		 Big issus is the checked exception(explicit exception)-명시적예외
	
* Error
	- java.lang.Error's subclass
	- System has a error, Usually VM problem
	- OutofMemoryError, ThreadDeath...
	- I don't care about Errors

* Exception and check Exception
	- java.lang.Exception's subclass
	- Programmer will make a Exception
	- check Exception
		1. Exception's subclass
	- unchecked Exception
		1. Exception's subclass
		2. RuntimeException's child
	
    ![Exception]
(https://raw.githubusercontent.com/KyleJeong/SpringFramework/master/springframework/src/main/java/chapter_4_Exception/images/exception.png)
	
	- Normal Exception is a red part
	- If Method throw a check exception, It have to make a catch or to define throws

* RuntimeException and uncheck/runtime exception

	- java.lang.RuntimeException's subclass
	- I don't care about it

#### 1_3. How to handle it

* Restoration 


		 To solve the problem and restoration


	~~~java
		Example
			If we use a poor network service, Our networkconnection might be disconnected
			so we can't access to DB Server and we get a SQLException
			
			In this case, We can solve the problem by retrying several times
	~~~
	
	~~~java
		int maxretry = MAX_RETRY;
		while(maxretry --> 0){
			try{
				return;
			}catch(SomeException e){
				// print log and wait 
			}finally{
				// return resource
			}
		}
		
		throw new RetryFailedException();//Exception occur when it over number of max
	~~~
	
* Avoid


		When Exception occur, Exception will be thrown
		1. Define throws statement
		2. Catch a Exception, print a log and then rethrow 

	- avoid 1
	~~~java
		public void add()throws SQLException{
			//JDBC API
		}
	~~~
	
	- avoid2
	~~~java
		public void add()throws SQLException{
			try{
				//JDBC API
			}catch(SQLException e){
				throw e;// print log
			}
			
		}
	~~~

* Exception translation

		When exception occur, exception will be translated to proper exception type
		
		
	- Exception translation has two purpose
	
		1. Define a proper exception to solve the exception quickly
		
			~~~java
			Example
				 When we add a user ID to DB, What if there is a already same ID?
			 	 In this case, JDBC API make a SQLException
				 and It's hard to find a resone why SQLException occur
				 so  Exception translation will change SQLException to DuplicateUserIdException
				 We can find a reason easily
				
			~~~
			
			~~~java
			public void add(User user)throws DuplicateUserIdException, SQLException{
				try{
					//add a userId
					//This code will call the SQLException
				}catch(SQLException e){
					//ErrorCode is Duplicate Entry(1062)
					if(e.getErrorCode() == MysqlErrorNumbers.ER_DUP_ENTRY)
						throw DuplicateUserIdException();
					else
						throw e; //SQLException

				}
			}
			
			//Usually it is better to make the nested exception(충첩)
			//I throw SQLException and DuplicateUserIdException at the same time
			
			1.Example for Nested Exception 1
				
				catch(SQLException e){
					throw DuplicatedUserIdException(e);
			
			2.Example for Nested Exceptioin 2
				
				catch(SQLException e){
					throw DuplicateUserIdException().initCause(e);
			~~~
		
		2. To wrap a exception to handle exception easily
			
			~~~java
			Usually We use it to change check exception to uncheck exception(runtimeexceptioin)
				
			EJBException
				Most of check exceptioin in EJB Component code will not be mean anything
				so We will wrap it with EJBException(RuntimeException) 
			~~~

			~~~java
			try{
				OrderHome orderHome = EJBHomeFactory.getInstance().getOrderHome();
				Order order = orderHome.findByPrimaryKey(Integer.id);
			}catch(NamingException ne){
				throw new EJBException(ne);
			}catch(SQLException se){
				throw new EJBException(se);
			}catch(RemoteException re){
				throw new EJBException(re);
			}
			
			~~~

#### 1_4. Strategy of Exception

* Handling of exception of add()
	

## 2. Exception translation

#### 2_1. limit to JDBC
	
	How can we switch the DB  flexibly?
	
	There are two problems 

* Nonstandard SQL
	
	If we use a specific DB(Mysql), We will use a Nonstandard SQL for Mysql in DAO
	It means our system depend on Mysql

* SQLException error

	There are a lot of error and Error's cause is different by each DB

#### 2_3. DAO Interface and DataAccessException hierarchy structor

		DataAccessException will make the consistent exception regardless of Data access tech

* DAO Interface and Separation of realization
	
	
		Why do I make a DAO far away other part?
			- Because of separation
			- Client will don't care about Which data access tech we use
		but It has a trouble with Exception info 
	
	- UserDao.interface
	~~~java
	public interface UserDao{
		public void add(User user)
	}

	// But I can use the above code because Data Access tech API that DAO use will throw a Exception 
	
	//If use a JDBC API, It will throw a SQLExcepion
	public void add(User user) throws SQLException 
	
	//If the method define throws SQLException, JDBC only access the method
	//so Other Data Access API(JPA, HIbernate, JDO) can't access it
	
	public void add(User user) throws PersistenException //JPA
	public void add(User user) throws HibernateException //Hibernate
	public void add(User user) throws JdoException //JDO
	public void add(User user) throws SQLException //JDBC
	
	~~~
	
	- How to solve it
	~~~java
	//To use the Exception
	public void add(User user) throws Exception
	
	//But the way is so irresponsible
	//I need a way to arrange data access API's Exception
	~~~

* Data access exception abstraction and DataAccessException hierarchy structor

		Spring organize the various data access tech's exception in DataAccessException hierarchy structor
		
	
	- Example for DataAccessException
		~~~java
		JdbcTemplate's SQLException will connect with each DB and return meaningful exception
		~~~
		
	

## 2_4. Isolation UserDao 

* UserDao.Interface
~~~java
public interface UserDao {
	
	void add(User user);
	User get(String id);
	List<User> getAll();
	void deleteAll();
	int getCount();
}
~~~

* UserDaoJdbc.java
~~~java

public class UserDaoJdbc implements UserDao{
	
	
	@Autowired
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	
	private RowMapper<User> userMapper = new RowMapper<User>(){
		public User mapRow(ResultSet rs, int rowNum)throws SQLException{
			User user = new User();
			user.setId(rs.getString("id"));
			user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
			return user;
		}
	};
	
	public void setDataSource(DataSource dataSource){
		
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public void add(final User user){
		
		this.jdbcTemplate.update("insert into users(id,name,password) values(?,?,?)",
				user.getId(),user.getName(),user.getPassword());
	}
	
	public List<User> getAll(){
		
		return this.jdbcTemplate.query("select * from users order by id",
				this.userMapper);
	}
	
	public User get(String id){
		
		return this.jdbcTemplate.queryForObject("select * from users where id=?",new Object[]{id},
				this.userMapper);
		
	}
	
	public void deleteAll(){
		
		this.jdbcTemplate.update("delete from users");
		
	}
	
	
	public int getCount(){
		return this.jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
	}

}

~~~

* applicatinContext.xml
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
	
	<bean id="userDao" class="chapter_4_Exception.dao.UserDaoJdbc">
		<property name="dataSource" ref="dataSource"/>
	</bean>
	
</beans>
							
~~~
