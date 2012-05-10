package wad.tukki.models;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class UserStoryTest {
    
    @Test
    public void userStoryInQueue() {
        
        UserStory story = new UserStory();
        story.addTask(new Task());
        
        assertTrue(story.getStatus().getCode() == StatusCode.IN_QUEUE);
    }
    
    @Test
    public void userStoryInProgress() {
        
        UserStory story = new UserStory();
        
        Task task = new Task();
        task.setImplementer(new User());
        
        story.addTask(task);
        
        assertTrue(story.getStatus().getCode() == StatusCode.IN_PROGRESS);
    }
    
    @Test
    public void taskDone() {
        
        UserStory story = new UserStory();
        
        Task task = new Task();
        task.setDone(true);
        
        story.addTask(task);
        
        assertTrue(story.getStatus().getCode() == StatusCode.DONE);
    }
}