package DI.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import DI.dao.ProductDao;
import DI.domain.Product;
//bean¡§¿«
/*public class ProductServiceImpl implements ProductService{
	
	private ProductDao productDao;
	
	public Product findProduct(String name){
		return productDao.findProduct(name);
	}
	
	public void setProductDao(ProductDao productDao){
		this.productDao = productDao;
	}
}*/

//annotation
@Component
public class ProductServiceImpl implements ProductService{

@Autowired
private ProductDao productDao;

public Product findProduct(String name){
	return productDao.findProduct(name);
}

}

//applicationContext
/*@Component
public class ProductServiceImpl implements ProductService{
	
	private ProductDao productDao;
	
	@Autowired
	private ApplicationContext ac;
	
	public Product findProduct(String name){
		productDao = (ProductDao) ac.getBean("/DI/dao/ProductDaoImpl.class");
		return productDao.findProduct(name);
	}
	
}*/
