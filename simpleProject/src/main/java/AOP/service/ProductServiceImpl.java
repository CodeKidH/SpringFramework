package AOP.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import AOP.dao.ProductDao;
import AOP.domain.Product;

@Component
public class ProductServiceImpl implements ProductService{

@Autowired
private ProductDao productDao;

public Product findProduct(String name){
	return productDao.getProduct(name);
}

}

