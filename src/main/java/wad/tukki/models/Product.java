package wad.tukki.models;

import java.util.Date;
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
    
    public Product() {
        createdOn = new Date();
    }

    public Date getCreatedOn() {
        return createdOn;
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
    
    public boolean canBeDeletedBy(User user) {
        
        if (user.equals(getProductOwner())) {
            return true;
        }
        
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        
        if (obj == null) {
            return false;
        }
        
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        final Product other = (Product) obj;
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