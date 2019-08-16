package com.smate.center.open.publication.xml.validate;



import java.io.Serializable;

/**
 * @author ajb校验的字段类
 */
public class ErrorField implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -2959214657145867652L;
  /**
   * 字段名.
   */
  private String name;
  /**
   * 错误编号.(0:为空)
   */
  private int errorNo;

  public ErrorField(String name, int errorNo) {
    this.name = name;
    this.errorNo = errorNo;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the errorNo
   */
  public int getErrorNo() {
    return errorNo;
  }

  /**
   * @param errorNo the errorNo to set
   */
  public void setErrorNo(int errorNo) {
    this.errorNo = errorNo;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
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
    ErrorField other = (ErrorField) obj;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    return true;
  }

}
