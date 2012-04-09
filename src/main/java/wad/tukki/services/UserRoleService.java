package wad.tukki.services;

import java.util.List;
import wad.tukki.models.UserRole;

public interface UserRoleService {

    public UserRole save(UserRole userRole);
    public UserRole findById(String id);
    public UserRole findByName(String name);
    public List<UserRole> findAll();
    public long count();
    public void delete(String id);
}