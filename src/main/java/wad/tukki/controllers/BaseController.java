package wad.tukki.controllers;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import wad.tukki.models.JSONErrorResponse;
import wad.tukki.models.JSONResponse;

public abstract class BaseController {
     
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public List<JSONErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        
        ArrayList<JSONErrorResponse> errors = new ArrayList<JSONErrorResponse>();
        
        // Validation errors
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            
            JSONErrorResponse error = new JSONErrorResponse(fieldError.getField(), fieldError.getDefaultMessage());
            errors.add(error);
        }
        
        return errors;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public JSONResponse handleException(Exception exception) {
        return new JSONResponse(exception.getMessage());
    }
}
