package wad.tukki.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Product extends MongoObject {
    
    private Date createdOn;
    
    @NotBlank(message = "Name may not be empty.")
    private String name;
    
    @DBRef
    private User productOwner;
    
    @DBRef
    private List<UserStory> stories;
    
    public Product() {
        
        createdOn = new Date();
        stories = new ArrayList<UserStory>();
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getProductOwner() {
        return productOwner;
    }

    public void setProductOwner(User productOwner) {
        this.productOwner = productOwner;
    }

    public List<UserStory> getStories() {
        return stories;
    }

    public void setStories(List<UserStory> stories) {
        this.stories = stories;
    }
    
    public boolean canBeDeletedBy(User user) {
        
        if (user.equals(getProductOwner())) {
            return true;
        }
        
        return false;
    }
    
    public void addUserStory(UserStory story) {
        stories.add(story);
    }
}