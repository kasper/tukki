package wad.tukki.models;

import java.io.IOException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializableWithType;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;

public enum StatusCode implements JsonSerializableWithType {
    
    // Status
    IN_QUEUE (1),
    IN_PROGRESS (2),
    DONE (3);
    
    private int code;
    
    StatusCode(int code) {
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