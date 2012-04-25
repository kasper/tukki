package wad.tukki.models;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class UserRole extends MongoObject {
    
    private String name;
    
    public UserRole(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}