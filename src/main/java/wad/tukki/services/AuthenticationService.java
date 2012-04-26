package wad.tukki.services;

import org.springframework.security.core.Authentication;
import wad.tukki.models.User;

public interface AuthenticationService {

    public void authenticate(User user);
    public void invalidate();
    public Authentication getAuthentication();
    public boolean isAuthenticated();
    public String getUsername();
}