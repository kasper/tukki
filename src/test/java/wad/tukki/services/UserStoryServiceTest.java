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
import wad.tukki.models.UserStory;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring-context.xml",
                                   "file:src/main/webapp/WEB-INF/spring-test-database.xml",
                                   "file:src/main/webapp/WEB-INF/spring-security.xml"})
public class UserStoryServiceTest {
    
    @Autowired
    private MongoTemplate mongoTemplate;
    
    @Autowired
    private UserStoryService userStoryService;
    
    @AfterClass
    public static void afterClass() throws UnknownHostException {
        
        MongoTemplate mongoTemplate = new MongoTemplate(new Mongo(), "test");
        mongoTemplate.dropCollection(UserStory.class);
    }
    
    @Test
    public void addingUserStoryIncreasesCount() {
        
        long startCount = userStoryService.count();
        userStoryService.save(new UserStory());
        long endCount = userStoryService.count();
        
        assertEquals(startCount + 1, endCount);
    }
    
    @Test
    public void addedUserStoryFoundById() {
        
        UserStory story = new UserStory();
        story = userStoryService.save(story);
        
        assertEquals(story, userStoryService.findById(story.getId()));
    }
    
    @Test
    public void nonExistingUserStoryNotFoundById() {
        assertEquals(null, userStoryService.findById("nontExistingUserStoryId"));
    }
    
    @Test
    public void deletingUserStoryDecreasesCount() {
        
        UserStory story = userStoryService.save(new UserStory());
        long startCount = userStoryService.count();
        
        userStoryService.delete(story.getId());
        long endCount = userStoryService.count();
        
        assertEquals(startCount - 1, endCount);
    }
    
    @Test
    public void deletedUserStoryNotFoundById() {
        
        UserStory story = new UserStory();
        story = userStoryService.save(story);
        userStoryService.delete(story.getId());
        
        assertEquals(null, userStoryService.findById(story.getId()));
    }
    
    @Test
    public void findAllAddedUserStories() {
        
        mongoTemplate.dropCollection(UserStory.class);
        
        ArrayList<UserStory> stories = new ArrayList<UserStory>();
        
        UserStory a = userStoryService.save(new UserStory());
        UserStory b = userStoryService.save(new UserStory());
        UserStory c = userStoryService.save(new UserStory());
        UserStory d = userStoryService.save(new UserStory());
        
        stories.add(a);
        stories.add(b);
        stories.add(c);
        stories.add(d);
        
        assertEquals(stories, userStoryService.findAll());
    }
}