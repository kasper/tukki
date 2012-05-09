package wad.tukki.models;

import java.util.LinkedList;
import java.util.List;
import static org.junit.Assert.*;
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
    
    @Test
    public void prioritise() {
        
        Product product = new Product();
        
        UserStory a = new UserStory();
        UserStory b = new UserStory();
        
        product.addUserStory(a);
        product.addUserStory(b);
        
        product.prioritiseUserStory(1, 0);
        
        List<UserStory> expected = new LinkedList<UserStory>();
        expected.add(b);
        expected.add(a);
        
        assertEquals(expected, product.getStories());
    }
}