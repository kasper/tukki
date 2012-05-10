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
    
    @JsonIgnore
    private String implementerId;
    
    @Transient
    private User implementer;
    
    private boolean done;
    
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

    public String getImplementerId() {
        return implementerId;
    }

    public User getImplementer() {
        return implementer;
    }

    public void setImplementer(User implementer) {
        
        if (implementer != null) {
            this.implementerId = implementer.getId();
        } else {
            this.implementerId = null;
        }
        
        this.implementer = implementer;
    }

    public boolean isDone() {
        return done;
    }
    
    public void setDone(boolean done) {
        this.done = done;
    }
    
    public Status getStatus() {
        
        if (done) {
            return new Status(StatusCode.DONE, "Done");
        }
        
        if (implementer != null) {
            return new Status(StatusCode.IN_PROGRESS, "In progress");
        }
        
        return new Status(StatusCode.IN_QUEUE, "In queue");
    }
}