package com.smate.center.open.model.wechat.mass;

/**
 * 图片消息.
 * 
 * @author xys
 *
 */
public class ImageMessage extends BaseMessage {

  public Image image;

  public Image getImage() {
    return image;
  }

  public void setImage(Image image) {
    this.image = image;
  }

}
