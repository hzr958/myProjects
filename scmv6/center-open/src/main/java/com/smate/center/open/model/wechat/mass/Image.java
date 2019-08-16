package com.smate.center.open.model.wechat.mass;

/**
 * 图片消息内容.
 * 
 * @author xys
 *
 */
public class Image {

  public Image() {

  }

  public Image(String media_id) {
    this.media_id = media_id;
  }

  public String media_id;

  public String getMedia_id() {
    return media_id;
  }

  public void setMedia_id(String media_id) {
    this.media_id = media_id;
  }

}
