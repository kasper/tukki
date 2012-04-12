package wad.tukki.services;

import com.mongodb.Mongo;
import java.net.UnknownHostException;
import java.util.ArrayList;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import wad.tukki.models.UserRole;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring-context.xml",
                                   "file:src/main/webapp/WEB-INF/spring-database.xml",
                                   "file:src/main/webapp/WEB-INF/spring-security.xml"})
public class UserRoleServiceTest {

    @Autowired
    private MongoTemplate mongoTemplate;
    
    @Autowired
    private UserRoleService userRoleService;
    
    private UserRole existingRole;
    
    @AfterClass
    public static void afterClass() throws UnknownHostException {
        
        MongoTemplate mongoTemplate = new MongoTemplate(new Mongo(), "test");
        mongoTemplate.dropCollection(UserRole.class);
    }
    
    @Before
    public void setUp() {
        
        UserRole role = new UserRole("user");
        
        if (userRoleService.findByName("user") == null) {
            existingRole = userRoleService.save(role);
        } else {
            existingRole = userRoleService.findByName("user");
        }
    }
    
    @Test
    public void addingNullReturnsNull() {
        
        UserRole role = userRoleService.save(null);
        assertEquals(null, role);
    }
    
    @Test
    public void addingUserRoleIncreasesCount() {
        
        long startCount = userRoleService.count();
        userRoleService.save(new UserRole(""));
        long endCount = userRoleService.count();
        
        assertEquals(startCount + 1, endCount);
    }
    
    @Test
    public void addedUserRoleFoundById() {
        
        UserRole role = new UserRole("");
        role = userRoleService.save(role);
        
        assertEquals(role, userRoleService.findById(role.getId()));
    }
    
    @Test
    public void nonExistingUserRoleNotFoundById() {
        assertEquals(null, userRoleService.findById("nonExistingUserRoleId"));
    }
    
    @Test
    public void addedUserRoleFoundByName() {
        assertEquals(existingRole, userRoleService.findByName("user"));
    }
    
    @Test
    public void nonExistingUserRoleNotFoundByName() {
        assertEquals(null, userRoleService.findByName("nonExistingName"));
    }
    
    @Test
    public void deletingUserRoleDecreasesCount() {
        
        UserRole role = userRoleService.save(new UserRole(""));
        long startCount = userRoleService.count();
        
        userRoleService.delete(role.getId());
        long endCount = userRoleService.count();
        
        assertEquals(startCount - 1, endCount);
    }
    
    @Test
    public void deletedUserNotFoundById() {
        
        UserRole role = new UserRole("");
        role = userRoleService.save(role);
        userRoleService.delete(role.getId());
        
        assertEquals(null, userRoleService.findById(role.getId()));
    }
    
    @Test
    public void findAllAddedUserRoles() {
        
        mongoTemplate.dropCollection(UserRole.class);
        
        ArrayList<UserRole> roles = new ArrayList<UserRole>();
        
        UserRole a = userRoleService.save(new UserRole(""));
        UserRole b = userRoleService.save(new UserRole(""));
        UserRole c = userRoleService.save(new UserRole(""));
        UserRole d = userRoleService.save(new UserRole(""));
        
        roles.add(a);
        roles.add(b);
        roles.add(c);
        roles.add(d);
        
        assertEquals(roles, userRoleService.findAll());
    }
}