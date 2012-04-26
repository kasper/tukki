package wad.tukki.models;

import java.util.Date;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Transient;

public class UserStory {
    
    private Date whenCreated;
    
    @JsonIgnore
    private String creatorId;
    
    @Transient
    private User creator;
    
    @NotBlank(message = "Title may not be blank.")
    private String scenario;
    
    @NotBlank(message = "Given may not be blank.")
    private String given;
    
    @NotBlank(message = "When may not be blank.")
    private String when;
    
    @NotBlank(message = "Then may not be blank.")
    private String then;

    public UserStory() {
        whenCreated = new Date();
    }
    
    public Date getWhenCreated() {
        return whenCreated;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        
        this.creatorId = creator.getId();
        this.creator = creator;
    }
    
    public String getGiven() {
        return given;
    }

    public void setGiven(String given) {
        this.given = given;
    }

    public String getScenario() {
        return scenario;
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    public String getThen() {
        return then;
    }

    public void setThen(String then) {
        this.then = then;
    }

    public String getWhen() {
        return when;
    }

    public void setWhen(String when) {
        this.when = when;
    }
}