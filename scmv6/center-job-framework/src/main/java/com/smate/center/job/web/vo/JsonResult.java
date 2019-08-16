package com.smate.center.job.web.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * ajax请求返回json结果模型
 * 
 * @author houchuanjie
 * @date 2018年2月27日 下午2:51:25
 */
public class JsonResult {

    /**
     * 状态字符串，可选值：success/error
     */
    private boolean success;
    /**
     * 提示消息
     */
    private String msg;
    /**
     * （预留）状态码，用于以后扩充
     */
    @JsonInclude(Include.NON_EMPTY)
    private String code;
    /**
     * 数据信息
     */
    @JsonInclude(Include.NON_NULL)
    private Object data;

    public JsonResult() {
        super();
    }

    public JsonResult(boolean success, String msg, Object data) {
        super();
        this.success = success;
        this.msg = msg;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    /**
     * 设置提示消息
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    /**
     * （预留）状态码，用于以后扩充
     */
    public void setCode(String code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    /**
     * 设置数据信息
     */
    public void setData(Object data) {
        this.data = data;
    }

}
