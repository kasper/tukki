package wad.tukki.models;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class TaskTest {
    
    @Test
    public void taskInQueue() {
        
        Task task = new Task();
        
        assertTrue(task.getStatus().getCode() == StatusCode.IN_QUEUE);
    }
    
    @Test
    public void taskInProgress() {
        
        Task task = new Task();
        task.setImplementer(new User());
        
        assertTrue(task.getStatus().getCode() == StatusCode.IN_PROGRESS);
    }
    
    @Test
    public void taskDone() {
        
        Task task = new Task();
        task.setDone(true);
        
        assertTrue(task.isDone());
        assertTrue(task.getStatus().getCode() == StatusCode.DONE);
    }
}