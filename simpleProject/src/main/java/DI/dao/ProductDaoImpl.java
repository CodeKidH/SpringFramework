package DI.dao;

import org.springframework.stereotype.Component;

import DI.domain.Product;

@Component
public class ProductDaoImpl implements ProductDao{
	
	public Product findProduct(String name){
		
		return new Product(name, 100);
	}
	
	
}
