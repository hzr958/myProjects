package com.smate.center.batch.service.rol.pub;

import java.io.Serializable;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 单位特殊智能提示service.
 * 
 * @author liqinghua
 * 
 */
@Service("autoCompleteRolService")
@Transactional(rollbackFor = Exception.class)
public class AutoCompleteRolServiceImpl implements AutoCompleteRolService, Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 594921585190110647L;


  /**
   * 获取符合当前语言环境的名称_MJG_ROL-1478.
   * 
   * @param zhName
   * @param enName
   * @param locale
   * @return
   */
  public String getShowName(String zhName, String enName, Locale locale) {
    String name = null;
    if (locale.equals(Locale.US)) {
      if (StringUtils.isNotBlank(enName)) {
        name = enName;
      } else {
        name = zhName;
      }
    } else {
      if (StringUtils.isNotBlank(zhName)) {
        name = zhName;
      } else {
        name = enName;
      }
    }
    return name;
  }


}
