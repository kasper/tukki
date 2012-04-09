package wad.tukki.services;

import com.mongodb.Mongo;
import java.net.UnknownHostException;
import java.util.ArrayList;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import wad.tukki.models.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring-context.xml",
                                   "file:src/main/webapp/WEB-INF/spring-database.xml",
                                   "file:src/main/webapp/WEB-INF/spring-security.xml"})
public class UserServiceTest {

    @Autowired
    private UserService userService;
    
    @Autowired
    private MongoTemplate mongoTemplate;
    
    @AfterClass
    public static void afterClass() throws UnknownHostException {
        
        MongoTemplate mongoTemplate = new MongoTemplate(new Mongo(), "test");
        mongoTemplate.dropCollection(User.class);
    }
    
    @Test
    public void addingNullReturnsNull() {
        
        User user = userService.save(null);
        assertEquals(null, user);
    }
    
    @Test
    public void addingUserIncreasesCount() {
        
        long startCount = userService.count();
        userService.save(new User());
        long endCount = userService.count();
        
        assertEquals(startCount + 1, endCount);
    }
    
    @Test
    public void addedUserFoundByIdFromDatabase() {
        
        User user = new User();
        user = userService.save(user);
        
        assertEquals(user, userService.findById(user.getId()));
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
    public void deletedUserNotFoundByIdFromDatabase() {
        
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