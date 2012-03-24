package wad.tukki.services;

import com.mongodb.Mongo;
import java.net.UnknownHostException;
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
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring-context.xml", "file:src/main/webapp/WEB-INF/spring-database.xml"})
public class ProductServiceTest {
    
    @Autowired
    private ProductService productService;
    
    @AfterClass
    public static void afterClass() throws UnknownHostException {
        
        MongoTemplate mongoTemplate = new MongoTemplate(new Mongo(), "test");
        mongoTemplate.dropCollection(Product.class);
    }
      
    @Test
    public void addedProductExistsInDatabase() {
        
        Product product = new Product();
        product.setName("Tukki");
        product = productService.save(product);
        
        assertEquals(product, productService.findById(product.getId()));
    }
}
