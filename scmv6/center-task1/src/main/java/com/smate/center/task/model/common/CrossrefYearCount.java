package com.smate.center.task.model.common;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CROSSREF_YEAR_COUNT")
public class CrossrefYearCount {
  @Id
  @Column(name = "YEAR")
  private Long year;
  @Column(name = "COUNT")
  private Long count;
  @Column(name = "STATUS")
  private Integer status;

  public CrossrefYearCount() {}

  public CrossrefYearCount(Long year, Long count, Integer status) {
    this.year = year;
    this.count = count;
    this.status = status;
  }

  public Long getYear() {
    return year;
  }

  public void setYear(Long year) {
    this.year = year;
  }

  public Long getCount() {
    return count;
  }

  public void setCount(Long count) {
    this.count = count;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
