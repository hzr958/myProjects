package com.smate.core.base.utils.model;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 通用的ajax请求返回结果类
 * 
 * @author ChuanjieHou
 * @date 2017年9月18日
 */
public class AjaxResult {

  /**
   * 状态字符串，可选值：success/error
   */
  private String status;
  /**
   * 提示消息
   */
  @JsonInclude(Include.NON_NULL)
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

  public static final String SUCCESS = "success";

  public static final String ERROR = "error";

  public AjaxResult() {
    super();
  }

  /**
   * 通过给定返回数据对象创建AjaxResult对象；通过此方式创建的对象，不能够调用{@link #addData()}增加返回数据，除非给定的data对象是Map
   * 
   * @param success true 表示成功，false表示失败
   * @param msg 消息提示内容
   * @param data 任意类型数据对象
   */
  public AjaxResult(boolean success, String msg, Object data) {
    this.msg = msg;
    this.data = data;
    this.status = success ? SUCCESS : ERROR;
  }

  /**
   * 不给定返回数据对象，创建AjaxResult对象；返回数据信息可通过方法{@link #addData()}增加。
   * 
   * @param success
   * @param msg
   */
  public AjaxResult(boolean success, String msg) {
    this.msg = msg;
    this.status = success ? SUCCESS : ERROR;
  }

  /**
   * 增加返回数据，返回数据结果例如："data": {"key1": value1, "key2": value2}；
   * 如果构造AjaxResult时给定的data对象不是Map类型，调用此方法不会添加key-value数据，返回false； 只有当data类型时Map时，才会添加成功，返回true
   *
   * @param key 键
   * @param value 值
   * @return 当data为Map时，添加成功返回true；否则返回false
   * @author houchuanjie
   * @date 2018年1月8日 下午3:08:46
   */
  @SuppressWarnings("unchecked")
  public boolean addData(String key, Object value) {
    if (data == null) {
      data = new HashMap<String, Object>();
    }
    if (data instanceof Map) {
      Map<String, Object> dataMap = (Map<String, Object>) data;
      dataMap.put(key, value);
      return true;
    }
    return false;
  }

  public String getStatus() {
    return status;
  }

  /**
   * 设置状态字符串，可选值：success/error
   */
  public void setStatus(String status) {
    this.status = status;
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
