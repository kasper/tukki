package wad.tukki.services;

import wad.tukki.models.User;

public interface AuthenticationService {

    public void login(User user);
    public void logout();
}