package wad.tukki.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wad.tukki.exceptions.UsernameExistsException;
import wad.tukki.models.User;
import wad.tukki.models.UserRole;
import wad.tukki.repositories.UserRepository;
import wad.tukki.repositories.UserRoleRepository;

@Service
public class UserServiceImplementation implements UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserRoleRepository userRoleRepository;
    
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
    public User create(User user) throws UsernameExistsException {
        
        if (findByUsername(user.getUsername()) != null) {
            throw new UsernameExistsException("Username already exists.");
        }
        
        return save(user);
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

    @Override
    public User addUserRole(String userId, String name) {
        
        User user = userRepository.findOne(userId);
        
        if (user == null) {
            return null;
        }
        
        UserRole role = userRoleRepository.findByName(name);
        
        if (role == null) {
            role = new UserRole(name);
            role = userRoleRepository.save(role);
        }
        
        user.addRole(role);
        
        return userRepository.save(user);
    }
}