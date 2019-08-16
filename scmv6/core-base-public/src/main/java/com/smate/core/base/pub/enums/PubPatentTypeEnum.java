package com.smate.core.base.pub.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.EnumSet;

/**
 * 成果专利类型枚举
 * 
 * @author YJ
 *
 *         2018年8月8日
 */
public enum PubPatentTypeEnum implements IBaseLocaleEnum<String> {

  INVENTION("INVENTION", 51, "发明专利", "Invention Patent"), PRACTICAL("PRACTICAL", 52, "实用新型",
      "Practical Patent"), DESIGN("DESIGN", 53, "外观设计", "Design Patent"), NULL("NULL", 0, "", "");

  private String value;
  private Integer identify;
  private String zhDescription;
  private String enDescription;

  PubPatentTypeEnum(String value, Integer identify, String zhDescription, String enDescription) {
    this.value = value;
    this.identify = identify;
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

  public Integer getIdentify() {
    return identify;
  }

  /**
   * 将字符串转换为PubPatentTypeEnum，如果给定字符串无法匹配枚举中的任意一个，则返回null, 如果给字符串为null，则返回null
   * 
   * @param val
   * @return
   */
  public static PubPatentTypeEnum parse(String val) {
    if (StringUtils.isEmpty(val)) {
      return NULL;
    }
    EnumSet<PubPatentTypeEnum> enumSet = EnumSet.allOf(PubPatentTypeEnum.class);
    return enumSet.stream().filter(e -> e.getValue().equalsIgnoreCase(val) || e.getZhDescription().equalsIgnoreCase(val)
        || e.getEnDescription().equalsIgnoreCase(val)).findFirst().orElse(NULL);
  }

  /**
   * 将字符串转换为PubPatentTypeEnum，如果给定数值无法匹配枚举中的任意一个，则返回null, 如果给数值为null或0，则返回null
   * 
   * @param identify
   * @return
   */
  public static PubPatentTypeEnum parse(Integer identify) {
    if (identify == null || identify == 0) {
      return NULL;
    }
    EnumSet<PubPatentTypeEnum> enumSet = EnumSet.allOf(PubPatentTypeEnum.class);
    return enumSet.stream().filter(e -> e.getIdentify() == identify).findFirst().orElse(NULL);
  }

  /**
   * JSON反序列化转换器
   */
  public static class PubPatentTypeEnumDeserializer extends JsonDeserializer<PubPatentTypeEnum> {
    @Override
    public PubPatentTypeEnum deserialize(JsonParser p, DeserializationContext ctxt)
        throws IOException, JsonProcessingException {
      return PubPatentTypeEnum.parse(p.getValueAsString());
    }
  }



}
