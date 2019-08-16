package com.smate.core.base.utils.service.consts;

import java.util.List;

import com.smate.core.base.utils.constant.SieDiscipline;

/**
 * 
 * @author yxs
 * @descript 学科接口
 */
public interface SieDisciplineService {

  /**
   * 
   *
   * @descript获取学科列表
   */
  public List<SieDiscipline> getDisciplinetNames(String disCode);

  /**
   * 
   *
   * @descript 获取第二级下的所有code
   */
  public List<String> getSecondCodeList(String superCode);

}
