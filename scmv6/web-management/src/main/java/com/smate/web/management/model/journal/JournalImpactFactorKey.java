package com.smate.web.management.model.journal;

import java.io.Serializable;

import javax.persistence.Embeddable;

/**
 * 期刊因子主键.
 * 
 * @author zb
 * 
 */
@Embeddable
public class JournalImpactFactorKey implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 7647319792468709247L;

  private Long jid;
  private Integer year;

  public Long getJid() {
    return jid;
  }

  public void setJid(Long jid) {
    this.jid = jid;
  }

  public Integer getYear() {
    return year;
  }

  public void setYear(Integer year) {
    this.year = year;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((jid == null) ? 0 : jid.hashCode());
    result = prime * result + ((year == null) ? 0 : year.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    JournalImpactFactorKey other = (JournalImpactFactorKey) obj;
    if (jid == null) {
      if (other.jid != null)
        return false;
    } else if (jid != other.jid)
      return false;
    if (year == null) {
      if (other.year != null)
        return false;
    } else if (year != other.year)
      return false;
    return true;
  }
}
