package com.smate.center.job.web.support.jqgrid;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.smate.core.base.enums.IBaseEnum;
import java.io.IOException;
import java.util.EnumSet;
import org.springframework.core.convert.converter.Converter;

/**
 * 排序规则
 *
 * @author Created by hcj
 * @date 2018/06/27 20:54
 */
public enum Order implements IBaseEnum<String> {
  ASC("asc", "升序") {
    @Override
    public org.hibernate.criterion.Order getHibernateOrder(String fieldName) {
      return org.hibernate.criterion.Order.asc(fieldName);
    }
  },
  DESC("desc", "降序") {
    @Override
    public org.hibernate.criterion.Order getHibernateOrder(String fieldName) {
      return org.hibernate.criterion.Order.desc(fieldName);
    }
  };

  private String value;
  private String description;

  Order(String value, String description) {
    this.value = value;
    this.description = description;
  }

  /**
   * 排序规则字符串转换为枚举
   *
   * @param order
   * @return
   */
  public static Order parse(String order) {
    EnumSet<Order> orders = EnumSet.allOf(Order.class);
    return orders.stream().filter(o -> o.getValue().equalsIgnoreCase(order)).findFirst()
        .orElse(null);
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

  public abstract org.hibernate.criterion.Order getHibernateOrder(String fieldName);

  /**
   * Order枚举类型Json反序列化器
   */
  public static class OrderJsonDeserializer extends JsonDeserializer<Order> {

    @Override
    public Order deserialize(JsonParser p, DeserializationContext ctxt)
        throws IOException, JsonProcessingException {
      return Order.parse(p.getValueAsString());
    }
  }

  /**
   * Order枚举类型对应的 SpringMVC模型属性转换器
   */
  public static class String2OrderSpringConverter implements Converter<String, Order> {

    @Override
    public Order convert(String source) {
      return Order.parse(source);
    }
  }
}
