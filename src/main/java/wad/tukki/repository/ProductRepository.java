package wad.tukki.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import wad.tukki.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {

}
