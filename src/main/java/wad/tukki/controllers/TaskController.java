package wad.tukki.controllers;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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
    public Object postTask(@PathVariable String productId, @PathVariable int index, @Valid @RequestBody Task task) {
        
        Product product = productService.findById(productId);
        
        if (product == null) {
            return new JSONMessage(JSONMessageCode.NOT_FOUND, "Product not found.");
        }
        
        if (index < 0 || index >= product.getStories().size()) {
            return new JSONMessage(JSONMessageCode.GENERAL_ERROR, "Invalid index.");
        }
        
        User creator = userService.findByUsername(authenticationService.getUsername());
        task.setCreator(creator);
        
        product.getStories().get(index).addTask(task);
        productService.save(product);
        
        return task;
    }
    
    @RequestMapping(method = RequestMethod.DELETE, value = "product/{productId}/story/{storyIndex}/task/{taskIndex}")
    @ResponseBody
    public JSONMessage deleteTask(@PathVariable String productId, @PathVariable int storyIndex, @PathVariable int taskIndex) {
        
        Product product = productService.findById(productId);
        
        if (product == null) {
            return new JSONMessage(JSONMessageCode.NOT_FOUND, "Product not found.");
        }
        
        if (storyIndex < 0 || storyIndex >= product.getStories().size()) {
            return new JSONMessage(JSONMessageCode.GENERAL_ERROR, "Invalid index.");
        }
        
        if (taskIndex < 0 || taskIndex >= product.getStories().get(storyIndex).getTasks().size()) {
            return new JSONMessage(JSONMessageCode.GENERAL_ERROR, "Invalid index.");
        }
        
        product.getStories().get(storyIndex).getTasks().remove(taskIndex);
        productService.save(product);
        
        return new JSONMessage(JSONMessageCode.OK, "Task deleted.");
    }
}