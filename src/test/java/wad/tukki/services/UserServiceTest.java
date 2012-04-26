package wad.tukki.services;

import com.mongodb.Mongo;
import java.net.UnknownHostException;
import java.util.ArrayList;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import wad.tukki.exceptions.UsernameExistsException;
import wad.tukki.models.User;
import wad.tukki.models.UserRole;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring-context.xml",
                                   "file:src/main/webapp/WEB-INF/spring-test-database.xml",
                                   "file:src/main/webapp/WEB-INF/spring-security.xml"})
public class UserServiceTest {

    @Autowired
    private MongoTemplate mongoTemplate;
    
    @Autowired
    private UserRoleService userRoleService;
    
    @Autowired
    private UserService userService;
    
    private UserRole existingRole;
    private User existingUser;
    
    @AfterClass
    public static void afterClass() throws UnknownHostException {
        
        MongoTemplate mongoTemplate = new MongoTemplate(new Mongo(), "test");
        mongoTemplate.dropCollection(User.class);
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
        
        User user = new User();
        user.setUsername("kasper");
        
        if (userService.findByUsername("kasper") == null) {
            existingUser = userService.save(user);
        } else {
            existingUser = userService.findByUsername("kasper");
        }
    }
    
    @Test
    public void addingUserIncreasesCount() {
        
        long startCount = userService.count();
        userService.save(new User());
        long endCount = userService.count();
        
        assertEquals(startCount + 1, endCount);
    }
    
    @Test
    public void addedUserFoundById() {
        
        User user = new User();
        user = userService.save(user);
        
        assertEquals(user, userService.findById(user.getId()));
    }
    
    @Test
    public void nonExistingUserNotFoundById() {
        assertEquals(null, userService.findById("nonExistingUserId"));
    }
    
    @Test
    public void addedUserFoundByUsername() {
        assertEquals(existingUser, userService.findByUsername("kasper"));
    }
    
    @Test
    public void nonExistingUserNotFoundByUsername() {
        assertEquals(null, userService.findByUsername("nonExistingUsername"));
    }
    
    @Test
    public void canCreateUserWithNonExistingUsername() throws UsernameExistsException {
        
        User user = new User();
        user.setUsername("kirk");
        
        userService.create(user);
    }
    
    @Test(expected = UsernameExistsException.class)
    public void cannotCreateUserWithExistingUsername() throws UsernameExistsException {
        
        User user = new User();
        user.setUsername("kasper");
        
        userService.create(user);
    }
    
    @Test
    public void userHasAddedUserRole() {
        
        existingUser.addRole(existingRole);
        existingUser = userService.save(existingUser);
        
        assertTrue(existingUser.getRoles().contains(existingRole));
    }
    
    @Test
    public void deletingUserDecreasesCount() {
        
        User user = userService.save(new User());
        long startCount = userService.count();
        
        userService.delete(user.getId());
        long endCount = userService.count();
        
        assertEquals(startCount - 1, endCount);
    }
    
    @Test
    public void deletedUserNotFoundById() {
        
        User user = new User();
        user = userService.save(user);
        userService.delete(user.getId());
        
        assertEquals(null, userService.findById(user.getId()));
    }
    
    @Test
    public void findAllAddedUsers() {
        
        mongoTemplate.dropCollection(User.class);
        
        ArrayList<User> users = new ArrayList<User>();
        
        User a = userService.save(new User());
        User b = userService.save(new User());
        User c = userService.save(new User());
        User d = userService.save(new User());
        
        users.add(a);
        users.add(b);
        users.add(c);
        users.add(d);
        
        assertEquals(users, userService.findAll());
    }
}