package wad.tukki.controllers;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import wad.tukki.exceptions.ItemNotFoundException;
import wad.tukki.models.*;
import wad.tukki.services.AuthenticationService;
import wad.tukki.services.ProductService;
import wad.tukki.services.UserService;

@Controller
@RequestMapping("api")
public class TaskController extends JSONBaseController {
    
    @Autowired
    AuthenticationService authenticationService;
    
    @Autowired
    UserService userService;
    
    @Autowired
    private ProductService productService;
           
    @RequestMapping(method = RequestMethod.POST, value = "product/{productId}/story/{index}/task", consumes = "application/json")
    @ResponseBody
    public Task postTask(@PathVariable String productId, @PathVariable int index, @Valid @RequestBody Task task) throws ItemNotFoundException {
        
        Product product = productService.findById(productId);
        
        if (product == null) {
            throw new ItemNotFoundException("Product not found.");
        }
        
        User creator = userService.findByUsername(authenticationService.getUsername());
        task.setCreator(creator);
        
        product.getStories().get(index).addTask(task);
        productService.save(product);
        
        return task;
    }
    
    @RequestMapping(method = RequestMethod.DELETE, value = "product/{productId}/story/{storyIndex}/task/{taskIndex}")
    @ResponseBody
    public JSONMessage deleteTask(@PathVariable String productId, @PathVariable int storyIndex, @PathVariable int taskIndex) throws ItemNotFoundException {
        
        Product product = productService.findById(productId);
        
        if (product == null) {
            throw new ItemNotFoundException("Product not found.");
        }
        
        product.getStories().get(storyIndex).getTasks().remove(taskIndex);
        productService.save(product);
        
        return new JSONMessage(JSONMessageCode.OK, "Task deleted.");
    }
    
    @RequestMapping(method = RequestMethod.PUT, value = "product/{productId}/story/{storyIndex}/task/{taskIndex}/implementer")
    @ResponseBody
    public JSONMessage toggleImplementer(@PathVariable String productId, @PathVariable int storyIndex, @PathVariable int taskIndex) throws ItemNotFoundException {
        
        Product product = productService.findById(productId);
        
        if (product == null) {
            throw new ItemNotFoundException("Product not found.");
        }
        
        User user = userService.findByUsername(authenticationService.getUsername());
        
        Task task = product.getStories().get(storyIndex).getTasks().get(taskIndex);
        
        // Already an implementer
        if (task.getImplementer() != null) {
            
            if (!task.getImplementer().equals(user)) {
                return new JSONMessage(JSONMessageCode.GENERAL_ERROR, "Task already has an implementer.");
            }
            
            user = null;
        }
        
        task.setImplementer(user);
        productService.save(product);
        
        return new JSONMessage(JSONMessageCode.OK, "Implementer toggled.");
    }
    
    @RequestMapping(method = RequestMethod.PUT, value = "product/{productId}/story/{storyIndex}/task/{taskIndex}/done")
    @ResponseBody
    public JSONMessage toggleDone(@PathVariable String productId, @PathVariable int storyIndex, @PathVariable int taskIndex) throws ItemNotFoundException {
        
        Product product = productService.findById(productId);
        
        if (product == null) {
            throw new ItemNotFoundException("Product not found.");
        }
        
        User user = userService.findByUsername(authenticationService.getUsername());
        
        Task task = product.getStories().get(storyIndex).getTasks().get(taskIndex);
        
        if (!task.getImplementer().equals(user)) {
            return new JSONMessage(JSONMessageCode.GENERAL_ERROR, "Only the implementer can set the task as done.");
        }
        
        if (task.isDone()) {
            task.setDone(false);
        } else {
            task.setDone(true);
        }
        
        productService.save(product);
        
        return new JSONMessage(JSONMessageCode.OK, "Done toggled.");
    }
}