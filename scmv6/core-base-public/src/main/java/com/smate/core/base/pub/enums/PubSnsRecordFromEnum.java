package com.smate.core.base.pub.enums;

import java.io.IOException;
import java.util.EnumSet;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.smate.core.base.enums.IBaseEnum;
import com.smate.core.base.enums.converter.AbstractCustomEnumAttributeConverter;

/**
 * 个人库成果记录来源枚举
 * 
 * @author houchuanjie
 * @date 2018/06/01 16:15
 */
public enum PubSnsRecordFromEnum implements IBaseEnum<Integer> {
  /**
   * 人工输入
   */
  MANUAL_INPUT(0, "手工录入"),
  /**
   * 在线数据库导入
   */
  IMPORT_FROM_DATABASE(1, "在线数据库导入"),
  /**
   * 从文件导入
   */
  IMPORT_FORM_FILE(2, "文件导入"),
  /**
   * 从基准库导入
   */
  IMPORT_FROM_PDWH(3, "基准库导入"),
  /**
   * 从单位库同步回个人库的
   */
  SYNC_FROM_INS(4, "单位库同步回个人库"),
  /**
   * 从同步个人库到单位库的
   */
  SYNC_FROM_SNS(5, "个人库到同步单位库"),
  /**
   * 离线后台导入
   */
  OFFLINE_IMPORT(6, "离线后台导入"),
  /**
   * crossref数据导入
   */
  CROSSREF_IMPORT(7, "crossref数据"),
  /**
   * PDF文件导入至个人库
   */
  IMPORT_FORM_PDF(8, "pdf导入");


  private Integer value;
  private String description;

  PubSnsRecordFromEnum(Integer value, String description) {
    this.value = value;
    this.description = description;
  }

  public static PubSnsRecordFromEnum valueOf(Integer val) {
    EnumSet<PubSnsRecordFromEnum> enumSet = EnumSet.allOf(PubSnsRecordFromEnum.class);
    return enumSet.stream().filter(e -> e.getValue().equals(val)).findFirst().orElse(MANUAL_INPUT);
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
   * {@link PubSnsRecordFromEnum} 持久化属性转换器
   */
  public static class PubSnsRecordFromEnumConverter
      extends AbstractCustomEnumAttributeConverter<PubSnsRecordFromEnum, Integer> {

  }

  /**
   * {@link PubSnsRecordFromEnum} 反序列化器
   */
  public static class PubSnsRecordFromEnumDeserializer extends JsonDeserializer<PubSnsRecordFromEnum> {

    @Override
    public PubSnsRecordFromEnum deserialize(JsonParser p, DeserializationContext ctxt)
        throws IOException, JsonProcessingException {
      return PubSnsRecordFromEnum.valueOf(p.getValueAsInt());
    }
  }
}
