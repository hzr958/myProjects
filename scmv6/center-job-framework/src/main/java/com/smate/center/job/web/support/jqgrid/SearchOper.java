package com.smate.center.job.web.support.jqgrid;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.smate.core.base.enums.IBaseEnum;
import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

/**
 * 搜索条件的操作类型枚举
 *
 * @author houchuanjie
 * @date 2018/05/15 16:46
 */
public enum SearchOper implements IBaseEnum<String> {
  /**
   * equal
   */
  EQ("eq", "equal") {
    @Override
    public Criterion getHibernateCriterion(String searchFieldName,
        Object value, Object... moreValues) {
      return Restrictions.eq(searchFieldName, value);
    }
  },
  /**
   * not equal
   */
  NE("ne", "not equal") {
    @Override
    public Criterion getHibernateCriterion(String searchFieldName,
        Object value, Object... moreValues) {
      return Restrictions.ne(searchFieldName, value);
    }
  },
  /**
   * less than
   */
  LT("lt", "less than") {
    @Override
    public Criterion getHibernateCriterion(String searchFieldName,
        Object value, Object... moreValues) {
      return Restrictions.lt(searchFieldName, value);
    }
  },
  /**
   * less than or equal
   */
  LE("le", "less than or equal") {
    @Override
    public Criterion getHibernateCriterion(String searchFieldName,
        Object value, Object... moreValues) {
      return Restrictions.le(searchFieldName, value);
    }
  },
  /**
   * great than
   */
  GT("gt", "greater than") {
    @Override
    public Criterion getHibernateCriterion(String searchFieldName,
        Object value, Object... moreValues) {
      return Restrictions.gt(searchFieldName, value);
    }
  },
  /**
   * great than or equal
   */
  GE("ge", "greater than or equal") {
    @Override
    public Criterion getHibernateCriterion(String searchFieldName,
        Object value, Object... moreValues) {
      return Restrictions.ge(searchFieldName, value);
    }
  },
  /**
   * begins with
   */
  BW("bw", "begins with") {
    @Override
    public Criterion getHibernateCriterion(String searchFieldName,
        Object value, Object... moreValues) {
      return Restrictions.like(searchFieldName, String.valueOf(value), MatchMode.START);
    }
  },

  /**
   * does not begin with
   */
  BN("bn", "does not begin with") {
    @Override
    public Criterion getHibernateCriterion(String searchFieldName,
        Object value, Object... moreValues) {
      return Restrictions
          .not(Restrictions.like(searchFieldName, String.valueOf(value), MatchMode.START));
    }
  },
  /**
   * is in
   */
  IN("in", "is in") {
    @Override
    public Criterion getHibernateCriterion(String searchFieldName,
        Object value, Object... moreValues) {
      HashSet<Object> objects = new HashSet<>(16);
      if (value != null) {
        objects.add(value);
      }
      if (moreValues != null) {
        objects.addAll(Arrays.asList(moreValues));
      }
      Object[] values = objects.toArray();
      return Restrictions.in(searchFieldName, values);
    }
  },
  /**
   * is not in
   */
  NI("ni", "is not in") {
    @Override
    public Criterion getHibernateCriterion(String searchFieldName,
        Object value, Object... moreValues) {
      HashSet<Object> objects = new HashSet<>(16);
      if (value != null) {
        objects.add(value);
      }
      if (moreValues != null) {
        objects.addAll(Arrays.asList(moreValues));
      }
      Object[] values = objects.toArray();
      return Restrictions.not(Restrictions.in(searchFieldName, values));
    }
  },
  /**
   * ends with
   */
  EW("ew", "ends with") {
    @Override
    public Criterion getHibernateCriterion(String searchFieldName,
        Object value, Object... moreValues) {
      return Restrictions.like(searchFieldName, String.valueOf(value), MatchMode.END);
    }
  },
  /**
   * does not end with
   */
  EN("en", "does not end with") {
    @Override
    public Criterion getHibernateCriterion(String searchFieldName,
        Object value, Object... moreValues) {
      return Restrictions
          .not(Restrictions.like(searchFieldName, String.valueOf(value), MatchMode.START));
    }
  },
  /**
   * contains
   */
  CN("cn", "contains") {
    @Override
    public Criterion getHibernateCriterion(String searchFieldName,
        Object value, Object... moreValues) {
      return Restrictions.like(searchFieldName, String.valueOf(value), MatchMode.ANYWHERE);
    }
  },
  /**
   * does not contain
   */
  NC("nc", "does not contain") {
    @Override
    public Criterion getHibernateCriterion(String searchFieldName,
        Object value, Object... moreValues) {
      return Restrictions
          .not(Restrictions.like(searchFieldName, String.valueOf(value), MatchMode.ANYWHERE));
    }
  },
  NU("nu", "is null") {
    @Override
    public Criterion getHibernateCriterion(String searchFieldName,
        Object value, Object... moreValues) {
      return Restrictions.isNull(searchFieldName);
    }
  },
  NN("nn", "is not null") {
    @Override
    public Criterion getHibernateCriterion(String searchFieldName,
        Object value, Object... moreValues) {
      return Restrictions.isNotNull(searchFieldName);
    }
  };

  private String value;
  private String description;

  SearchOper(String value, String desc) {
    this.value = value;
    this.description = desc;
  }

  /**
   * 将字符串转换为SearchOper类型
   *
   * @param val
   * @return val对应的SearchOper枚举对象，找不到对应关系则返回null
   */
  public static SearchOper parse(String val) {
    EnumSet<SearchOper> searchOpers = EnumSet.allOf(SearchOper.class);
    return searchOpers.stream().filter(op -> op.getValue().equalsIgnoreCase(val)).findFirst()
        .orElse(null);
  }

  @JsonValue
  @Override
  public String getValue() {
    return this.value;
  }

  @Override
  public String getDescription() {
    return description;
  }

  public abstract Criterion getHibernateCriterion(String searchFieldName,
      Object value, Object... moreValues);

  @Override
  public String toString() {
    return this.value.toString();
  }

  /**
   * SearchOper类型Json反序列化器
   */
  public static class SearchOperDeserializer extends JsonDeserializer<SearchOper> {

    @Override
    public SearchOper deserialize(JsonParser p, DeserializationContext ctxt)
        throws IOException, JsonProcessingException {
      return SearchOper.parse(p.getValueAsString());
    }
  }
}
