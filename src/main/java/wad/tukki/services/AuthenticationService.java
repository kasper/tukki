package wad.tukki.services;

import wad.tukki.models.User;

public interface AuthenticationService {

    public void authenticate(User user);
    public void invalidate();
    public boolean isAuthenticated();
    public String getUsername();
}