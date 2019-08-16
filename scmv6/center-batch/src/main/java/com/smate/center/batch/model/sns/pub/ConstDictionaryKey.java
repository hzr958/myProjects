package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Embeddable;

/**
 * 
 * @author liqinghua
 * 
 */
@Embeddable
public class ConstDictionaryKey implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -5145141670752090017L;
  /**
   * 常量类别.
   */
  private String category;
  /**
   * 常量代码.
   */
  private String code;

  public ConstDictionaryKey() {
    super();
  }

  public ConstDictionaryKey(String category, String code) {
    super();
    this.category = category;
    this.code = code;
  }

  /**
   * @return category
   */
  public String getCategory() {
    return category;
  }

  /**
   * @param category
   */
  public void setCategory(String category) {
    this.category = category;
  }

  /**
   * @return code
   */
  public String getCode() {
    return code;
  }

  /**
   * @param code
   */
  public void setCode(String code) {
    this.code = code;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((category == null) ? 0 : category.hashCode());
    result = prime * result + ((code == null) ? 0 : code.hashCode());
    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ConstDictionaryKey other = (ConstDictionaryKey) obj;
    if (category == null) {
      if (other.category != null)
        return false;
    } else if (!category.equals(other.category))
      return false;
    if (code == null) {
      if (other.code != null)
        return false;
    } else if (!code.equals(other.code))
      return false;
    return true;
  }

}
