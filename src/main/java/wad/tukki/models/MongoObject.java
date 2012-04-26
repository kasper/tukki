package wad.tukki.models;

import org.springframework.data.annotation.Id;

public abstract class MongoObject {

    @Id
    private String id;

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        
        if (obj == null) {
            return false;
        }
        
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        final MongoObject other = (MongoObject) obj;
        if ((this.getId() == null) ? (other.getId() != null) : !this.getId().equals(other.getId())) {
            return false;
        }
        
        return true;
    }

    @Override
    public int hashCode() {
        
        int hash = 7;
        hash = 41 * hash + (this.getId() != null ? this.getId().hashCode() : 0);
        
        return hash;
    }
}