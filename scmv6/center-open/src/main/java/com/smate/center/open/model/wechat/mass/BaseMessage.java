package com.smate.center.open.model.wechat.mass;

import java.util.List;

/**
 * 群发消息基类.
 * 
 * @author xys
 *
 */
public class BaseMessage {

  public Filter filter;// 用于设定消息的接收者
  public List<String> touser;// 消息的接收者，一串OpenID列表，OpenID最少2个，最多10000个
  public String msgtype;// 消息类型

  public Filter getFilter() {
    return filter;
  }

  public void setFilter(Filter filter) {
    this.filter = filter;
  }

  public List<String> getTouser() {
    return touser;
  }

  public void setTouser(List<String> touser) {
    this.touser = touser;
  }

  public String getMsgtype() {
    return msgtype;
  }

  public void setMsgtype(String msgtype) {
    this.msgtype = msgtype;
  }

}
