package wad.tukki.controllers;

import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import wad.tukki.models.JSONResponse;

public abstract class BaseController {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public JSONResponse handleException(HttpServletResponse response, Exception exception) {
        
        response.setStatus(400);
        
        JSONResponse error = new JSONResponse();
        error.setMessage(exception.getMessage());
        
        return error;
    }
}
