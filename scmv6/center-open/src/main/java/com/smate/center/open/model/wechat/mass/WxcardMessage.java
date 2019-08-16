package com.smate.center.open.model.wechat.mass;

/**
 * 卡券消息.
 * 
 * @author xys
 *
 */
public class WxcardMessage extends BaseMessage {

  public Wxcard wxcard;

  public Wxcard getWxcard() {
    return wxcard;
  }

  public void setWxcard(Wxcard wxcard) {
    this.wxcard = wxcard;
  }

}
