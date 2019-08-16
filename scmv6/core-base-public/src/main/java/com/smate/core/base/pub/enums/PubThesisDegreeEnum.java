package com.smate.core.base.pub.enums;

import java.io.IOException;
import java.util.EnumSet;
import org.springframework.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * 学位成果学位类别枚举
 * 
 * @author YJ
 *
 *         2018年8月8日
 */
public enum PubThesisDegreeEnum implements IBaseLocaleEnum<String> {

  MASTER("MASTER", "硕士", "Master"), DOCTOR("DOCTOR", "博士", "Doctor"), OTHER("OTHER", "其他", "Other");

  private String value;
  private String zhDescription;
  private String enDescription;

  PubThesisDegreeEnum(String value, String zhDescription, String enDescription) {
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
   * 将数值转换为PubThesisDegreeEnum，如果给定数值无法匹配枚举中的任意一个，则返回{ {@link PubThesisDegreeEnum#OTHER}},
   * 如果给数值为null或0，则返回null
   * 
   * @param val
   * @return
   */
  public static PubThesisDegreeEnum parse(String val) {
    if (StringUtils.isEmpty(val)) {
      return null;
    }
    EnumSet<PubThesisDegreeEnum> enumSet = EnumSet.allOf(PubThesisDegreeEnum.class);
    return enumSet.stream()
        .filter(e -> e.getValue().equalsIgnoreCase(val) || e.getZhDescription().equalsIgnoreCase(val)).findFirst()
        .orElse(OTHER);
  }

  /**
   * JSON反序列化转换器
   */
  public static class PubThesisDegreeEnumDeserializer extends JsonDeserializer<PubThesisDegreeEnum> {
    @Override
    public PubThesisDegreeEnum deserialize(JsonParser p, DeserializationContext ctxt)
        throws IOException, JsonProcessingException {
      return PubThesisDegreeEnum.parse(p.getValueAsString());
    }
  }


}
