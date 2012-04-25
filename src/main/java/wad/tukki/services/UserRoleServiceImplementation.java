package wad.tukki.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wad.tukki.models.UserRole;
import wad.tukki.repositories.UserRoleRepository;

@Service
public class UserRoleServiceImplementation implements UserRoleService {
    
    @Autowired
    private UserRoleRepository userRoleRepository;
    
    @Override
    public UserRole save(UserRole userRole) {
        return userRoleRepository.save(userRole);
    }

    @Override
    public UserRole findById(String id) {
        return userRoleRepository.findOne(id);
    }
    
    @Override
    public UserRole findByName(String name) {
        return userRoleRepository.findByName(name);
    }

    @Override
    public List<UserRole> findAll() {
        return userRoleRepository.findAll();
    }

    @Override
    public long count() {
        return userRoleRepository.count();
    }

    @Override
    public void delete(String id) {
        userRoleRepository.delete(id);
    }
}