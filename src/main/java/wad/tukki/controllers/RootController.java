package wad.tukki.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RootController extends BaseController {

    @RequestMapping("/")
    public String root() {
        return "static/index.html";
    }
}