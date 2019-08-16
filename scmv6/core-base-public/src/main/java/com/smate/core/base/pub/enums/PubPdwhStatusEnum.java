package com.smate.core.base.pub.enums;

import java.io.IOException;
import java.util.EnumSet;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.smate.core.base.enums.AbstractBaseEnumConverter;
import com.smate.core.base.enums.IBaseEnum;

/**
 * 基准库状态枚举
 * 
 * @author YJ
 *
 *         2019年3月5日
 */
public enum PubPdwhStatusEnum implements IBaseEnum<Integer> {
  DEFAULT(0, "未删除"), DELETED(1, "已删除");


  private Integer value;
  private String description;

  PubPdwhStatusEnum(Integer value, String description) {
    this.value = value;
    this.description = description;
  }

  public static PubPdwhStatusEnum valueOf(Integer val) {
    EnumSet<PubPdwhStatusEnum> enumSet = EnumSet.allOf(PubPdwhStatusEnum.class);
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
   * PubPdwhStatusEnum持久化属性枚举转换器
   */
  public static class PubPdwhStatusEnumConverter extends AbstractBaseEnumConverter<PubPdwhStatusEnum, Integer> {
  }
  /**
   * PubPdwhStatusEnum反序列化器
   */
  public static class PubPdwhStatusEnumDeserializer extends JsonDeserializer<PubPdwhStatusEnum> {
    @Override
    public PubPdwhStatusEnum deserialize(JsonParser p, DeserializationContext ctxt)
        throws IOException, JsonProcessingException {
      return PubPdwhStatusEnum.valueOf(p.getValueAsInt());
    }
  }
}
