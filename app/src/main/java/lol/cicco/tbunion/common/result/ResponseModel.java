package lol.cicco.tbunion.common.result;

public class ResponseModel<T> {

    public static final int OK_CODE = 10000;

    public int code;
    public String message;
    public T data;

}
