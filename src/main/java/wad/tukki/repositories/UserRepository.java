package wad.tukki.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import wad.tukki.models.User;

public interface UserRepository extends MongoRepository<User, String> {
    
    public User findByUsername(String username);
}