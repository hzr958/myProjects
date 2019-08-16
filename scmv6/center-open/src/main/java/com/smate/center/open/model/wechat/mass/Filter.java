package com.smate.center.open.model.wechat.mass;

/**
 * 用于设定消息的接收者.
 * 
 * @author xys
 *
 */
public class Filter {

  public boolean is_to_all;// 是否向全部用户发送
  public String group_id;// 群发到的分组的group_id，若is_to_all值为true，可不填写group_id

  public boolean getIs_to_all() {
    return is_to_all;
  }

  public void setIs_to_all(boolean is_to_all) {
    this.is_to_all = is_to_all;
  }

  public String getGroup_id() {
    return group_id;
  }

  public void setGroup_id(String group_id) {
    this.group_id = group_id;
  }

}
