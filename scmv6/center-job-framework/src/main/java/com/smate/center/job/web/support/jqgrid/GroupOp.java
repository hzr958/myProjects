package com.smate.center.job.web.support.jqgrid;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.smate.core.base.enums.IBaseEnum;
import java.io.IOException;
import java.util.EnumSet;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Restrictions;

/**
 * 搜索过滤条件组的操作类型枚举
 *
 * @author Created by hcj
 * @date 2018/06/27 16:57
 */
public enum GroupOp implements IBaseEnum<String> {
  AND("AND", "符合所有条件"){
    @Override
    public Junction getHibernateJunction(Criterion... conditions) {
      return Restrictions.conjunction(conditions);
    }
  },
  OR("OR", "满足任一条件") {
    @Override
    public Junction getHibernateJunction(Criterion... conditions) {
      return Restrictions.disjunction(conditions);
    }
  };

  private String value;
  private String description;

  GroupOp(String value, String description) {
    this.value = value;
    this.description = description;
  }

  /**
   * 转换为{@link GroupOp}类型
   *
   * @param op 操作符
   * @return 匹配{@link GroupOp}类型的实例，匹配不到返回null
   */
  public static GroupOp parse(String op) {
    EnumSet<GroupOp> groupOps = EnumSet.allOf(GroupOp.class);
    return groupOps.stream().filter(groupOp -> groupOp.getValue().equalsIgnoreCase(op)).findFirst()
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

  public abstract Junction getHibernateJunction(Criterion... conditions);

  /**
   * GroupOp类型的反序列化枚举转换器
   */
  public static final class GroupOpDeserializer extends JsonDeserializer<GroupOp> {

    @Override
    public GroupOp deserialize(JsonParser p, DeserializationContext ctxt)
        throws IOException, JsonProcessingException {
      return GroupOp.parse(p.getValueAsString());
    }
  }

}
