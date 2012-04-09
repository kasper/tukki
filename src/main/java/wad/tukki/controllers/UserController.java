package wad.tukki.controllers;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import wad.tukki.models.User;
import wad.tukki.services.UserService;

@Controller
@RequestMapping("api")
public class UserController extends JSONBaseController {

    @Autowired
    private UserService userService;
    
    @RequestMapping(method = RequestMethod.POST, value = "user", consumes = "application/json")
    @ResponseBody
    public User postUser(@Valid @RequestBody User user) {
        
        user = userService.save(user);
        user = userService.addUserRole(user.getId(), "user");
        
        return user;
    }
}