package com.smate.center.batch.util.pub;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * 与具体ORM实现无关的分页参数及查询结果封装.
 * 
 * @param <T> Page中记录的类型.
 * 
 */
public class FsysPage<T> implements Serializable {

  // 公共变量
  public static final String ASC = "asc";
  public static final String DESC = "desc";
  public static final int MIN_PAGESIZE = 1;
  public static final int MAX_PAGESIZE = 100;
  private static final long serialVersionUID = 4280689107688216002L;

  // 分页参数
  protected int pageSize = MIN_PAGESIZE;
  protected int pageNo = 1;
  protected String orderBy = null;
  protected String order = null;
  protected boolean autoCount = true;
  protected int lastPage = -1;
  protected int first = 0;
  protected int pageCount = 0;
  protected long totalPages = 0;

  // 返回结果
  protected List<T> result = Collections.emptyList();
  protected long totalCount = -1;

  public FsysPage() {
    super();
  }

  public FsysPage(final int pageSize) {
    setPageSize(pageSize);
  }

  public FsysPage(final int pageSize, final boolean autoCount) {
    setPageSize(pageSize);
    setAutoCount(autoCount);
  }

  // -- 访问查询参数函数 --//
  /**
   * 获得当前页的页号,序号从1开始,默认为1.
   */
  public int getPageNo() {
    return pageNo;
  }

  /**
   * 设置当前页的页号,序号从1开始,低于1时自动调整为1.
   */
  public void setPageNo(final int pageNo) {
    this.pageNo = pageNo;
    if (pageNo < 1) {
      this.pageNo = 1;
    }
  }

  /**
   * 获得每页的记录数量,默认为10.
   */
  public int getPageSize() {
    return pageSize;
  }

  /**
   * 设置每页的记录数量,超出MIN_PAGESIZE与MAX_PAGESIZE范围时会自动调整.
   */
  public void setPageSize(final int pageSize) {
    this.pageSize = pageSize;

    if (pageSize < MIN_PAGESIZE) {
      this.pageSize = MIN_PAGESIZE;
    }
    if (pageSize > MAX_PAGESIZE) {
      this.pageSize = MAX_PAGESIZE;
    }
  }

  /**
   * 根据pageNo和pageSize计算当前页第一条记录在总结果集中的位置,序号从1开始.
   */
  public int getFirst() {
    return ((this.getPageNo() - 1) * pageSize) + 1;
  }

  /**
   * lqh add 根据pageNo和PageSize计算当前最后一条记录在结果集中的位置，序号从0开始.
   * 
   * @return
   */
  public long getEnd() {

    if (totalCount == -1 || totalCount == 0) {
      return 0;
    } else {
      int end = getFirst() + pageSize;
      if (end > totalCount)
        return totalCount;
      return end;
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
    this.orderBy = orderBy;
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
    // 检查order字符串的合法值
    String[] orders = StringUtils.split(StringUtils.lowerCase(order), ',');
    for (String orderStr : orders) {
      if (!StringUtils.equals(DESC, orderStr) && !StringUtils.equals(ASC, orderStr))
        throw new IllegalArgumentException("排序方向" + orderStr + "不是合法值");
    }

    this.order = StringUtils.lowerCase(order);
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
  public long getTotalCount() {
    return totalCount;
  }

  /**
   * 设置总记录数.
   */
  public void setTotalCount(final long totalCount) {
    this.totalCount = totalCount;
  }

  /**
   * 根据pageSize与totalCount计算总页数,默认值为-1.
   */
  public long getTotalPages() {
    if (totalCount < 0)
      return -1;

    long count = totalCount / pageSize;
    if (totalCount % pageSize > 0) {
      count++;
    }
    return count;
  }

  public long getLastPage() {
    return getTotalPages();
  }

  /**
   * 是否还有下一页.
   */
  public boolean isHasNext() {
    return (this.getPageNo() + 1 <= getTotalPages());
  }

  /**
   * 取得下页的页号, 序号从1开始. 当前页为尾页时仍返回尾页序号.
   */
  public int getNextPage() {
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
  public int getPrePage() {
    if (isHasPre())
      return this.getPageNo() - 1;
    else
      return this.getPageNo();
  }

  public int getPageCount() {
    return pageCount;
  }

  public void setPageCount(int pageCount) {
    this.pageCount = pageCount;
  }

  public void setTotalPages(long totalPages) {
    this.totalPages = getTotalPages();
  }
}
