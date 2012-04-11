package wad.tukki.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import wad.tukki.models.JSONMessage;
import wad.tukki.models.JSONMessageCode;
import wad.tukki.models.User;
import wad.tukki.services.AuthenticationService;

@Controller
@RequestMapping("api")
public class AuthenticationController extends JSONBaseController {
    
    @Autowired
    AuthenticationService authenticationService;
    
    @Autowired
    TokenBasedRememberMeServices rememberMeServices;
    
    @RequestMapping(method = RequestMethod.POST, value = "login", consumes = "application/json")
    @ResponseBody
    public JSONMessage login(HttpServletRequest request, 
                             HttpServletResponse response,
                             @RequestParam(required = false, defaultValue = "false") boolean remember,
                             @RequestBody User user) {
        
        authenticationService.authenticate(user);
        
        // Remember me
        if (remember) {
            rememberMeServices.onLoginSuccess(request, response, authenticationService.getAuthentication());
        }
        
        return new JSONMessage(JSONMessageCode.AUTHENTICATED, "Authenticated.");
    }
    
    @RequestMapping("logout")
    @ResponseBody
    public JSONMessage logout(HttpServletRequest request, HttpServletResponse response) {
        
        authenticationService.invalidate();
        rememberMeServices.logout(request, response, authenticationService.getAuthentication());
        
        return new JSONMessage(JSONMessageCode.OK, "Logged out.");
    }
    
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseBody
    public JSONMessage handleBadCredentialsException(BadCredentialsException exception) {
        return new JSONMessage(JSONMessageCode.BAD_CREDENTIALS, "Wrong username or password.");
    }
}