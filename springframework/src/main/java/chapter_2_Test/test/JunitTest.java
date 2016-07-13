package chapter_2_Test.test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.matchers.JUnitMatchers.hasItem;
import static org.junit.matchers.JUnitMatchers.either;


import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class JunitTest {
	
	@Autowired
	ApplicationContext context;
	
	static Set<JunitTest> testObjects = new HashSet<JunitTest>();
	static ApplicationContext contextObject = null;
	
	@Test
	public void test1(){
		assertThat(testObjects,not(hasItem(this)) );
		testObjects.add(this);
	}
	
	@Test
	public void test2(){
		assertThat(testObjects,not(hasItem(this)) );
		testObjects.add(this);
	}
	
	@Test
	public void test3(){
		assertThat(testObjects,not(hasItem(this)) );
		testObjects.add(this);
		
		/*assertThat(contextObject, either(is(nullValue())).or(is(this.contextObject)));
		contextObject = this.context;*/
	}

}
