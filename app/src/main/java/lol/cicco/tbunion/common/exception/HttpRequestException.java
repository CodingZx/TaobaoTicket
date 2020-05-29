package lol.cicco.tbunion.common.exception;

public class HttpRequestException extends RuntimeException {
    private int code;

    public HttpRequestException(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
