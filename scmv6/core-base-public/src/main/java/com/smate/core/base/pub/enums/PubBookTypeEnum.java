package com.smate.core.base.pub.enums;

import java.io.IOException;
import java.util.EnumSet;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public enum PubBookTypeEnum implements IBaseLocaleEnum<String> {

  MONOGRAPH("MONOGRAPH", "专著", "Monograph"), TEXTBOOK("TEXTBOOK", "教科书", "Textbook"), COMPILE("COMPILE", "编著",
      "Compile"), NULL("NULL", "", "");

  private String value;
  private String zhDescription;
  private String enDescription;

  PubBookTypeEnum(String value, String zhDescription, String enDescription) {
    this.value = value;
    this.zhDescription = zhDescription;
    this.enDescription = enDescription;
  }

  @Override
  public String getValue() {
    return value;
  }

  @Override
  public String getZhDescription() {
    return zhDescription;
  }

  @Override
  public String getEnDescription() {
    return enDescription;
  }

  /**
   * 将字符串转换为PubBookTypeEnum，如果给定数值无法匹配枚举中的任意一个，则返回null, 如果给数值为null或0，则返回null
   * 
   * @param val
   * @return
   */
  public static PubBookTypeEnum parse(String val) {
    if (StringUtils.isEmpty(val)) {
      return NULL;
    }
    EnumSet<PubBookTypeEnum> enumSet = EnumSet.allOf(PubBookTypeEnum.class);
    return enumSet.stream().filter(e -> e.getValue().equalsIgnoreCase(val) || e.getZhDescription().equalsIgnoreCase(val)
        || e.getEnDescription().equalsIgnoreCase(val)).findFirst().orElse(NULL);
  }

  /**
   * PubBookTypeEnum反序列化器
   */
  public static class PubBookTypeEnumDeserializer extends JsonDeserializer<PubBookTypeEnum> {

    @Override
    public PubBookTypeEnum deserialize(JsonParser p, DeserializationContext ctxt)
        throws IOException, JsonProcessingException {
      return PubBookTypeEnum.parse(p.getValueAsString());
    }
  }

}
