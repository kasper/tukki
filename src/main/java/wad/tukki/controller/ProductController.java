package wad.tukki.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import wad.tukki.model.Product;
import wad.tukki.service.ProductService;

@Controller
@RequestMapping("api")
public class ProductController {
    
    @Autowired
    ProductService productService;

    @RequestMapping(method = RequestMethod.GET, value = "product/{id}")
    @ResponseBody
    public Product getProduct(@PathVariable String id) {
        Product product = new Product();
        product.setId(id);
        product.setName("Hello World!");
        return product;
    }
}
