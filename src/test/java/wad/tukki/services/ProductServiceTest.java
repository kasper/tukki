package wad.tukki.services;

import com.mongodb.Mongo;
import java.net.UnknownHostException;
import java.util.ArrayList;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import wad.tukki.models.Product;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring-context.xml",
                                   "file:src/main/webapp/WEB-INF/spring-database.xml",
                                   "file:src/main/webapp/WEB-INF/spring-security.xml"})
public class ProductServiceTest {
    
    @Autowired
    private MongoTemplate mongoTemplate;
    
    @Autowired
    private ProductService productService;
    
    @AfterClass
    public static void afterClass() throws UnknownHostException {
        
        MongoTemplate mongoTemplate = new MongoTemplate(new Mongo(), "test");
        mongoTemplate.dropCollection(Product.class);
    }
    
    @Test
    public void addingNullReturnsNull() {
        
        Product product = productService.save(null);
        assertEquals(null, product);
    }
    
    @Test
    public void addingProductIncreasesCount() {
        
        long startCount = productService.count();
        productService.save(new Product());
        long endCount = productService.count();
        
        assertEquals(startCount + 1, endCount);
    }
    
    @Test
    public void addedProductFoundByIdFromDatabase() {
        
        Product product = new Product();
        product = productService.save(product);
        
        assertEquals(product, productService.findById(product.getId()));
    }
    
    @Test
    public void deletingProductDecreasesCount() {
        
        Product product = productService.save(new Product());
        long startCount = productService.count();
        
        productService.delete(product.getId());
        long endCount = productService.count();
        
        assertEquals(startCount - 1, endCount);
    }
    
    @Test
    public void deletedProductNotFoundByIdFromDatabase() {
        
        Product product = new Product();
        product = productService.save(product);
        productService.delete(product.getId());
        
        assertEquals(null, productService.findById(product.getId()));
    }
    
    @Test
    public void findAllAddedProducts() {
        
        mongoTemplate.dropCollection(Product.class);
        
        ArrayList<Product> products = new ArrayList<Product>();
        
        Product a = productService.save(new Product());
        Product b = productService.save(new Product());
        Product c = productService.save(new Product());
        Product d = productService.save(new Product());
        
        products.add(a);
        products.add(b);
        products.add(c);
        products.add(d);
        
        assertEquals(products, productService.findAll());
    }
}