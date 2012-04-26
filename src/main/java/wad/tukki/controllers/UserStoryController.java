package wad.tukki.controllers;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import wad.tukki.models.Product;
import wad.tukki.models.User;
import wad.tukki.models.UserStory;
import wad.tukki.services.AuthenticationService;
import wad.tukki.services.ProductService;
import wad.tukki.services.UserService;

@Controller
@RequestMapping("api")
public class UserStoryController extends JSONBaseController {
    
    @Autowired
    AuthenticationService authenticationService;
    
    @Autowired
    UserService userService;
    
    @Autowired
    private ProductService productService;
       
    @RequestMapping(method = RequestMethod.POST, value = "product/{productId}/story", consumes = "application/json")
    @ResponseBody
    public UserStory postUser(@PathVariable String productId, @Valid @RequestBody UserStory story) {
        
        User creator = userService.findByUsername(authenticationService.getUsername());
        story.setCreator(creator);
        
        Product product = productService.findById(productId);
        product.addUserStory(story);
        productService.save(product);
        
        return story;
    }
}