package wad.tukki.services;

import com.mongodb.Mongo;
import java.net.UnknownHostException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
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
    
    @BeforeClass
    public static void beforeClass() throws UnknownHostException {
        
        User user = new User();
        user.setUsername("kasper");
        user.setPassword("repsakkasper");
        
        MongoTemplate mongoTemplate = new MongoTemplate(new Mongo(), "test");
        mongoTemplate.save(user);
    }
    
    @AfterClass
    public static void afterClass() throws UnknownHostException {
        
        MongoTemplate mongoTemplate = new MongoTemplate(new Mongo(), "test");
        mongoTemplate.dropCollection(User.class);
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
    public void getAuthenticationOfAuthenticatedUser() {
        
        User user = new User();
        user.setUsername("kasper");
        user.setPassword("repsakkasper");
        
        authenticationService.authenticate(user);
        
        assertNotNull(authenticationService.getAuthentication());
    }
    
    @Test
    public void getAuthenticationOfUnauthenticatedUser() {
        assertNull(authenticationService.getAuthentication());
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
    public void getUsernameOfAuthenticatedUser() {
        
        User user = new User();
        user.setUsername("kasper");
        user.setPassword("repsakkasper");
        
        authenticationService.authenticate(user);
        
        assertEquals(user.getUsername(), authenticationService.getUsername());
    }
    
    @Test
    public void getUsernameOfUnauthenticatedUserReturnsNull() {
        assertNull(authenticationService.getUsername());
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