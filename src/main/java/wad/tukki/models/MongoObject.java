package wad.tukki.models;

import org.springframework.data.annotation.Id;

public abstract class MongoObject {

    @Id
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        
        return true;
    }

    @Override
    public int hashCode() {
        
        int hash = 7;
        hash = 41 * hash + (this.id != null ? this.id.hashCode() : 0);
        
        return hash;
    }
}