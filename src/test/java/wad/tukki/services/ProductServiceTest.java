package wad.tukki.services;

import com.mongodb.Mongo;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import wad.tukki.models.Product;
import wad.tukki.models.Task;
import wad.tukki.models.User;
import wad.tukki.models.UserStory;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring-context.xml",
                                   "file:src/main/webapp/WEB-INF/spring-test-database.xml",
                                   "file:src/main/webapp/WEB-INF/spring-security.xml"})
public class ProductServiceTest {
    
    @Autowired
    private MongoTemplate mongoTemplate;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ProductService productService;
    
    @AfterClass
    public static void afterClass() throws UnknownHostException {
        
        MongoTemplate mongoTemplate = new MongoTemplate(new Mongo(), "test");
        mongoTemplate.dropCollection(User.class);
        mongoTemplate.dropCollection(Product.class);
    }
    
    @Test
    public void addingProductIncreasesCount() {
        
        long startCount = productService.count();
        productService.save(new Product());
        long endCount = productService.count();
        
        assertEquals(startCount + 1, endCount);
    }
    
    @Test
    public void addedProductFoundById() {
        
        Product product = new Product();
        product = productService.save(product);
        
        assertEquals(product, productService.findById(product.getId()));
    }
    
    @Test
    public void nonExistingProductNotFoundById() {
        assertNull(productService.findById("nontExistingProductId"));
    }
    
    @Test
    public void deletingProductDecreasesCount() {
        
        Product product = productService.save(new Product());
        long startCount = productService.count();
        
        productService.delete(product.getId());
        long endCount = productService.count();
        
        assertEquals(startCount - 1, endCount);
    }
    
    @Test
    public void deletedProductNotFoundById() {
        
        Product product = new Product();
        product = productService.save(product);
        productService.delete(product.getId());
        
        assertNull(productService.findById(product.getId()));
    }
    
    @Test
    public void findAllAddedProducts() {
        
        mongoTemplate.dropCollection(Product.class);
        
        ArrayList<Product> products = new ArrayList<Product>();
        
        Product a = productService.save(new Product());
        Product b = productService.save(new Product());
        Product c = productService.save(new Product());
        Product d = productService.save(new Product());
        
        products.add(a);
        products.add(b);
        products.add(c);
        products.add(d);
        
        assertEquals(products, productService.findAll());
    }
    
    @Test
    public void productOwnerResolved() {
        
        User user = userService.save(new User());
        
        Product product = new Product();
        product.setProductOwner(user);
        
        product = productService.save(product);
        
        assertEquals(product.getProductOwner(), userService.findById(product.getProductOwnerId()));
    }
    
    @Test
    public void userStoryCreatorsResolved() {
        
        User user = userService.save(new User());
        
        Product product = new Product();
        
        UserStory story = new UserStory();
        story.setCreator(user);
        product.addUserStory(story);
        
        product = productService.save(product);
        
        List<User> expectedStoryCreators = new ArrayList<User>();
        expectedStoryCreators.add(user);
        
        List<User> actualStoryCreators = new ArrayList<User>();
        
        for (UserStory actualStory : product.getStories()) {
            actualStoryCreators.add(actualStory.getCreator());
        }
        
        assertEquals(expectedStoryCreators, actualStoryCreators);
    }
    
    @Test
    public void taskCreatorsResolved() {
        
        User user = userService.save(new User());
             
        Product product = new Product();
        
        UserStory story = new UserStory();
        story.setCreator(user);
        
        Task task = new Task();
        task.setCreator(user);
        story.addTask(task);
        
        product.addUserStory(story);
        
        product = productService.save(product);
        
        List<User> expectedTaskCreators = new ArrayList<User>();
        expectedTaskCreators.add(user);
        
        List<User> actualTaskCreators = new ArrayList<User>();
        
        for (UserStory actualStory : product.getStories()) {
            
            for (Task actualTask : actualStory.getTasks()) {
                actualTaskCreators.add(actualTask.getCreator());
            }
        }
        
        assertEquals(expectedTaskCreators, expectedTaskCreators);
    }
    
    @Test
    public void taskImplementersResolved() {
        
        User creator = userService.save(new User());
        User implementer = userService.save(new User());
             
        Product product = new Product();
        
        UserStory story = new UserStory();
        story.setCreator(creator);
        
        Task task = new Task();
        task.setCreator(creator);
        
        task.setImplementer(implementer);
        story.addTask(task);
        
        product.addUserStory(story);
        
        product = productService.save(product);
        
        List<User> expectedTaskImplementers = new ArrayList<User>();
        expectedTaskImplementers.add(implementer);
        
        List<User> actualTaskImplementers = new ArrayList<User>();
        
        for (UserStory actualStory : product.getStories()) {
            
            for (Task actualTask : actualStory.getTasks()) {
                actualTaskImplementers.add(actualTask.getImplementer());
            }
        }
        
        assertEquals(expectedTaskImplementers, expectedTaskImplementers);
    }
}