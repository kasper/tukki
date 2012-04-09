package wad.tukki.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import wad.tukki.models.UserRole;

public interface UserRoleRepository extends MongoRepository<UserRole, String> {

    public UserRole findByName(String name);
}