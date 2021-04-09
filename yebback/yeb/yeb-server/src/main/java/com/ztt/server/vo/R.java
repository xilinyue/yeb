package com.ztt.server.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author LiuSu
 * @create 2021/3/12 16:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class R {
    private long code;
    private String message;
    private Object data;

    /**
     * 成功
     * @param message
     * @return
     */
    public static R success(String message) {
        return new R(0,message,null);
    }

    /**
     * 成功
     * @param message,obj
     * @return
     */
    public static R success(String message,Object obj){
        return new R(0,message,obj);
    }

    /**
     * 成功
     * @param obj
     * @return
     */
    public static R success(Object obj){
        return new R(0,"成功",obj);
    }

    /**
     * 失败
     * @param message
     * @return
     */
    public static R error(String message) {
        return new R(-1,message,null);
    }
}
