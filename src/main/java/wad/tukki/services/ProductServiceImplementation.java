package wad.tukki.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wad.tukki.models.Product;
import wad.tukki.repositories.ProductRepository;

@Service
public class ProductServiceImplementation implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    
    @Override
    public Product save(Product product) {
        
        if (product == null) {
            return null;
        }
        
        return productRepository.save(product);
    }
    
    @Override
    public Product findById(String id) {
        return productRepository.findOne(id);
    }
    
    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
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