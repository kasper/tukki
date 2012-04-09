package wad.tukki.models;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializableWithType;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;

public class JSONMessageMap implements JsonSerializableWithType {

    private String name;
    private Map<String, JSONMessage> messages;
    
    public JSONMessageMap(String name) {
        
        this.name = name;
        messages = new HashMap<String, JSONMessage>();
    }
    
    public void put(String key, JSONMessage value) {
        messages.put(key, value);
    }

    @Override
    public void serializeWithType(JsonGenerator generator, SerializerProvider provider, TypeSerializer serializer) 
                                  throws IOException, JsonProcessingException {
        serialize(generator, provider);
    }

    @Override
    public void serialize(JsonGenerator generator, SerializerProvider provider) 
                          throws IOException, JsonProcessingException {
        
        generator.writeStartObject();
        generator.writeObjectField(name, messages);
        generator.writeEndObject();
    }
}