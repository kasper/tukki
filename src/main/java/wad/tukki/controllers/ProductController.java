package wad.tukki.controllers;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import wad.tukki.models.Product;
import wad.tukki.models.User;
import wad.tukki.services.AuthenticationService;
import wad.tukki.services.ProductService;
import wad.tukki.services.UserService;

@Controller
@RequestMapping("api")
public class ProductController extends JSONBaseController {

    @Autowired
    AuthenticationService authenticationService;
    
    @Autowired
    UserService userService;
    
    @Autowired
    ProductService productService;

    @RequestMapping(method = RequestMethod.GET, value = "products")
    @ResponseBody
    public List<Product> getProducts() {
        return productService.findAll();
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "product/{id}")
    @ResponseBody
    public Product getProduct(@PathVariable String id) {
        return productService.findById(id);
    }

    @RequestMapping(method = RequestMethod.POST, value = "product", consumes = "application/json")
    @ResponseBody
    public Product postProduct(@Valid @RequestBody Product product) {
        
        User productOwner = userService.findByUsername(authenticationService.getUsername());
        product.setProductOwner(productOwner);
        
        return productService.save(product);
    }
}