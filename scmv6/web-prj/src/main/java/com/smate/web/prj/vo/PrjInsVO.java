package com.smate.web.prj.vo;

import java.util.Objects;

/**
 * 项目依托单位页面提示框模型类
 * 
 * @author houchuanjie
 * @date 2018年3月26日 上午11:25:17
 */
public class PrjInsVO {
  // 编码
  private Long code;
  // 名称
  private String name;

  public PrjInsVO(Long code, String name) {
    this.code = code;
    this.name = name;
  }

  /**
   * @return name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name 要设置的 name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return code
   */
  public Long getCode() {
    return code;
  }

  /**
   * @param code 要设置的 code
   */
  public void setCode(Long code) {
    this.code = code;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof PrjInsVO) {
      return Objects.equals(this.code, ((PrjInsVO) obj).code);
    } else {
      return false;
    }
  }
}
