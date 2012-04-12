package wad.tukki.models;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class UserRoleTest {

    @Test
    public void equalUserRoles() {
        
        UserRole a = new UserRole("user");
        a.setId("testUserRoleId");
        
        UserRole b = new UserRole("user");
        b.setId("testUserRoleId");
        
        assertTrue(a.equals(b));
    }
}