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
public class UserStoryController extends JSONBaseController {
    
    @Autowired
    AuthenticationService authenticationService;
    
    @Autowired
    UserService userService;
    
    @Autowired
    private ProductService productService;
       
    @RequestMapping(method = RequestMethod.POST, value = "product/{productId}/story", consumes = "application/json")
    @ResponseBody
    public Object postUserStory(@PathVariable String productId, @Valid @RequestBody UserStory story) {
        
        Product product = productService.findById(productId);
        
        if (product == null) {
            return new JSONMessage(JSONMessageCode.NOT_FOUND, "Product not found.");
        }
        
        User creator = userService.findByUsername(authenticationService.getUsername());
        story.setCreator(creator);
        
        product.addUserStory(story);
        productService.save(product);
        
        return story;
    }
    
    @RequestMapping(method = RequestMethod.PUT, value = "product/{productId}/story/{from}/to/{to}")
    @ResponseBody
    public Object prioritiseUserStory(@PathVariable String productId, @PathVariable Integer from, @PathVariable Integer to) {
        
        Product product = productService.findById(productId);
        
        if (product == null) {
            return new JSONMessage(JSONMessageCode.NOT_FOUND, "Product not found.");
        }
        
        User authenticatedUser = userService.findByUsername(authenticationService.getUsername());
        
        if (!product.isProductOwner(authenticatedUser)) {
            return new JSONMessage(JSONMessageCode.GENERAL_ERROR, "Only the product owner can prioritise the user stories.");
        }
        
        product.prioritiseUserStory(from, to);
        productService.save(product);
        
        return new JSONMessage(JSONMessageCode.OK, "User story prioritised.");
    }
    
    @RequestMapping(method = RequestMethod.DELETE, value = "product/{productId}/story/{userStoryId}")
    @ResponseBody
    public JSONMessage deleteUserStory(@PathVariable String productId, @PathVariable String userStoryId) {
        
        System.out.println("Deleting user story from product: " + productId);
        System.out.println("Deleting user story with index: " + userStoryId);
        
        return new JSONMessage(JSONMessageCode.OK, "User story deleted.");
    }
}