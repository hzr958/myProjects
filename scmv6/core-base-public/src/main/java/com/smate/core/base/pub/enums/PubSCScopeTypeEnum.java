package com.smate.core.base.pub.enums;

import java.io.IOException;
import java.util.EnumSet;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.smate.core.base.enums.AbstractBaseEnumConverter;
import com.smate.core.base.enums.IBaseEnum;
import com.smate.core.base.utils.string.StringUtils;

/**
 * 软件著作权权利范围类型枚举
 * 
 * @author YJ
 *
 *         2019年5月20日
 */
public enum PubSCScopeTypeEnum implements IBaseEnum<String> {

  ALL("ALL", "全部权利"), PART("PART", "部分权利"), NULL("NULL", "");

  private String value;

  private String description;

  PubSCScopeTypeEnum(String value, String description) {
    this.value = value;
    this.description = description;
  }

  @Override
  public String getValue() {
    return value;
  }

  @Override
  public String getDescription() {
    return description;
  }

  /**
   * 将字符串转换为PubSCScopeTypeEnum，如果给定字符串无法匹配枚举中的任意一个，则返回{@link PubSCScopeTypeEnum#NULL},
   * 如果给定字符串为null或空白字符，则返回null
   * 
   * @param val
   * @return
   */
  public static PubSCScopeTypeEnum parse(String val) {
    if (StringUtils.isBlank(val)) {
      return NULL;
    }
    EnumSet<PubSCScopeTypeEnum> enumSet = EnumSet.allOf(PubSCScopeTypeEnum.class);
    return enumSet.stream().filter(e -> e.getValue().equalsIgnoreCase(val) || e.getDescription().equalsIgnoreCase(val))
        .findFirst().orElse(NULL);
  }

  /**
   * 专利国家地区枚举类型 持久化属性转换器
   */
  public static class PubSCScopeTypeEnumConverter extends AbstractBaseEnumConverter<PubSCScopeTypeEnum, String> {

  }

  /**
   * JSON反序列化转换器
   */
  public static class PubSCScopeTypeEnumDeserializer extends JsonDeserializer<PubSCScopeTypeEnum> {
    @Override
    public PubSCScopeTypeEnum deserialize(JsonParser p, DeserializationContext ctxt)
        throws IOException, JsonProcessingException {
      return PubSCScopeTypeEnum.parse(p.getValueAsString());
    }
  }
}
