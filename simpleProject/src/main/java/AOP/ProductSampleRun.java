package AOP;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import AOP.domain.Product;
import AOP.service.ProductService;

public class ProductSampleRun {
	
	public static void main(String[] args){
		
		ProductSampleRun productSampleRun = new ProductSampleRun();
		productSampleRun.execute();
	}
	
	public void execute(){
		ApplicationContext ctx = new ClassPathXmlApplicationContext("/AOP/applicationContext.xml");
		ProductService productService = ctx.getBean(ProductService.class);
		Product product = productService.findProduct("Kyle");
		System.out.println(product);
		
	}
}
