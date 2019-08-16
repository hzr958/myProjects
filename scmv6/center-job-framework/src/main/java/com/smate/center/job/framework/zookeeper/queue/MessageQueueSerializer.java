package com.smate.center.job.framework.zookeeper.queue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.apache.curator.framework.recipes.queue.QueueSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 队列消息序列化器
 *
 * @author Created by houchuanjie
 * @date 2018/06/20 11:42
 */
public class MessageQueueSerializer<T> implements QueueSerializer<Message<T>> {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public byte[] serialize(Message item) {
    byte[] bytes = null;
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      bytes = objectMapper.writeValueAsBytes(item);
    } catch (JsonProcessingException e) {
      logger.error("序列化队列消息失败！", e);
    }
    return bytes;
  }

  @Override
  public Message<T> deserialize(byte[] bytes) {
    Message message = null;
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      message = objectMapper.readValue(bytes, new TypeReference<Message<T>>() {
      });
    } catch (IOException e) {
      logger.error("反序列化队列消息失败！", e);
    }
    return message;
  }
}
