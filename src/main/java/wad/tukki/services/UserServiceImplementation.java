package wad.tukki.services;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wad.tukki.exceptions.UsernameExistsException;
import wad.tukki.models.User;
import wad.tukki.models.UserRole;
import wad.tukki.repositories.UserRepository;

@Service
public class UserServiceImplementation implements UserService {

    @Autowired
    private UserRoleService userRoleService;
    
    @Autowired
    private UserRepository userRepository;
    
    private void resolveRoles(User... users) {
        
        for (User user : users) {
            
            List<UserRole> roles = new ArrayList<UserRole>();
            
            for (String roleId : user.getRoleIds()) {
                roles.add(userRoleService.findById(roleId));
            }
            
            user.setRoles(roles);
        }
    }
    
    @Override
    public User save(User user) {
        
        user = userRepository.save(user);
        resolveRoles(user);
        
        return user;
    }

    @Override
    public User findById(String id) {
        
        User user = userRepository.findOne(id);
        
        if (user == null) {
            return null;
        }
        
        resolveRoles(user);
        
        return user;
    }

    @Override
    public User findByUsername(String username) {
        
        User user = userRepository.findByUsername(username);
        
        if (user == null) {
            return null;
        }
        
        resolveRoles(user);
        
        return user;
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
        
        List<User> users = userRepository.findAll();
        
        User[] usersAsArray = new User[users.size()];
        users.toArray(usersAsArray);
        
        resolveRoles(usersAsArray);
        
        return users;
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
        
        UserRole role = userRoleService.findByName(name);
        
        if (role == null) {
            role = new UserRole(name);
            role = userRoleService.save(role);
        }
        
        user.addRole(role);
        
        return save(user);
    }
}