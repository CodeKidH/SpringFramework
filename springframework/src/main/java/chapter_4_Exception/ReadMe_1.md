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
	
	
    

