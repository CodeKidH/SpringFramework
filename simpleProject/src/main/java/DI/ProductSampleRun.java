package DI;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import DI.domain.Product;
import DI.service.ProductService;

public class ProductSampleRun {
	
	public static void main(String[] args){
		
		ProductSampleRun productSampleRun = new ProductSampleRun();
		productSampleRun.execute();
	}
	
	public void execute(){
		ApplicationContext ctx = new ClassPathXmlApplicationContext("/DI/applicationContext.xml");
		ProductService productService = ctx.getBean(ProductService.class);
		Product product = productService.findProduct("Kyle");
		System.out.println(product);
		
	}
}
