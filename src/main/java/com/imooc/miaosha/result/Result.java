package com.imooc.miaosha.result;

import lombok.Data;

@Data
public class Result<T> {

    private int code;
    private String msg;
    private T data;


    private Result(T data) {
        this.code = 0;
        this.msg = CodeMsg.SUCCESS.getMsg();
        this.data = data;
    }

    private Result(CodeMsg cm) {
        if(cm == null) {
            return;
        }
        this.code = cm.getCode();
        this.msg = cm.getMsg();
    }

    /**
     * 成功时候的调用
     * */
    public static <T> Result<T> success(T t) {
        return new Result<T>(t);
    }



    /**
     * 失败时候的调用
     * */
    public static <T> Result<T> error(CodeMsg cm){
        return new  Result<T>(cm);
    }
}
