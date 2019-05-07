package com.weikwer.market.common.bean;

public class Result<T> {
    private int code;
    private String description;
    T data;
    public Result(int code){
        this.code=code;
    }

    public int getCode() {
        return code;
    }

    public T getData() {
        return data;
    }

    public Result<T> setData(T data) {
        this.data = data;
        return this;
    }

    public Result setCode(int code) {
        this.code = code;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Result setDescription(String description) {
        this.description = description;
        return this;
    }

}
