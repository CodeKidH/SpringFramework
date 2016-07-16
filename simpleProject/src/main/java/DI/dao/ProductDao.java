package DI.dao;

import DI.domain.Product;

public interface ProductDao {
	Product findProduct(String name);
}
