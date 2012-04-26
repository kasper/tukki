package wad.tukki.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import wad.tukki.models.User;

@Service
public class AuthenticationServiceImplementation implements AuthenticationService {

    @Autowired
    @Qualifier("authenticationManager")
    private AuthenticationManager authenticationManager;
    
    @Override
    public void authenticate(User user) {
        
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
    }

    @Override
    public void invalidate() {
        SecurityContextHolder.clearContext();
    }
    
    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public boolean isAuthenticated() {
        
        Authentication authentication = getAuthentication();
        
        if (authentication == null) {
            return false;
        }
        
        return authentication.isAuthenticated();
    }

    @Override
    public String getUsername() {
        
        Authentication authentication = getAuthentication();
        
        if (authentication == null) {
            return null;
        }
        
        return authentication.getName();
    }
}