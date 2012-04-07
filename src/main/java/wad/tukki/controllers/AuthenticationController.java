package wad.tukki.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import wad.tukki.models.JSONMessage;
import wad.tukki.models.JSONMessageCode;
import wad.tukki.models.User;

@Controller
@RequestMapping("api")
public class AuthenticationController extends JSONBaseController {

    @Autowired
    @Qualifier("authenticationManager")
    private AuthenticationManager authenticationManager;
    
    @RequestMapping(method = RequestMethod.POST, value = "login", consumes = "application/json")
    @ResponseBody
    public JSONMessage login(@RequestBody User user) {
        
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        
        return new JSONMessage(JSONMessageCode.AUTHENTICATED, "Authenticated.");
    }
    
    @RequestMapping("logout")
    @ResponseBody
    public JSONMessage logout() {
        
        SecurityContextHolder.clearContext();
        
        return new JSONMessage(JSONMessageCode.GENERAL_MESSAGE, "Logged out.");
    }
    
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseBody
    public JSONMessage handleBadCredentialsException(BadCredentialsException exception) {
        return new JSONMessage(JSONMessageCode.BAD_CREDENTIALS, "Wrong username or password.");
    }
}