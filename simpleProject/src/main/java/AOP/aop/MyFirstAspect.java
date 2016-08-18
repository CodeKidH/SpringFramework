package AOP.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import AOP.domain.Product;

@Aspect
@Component
public class MyFirstAspect {
	
	@Before("execution(* getProduct(String))")
	public void before(JoinPoint jp){
		System.out.println("Hello Before!");
		
		Signature sig = jp.getSignature();
		System.out.println("Methodname:"+sig.getName());
		Object[] obj = jp.getArgs();
		
		System.out.println("instance value:"+obj[0]);
	}
	
	@After("execution(* getProduct(String))")
	public void after(){
		System.out.println("Hello After");
	}
	
	@AfterReturning(value="execution(* getProduct(String))", returning="product")
	public void afterReturning(Product product){
		//메소드 호출이 예외를 내보내지 않고 종료했을때 동작함
		System.out.println("Hello AfterReturning");
	}
	
	@Around("execution(* getProduct(String))")
	public Product around(ProceedingJoinPoint pjp)throws Throwable{
		//메소드전후
		System.out.println("Hello Around before");
		
		Signature sig = pjp.getSignature();
		System.out.println("around name:"+sig.getName());
		
		Product p = (Product)pjp.proceed();
		System.out.println("Hello Around After");
		return p;
	}
	
	@AfterThrowing(value="execution(* getProduct(String))", throwing="ex")
	public void afterThrowing(Throwable ex){
		
		//예외 발생시
		System.out.println("Hello Throwing");
	}
	

}
