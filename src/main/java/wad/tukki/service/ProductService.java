package wad.tukki.service;

import wad.tukki.model.Product;

public interface ProductService {
    public Product addProduct(Product product);
    public Product findById(String id);
}
