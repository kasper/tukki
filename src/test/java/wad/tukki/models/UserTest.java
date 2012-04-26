package wad.tukki.models;

import java.util.ArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class UserTest {
    
    private class UserRoleStub extends UserRole {
        
        private String stubId;
        
        public UserRoleStub(String stubId, String name) {
            super(name);
            this.stubId = stubId;
        }
        
        @Override
        public String getId() {
            return stubId;
        }
    }

    @Test
    public void addedUserRoleFound() {
        
        User user = new User();
        user.setRoles(new ArrayList<UserRole>());
        
        UserRole role = new UserRoleStub("testUserRoleId", "user");
        
        user.addRole(role);
        
        assertTrue(user.getRoles().contains(role));
    }
    
    @Test
    public void canNotAddDuplicateUserRole() {
        
        User user = new User();
        user.setRoles(new ArrayList<UserRole>());
        
        UserRole a = new UserRoleStub("testUserRoleId", "user");
        UserRole b = new UserRoleStub("testUserRoleId", "user");
        
        user.addRole(a);
        user.addRole(b);

        assertEquals(1, user.getRoles().size());
    }
}