package wad.tukki.services;

import java.util.List;
import wad.tukki.exceptions.UsernameExistsException;
import wad.tukki.models.User;

public interface UserService {

    public User save(User user);
    public User findById(String id);
    public User findByUsername(String username);
    public User create(User user) throws UsernameExistsException;
    public List<User> findAll();
    public long count();
    public void delete(String id);
    public User addUserRole(String userId, String name);
}