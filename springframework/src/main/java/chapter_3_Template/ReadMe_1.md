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


#### 4_2. Special DI of JdbcContext

	 UserDao use a JdbcContext by not using a interfce
	 
	 Is there a problem?


* DI as a Spring Bean

		Above a case, Use a IOC
		UserDAO was injected with jdbcContext by using spring
		So, UserDAO and jdbcContext follow the rule of DI
	
	- Why do we make a DI between UserDao and jdbcContext?
		1. JdbcContext will be singleton bean 
		2. JdbcCotnext depend on other bean through DI

* Hand-operated DI
	
	- There are two ways to use a JdbcContext
		1. To use a Spring bean
		2. Hand-operated DI in UserDAO


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
		</bean>
	</beans>
	~~~
	
	- UserDao.class
	~~~java
	public void setDataSource(DataSource dataSource){ //setterMethod, create a jdbcContext and DI
		
		this.jdbcContext = new JdbcContext(); // Create a jdbcContext
		this.jdbcContext.setDataSource(dataSource); //DI
		
		this.dataSource = dataSource; //for other method that not apply jdbcContext
	}	
	~~~

## 5. Template and callback

	Template Callback Pattern
		- Strategy Pattern + Anonymouse class in spiring
		- Template : Strategy's context
		- Callback : Object of Anonymouse class

#### 5_1. Operation principle

* Feature of template/callback

![Template_Callback]
(https://raw.githubusercontent.com/CodeKidH/SpringFramework/master/springframework/src/main/java/chapter_3_Template/images/template_callback.png)		

* JdbcContext that apply Template/Callback Pattern
![Jdbc_Template_Callback]
(https://raw.githubusercontent.com/CodeKidH/SpringFramework/master/springframework/src/main/java/chapter_3_Template/images/jdbc_template.png)
	
#### 5_2. Reuse of Callback

	 Template/Callback's Issue
	 	It is poorly readable using anonymouse class

* Separation of callback

	- UserDao.class
	~~~java
	public void deleteAll() throws SQLException{
		
		executeSql("delete from users"); // changed part
		
	}
	
	//callback, not changed part
	private void executeSql(final String query)throws SQLException{
		this.jdbcContext.workWithStatementStrategy(
				new StatementStrategy(){
					public PreparedStatement makePreparedStatement(Connection c)throws SQLException{
						return c.prepareStatement(query);
					}
				}
		);
	}
	~~~

* To combine Callback with Template

		Other DAO classes can use a executeSql() by making it as a class
	
	
	- UserDao.class [deleteAll()]
	~~~java
	public void deleteAll() throws SQLException{
		
		this.jdbcContext.executeSql("delete from users");
		
	}
	~~~
	
	- JdbcContext.class
	~~~java
	public void executeSql(final String query)throws SQLException{
		workWithStatementStrategy(
				new StatementStrategy(){
					public PreparedStatement makePreparedStatement(Connection c)throws SQLException{
						return c.prepareStatement(query);
					}
				}
		);
	}
	~~~

	
	- Now, JdbcContext has Client, template and callback
	![Jdbc_Template_Callback]
	(https://raw.githubusercontent.com/CodeKidH/SpringFramework/master/springframework/src/main/java/chapter_3_Template/images/jdbc_template_2.png)
	 	

#### 5_3. Application of Template callback
	
		There are a lot of Templata/callback class and api in springframework
		
		If a user who uses the springframework will has to know about template/callback provided by spring

* Test and try/catch/finally
		
		Simple template/callback pattern
		
		Define
			- This application will open a file and return a sum of numbers
	
	- numbers.txt
	~~~java
	1
	2
	3
	4
	~~~
	
	- Calculator.class
	~~~java
	public class Calculator {
	
		public Integer calcSum(String filepath)throws IOException{
			
			BufferedReader br = new BufferedReader(new FileReader(filepath)); // Open a file
			Integer sum = 0;
			String line = null;
			while((line = br.readLine()) != null){ // Read lines one by one
				sum += Integer.valueOf(line);
			}
			
			br.close(); // Have to close
			return sum;
		}
	}
	~~~
	
	- CalcSumTest.class
	~~~java
	import java.io.IOException;
	import static org.hamcrest.CoreMatchers.is;
	import static org.junit.Assert.assertThat;
	
	import org.junit.Test;
	
	public class CalcSumTest {
	
			@Test
			public void sumOfNumbers()throws IOException{
				Calculator calculator = new Calculator();
				int sum = calculator.calcSum(getClass().getResource("numbers.txt").getPath());
				assertThat(sum,is(10));
			}
	}
	~~~

	~~~java
	If it might has a exception while working, It will not be closed a file
	so we have to deal with a exception
	~~~
	
	- Calculator.class
	~~~java
	public class Calculator {
	
		public Integer calcSum(String filepath)throws IOException{
			
			BufferedReader br = null;
			
			try{
				
				br = new BufferedReader(new FileReader(filepath)); 
				Integer sum = 0;
				String line = null;
				while((line = br.readLine()) != null){ 
					sum += Integer.valueOf(line);
				}
				
				return sum;
				
			}catch(IOException e){
				System.out.println(e.getMessage());
				throw e;
			}finally{
				if(br != null){
					try{
						br.close();
					}catch(IOException e){
						System.out.println(e.getMessage());
					}
				}
			}
		}
	}
	~~~
* Remove a overrap and Template/callback
	
		To add a multiply
	
	- BufferedReaderCallback.interface
	~~~java
	public interface BufferedReaderCallback {
		Integer doSomethingWithReader(BufferedReader br) throws IOException;
	}
	~~~
	
	- Calculator.class
	~~~java
	public class Calculator {
	
		public Integer calcSum(String filepath)throws IOException{
			
			BufferedReaderCallback sumCallback = 
					new BufferedReaderCallback(){
				public Integer doSomethingWithReader(BufferedReader br)throws IOException{
					Integer sum = 0;
					String line = null;
					
					while((line = br.readLine()) != null){
						sum += Integer.valueOf(line);
					}
					
					return sum;
				}
			};
			
			return fileReadTemplate(filepath, sumCallback);
		}
		
		public Integer calcMultiply(String filepath)throws IOException{
			
			BufferedReaderCallback sumCallback = 
					new BufferedReaderCallback(){
				public Integer doSomethingWithReader(BufferedReader br)throws IOException{
					Integer multiply = 1;
					String line = null;
					
					while((line = br.readLine()) != null){
						multiply *= Integer.valueOf(line);
					}
					
					return multiply;
				}
			};
			
			return fileReadTemplate(filepath, sumCallback);
		}
		
		public Integer fileReadTemplate(String filepath, BufferedReaderCallback callback)throws IOException{
			
			BufferedReader br = null;
			
			try{
				
				br = new BufferedReader(new FileReader(filepath)); 
				
				int ret = callback.doSomethingWithReader(br);
				
				return ret;
				
			}catch(IOException e){
				System.out.println(e.getMessage());
				throw e;
			}finally{
				if(br != null){
					try{
						br.close();
					}catch(IOException e){
						System.out.println(e.getMessage());
					}
				}
			}
		}
	}

	~~~
	
	- calcTest.class
	~~~java
	public class CalcSumTest {
		
			Calculator calculator;
			String numberFilepath;
			
			@Before
			public void setUp(){
				this.calculator = new Calculator();
				this.numberFilepath = getClass().getResource("numbers.txt").getPath();
			}
			
			@Test
			public void sumOfNumbers()throws IOException{
				
				assertThat(calculator.calcSum(this.numberFilepath),is(10));
			}
			
			@Test
			public void multiplyOfNumbers()throws IOException{
				assertThat(calculator.calcMultiply(this.numberFilepath),is(24));
			}
	}
	~~~

* Template/callback refactoring

		Compare method between sumOfNumbers() and multiplyOfNumbers()
		
		There are many overrap codes
	

	- LineCallback.interface
	~~~java
	public interface LineCallback {
		Integer doSomethingWithLine(String line, Integer value);
	}
	~~~
	
	- Calculator.clss
	~~~java
	public class Calculator {
	
		public Integer calcSum(String filepath)throws IOException{
			
			LineCallback sumCallback = 
					new LineCallback(){
						public Integer doSomethingWithLine(String line, Integer value){
							return value + Integer.valueOf(line);
						}
			};
			
			return lineReadTemplate(filepath, sumCallback,0);
		}
		
		public Integer calcMultiply(String filepath)throws IOException{
			
			LineCallback multiplyCallback = 
					new LineCallback(){
						public Integer doSomethingWithLine(String line, Integer value){
							return value * Integer.valueOf(line);
						}
			};
			
			return lineReadTemplate(filepath, multiplyCallback,1);
		}
		
		
		public Integer lineReadTemplate(String filepath, LineCallback callback, int initVal)throws IOException{
			
			BufferedReader br = null;
			
			try{
				
				br = new BufferedReader(new FileReader(filepath)); 
				
				Integer res = initVal;
				String line = null;
				
				while((line = br.readLine())!= null){
					res = callback.doSomethingWithLine(line, res);
				}
				
				return res;
					
				
			}catch(IOException e){
				System.out.println(e.getMessage());
				throw e;
			}finally{
				if(br != null){
					try{
						br.close();
					}catch(IOException e){
						System.out.println(e.getMessage());
					}
				}
			}
		}
	}

	~~~
	
* Callback interface by using Generics

	- LineCallback.interface
	~~~java
	public interface LineCallback<T> {
		T doSomethingWithLine(String line, T value);
	}
	~~~
	
	- Calculator.class
	~~~java
	public class Calculator {
	
		public Integer calcSum(String filepath)throws IOException{
			
			LineCallback<Integer> sumCallback = 
					new LineCallback<Integer>(){
						public Integer doSomethingWithLine(String line, Integer value){
							return value + Integer.valueOf(line);
						}
	
						
			};
			
			return lineReadTemplate(filepath, sumCallback,0);
		}
		
		public Integer calcMultiply(String filepath)throws IOException{
			
			LineCallback<Integer> multiplyCallback = 
					new LineCallback<Integer>(){
						public Integer doSomethingWithLine(String line, Integer value){
							return value * Integer.valueOf(line);
						}
			};
			
			return lineReadTemplate(filepath, multiplyCallback,1);
		}
		
		
		public String concatenate(String filepath)throws IOException{
			LineCallback<String> concatenateCallback = 
					new LineCallback<String>(){
				public String doSomethingWithLine(String line, String value){
					return value + line;
				}
			};
			
			return lineReadTemplate(filepath, concatenateCallback,"");
		}
		
		public <T> T lineReadTemplate(String filepath, LineCallback<T> callback, T initVal)throws IOException{
			
		BufferedReader br = null;
		
		try{
			
			br = new BufferedReader(new FileReader(filepath)); 
			
			T res = initVal;
			String line = null;
			
			while((line = br.readLine())!= null){
				res = callback.doSomethingWithLine(line, res);
				}
				
				return res;
									
				
			}catch(IOException e){
				System.out.println(e.getMessage());
				throw e;
			}finally{
				if(br != null){
					try{
						br.close();
					}catch(IOException e){
						System.out.println(e.getMessage());
					}
				}
			}
		}
	}	
	~~~

	- test
	~~~java
		Calculator calculator;
		String numberFilepath;
		
		@Before
		public void setUp(){
			this.calculator = new Calculator();
			this.numberFilepath = getClass().getResource("numbers.txt").getPath();
		}
		
		@Test
		public void sumOfNumbers()throws IOException{
			
			assertThat(calculator.calcSum(this.numberFilepath),is(10));
		}
		
		@Test
		public void multiplyOfNumbers()throws IOException{
			assertThat(calculator.calcMultiply(this.numberFilepath),is(24));
		}
		
		@Test
		public void concatenateString()throws IOException{
			assertThat(calculator.concatenate(this.numberFilepath),is("1234"));
		}
	~~~

## 6. JdbcTemplate

	Springframework provide template/callback of JDBC for us 
	
#### 6_1. update()

* UserDao.class(Delete, add)
~~~java
public class UserDao {
	
	@Autowired
	private DataSource dataSource;
	private Connection c;
	private User user;
	private JdbcTemplate jdbcTemplate;
	
	public void setDataSource(DataSource dataSource){
		
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.dataSource = dataSource;
	}
	
	public void add(final User user)throws ClassNotFoundException, SQLException{
		this.jdbcTemplate.update("insert into users(id,name,password) values(?,?,?)",
				user.getId(),user.getName(),user.getPassword());
	}
	
	public User get(String id)throws ClassNotFoundException, SQLException{
		
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		User user = null;
		
		try{
			c = dataSource.getConnection();
			ps = c.prepareStatement("select * from users where id = ?");
			ps.setString(1,id);
			rs = ps.executeQuery();
			
			if(rs.next()){
				user = new User();
				user.setId(rs.getString("id"));
				user.setName(rs.getString("name"));
				user.setPassword(rs.getString("password"));
			}
			rs.close();
			ps.close();
			c.close();
			
			if(user == null){
				throw new EmptyResultDataAccessException(1);
			}
			
			return user;
		}catch(SQLException e){
			throw e;
		}finally{
			if(rs != null){
				try{
					rs.close();
				}catch(SQLException e){
					
				}
			}
			if(ps != null){
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
	
	public void deleteAll() throws SQLException{
		
		this.jdbcTemplate.update("delete from users");
		
	}
	
	
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
	

}
~~~

#### 6_2. queryForInt()

* getCount()

~~~java
	public int getCount() throws SQLException{
		
		
		return this.jdbcTemplate.query(new PreparedStatementCreator(){//First callback, To create Statement
			public PreparedStatement createPreparedStatement(Connection con)throws SQLException{
				return con.prepareStatement("select count(*) from users");
			}

		}, new ResultSetExtractor<Integer>(){	//Second callback, To extract value from ResultSet
			public Integer extractData(ResultSet rs)throws SQLException, DataAccessException{
				rs.next();
				return rs.getInt(1);
			}
		});
	}
	
	public int getCount(){
		return this.jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
	}
~~~

#### 6_3. queryForObject()

		ResultSetExtractor 
			- It will return a last result
		RowMapper
			- It will call several times to be in mapping the ResultSet of each row

* get()
~~~java
	public User get(String id){
		
		return this.jdbcTemplate.queryForObject("select * from users where id=?",new Object[]{id},//parameter value
				new RowMapper<User>(){
					public User mapRow(ResultSet rs, int rowNum)throws SQLException{
						User user = new User();
						user.setId(rs.getString("id"));
						user.setName(rs.getString("name"));
						user.setPassword(rs.getString("password"));
						return user;
					}
		});
		
	}
	
	//queryForObject(1,2,3)
	// 1 - PreparedStatement sql
	// 2 - Binding value
	// 3 - 
~~~

#### 6_4. query()


* functino definition and test

		 To add a getAll()

	- UserDaoTest.class(testGetAll())
	~~~java
	@Test
	public void getAll() throws SQLException, ClassNotFoundException{
		
		dao.deleteAll();
		
		dao.add(user1);
		List<User> users1 = dao.getAll();
		assertThat(users1.size(),is(1));
		checkSameUser(user1, users1.get(0));
		
		dao.add(user2);
		List<User>users2 = dao.getAll();
		assertThat(users2.size(),is(2));
		checkSameUser(user1,users2.get(0));
		checkSameUser(user2,users2.get(1));
		
		dao.add(user3);
		List<User>users3 = dao.getAll();
		assertThat(users3.size(),is(3));
		checkSameUser(user3, users3.get(0));
		checkSameUser(user3, users3.get(1));
		checkSameUser(user3, users3.get(2));
		
	}
	
	private void checkSameUser(User user1, User user2){
		assertThat(user1.getId(),is(user2.getId()));
		assertThat(user1.getName(),is(user2.getName()));
		assertThat(user1.getPassword(),is(user2.getPassword()));
	}
	~~~

* getAll() by using query() 

	- getAll()
	~~~java
	public List<User> getAll(){
	
		return this.jdbcTemplate.query("select * from users order by id",
				new RowMapper<User>() {
					
					public User mapRow(ResultSet rs, int rowNum)throws SQLException{
						User user = new User();
						user.setId(rs.getString("id"));
						user.setName(rs.getString("name"));
						user.setPassword(rs.getString("password"));
						return user;
					}
		});
	}
	~~~

* Test supplementation(negative test)

		Test for negative
			- What if getAll() will return nothing?
				- query() will return a List<T> object

	- getAll()
	~~~java
			
			dao.deleteAll();
			
			
			List<User> users0 = dao.getAll();
			assertThat(users0.size(),is(0));
			
	~~~

#### 6_5. Separatin of callback


* Code arrangement for DI

	- UserDao.class
	~~~java
	public class UserDao {
	
		@Autowired
		private DataSource dataSource;
		private JdbcTemplate jdbcTemplate;
		
		public void setDataSource(DataSource dataSource){
			
			this.jdbcTemplate = new JdbcTemplate(dataSource);
		}
		
		public void add(final User user)throws ClassNotFoundException, SQLException{
			this.jdbcTemplate.update("insert into users(id,name,password) values(?,?,?)",
					user.getId(),user.getName(),user.getPassword());
		}
		
		public List<User> getAll(){
			
			return this.jdbcTemplate.query("select * from users order by id",
					new RowMapper<User>() {
						
						public User mapRow(ResultSet rs, int rowNum)throws SQLException{
							User user = new User();
							user.setId(rs.getString("id"));
							user.setName(rs.getString("name"));
							user.setPassword(rs.getString("password"));
							return user;
						}
			});
		}
		
		public User get(String id){
			
			return this.jdbcTemplate.queryForObject("select * from users where id=?",new Object[]{id},
					new RowMapper<User>(){
						public User mapRow(ResultSet rs, int rowNum)throws SQLException{
							User user = new User();
							user.setId(rs.getString("id"));
							user.setName(rs.getString("name"));
							user.setPassword(rs.getString("password"));
							return user;
						}
			});
			
		}
		
		public void deleteAll() throws SQLException{
			
			this.jdbcTemplate.update("delete from users");
			
		}
		
		
		public int getCount(){
			return this.jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
		}
		
	
	}
	~~~

* Remove a overrap

	- UserDao.class
	~~~java
	public class UserDao {
	
	
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
		
		public void add(final User user)throws ClassNotFoundException, SQLException{
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
		
		public void deleteAll() throws SQLException{
			
			this.jdbcTemplate.update("delete from users");
			
		}
		
		
		public int getCount(){
			return this.jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
		}
		
	
	}
	~~~

* 
