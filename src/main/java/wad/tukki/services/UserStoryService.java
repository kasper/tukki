package wad.tukki.services;

import java.util.List;
import wad.tukki.models.UserStory;

public interface UserStoryService {

    public UserStory save(UserStory userStory);
    public UserStory findById(String id);
    public List<UserStory> findAll();
    public long count();
    public void delete(String id);
}