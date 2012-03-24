package wad.tukki.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import wad.tukki.models.Product;

public interface ProductRepository extends MongoRepository<Product, String> {

}