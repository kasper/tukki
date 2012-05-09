package wad.tukki.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wad.tukki.models.Product;
import wad.tukki.models.Task;
import wad.tukki.models.UserStory;
import wad.tukki.repositories.ProductRepository;

@Service
public class ProductServiceImplementation implements ProductService {

    @Autowired
    private UserService userService;
    
    @Autowired
    private ProductRepository productRepository;
    
    private void resolveLinks(Product... products) {
        
        resolveProductOwner(products);
        resolveUserStoryCreators(products);
        resolveTaskCreators(products);
    }
    
    private void resolveProductOwner(Product... products) {
        
        for (Product product : products) {
            
            if (product.getProductOwnerId() != null) {
                product.setProductOwner(userService.findById(product.getProductOwnerId()));
            }
        }
    }
    
    private void resolveUserStoryCreators(Product... products) {
        
        for (Product product : products) {
            
            for (UserStory story : product.getStories()) {
                story.setCreator(userService.findById(story.getCreatorId()));
            }
        }
    }
    
    private void resolveTaskCreators(Product... products) {
        
        for (Product product : products) {
            
            for (UserStory story : product.getStories()) {
                
                for (Task task : story.getTasks()) {
                    task.setCreator(userService.findById(task.getCreatorId()));
                }
            }
        }
    }
    
    @Override
    public Product save(Product product) {
        
        product = productRepository.save(product);
        resolveLinks(product);
        
        return product;
    }
    
    @Override
    public Product findById(String id) {
        
        Product product = productRepository.findOne(id);
        
        if (product == null) {
            return null;
        }
        
        resolveLinks(product);
        
        return product;
    }
    
    @Override
    public List<Product> findAll() {
        
        List<Product> products = productRepository.findAll();
        
        Product[] productsAsArray = new Product[products.size()];
        products.toArray(productsAsArray);
        
        resolveLinks(productsAsArray);
        
        return products;
    }
    
    @Override
    public long count() {
        return productRepository.count();
    }
    
    @Override
    public void delete(String id) {
        productRepository.delete(id);
    }
}