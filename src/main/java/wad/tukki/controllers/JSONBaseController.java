package wad.tukki.controllers;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import wad.tukki.models.JSONAttributeMessage;
import wad.tukki.models.JSONMessage;
import wad.tukki.models.JSONMessageCode;

public abstract class JSONBaseController {
        
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public List<JSONAttributeMessage> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        
        ArrayList<JSONAttributeMessage> errors = new ArrayList<JSONAttributeMessage>();
        
        // Validation errors
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            
            JSONAttributeMessage error = new JSONAttributeMessage(JSONMessageCode.VALIDATION_ERROR,
                                                                  fieldError.getField(),
                                                                  fieldError.getDefaultMessage());
            errors.add(error);
        }
        
        return errors;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public JSONMessage handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        return new JSONMessage(JSONMessageCode.PARSING_ERROR, "Could not read JSON.");
    }
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public JSONMessage handleException(Exception exception) {
        
        System.out.println("handleException");
        System.out.println(exception);
        return new JSONMessage(JSONMessageCode.INTERNAL_ERROR, exception.getMessage());
    }
}