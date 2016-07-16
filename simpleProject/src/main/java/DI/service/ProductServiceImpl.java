package DI.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import DI.dao.ProductDao;
import DI.domain.Product;

@Component
public class ProductServiceImpl implements ProductService{
	
	@Autowired
	private ProductDao productDao;
	
	public Product findProduct(String name){
		return productDao.findProduct(name);
	}
}
