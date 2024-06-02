package com.iweb.derxt.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CallResult<T> implements Serializable {
    private int code;
    private String message;
    private T result;

    public static <T> CallResult<T> success() {
        return new CallResult<>(BusinessCodeEnum.DEFAULT_SUCCESS.getCode(), BusinessCodeEnum.DEFAULT_SUCCESS.getMsg(), null);
    }
    public static <T> CallResult<T> success(T result) {
        return new CallResult<>(BusinessCodeEnum.DEFAULT_SUCCESS.getCode(), BusinessCodeEnum.DEFAULT_SUCCESS.getMsg(), result);
    }
    public static <T> CallResult<T> sucess(int code, String message, T result){
        return new CallResult<T>(code, message, result);
    }

    public static <T> CallResult<T> fail() {
        return new CallResult<>(BusinessCodeEnum.DEFAULT_SYS_ERROR.getCode(), BusinessCodeEnum.DEFAULT_SYS_ERROR.getMsg(), null);
    }
    public static <T> CallResult<T> fail(T result) {
        return new CallResult<>(BusinessCodeEnum.DEFAULT_SYS_ERROR.getCode(), BusinessCodeEnum.DEFAULT_SYS_ERROR.getMsg(), result);
    }
    public static <T> CallResult<T> fail(int code, String message) {
        return new CallResult<>(code, message, null);
    }
    public static <T> CallResult<T> fail(int code, String message, T result){
        return new CallResult<T>(code, message, result);
    }

    public boolean isSuccess(){
        return this.code == BusinessCodeEnum.DEFAULT_SUCCESS.getCode();
    }

}
