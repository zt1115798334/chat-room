package com.example.chatroom.custom;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/9 10:18
 * description:系统常量
 */
public class SysConst {

    public static final int DEFAULT_BATCH_SIZE = 200;

    ///////////////////////////////////////////////////////////////////////////
    // 通用常量 -- 多实体类通用
    ///////////////////////////////////////////////////////////////////////////

    public enum ResultStatusCode {
        SUCCESS(0, "success"),
        FAILED(1, "failed"),
        ERROR(2, "error"),
        SYSTEM_ERROR(99, "system_error");

        private Integer code;
        private String name;

        ResultStatusCode(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        public Integer getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }


}
