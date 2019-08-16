package com.smate.core.base.pub.enums;

import java.io.IOException;
import java.util.EnumSet;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.smate.core.base.enums.AbstractBaseEnumConverter;
import com.smate.core.base.enums.IBaseEnum;
import com.smate.core.base.utils.string.StringUtils;

/**
 * 专利国家或地区枚举
 * 
 * @author houchuanjie
 * @date 2018/06/01 14:10
 */
public enum PubPatentAreaEnum implements IBaseEnum<String> {
  CHINA("CHINA", "中国专利"), USA("USA", "美国专利"), EUROPE("EUROPE", "欧洲专利"), JAPAN("JAPAN", "日本专利"), WIPO("WIPO",
      "WIPO专利"), OTHER("OTHER", "其他国家专利");

  private String value;

  private String description;

  PubPatentAreaEnum(String value, String description) {
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
   * 将字符串转换为PubPatentAreaEnum，如果给定字符串无法匹配枚举中的任意一个，则返回{@link PubPatentAreaEnum#OTHER},
   * 如果给定字符串为null或空白字符，则返回null
   * 
   * @param val
   * @return
   */
  public static PubPatentAreaEnum parse(String val) {
    if (StringUtils.isBlank(val)) {
      return OTHER;
    }
    EnumSet<PubPatentAreaEnum> enumSet = EnumSet.allOf(PubPatentAreaEnum.class);
    return enumSet.stream().filter(e -> e.getValue().equalsIgnoreCase(val) || e.getDescription().equalsIgnoreCase(val))
        .findFirst().orElse(OTHER);
  }

  /**
   * 专利国家地区枚举类型 持久化属性转换器
   */
  public static class PubPatentAreaEnumConverter extends AbstractBaseEnumConverter<PubPatentAreaEnum, String> {

  }

  /**
   * JSON反序列化转换器
   */
  public static class PubPatentAreaEnumDeserializer extends JsonDeserializer<PubPatentAreaEnum> {
    @Override
    public PubPatentAreaEnum deserialize(JsonParser p, DeserializationContext ctxt)
        throws IOException, JsonProcessingException {
      return PubPatentAreaEnum.parse(p.getValueAsString());
    }
  }
}
