package com.smate.center.open.model.wechat.mass;

/**
 * 卡券消息内容.
 * 
 * @author xys
 *
 */
public class Wxcard {

  public Wxcard() {

  }

  public Wxcard(String card_id) {
    this.card_id = card_id;
  }

  public String card_id;

  public String getCard_id() {
    return card_id;
  }

  public void setCard_id(String card_id) {
    this.card_id = card_id;
  }

}
