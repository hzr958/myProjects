package com.smate.center.open.model.wechat.mass;

/**
 * 语音消息.
 * 
 * @author xys
 *
 */
public class VoiceMessage extends BaseMessage {

  public Voice voice;

  public Voice getVoice() {
    return voice;
  }

  public void setVoice(Voice voice) {
    this.voice = voice;
  }

}
