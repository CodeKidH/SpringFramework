package chapter_3_Template.learningtest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

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
		
		@Test
		public void concatenateString()throws IOException{
			assertThat(calculator.concatenate(this.numberFilepath),is("1234"));
		}
}
