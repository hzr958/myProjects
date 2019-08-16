package com.smate.center.batch.service.psn;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.dao.local.SysUserLocaleDao;
import com.smate.core.base.utils.model.local.SysUserLocale;

@Service("sysUserLocaleService")
@Transactional(rollbackFor = Exception.class)
public class SysUserLocaleServiceImpl implements SysUserLocaleService {
  /**
   * 
   */
  private static final long serialVersionUID = 5181616427668042982L;

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private SysUserLocaleDao sysUserLocaleDao;


  @Override
  public SysUserLocale getSysUserLocaleByPsnId(Long psnId) throws Exception {
    try {
      return sysUserLocaleDao.get(psnId);
    } catch (Exception e) {
      logger.error("查询用户本地化时出错", e);
      throw new Exception(e);
    }
  }


  @Override
  public String getLocale(Long psnId) throws Exception {
    try {
      String locale = null;
      SysUserLocale sysuserLocale = this.getSysUserLocaleByPsnId(psnId);
      // 修正完善了获取locale值的代码_MJG_2012-11-28_SCM-1341.
      if (sysuserLocale != null) {
        locale = sysuserLocale.getLocale();
      }
      if (StringUtils.isBlank(locale)) {
        locale = LocaleContextHolder.getLocale().toString();
      }
      return locale;
    } catch (Exception e) {
      logger.error("根据人员ID获取其语言环境", e);
      throw new Exception("根据人员ID获取其语言环境", e);
    }
  }

}
