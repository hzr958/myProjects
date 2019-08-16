package com.smate.center.oauth.service.security;

import com.smate.center.oauth.exception.OauthException;
import com.smate.center.oauth.model.profile.PsnSieRoleForm;
import com.smate.core.base.utils.dao.security.role.Sie6InsRoleDao;
import com.smate.core.base.utils.dao.security.role.SieInsRoleDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userRoleService")
@Transactional(rollbackFor = Exception.class)
public class UserRoleServiceImpl implements UserRoleService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private Sie6InsRoleDao sie6InsRoleDao;
  @Autowired
  private SieInsRoleDao sieInsRoleDao;

  @Override
  public boolean HasMultiRoleInSie(PsnSieRoleForm form) throws OauthException {
    boolean result = false;
    try {
      // 只要insId不为空 就去判断角色
      result = sie6InsRoleDao.hasMultiRole(form.getInsId(), form.getPsnId());
      /*
       * Cookie sysCookie = Struts2Utils.getCookie(Struts2Utils.getRequest(), "SYS"); if (sysCookie !=
       * null && "SIE6".equals(sysCookie.getValue())) { result =
       * sie6InsRoleDao.hasMultiRole(form.getInsId(), form.getPsnId()); } else { result =
       * sieInsRoleDao.hasMultiRole(form.getInsId(), form.getPsnId()); }
       */
    } catch (Exception e) {
      logger.error("查询人员在SIE是否有多个角色出错，psnId = " + form.getPsnId() + ", insId = " + form.getInsId(), e);
      throw new OauthException(e);
    }
    return result;
  }
}
