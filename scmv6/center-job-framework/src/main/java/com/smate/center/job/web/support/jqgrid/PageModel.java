package com.smate.center.job.web.support.jqgrid;

import static com.smate.core.base.utils.string.StringUtils.trimToEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.smate.center.job.framework.util.BeanUtil;
import com.smate.center.job.web.support.jqgrid.Order.OrderJsonDeserializer;
import com.sun.istack.internal.NotNull;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.dozer.Mapper;
import org.dozer.metadata.ClassMappingMetadata;
import org.hibernate.criterion.Criterion;

/**
 * JqGrid分页数据页面展示模型
 *
 * @param <VO> 页面数据对象模型
 * @author houchuanjie
 * @date 2018/06/28 17:18
 */
public class PageModel<VO> {

  /**
   * dozer mapper对象
   */
  @JsonIgnore
  private final Mapper MAPPER;

  /**
   * 搜索标志， true或false
   */
  private boolean search;
  private Long nd;
  /**
   * 请求的记录数
   */
  private Integer rows;
  /**
   * 请求的页码
   */
  private Integer page;
  /**
   * 排序字段名称
   */
  private String sidx;
  /**
   * 排序方式，desc/asc
   */
  @JsonDeserialize(using = OrderJsonDeserializer.class)
  private Order sord;
  /**
   * 过滤条件组（多字段）， _search为true时有效
   */
  private SearchFilter filters;
  /**
   * 搜索字段域（单字段），_search为true时有效
   */
  private String searchField;
  /**
   * 搜索字段域对应值，_search为true时有效
   */
  private String searchString;
  /**
   * 搜索操作类型，_search为true时有效
   */
  private SearchOper searchOper;
  /**
   * 查询得到的结果集
   */
  private List<VO> rowData;
  /**
   * 总页数
   */
  private Long totalPage;
  /**
   * 总记录数
   */
  private Long totalCount;
  /**
   * 查询结果，成功：true，失败：false
   */
  private boolean success;
  /**
   * 查询失败时的错误信息
   */
  private String errMsg;
  /**
   * VO <-> PO 的映射元信息
   */
  @JsonIgnore
  private ClassMappingMetadata classMappingMetadata;

  public PageModel() {
    MAPPER = BeanUtil.getMapper();
  }

  /**
   * 设置类映射关系
   *
   * @param voClass
   * @param poClass
   */
  public void setClassMapping(Class<VO> voClass, Class<?> poClass) {
    classMappingMetadata = MAPPER.getMappingMetadata().getClassMapping(voClass, poClass);
  }

  /**
   * 填充查询结果
   *
   * @param totalCount 总记录数
   * @param rowData 此次查询的结果集
   */
  public void setResultData(@NotNull Long totalCount, @NotNull List<VO> rowData) {
    // 设置此次查询结果总页数
    this.totalPage = Double.valueOf(Math.ceil(totalCount * 1.0 / this.rows)).longValue();
    //设置总记录数
    this.totalCount = totalCount;
    //设置当前结果集的条数
    this.rows = rowData.size();
    //设置当前结果集列表
    this.rowData = rowData;
    //查询成功
    this.success = true;
  }

  /**
   * 获取 Hibernate 查询筛选过滤条件
   *
   * @return
   */
  @JsonIgnore
  public Criterion getHibernateCriterion() {
    Criterion criterion = null;
    if (isSearch()) {
      SearchCriterion searchCriterion = new SearchCriterion(searchField, searchString, searchOper);
      criterion = new RestrictionBuilder(classMappingMetadata, getFilters(), searchCriterion)
          .build();
    }
    return criterion;
  }

  /**
   * 获取Hibernate criterion 排序规则
   *
   * @return org.hibernate.criterion.Order对象实例或者null
   */
  @JsonIgnore
  public org.hibernate.criterion.Order getHibernateOrder() {
    org.hibernate.criterion.Order order = null;
    if (StringUtils.isNotBlank(sidx) && Objects.nonNull(sord)) {
      String fieldName = trimToEmpty(sidx);
      if (Objects.nonNull(classMappingMetadata)) {
        fieldName = classMappingMetadata.getFieldMappingBySource(fieldName)
            .getDestinationName();
      }
      order = sord.getHibernateOrder(fieldName);
    }
    return order;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public boolean isSearch() {
    return search;
  }

  public void setSearch(boolean search) {
    this.search = search;
  }

  public Long getNd() {
    return nd;
  }

  public void setNd(Long nd) {
    this.nd = nd;
  }

  public Integer getRows() {
    return rows;
  }

  public void setRows(Integer rows) {
    this.rows = rows;
  }

  public Integer getPage() {
    return page;
  }


  public void setPage(Integer page) {
    this.page = page;
  }

  public String getSidx() {
    return sidx;
  }

  public void setSidx(String sidx) {
    this.sidx = sidx;
  }

  public Order getSord() {
    return sord;
  }

  public void setSord(Order sord) {
    this.sord = sord;
  }

  public List<VO> getRowData() {
    return rowData;
  }

  public void setRowData(List<VO> rowData) {
    this.rowData = rowData;
  }

  public Long getTotalPage() {
    return totalPage;
  }

  public void setTotalPage(Long totalPage) {
    this.totalPage = totalPage;
  }

  public Long getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(Long totalCount) {
    this.totalCount = totalCount;
  }

  public SearchFilter getFilters() {
    return filters;
  }

  public void setFilters(SearchFilter filters) {
    this.filters = filters;
  }

  public String getSearchField() {
    return searchField;
  }

  public void setSearchField(String searchField) {
    this.searchField = searchField;
  }


  public String getSearchString() {
    return searchString;
  }


  public void setSearchString(String searchString) {
    this.searchString = searchString;
  }


  public SearchOper getSearchOper() {
    return searchOper;
  }

  public void setSearchOper(SearchOper searchOper) {
    this.searchOper = searchOper;
  }

  public String getErrMsg() {
    return errMsg;
  }

  public void setErrMsg(String errMsg) {
    this.errMsg = errMsg;
    //查询失败
    this.success = false;
  }


  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("search", search)
        .append("nd", nd)
        .append("rows", rows)
        .append("page", page)
        .append("sidx", sidx)
        .append("sord", sord)
        .append("filters", filters)
        .append("searchField", searchField)
        .append("searchString", searchString)
        .append("searchOper", searchOper)
        .append("rowData", rowData)
        .append("totalPage", totalPage)
        .append("totalCount", totalCount)
        .append("errMsg", errMsg)
        .toString();
  }
}
