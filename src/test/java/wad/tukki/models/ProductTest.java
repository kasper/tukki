package wad.tukki.models;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class ProductTest {
    
    @Test
    public void canBeDeletedByProductOwner() {
        
        User productOwner = new User();
        productOwner.setId("testProductOwner");
        
        Product product = new Product();
        product.setProductOwner(productOwner);
        
        assertTrue(product.canBeDeletedBy(productOwner));
    }
    
    @Test
    public void cannotBeDeletedByOthers() {
        
        User user = new User();
        user.setEmail("testUser");
        
        Product product = new Product();
        
        assertFalse(product.canBeDeletedBy(user));
    }
       
    @Test
    public void equalProducts() {
        
        Product a = new Product();
        a.setId("testProduct");
        
        Product b = new Product();
        b.setId("testProduct");
        
        assertTrue(a.equals(b));
    }
}