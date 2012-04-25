package wad.tukki.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.Size;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class User extends MongoObject {
    
    private Date when;
    
    @NotBlank(message = "Username may not be blank.")
    private String username;
    
    @Size(min = 10, message = "Password must be at least 10 characters long.")
    private String password;
    
    @NotBlank(message = "Email may not be blank.")
    @Email(message = "Email is not well-formed.")
    private String email;

    @JsonIgnore
    private List<String> roleIds;
    
    @Transient
    private List<UserRole> roles;
    
    public User() {
        
        when = new Date();
        roleIds = new ArrayList<String>();
        roles = new ArrayList<UserRole>();
    }

    public Date getWhen() {
        return when;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRoleIds() {
        return roleIds;
    }
    
    public List<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(List<UserRole> roles) {
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public void addRole(UserRole role) {
        
        if (roles.contains(role)) {
            return;
        }
        
        roleIds.add(role.getId());
        roles.add(role);
    }
}