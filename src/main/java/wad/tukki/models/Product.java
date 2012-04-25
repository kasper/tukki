package wad.tukki.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Product extends MongoObject {
    
    private Date when;
    
    @NotBlank(message = "Name may not be empty.")
    private String name;
    
    @JsonIgnore
    private String productOwnerId;
    
    @Transient
    private User productOwner;
    
    private List<UserStory> stories;
    
    public Product() {
        
        when = new Date();
        stories = new ArrayList<UserStory>();
    }

    public Date getWhen() {
        return when;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductOwnerId() {
        return productOwnerId;
    }
    
    public User getProductOwner() {
        return productOwner;
    }

    public void setProductOwner(User productOwner) {
        
        this.productOwnerId = productOwner.getId();
        this.productOwner = productOwner;
    }

    public List<UserStory> getStories() {
        return stories;
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