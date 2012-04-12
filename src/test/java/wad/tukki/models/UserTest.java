package wad.tukki.models;

import java.util.ArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class UserTest {

    @Test
    public void addedUserRoleFound() {
        
        User user = new User();
        user.setRoles(new ArrayList<UserRole>());
        
        UserRole role = new UserRole("user");
        role.setId("testUserRole");
        user.addRole(role);
        
        assertTrue(user.getRoles().contains(role));
    }
    
    @Test
    public void canNotAddDuplicateUserRole() {
        
        User user = new User();
        user.setRoles(new ArrayList<UserRole>());
        
        UserRole a = new UserRole("user");
        a.setId("testUserRole");
        
        UserRole b = new UserRole("user");
        b.setId("testUserRole");
        
        user.addRole(a);
        user.addRole(b);
        
        assertEquals(1, user.getRoles().size());
    }
    
    @Test
    public void equalUsers() {
        
        User a = new User();
        a.setId("testUser");
        
        User b = new User();
        b.setId("testUser");
        
        assertTrue(a.equals(b));
    }
}