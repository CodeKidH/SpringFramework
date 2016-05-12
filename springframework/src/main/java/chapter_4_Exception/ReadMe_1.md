# Exceptioin

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
	
		
