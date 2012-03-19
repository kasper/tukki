package wad.tukki.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wad.tukki.model.Product;
import wad.tukki.repository.ProductRepository;

@Service
public class ProductServiceImplementation implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product addProduct(Product product) {
        
        if (product == null) {
            return null;
        }
        
        return productRepository.save(product);
    }
}
