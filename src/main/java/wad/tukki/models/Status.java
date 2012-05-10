package wad.tukki.models;

public class Status {

    private StatusCode code;
    private String message;

    public Status(StatusCode code, String message) {
        this.code = code;
        this.message = message;
    }

    public StatusCode getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}