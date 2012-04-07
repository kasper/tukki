package wad.tukki.models;

import java.io.IOException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializableWithType;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;

public enum JSONMessageCode implements JsonSerializableWithType {
    
    GENERAL_ERROR (1),
    INTERNAL_ERROR (2),
    PARSING_ERROR (3),
    AUTHENTICATION_REQUIRED (5),
    NOT_FOUND (6),
    VALIDATION_ERROR (7);
    
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