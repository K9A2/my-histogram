package main.java.com.stormlin.common;

public class RequiredKeyNotFoundException extends RuntimeException {

    private String key;
    private String type;

    public RequiredKeyNotFoundException(String type, String key) {
        super();
        this.key = key;
        this.type = type;
    }

    public String toString() {
        String messageTemplate = "[Type: %s, Key: %s] Not Found";
        return String.format(messageTemplate, this.type, this.key);
    }

}
