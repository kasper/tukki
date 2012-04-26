package wad.tukki.models;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class MongoObjectTest {
    
    private class MongoObjectStub extends MongoObject {
        
        private String stubId;
        
        public MongoObjectStub(String stubId) {
            this.stubId = stubId;
        }
        
        @Override
        public String getId() {
            return stubId;
        }
    }

    @Test
    public void equalMongoObjects() {
        
        MongoObject a = new MongoObjectStub("testMongoObjectId");
        MongoObject b = new MongoObjectStub("testMongoObjectId");
        
        assertTrue(a.equals(b));
    }
    
    @Test
    public void notEqualMongoObjects() {
        
        MongoObject a = new MongoObjectStub("testMongoObjectIdA");
        MongoObject b = new MongoObjectStub("testMongoObjectIdB");
        
        assertFalse(a.equals(b));
    }
}