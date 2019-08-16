package com.smate.center.job.framework.zookeeper.queue;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.smate.center.job.framework.zookeeper.queue.MessageType.MessageTypeDeserializer;
import java.io.Serializable;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 消息
 *
 * @author Created by houchuanjie
 * @date 2018/06/20 11:42
 */
public class Message<T> implements Serializable {

  private static final long serialVersionUID = 5418667156113933589L;

  @JsonDeserialize(using = MessageTypeDeserializer.class)
  private MessageType type;
  private T content;

  public Message(MessageType type, T content) {
    this.type = type;
    this.content = content;
  }

  public Message() {
  }

  public MessageType getType() {
    return type;
  }

  public void setType(MessageType type) {
    this.type = type;
  }

  public T getContent() {
    return content;
  }

  public void setContent(T content) {
    this.content = content;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("type", type).append("content", content).toString();
  }
}
