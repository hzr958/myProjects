package com.smate.web.mobile.v8pub.enums;

import java.io.IOException;
import java.util.EnumSet;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.smate.core.base.enums.AbstractBaseEnumConverter;
import com.smate.core.base.enums.IBaseEnum;

/**
 * 个人库成果状态枚举
 * 
 * @author houchuanjie
 * @date 2018/06/01 15:54
 */
public enum PubSnsStatusEnum implements IBaseEnum<Integer> {
  DELETED(1, "已删除"), DEFAULT(0, "默认状态");
  private Integer value;
  private String description;

  PubSnsStatusEnum(Integer value, String description) {
    this.value = value;
    this.description = description;
  }

  public static PubSnsStatusEnum valueOf(Integer val) {
    EnumSet<PubSnsStatusEnum> enumSet = EnumSet.allOf(PubSnsStatusEnum.class);
    return enumSet.stream().filter(e -> e.getValue().equals(val)).findFirst().orElse(DEFAULT);
  }

  @Override
  public Integer getValue() {
    return value;
  }

  @Override
  public String getDescription() {
    return description;
  }

  /**
   * PubSnsStatusEnum持久化属性枚举转换器
   */
  public static class PubSnsStatusEnumConverter extends AbstractBaseEnumConverter<PubSnsStatusEnum, Integer> {
  }
  /**
   * PubSnsStatusEnum反序列化器
   */
  public static class PubSnsStatusEnumDeserializer extends JsonDeserializer<PubSnsStatusEnum> {
    @Override
    public PubSnsStatusEnum deserialize(JsonParser p, DeserializationContext ctxt)
        throws IOException, JsonProcessingException {
      return PubSnsStatusEnum.valueOf(p.getValueAsInt());
    }
  }
}
