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

    public void setCode(JSONMessageCode code) {
        this.code = code;
    }
    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}