package wad.tukki.models;

import java.io.IOException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializableWithType;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;

public enum JSONMessageCode implements JsonSerializableWithType {
    
    // Messages
    GENERAL_MESSAGE (100),
    
    // Errors
    GENERAL_ERROR (200),
    INTERNAL_ERROR (201),
    PARSING_ERROR (202),
    VALIDATION_ERROR (203),
    
    // Status
    NOT_FOUND (300),
    
    // Authentication
    AUTHENTICATION_REQUIRED (400),
    BAD_CREDENTIALS (401),
    AUTHENTICATED (402);
    
    private int code;
    
    JSONMessageCode(int code) {
        this.code = code;
    }

    @Override
    public void serializeWithType(JsonGenerator generator, SerializerProvider provider, TypeSerializer serializer) 
                                  throws IOException, JsonProcessingException {
        serialize(generator, provider);
    }

    @Override
    public void serialize(JsonGenerator generator, SerializerProvider provider) 
                          throws IOException, JsonProcessingException {
        generator.writeNumber(code);
    }
}