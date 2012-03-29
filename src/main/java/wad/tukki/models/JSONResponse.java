package wad.tukki.models;

public class JSONResponse {

    private String message;
    
    public JSONResponse(String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}