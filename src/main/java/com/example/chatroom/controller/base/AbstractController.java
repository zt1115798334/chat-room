package com.example.chatroom.controller.base;

import com.alibaba.fastjson.JSONObject;
import com.example.chatroom.custom.SysConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/9 14:52
 * description:
 */
public abstract class AbstractController {

    private SysConst.ResultStatusCode successCode = SysConst.ResultStatusCode.SUCCESS;
    private SysConst.ResultStatusCode failedCode = SysConst.ResultStatusCode.FAILED;

    /**
     * 统一的日志对象，子类中不再需要定义 Logger类的日志对象，可以直接调用父类中的日志对象记录日志
     */
    protected final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 格式输出结果
     *
     * @return
     */
    protected JSONObject success() {
        return result(successCode.getCode(), successCode.getName(), null);
    }

    /**
     * 格式输出结果
     *
     * @param msg 描述
     * @return
     */
    protected JSONObject success(String msg) {
        return result(successCode.getCode(), msg, null);
    }

    /**
     * 格式输出结果
     *
     * @param data 数据
     * @return
     */
    protected JSONObject success(Object data) {
        return result(successCode.getCode(), successCode.getName(), data);
    }

    /**
     * 格式输出结果
     *
     * @param msg  描述
     * @param data 数据
     * @return
     */
    protected JSONObject success(String msg, Object data) {
        return result(successCode.getCode(), msg, data);
    }

    /**
     * 格式输出结果
     *
     * @return
     */
    protected JSONObject failure() {
        return result(failedCode.getCode(), failedCode.getName(), null);
    }

    /**
     * 格式输出结果
     *
     * @param msg 描述
     * @return
     */
    protected JSONObject failure(String msg) {
        return result(failedCode.getCode(), msg, null);
    }

    private JSONObject result(Integer status, String msg, Object data) {
        JSONObject json = new JSONObject();
        //状态
        json.put("status", status);
        //描述
        json.put("msg", msg);
        //数据
        json.put("data", data);
        return json;
    }


    /**
     * 格式输出结果
     *
     * @param pageNumber 当前页
     * @param pageSize   页大小
     * @param total      总记录数
     * @param rows       当前页数据
     * @return
     */
    protected JSONObject success(long pageNumber, long pageSize, long total, Object rows) {
        return buildSuccessResultForPaging("success", pageNumber, pageSize, total, rows);
    }

    /**
     * 格式输出结果
     *
     * @param pageNumber 当前页
     * @param pageSize   页大小
     * @param total      总记录数
     * @param rows       当前页数据
     * @return
     */
    protected JSONObject failure(long pageNumber, long pageSize, long total, Object rows) {
        return buildSuccessResultForPaging("failure", pageNumber, pageSize, total, rows);
    }

    private JSONObject buildSuccessResultForPaging(String msg, long pageNumber, long pageSize,
                                                   long total, Object rows) {
        long tempPageSize = pageSize;
        if (pageSize <= 0) {
            tempPageSize = 1;
        }
        JSONObject json = new JSONObject();
        //状态
        json.put("status", 0);
        //描述
        json.put("msg", msg);
        //当前页
        json.put("pageNumber", pageNumber);
        //页大小
        json.put("pageSize", tempPageSize);
        //总页数
        json.put("pageCount", total / tempPageSize + (total % tempPageSize == 0 ? 0 : 1));
        //总记录数
        json.put("total", total);
        //当前页数据
        json.put("rows", rows);
        return json;
    }


    /**
     * 格式输出结果
     *
     * @return
     */
    protected JSONObject failureForPaging() {
        return buildFailResultForPaging("failed");
    }

    /**
     * 格式输出结果
     *
     * @return
     */
    protected JSONObject failureForPaging(String msg) {
        return buildFailResultForPaging(msg);
    }

    private JSONObject buildFailResultForPaging(String msg) {
        long pageSize = 1;
        JSONObject json = new JSONObject();
        //状态
        json.put("status", 1);
        //描述
        json.put("msg", msg);
        //当前页
        json.put("pageNumber", 0);
        //页大小
        json.put("pageSize", pageSize);
        //总页数
        json.put("pageCount", 0);
        //总记录数
        json.put("total", 0);
        //当前页数据
        json.put("rows", null);
        return json;
    }


}
