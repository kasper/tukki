package wad.tukki.controllers;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import wad.tukki.exceptions.UsernameExistsException;
import wad.tukki.models.JSONMessage;
import wad.tukki.models.JSONMessageCode;
import wad.tukki.models.JSONMessageMap;
import wad.tukki.models.User;
import wad.tukki.services.AuthenticationService;
import wad.tukki.services.UserService;

@Controller
@RequestMapping("api")
public class UserController extends JSONBaseController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private AuthenticationService authenticationService;
    
    @RequestMapping(method = RequestMethod.GET, value = "user")
    @ResponseBody
    public User getUser() {
        return userService.findByUsername(authenticationService.getUsername());
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "user", consumes = "application/json")
    @ResponseBody
    public User postUser(@Valid @RequestBody User user) throws UsernameExistsException {
        
        user = userService.create(user);
        user = userService.addUserRole(user.getId(), "user");
        
        return user;
    }
    
    @ExceptionHandler(UsernameExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public JSONMessageMap handleUsernameExistsException(UsernameExistsException exception) {
        
        JSONMessageMap errors = new JSONMessageMap("errors");
        JSONMessage error = new JSONMessage(JSONMessageCode.VALIDATION_ERROR, "Username is already taken.");
        
        errors.put("username", error);
        
        return errors;
    }
}