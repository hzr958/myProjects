package com.smate.core.base.pub.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.EnumSet;

public enum PubConferencePaperTypeEnum implements IBaseLocaleEnum<String> {

  INVITED("INVITED", "特邀报告", "Invited Presentation"), GROUP("GROUP", "分组报告", "Grouping Presentation"), ORAL("ORAL",
      "口头报告", "Oral Presentation"), POSTER("POSTER", "墙报展示", "Poster Presentation"), NULL("NULL", "", "");

  private String value;
  private String zhDescription;
  private String enDescription;

  PubConferencePaperTypeEnum(String value, String zhDescription, String enDescription) {
    this.value = value;
    this.zhDescription = zhDescription;
    this.enDescription = enDescription;
  }

  @JsonValue
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
   * 将字符串转换为PubConferencePaperTypeEnum，如果给定数值无法匹配枚举中的任意一个，则返回null, 如果给数值为null或0，则返回null
   * 
   * @param val
   * @return
   */
  public static PubConferencePaperTypeEnum parse(String val) {
    if (StringUtils.isEmpty(val)) {
      return NULL;
    }
    EnumSet<PubConferencePaperTypeEnum> enumSet = EnumSet.allOf(PubConferencePaperTypeEnum.class);
    return enumSet.stream().filter(e -> e.getValue().equalsIgnoreCase(val) || e.getZhDescription().equalsIgnoreCase(val)
        || e.getEnDescription().equalsIgnoreCase(val)).findFirst().orElse(NULL);
  }

  /**
   * PubConferencePaperTypeEnum反序列化器
   */
  public static class PubConferencePaperTypeEnumDeserializer extends JsonDeserializer<PubConferencePaperTypeEnum> {

    @Override
    public PubConferencePaperTypeEnum deserialize(JsonParser p, DeserializationContext ctxt)
        throws IOException, JsonProcessingException {
      return PubConferencePaperTypeEnum.parse(p.getValueAsString());
    }
  }
}
