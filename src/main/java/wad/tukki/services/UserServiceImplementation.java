package wad.tukki.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import wad.tukki.models.User;
import wad.tukki.repositories.UserRepository;

public class UserServiceImplementation implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User save(User user) {
        
        if (user == null) {
            return null;
        }
        
        return userRepository.save(user);
    }

    @Override
    public User findById(String id) {
        return userRepository.findOne(id);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public long count() {
        return userRepository.count();
    }

    @Override
    public void delete(String id) {
        userRepository.delete(id);
    }
}