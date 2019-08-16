package com.smate.core.base.pub.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.smate.core.base.enums.AbstractBaseEnumConverter;
import com.smate.core.base.enums.IBaseEnum;
import com.smate.core.base.utils.string.StringUtils;

import java.io.IOException;
import java.util.EnumSet;

/**
 * 成果转化状态
 * 
 * @author YJ
 *
 *         2018年8月8日
 */
public enum PubPatentTransitionStatusEnum implements IBaseEnum<String> {
  TRANSFER("TRANSFER", "转让"), LICENCE("LICENCE", "许可"), INVESTMENT("INVESTMENT", "作价投资"), NONE("NONE", "无");

  private String value;
  private String description;

  PubPatentTransitionStatusEnum(String value, String description) {
    this.value = value;
    this.description = description;
  }

  @JsonValue
  @Override
  public String getValue() {
    return value;
  }

  @Override
  public String getDescription() {
    return description;
  }

  /**
   * 将字符串转换为PubTransitionStatusEnum，如果给定字符串无法匹配枚举中的任意一个，则返回{@link PubPatentTransitionStatusEnum#NONE},
   * 如果给定字符串为null或空白字符，则返回null
   * 
   * @param val
   * @return
   */
  public static PubPatentTransitionStatusEnum parse(String val) {
    if (StringUtils.isBlank(val)) {
      return NONE;
    }
    EnumSet<PubPatentTransitionStatusEnum> enumSet = EnumSet.allOf(PubPatentTransitionStatusEnum.class);
    return enumSet.stream().filter(e -> e.getValue().equalsIgnoreCase(val) || e.getDescription().equalsIgnoreCase(val))
        .findFirst().orElse(NONE);
  }

  /**
   * 专利转换状态枚举类型 持久化属性转换器
   */
  public static class PubTransformStatusEnumConverter
      extends AbstractBaseEnumConverter<PubPatentTransitionStatusEnum, String> {

  }

  /**
   * JSON反序列化转换器
   */
  public static class PubTransformStatusEnumDeserializer extends JsonDeserializer<PubPatentTransitionStatusEnum> {
    @Override
    public PubPatentTransitionStatusEnum deserialize(JsonParser p, DeserializationContext ctxt)
        throws IOException, JsonProcessingException {
      return PubPatentTransitionStatusEnum.parse(p.getValueAsString());
    }
  }

}
