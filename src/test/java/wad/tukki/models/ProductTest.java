package wad.tukki.models;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class ProductTest {
    
    private class UserStub extends User {
        
        private String stubId;
        
        public UserStub(String stubId) {
            this.stubId = stubId;
        }
        
        @Override
        public String getId() {
            return stubId;
        }
    }
    
    @Test
    public void isProductOwner() {
        
        User productOwner = new UserStub("testProductOwnerId");    
        
        Product product = new Product();
        product.setProductOwner(productOwner);
        
        assertTrue(product.isProductOwner(productOwner));
    }
    
    @Test
    public void isNotProductOwner() {
        
        User productOwner = new UserStub("testProductOwnerId");
        
        Product product = new Product();
        product.setProductOwner(productOwner);
        
        User user = new UserStub("testUserId");
        
        assertFalse(product.isProductOwner(user));
    }
}