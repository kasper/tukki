package wad.tukki.services;

import com.mongodb.Mongo;
import java.net.UnknownHostException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import wad.tukki.exceptions.UsernameExistsException;
import wad.tukki.models.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring-context.xml",
                                   "file:src/main/webapp/WEB-INF/spring-test-database.xml",
                                   "file:src/main/webapp/WEB-INF/spring-security.xml"})
public class AuthenticationServiceTest {

    @Autowired
    UserService userService;
    
    @Autowired
    AuthenticationService authenticationService;
    
    @AfterClass
    public static void afterClass() throws UnknownHostException {
        
        MongoTemplate mongoTemplate = new MongoTemplate(new Mongo(), "test");
        mongoTemplate.dropCollection(User.class);
    }
    
    
    @Before
    public void setUp() throws UsernameExistsException {
        
        User user = new User();
        user.setUsername("kasper");
        user.setPassword("repsakkasper");
        
        if (userService.findByUsername(user.getUsername()) == null) {
            userService.create(user);
        }
    }
    
    @After
    public void tearDown() {
        authenticationService.invalidate();
    }
    
    @Test
    public void canAuthenticateWithExistingUser() {
        
        User user = new User();
        user.setUsername("kasper");
        user.setPassword("repsakkasper");
        
        authenticationService.authenticate(user);
        
        assertTrue(authenticationService.isAuthenticated());
    }
    
    @Test(expected = BadCredentialsException.class)
    public void badCredentialsCaught() {
        
        User user = new User();
        user.setUsername("kasper");
        user.setPassword("kasper");
        
        authenticationService.authenticate(user);
    }
    
    @Test
    public void afterInvalidationNotAuthenticated() {
        
        User user = new User();
        user.setUsername("kasper");
        user.setPassword("repsakkasper");
        
        authenticationService.authenticate(user);
        authenticationService.invalidate();
        
        assertFalse(authenticationService.isAuthenticated());
    }
    
    @Test
    public void getUsernameOfUnauthenticatedUserReturnsNull() {
        assertEquals(null, authenticationService.getUsername());
    }
    
    @Test
    public void correctUsernameForAuthenticatedUser() {
        
        User user = new User();
        user.setUsername("kasper");
        user.setPassword("repsakkasper");
        
        authenticationService.authenticate(user);
        
        assertEquals("kasper", authenticationService.getUsername());
    }
}