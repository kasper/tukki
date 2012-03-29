package wad.tukki.models;

public class JSONMessage {

    private String message;
    
    public JSONMessage(String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}