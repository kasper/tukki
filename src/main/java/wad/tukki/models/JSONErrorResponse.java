package wad.tukki.models;

public class JSONErrorResponse extends JSONResponse {

    private String attribute;
    
    public JSONErrorResponse(String attribute, String message) {
        
        super(message);
        this.attribute = attribute;
    }
    
    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }
}
