package lol.cicco.tbunion.common.exception;

public class HttpRequestException extends RuntimeException {
    private int code;
    private String message;

    public HttpRequestException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public int getCode() {
        return this.code;
    }



}
