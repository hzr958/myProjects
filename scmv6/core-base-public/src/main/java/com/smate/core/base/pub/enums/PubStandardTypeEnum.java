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

public enum PubStandardTypeEnum implements IBaseEnum<String> {

  INTERNATIONAL("INTERNATIONAL", "国际标准"), NATION_FORCE("NATION_FORCE", "国家标准（强制）"), NATION_RECOMMENDED(
      "NATION_RECOMMENDED",
      "国家标准（推荐）"), INDUSTRIAL_FORCE("INDUSTRIAL_FORCE", "行业标准（强制）"), INDUSTRIAL_RECOMMENDED("INDUSTRIAL_RECOMMENDED",
          "行业标准（推荐）"), LOCAL("LOCAL", "地方标准"), ENTERPRISE("ENTERPRISE", "公司标准"), OTHER("OTHER", "其他标准");

  private String value;

  private String description;

  PubStandardTypeEnum(String value, String description) {
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
   * 将字符串转换为PubStandardTypeEnum，如果给定字符串无法匹配枚举中的任意一个，则返回{@link PubStandardTypeEnum#OTHER},
   * 如果给定字符串为null或空白字符，则返回null
   * 
   * @param val
   * @return
   */
  public static PubStandardTypeEnum parse(String val) {
    if (StringUtils.isBlank(val)) {
      return OTHER;
    }
    EnumSet<PubStandardTypeEnum> enumSet = EnumSet.allOf(PubStandardTypeEnum.class);
    return enumSet.stream().filter(e -> e.getValue().equalsIgnoreCase(val) || e.getDescription().equalsIgnoreCase(val))
        .findFirst().orElse(OTHER);
  }

  /**
   * 专利国家地区枚举类型 持久化属性转换器
   */
  public static class PubStandardTypeEnumConverter extends AbstractBaseEnumConverter<PubStandardTypeEnum, String> {

  }

  /**
   * JSON反序列化转换器
   */
  public static class PubStandardTypeEnumDeserializer extends JsonDeserializer<PubStandardTypeEnum> {
    @Override
    public PubStandardTypeEnum deserialize(JsonParser p, DeserializationContext ctxt)
        throws IOException, JsonProcessingException {
      return PubStandardTypeEnum.parse(p.getValueAsString());
    }
  }
}
