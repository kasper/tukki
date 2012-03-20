package wad.tukki.services;

import java.util.List;
import wad.tukki.models.Product;

public interface ProductService {
    public Product save(Product product);
    public Product findById(String id);
    public List<Product> findAll();
}
