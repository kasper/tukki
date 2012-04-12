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
    
    @Override
    public boolean equals(Object obj) {
        
        if (obj == null) {
            return false;
        }
        
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        final UserStory other = (UserStory) obj;
        if ((this.getId() == null) ? (other.getId() != null) : !this.getId().equals(other.getId())) {
            return false;
        }
        
        return true;
    }

    @Override
    public int hashCode() {
        
        int hash = 7;
        hash = 89 * hash + (this.getId() != null ? this.getId().hashCode() : 0);
        
        return hash;
    }
}