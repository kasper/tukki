package wad.tukki.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api")
public class AuthenticationController extends JSONBaseController {

    @Autowired
    @Qualifier("authenticationManager")
    private AuthenticationManager authenticationManager;
    
    @RequestMapping("login")
    public void login() {
        
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("kasper", "repsak");
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
    }
    
    @RequestMapping("logout")
    public void logout() {
        SecurityContextHolder.clearContext();
    }
}