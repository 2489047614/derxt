package com.iweb.derxt.sso.model.enums;

import java.util.HashMap;
import java.util.Map;

public enum LoginType {
    WX(0, "微信登录");
    private static final Map<Integer, LoginType> CODE_MAP = new HashMap<>(3);

    static {
        for (LoginType topicType : values()) {
            CODE_MAP.put(topicType.getCode(), topicType);
        }
    }

    /**
     * 根据code获取枚举值
     *
     * @param code return
     */
    public static LoginType value0fCode(int code) {
        return CODE_MAP.get(code);
    }

    private int code;
    private String msg;

    LoginType(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
