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
    public void canBeDeletedByProductOwner() {
        
        User productOwner = new UserStub("testProductOwnerId");    
        
        Product product = new Product();
        product.setProductOwner(productOwner);
        
        assertTrue(product.canBeDeletedBy(productOwner));
        
    }
    
    @Test
    public void cannotBeDeletedByOthers() {
        
        User productOwner = new UserStub("testProductOwnerId");
        
        Product product = new Product();
        product.setProductOwner(productOwner);
        
        User user = new UserStub("testUserId");
        
        assertFalse(product.canBeDeletedBy(user));
    }
}