package wad.tukki.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wad.tukki.models.UserStory;
import wad.tukki.repositories.UserStoryRepository;

@Service
public class UserStoryServiceImplementation implements UserStoryService {
    
    @Autowired
    UserStoryRepository userStoryRepository;

    @Override
    public UserStory save(UserStory userStory) {
        return userStoryRepository.save(userStory);
    }

    @Override
    public UserStory findById(String id) {
        return userStoryRepository.findOne(id);
    }

    @Override
    public List<UserStory> findAll() {
        return userStoryRepository.findAll();
    }

    @Override
    public long count() {
        return userStoryRepository.count();
    }

    @Override
    public void delete(String id) {
        userStoryRepository.delete(id);
    }
}