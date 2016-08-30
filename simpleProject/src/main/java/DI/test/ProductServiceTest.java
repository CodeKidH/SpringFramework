/*package DI.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import DI.domain.Product;
import DI.service.ProductService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"../applicationContext.xml"})
public class ProductServiceTest {
	
	@Autowired
	ProductService productService;
	
	@Test
	public void testFindProduct(){
		Product product = productService.findProduct("Kyle");
		assertEquals(new Product("Kyle",100),product);
		
	}
}
*/