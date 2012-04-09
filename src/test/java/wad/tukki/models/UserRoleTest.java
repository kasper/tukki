package wad.tukki.models;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class UserRoleTest {

    @Test
    public void equalUserRoles() {
        
        UserRole a = new UserRole("user");
        a.setId("testUserRole");
        
        UserRole b = new UserRole("user");
        b.setId("testUserRole");
        
        assertTrue(a.equals(b));
    }
}