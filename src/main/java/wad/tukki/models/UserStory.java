package wad.tukki.models;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class UserStory extends MongoObject {
    
    @NotBlank(message = "Title may not be blank.")
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}