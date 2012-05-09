package wad.tukki.models;

import java.util.Date;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Transient;

public class Task {

    private Date whenCreated;
    
    @JsonIgnore
    private String creatorId;
    
    @Transient
    private User creator;
    
    @NotBlank(message = "Description may not be blank.")
    private String description;
    
    public Task() {
        whenCreated = new Date();
    }

    public Date getWhenCreated() {
        return whenCreated;
    }
    
    public User getCreator() {
        return creator;
    }

    public String getCreatorId() {
        return creatorId;
    }
    
    public void setCreator(User creator) {
        
        this.creatorId = creator.getId();
        this.creator = creator;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}