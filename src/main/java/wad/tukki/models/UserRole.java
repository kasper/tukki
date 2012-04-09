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

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        
        if (obj == null) {
            return false;
        }
        
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        final UserRole other = (UserRole) obj;
        if ((this.getId() == null) ? (other.getId() != null) : !this.getId().equals(other.getId())) {
            return false;
        }
        
        return true;
    }

    @Override
    public int hashCode() {
        
        int hash = 7;
        hash = 23 * hash + (this.getId() != null ? this.getId().hashCode() : 0);
        
        return hash;
    }
}