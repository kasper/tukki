package wad.tukki.models;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class ProductTest {
       
    @Test
    public void equalProducts() {
        
        Product a = new Product();
        a.setId("test");
        
        Product b = new Product();
        b.setId("test");
        
        assertTrue(a.equals(b));
    }
}