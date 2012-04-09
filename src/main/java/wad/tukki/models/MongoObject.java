package wad.tukki.models;

import org.springframework.data.annotation.Id;

public abstract class MongoObject {

    @Id
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}