package wad.tukki.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import wad.tukki.models.UserStory;

public interface UserStoryRepository extends MongoRepository<UserStory, String> {

}