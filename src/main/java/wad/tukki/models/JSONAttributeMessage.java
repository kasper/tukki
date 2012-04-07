package wad.tukki.models;

public class JSONAttributeMessage extends JSONMessage {

    private String attribute;
    
    public JSONAttributeMessage(JSONMessageCode code, String attribute, String message) {
        
        super(code, message);
        this.attribute = attribute;
    }
    
    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }
}