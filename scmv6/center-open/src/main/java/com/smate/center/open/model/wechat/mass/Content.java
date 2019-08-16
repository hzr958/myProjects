package com.smate.center.open.model.wechat.mass;

/**
 * 文本消息内容.
 * 
 * @author xys
 *
 */
public class Content {

  public Content() {

  }

  public Content(String content) {
    this.content = content;
  }

  public String content;

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

}
