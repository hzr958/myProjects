package com.smate.center.job.framework.zookeeper.queue;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.smate.core.base.enums.IBaseEnum;
import java.io.IOException;
import java.util.EnumSet;

/**
 * 消息类型
 */
public enum MessageType implements IBaseEnum<Integer> {
  /**
   * 用于通知节点停止执行的子任务
   */
  INTERRUPT_TASKLET(0, "停止子任务"),
  /**
   * 用于通知主调度停止任务
   */
  INTERRUPT_JOB(1, "停止任务");

  private Integer value;
  private String description;

  MessageType(int val, String desc) {
    this.value = Integer.valueOf(val);
    this.description = desc;
  }

  /**
   * 根据整型值返回对应的MessageType类型，如果val为null或者无法匹配任意一个MessageType类型，将返回null
   *
   * @param val 值
   * @return 对应的枚举值或者null
   */
  public static MessageType valueOf(Integer val) {
    EnumSet<MessageType> messageTypes = EnumSet.allOf(MessageType.class);
    return messageTypes.stream().filter(messageType -> messageType.getValue().equals(val))
        .findFirst().orElse(null);
  }

  @JsonValue
  @Override
  public Integer getValue() {
    return value;
  }

  @Override
  public String getDescription() {
    return description;
  }

  /**
   * MessageType类型json反序列化器
   */
  public static class MessageTypeDeserializer extends JsonDeserializer<MessageType> {

    @Override
    public MessageType deserialize(JsonParser p, DeserializationContext ctxt)
        throws IOException, JsonProcessingException {
      return MessageType.valueOf(p.getValueAsInt());
    }
  }
}
