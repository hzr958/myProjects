package com.smate.core.base.utils.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 与具体ORM实现无关的分页参数及查询结果封装.
 * 
 * @param <T> Page中记录的类型.
 * 
 */
public class Page<T> implements Serializable {

  // 公共变量
  public static final String ASC = "asc";
  public static final String DESC = "desc";
  public static final int MIN_PAGESIZE = 10;
  public static final int MAX_PAGESIZE = Integer.MAX_VALUE;

  private static final long serialVersionUID = -4254508930017843989L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  // 分页参数
  protected Integer pageNo = 1; // 主要是这个 要获取的第几页
  protected Integer pageSize = MIN_PAGESIZE;
  protected String orderBy = "";
  protected String order = "";
  protected boolean autoCount = true;
  protected Integer lastPage = -1;
  protected Integer first = 0;
  protected Integer pageCount = 0; // 页面有几条数据的数量
  protected Long totalPages = 0L; // 总页数
  protected Long lastId = 0L; // 最后加载的记录id

  // 未结果计算，从页面传过来的值.
  protected Integer paramPageNo = 1; // 要获取的第几页
  // 返回结果
  protected List<T> result = Collections.emptyList();

  protected Map<Long, Long> yearMap; // pubYear
  protected Map<Long, Long> pubTypeMap;
  protected Map<String, Long> languageMap; // ZH_CN EN

  protected Long totalCount = -1L;

  // 忽略最小分页
  private boolean ignoreMin = false;

  // 最大分页列表数
  protected Integer listNum = 5;

  // 分页下拉框数值
  @SuppressWarnings("unchecked")
  protected Map selectList = null;

  public Page() {
    super();
  }

  public Page(final Integer pageSize) {
    setPageSize(pageSize);
  }

  public Page(final Integer pageSize, final boolean autoCount) {
    setPageSize(pageSize);
    setAutoCount(autoCount);
  }

  // -- 访问查询参数函数 --//
  /**
   * 获得当前页的页号,序号从1开始,默认为1.
   */
  public Integer getPageNo() {
    this.setTotalPages(getTotalPages());
    if (pageNo.intValue() > this.totalPages) {
      if (this.totalPages <= 0) {
        pageNo = 1;
      } else {
        pageNo = Integer.valueOf(this.totalPages.toString());
      }
    }
    return pageNo;
  }

  public Integer getParamPageNo() {
    return this.pageNo;
  }

  /**
   * 设置当前页的页号,序号从1开始,低于1时自动调整为1.
   */
  public void setPageNo(final Integer pageNo) {
    if (pageNo == null || pageNo < 1) {
      this.pageNo = 1;
    } else {
      this.pageNo = pageNo;
    }
  }

  /**
   * 获得每页的记录数量,默认为10.
   */
  public Integer getPageSize() {
    return pageSize;
  }

  /**
   * 设置每页的记录数量,超出MIN_PAGESIZE与MAX_PAGESIZE范围时会自动调整.
   */
  public void setPageSize(final Integer pageSize) {
    if (pageSize == null) {
      this.pageSize = MIN_PAGESIZE;
    } else {
      this.pageSize = pageSize;
    }
    if (this.pageSize < MIN_PAGESIZE && !ignoreMin) {// 增加忽略最小分页
      this.pageSize = MIN_PAGESIZE;
    }
    if (this.pageSize > MAX_PAGESIZE) {
      this.pageSize = MAX_PAGESIZE;
    }
  }

  /**
   * 根据pageNo和pageSize计算当前页第一条记录在总结果集中的位置,序号从1开始.
   */
  public Integer getFirst() {
    return ((this.getPageNo() - 1) * pageSize) + 1;
  }

  /**
   * lqh add 根据pageNo和PageSize计算当前最后一条记录在结果集中的位置，序号从0开始.
   * 
   * @return
   */
  public Long getEnd() {

    if (totalCount == -1 || totalCount == 0) {
      return 0L;
    } else {
      Integer end = getFirst() + pageSize;
      if (end > totalCount)
        return totalCount;
      return Long.valueOf(end.toString());
    }
  }

  /**
   * 获得排序字段,无默认值.多个排序字段时用','分隔,仅在Criterion查询时有效.
   */
  public String getOrderBy() {
    return orderBy;
  }

  /**
   * 设置排序字段,多个排序字段时用','分隔.
   */
  public void setOrderBy(final String orderBy) {
    if (orderBy == null) {
      this.orderBy = "";
    } else {
      this.orderBy = orderBy;
    }
  }

  /**
   * 是否已设置排序字段,无默认值.
   */
  public boolean isOrderBySetted() {
    return (StringUtils.isNotBlank(orderBy) && StringUtils.isNotBlank(order));
  }

  /**
   * 获得排序方向.
   */
  public String getOrder() {
    return order;
  }

  /**
   * 设置排序方式向.
   * 
   * @param order 可选值为desc或asc,多个排序字段时用','分隔.
   */
  public void setOrder(final String order) {
    if (order == null) {
      // 检查order字符串的合法值
      String[] orders = StringUtils.split(StringUtils.lowerCase(order), ',');
      for (String orderStr : orders) {
        if (!StringUtils.equals(DESC, orderStr) && !StringUtils.equals(ASC, orderStr))
          throw new IllegalArgumentException("排序方向" + orderStr + "不是合法值");
      }

      this.order = StringUtils.lowerCase(order);
    } else {
      this.order = order;
    }
  }

  /**
   * 查询对象时是否自动另外执行count查询获取总记录数, 默认为false.
   */
  public boolean isAutoCount() {
    return autoCount;
  }

  /**
   * 查询对象时是否自动另外执行count查询获取总记录数.
   */
  public void setAutoCount(final boolean autoCount) {
    this.autoCount = autoCount;
  }

  // -- 访问查询结果函数 --//

  /**
   * 取得页内的记录列表.
   */
  public List<T> getResult() {
    return result;
  }

  /**
   * 设置页内的记录列表.
   */
  public void setResult(final List<T> result) {
    this.result = result;
    if (result != null && result.size() > 0) {
      this.pageCount = result.size();
    }
  }

  /**
   * 取得总记录数,默认值为-1.
   */
  public Long getTotalCount() {
    return totalCount;
  }

  /**
   * 设置总记录数.
   */
  public void setTotalCount(final Long totalCount) {
    this.totalCount = totalCount;
  }

  /**
   * 设置总记录数.
   */
  public void setTotalCount(final Integer totalCount) {
    if (totalCount != null) {
      this.totalCount = Long.valueOf(totalCount.toString());
    } else {
      this.totalCount = null;
    }
  }

  /**
   * 根据pageSize与totalCount计算总页数,默认值为-1.
   */
  public Long getTotalPages() {
    if (totalCount == null || totalCount < 0)
      return -1L;

    Long count = totalCount / pageSize;
    if (totalCount % pageSize > 0) {
      count++;
    }
    return count;
  }

  public Long getLastPage() {
    return getTotalPages();
  }

  /**
   * 是否还有下一页.
   */
  public boolean isHasNext() {
    return (getTotalPages() > this.getPageNo());
  }

  /**
   * 取得下页的页号, 序号从1开始. 当前页为尾页时仍返回尾页序号.
   */
  public Integer getNextPage() {
    int pageNo = this.getPageNo();
    if (isHasNext())
      return pageNo + 1;
    else
      return pageNo;
  }

  /**
   * 是否还有上一页.
   */
  public boolean isHasPre() {
    return (pageNo - 1 >= 1);
  }

  /**
   * 取得上页的页号, 序号从1开始. 当前页为首页时返回首页序号.
   */
  public Integer getPrePage() {
    if (isHasPre())
      return this.getPageNo() - 1;
    else
      return this.getPageNo();
  }

  public Integer getPageCount() {
    return pageCount;
  }

  public void setPageCount(Integer pageCount) {
    this.pageCount = pageCount;
  }

  public void setTotalPages(Long totalPages) {
    if (getTotalPages() != -1L) {
      totalPages = getTotalPages();
    }
    this.totalPages = totalPages;
  }

  public boolean isIgnoreMin() {
    return ignoreMin;
  }

  public void setIgnoreMin(boolean ignoreMin) {
    this.ignoreMin = ignoreMin;
  }

  public Integer getListNum() {
    return listNum;
  }

  public void setListNum(Integer listNum) {
    if (!(listNum instanceof Integer) || listNum < 0 || listNum % 2 != 0) {
      return;
    }
    this.listNum = listNum;
  }

  public Map getSelectList() {
    return selectList;
  }

  public void setSelectList(Map selectList) {
    this.selectList = selectList;
  }

  public String getPageString() {
    return "" + pageNo + pageSize + orderBy + order + autoCount + lastPage + first + pageCount + totalPages
        + paramPageNo + totalCount + ignoreMin + listNum + (selectList == null ? "" : selectList.toString());
  }

  public Long getLastId() {
    return lastId;
  }

  public void setLastId(Long lastId) {
    if (lastId == null) {
      this.lastId = 0L;
    }
    this.lastId = lastId;
  }

  public Map<Long, Long> getYearMap() {
    return yearMap;
  }

  public void setYearMap(Map<Long, Long> yearMap) {
    this.yearMap = yearMap;
  }

  public Map<Long, Long> getPubTypeMap() {
    return pubTypeMap;
  }

  public void setPubTypeMap(Map<Long, Long> pubTypeMap) {
    this.pubTypeMap = pubTypeMap;
  }

  public Map<String, Long> getLanguageMap() {
    return languageMap;
  }

  public void setLanguageMap(Map<String, Long> languageMap) {
    this.languageMap = languageMap;
  }

}
