package wad.tukki.services;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import wad.tukki.models.User;
import wad.tukki.models.UserRole;

@Service
public class UserDetailsServiceImplementation implements UserDetailsService {

    @Autowired
    private UserService userService;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        User user = userService.findByUsername(username);
        
        if (user == null) {
            throw new UsernameNotFoundException("Username not found.");
        }
        
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                                                                      user.getPassword(),
                                                                      getUserRolesAsGrantedAuthorities(user.getRoles()));
    }
    
    private List<GrantedAuthority> getUserRolesAsGrantedAuthorities(List<UserRole> roles) {
        
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        
        for (UserRole role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        
        return authorities;
    }
}