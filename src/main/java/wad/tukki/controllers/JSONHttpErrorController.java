package wad.tukki.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import wad.tukki.models.JSONMessage;

@Controller
@RequestMapping("api")
public class JSONHttpErrorController {
    
    @RequestMapping("404")
    @ResponseBody
    public JSONMessage notFound() {
        return new JSONMessage("Not found.");
    }

    @RequestMapping("403")
    @ResponseBody
    public JSONMessage accessDenied() {
        return new JSONMessage("Authentication required.");
    }
}