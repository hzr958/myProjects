package com.smate.center.open.model.wechat.mass;

/**
 * 文本消息.
 * 
 * @author xys
 *
 */
public class TextMessage extends BaseMessage {

  public Content text;

  public Content getText() {
    return text;
  }

  public void setText(Content text) {
    this.text = text;
  }

}
