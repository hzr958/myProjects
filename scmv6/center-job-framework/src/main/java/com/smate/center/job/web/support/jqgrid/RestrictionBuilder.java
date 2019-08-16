package com.smate.center.job.web.support.jqgrid;

import com.smate.center.job.framework.util.BeanPropertyParser;
import com.smate.core.base.utils.string.StringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.dozer.metadata.ClassMappingMetadata;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Restrictions;

/**
 * 查询条件构建器，用于将JqGrid查询条件转换为Hibernate对应的查询条件
 *
 * @author Created by hcj
 * @date 2018/06/27 17:55
 */
public class RestrictionBuilder {

  //搜索过滤条件类
  private SearchFilter filter;
  //单字段查询信息
  private SearchCriterion searchCriterion;

  //依赖dozer的类映射元信息（VO <-> PO的映射）
  private ClassMappingMetadata classMappingMetadata;

  public RestrictionBuilder(ClassMappingMetadata classMappingMetadata, SearchFilter filter,
      SearchCriterion searchCriterion) {
    this.searchCriterion = searchCriterion;
    this.filter = filter;
    this.classMappingMetadata = classMappingMetadata;
  }

  /**
   * 构建（联合的）查询条件
   *
   * @return
   */
  public Criterion build() {
    Junction filterJunction = Optional.ofNullable(filter).map(filter -> filter.getGroupOp())
        .map(groupOp -> groupOp.getHibernateJunction(buildCriterions())).orElse(null);
    Criterion singleCriterion = Optional.ofNullable(searchCriterion).map(this::buildCriterion)
        .orElse(null);
    //多条件结果
    Criterion criterion = null;
    if (filterJunction != null && singleCriterion != null) {
      //与操作合并过滤和查询条件
      criterion = Restrictions.and(singleCriterion, filterJunction);
    } else if (filterJunction == null && singleCriterion != null) {
      criterion = singleCriterion;
    } else if (singleCriterion == null && filterJunction != null) {
      criterion = filterJunction;
    }
    return criterion;
  }

  /**
   * 通过SearchCriterion条件列表构建Hibernate查询条件
   *
   * @return
   */
  protected Criterion[] buildCriterions() {
    List<SearchCriterion> rules = filter.getRules();
    Criterion[] criterions = new Criterion[rules.size()];
    List<Criterion> criterionList = new ArrayList<>(rules.size());
    for (SearchCriterion rule : rules) {
      criterionList.add(buildCriterion(rule));
    }
    criterionList.toArray(criterions);
    return criterions;
  }

  /**
   * 构建查询条件
   *
   * @param searchCriterion
   * @return
   */
  protected Criterion buildCriterion(final SearchCriterion searchCriterion) {
    SearchOper searchOper = searchCriterion.getOp();
    String fieldName = searchCriterion.getField();
    String value = searchCriterion.getData();
    if (Objects.isNull(searchOper)) {
      return null;
    }
    if (Objects.isNull(classMappingMetadata)) {
      return searchOper.getHibernateCriterion(fieldName, value);
    }
    //获得目标类对应的属性名称
    fieldName = classMappingMetadata.getFieldMappingBySource(fieldName).getDestinationName();
    BeanPropertyParser beanPropertyParser = new BeanPropertyParser(
        classMappingMetadata.getDestinationClass());
    if (searchOper == SearchOper.IN || searchOper == SearchOper.NI) {
      String[] values = StringUtils.split(value, " \t\n\r\f,;:");
      Object[] propValues = new Object[values.length];
      for (int i = 0; i < values.length; i++) {
        propValues[i] = beanPropertyParser.convertForProperty(values[i], fieldName);
      }
      return searchOper.getHibernateCriterion(fieldName, null, propValues);
    } else {
      Object propValue = beanPropertyParser.convertForProperty(value, fieldName);
      return searchOper.getHibernateCriterion(fieldName, propValue);
    }
  }
}
