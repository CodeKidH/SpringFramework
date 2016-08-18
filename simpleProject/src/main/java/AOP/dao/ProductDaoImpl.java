package AOP.dao;

import org.springframework.stereotype.Component;

import AOP.domain.Product;

@Component
public class ProductDaoImpl implements ProductDao{
	
	public Product getProduct(String name){
		
		return new Product(name, 100);
	}
	
	
}
