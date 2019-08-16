package com.smate.sie.core.base.utils.model.pay.wechat;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 收到微信支付结果通知后，用来通知微信的数据
 * 
 * @author wsn
 * @date Feb 26, 2019
 */
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class BaseResult implements Serializable {

  private String return_code;// 状态码
  private String return_msg;// 返回信息

  public BaseResult() {
    super();
  }

  public BaseResult(String return_code, String return_msg) {
    super();
    this.return_code = return_code;
    this.return_msg = return_msg;
  }

  public String getReturn_code() {
    return return_code;
  }

  public void setReturn_code(String return_code) {
    this.return_code = return_code;
  }

  public String getReturn_msg() {
    return return_msg;
  }

  public void setReturn_msg(String return_msg) {
    this.return_msg = return_msg;
  }


}
