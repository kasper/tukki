package wad.tukki.models;

public class JSONMessage {

    private JSONMessageCode code;
    private String message;
    
    public JSONMessage(JSONMessageCode code, String message) {
        this.code = code;
        this.message = message;
    }

    public JSONMessageCode getCode() {
        return code;
    }
    
    public String getMessage() {
        return message;
    }
}