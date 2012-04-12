package wad.tukki.models;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class UserStoryTest {

    @Test
    public void equalUserStories() {
        
        UserStory a = new UserStory();
        a.setId("testUserStoryId");
        
        UserStory b = new UserStory();
        b.setId("testUserStoryId");
        
        assertTrue(a.equals(b));
    }
}