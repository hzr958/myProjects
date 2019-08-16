package com.smate.center.batch.service.rol.pub;

import java.io.Serializable;

import net.sf.ehcache.CacheException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.smate.center.batch.base.AppSettingConstants;
import com.smate.center.batch.base.AppSettingContext;
import com.smate.center.batch.dao.rol.psn.RolPsnInsDao;
import com.smate.center.batch.dao.rol.pub.PubPsnRolDao;
import com.smate.center.batch.dao.rol.pub.PublicationRolDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.ClearTaskNoticeEvent;
import com.smate.center.batch.model.rol.pub.ClearTaskNoticeUserInfo;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.constant.ScmRolRoleConstants;
import com.smate.core.base.utils.security.SecurityUtils;


/**
 * 
 * @author liqinghua
 * 
 */
@Service("taskNoticeService")
@Transactional(rollbackFor = Exception.class)
public class TaskNoticeServiceImpl implements TaskNoticeService, InitializingBean {

  /**
   * 
   */
  private static final long serialVersionUID = -730296168121224241L;
  private static final String PUB_KPI_UNVALID = "ins_id|role_id|unit_id|PUB_KPI_UNVALID";
  private static final String PUB_MERGE_PUB = "ins_id|role_id|unit_id|PUB_MERGE_PUB";
  private static final String PUB_CONFIRM_PUBPSN = "ins_id|role_id|unit_id|PUB_CONFIRM_PUBPSN";
  private static final String PSN_APPROVE_NUM = "ins_id|role_id|unit_id|PSN_APPROVE_NUM";
  private static final String PSN_NO_UNIT = "ins_id|role_id|unit_id|PSN_NO_UNIT";

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private final static String ROL_TASK_NOTICE_CACHE = "rol_task_notice_cache";
  private final static int ROL_TASK_NOTICE_CACHE_TIMEOUT = 60 * 5;

  @Autowired
  private CacheService cacheService;
  @Autowired
  private PublicationRolDao publicationRolDao;
  @Autowired
  private PubPsnRolDao pubPsnRolDao;
  @Autowired
  private RolPsnInsDao rolPsnInsDao;

  @Override
  public Long getPubKpiUnValidNum() throws ServiceException {

    String key = this.getCacheKey(PUB_KPI_UNVALID);
    try {
      // 查询缓存先
      Long num = (Long) this.get(key);
      if (num == null) {
        Long insId = SecurityUtils.getCurrentInsId();
        Long unitId = null;
        Integer roleId = SecurityUtils.getCurrentUserRoleId();
        // 部门管理员
        if (ScmRolRoleConstants.UNIT_RO.equals(roleId) || ScmRolRoleConstants.UNIT_CONTACT.equals(roleId)) {
          unitId = SecurityUtils.getCurrentUnitId();
        }
        num = publicationRolDao.queryUnKpiValid(insId, unitId);
        this.put(key, num);
      }
      return num;
    } catch (Exception e) {
      logger.error("获取成果KPI统计不完善的成果统计数,key=" + key, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Long getNeedMergePubNum() throws ServiceException {

    String key = this.getCacheKey(PUB_MERGE_PUB);
    try {
      // 查询缓存先
      Long num = (Long) this.get(key);
      if (num == null) {
        Long insId = SecurityUtils.getCurrentInsId();
        Long unitId = null;
        Integer roleId = SecurityUtils.getCurrentUserRoleId();
        // 部门管理员
        if (ScmRolRoleConstants.UNIT_RO.equals(roleId) || ScmRolRoleConstants.UNIT_CONTACT.equals(roleId)) {
          unitId = SecurityUtils.getCurrentUnitId();
        }
        num = publicationRolDao.queryNeedMergePub(insId, unitId);
        this.put(key, num);
      }
      return num;
    } catch (Exception e) {
      logger.error("获取需要合并的成果统计数,key=" + key, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Long getNeedConfirmPubPsnNum() throws ServiceException {

    String key = this.getCacheKey(PUB_CONFIRM_PUBPSN);
    try {

      // 查询缓存先
      Long num = (Long) this.get(key);
      if (num == null) {
        Long insId = SecurityUtils.getCurrentInsId();
        Long unitId = null;
        Integer roleId = SecurityUtils.getCurrentUserRoleId();
        // 部门管理员
        if (ScmRolRoleConstants.UNIT_RO.equals(roleId) || ScmRolRoleConstants.UNIT_CONTACT.equals(roleId)) {
          unitId = SecurityUtils.getCurrentUnitId();
        }
        num = pubPsnRolDao.queryNeedConfirmPsnNum(insId, unitId);
        this.put(key, num);
      }
      return num;
    } catch (Exception e) {
      logger.error("获取等待确认成果人员数,key=" + key, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Long getNeedApprovePsnNum() throws ServiceException {
    String key = this.getCacheKey(PSN_APPROVE_NUM);
    try {

      // 查询缓存先
      Long num = (Long) this.get(key);
      if (num == null) {
        Long insId = SecurityUtils.getCurrentInsId();
        Long unitId = null;
        Integer roleId = SecurityUtils.getCurrentUserRoleId();
        // 部门管理员
        if (ScmRolRoleConstants.UNIT_RO.equals(roleId) || ScmRolRoleConstants.UNIT_CONTACT.equals(roleId)) {
          unitId = SecurityUtils.getCurrentUnitId();
        }
        num = rolPsnInsDao.getNeedApprovePsnNum(insId, unitId);
        this.put(key, num);
      }
      return num;
    } catch (Exception e) {
      logger.error("获取等待审核的单位人员数,key=" + key, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Long getNoUnitPsnNum() throws ServiceException {
    String key = this.getCacheKey(PSN_NO_UNIT);
    try {

      // 查询缓存先
      Long num = (Long) this.get(key);
      if (num == null) {
        Long insId = SecurityUtils.getCurrentInsId();
        num = rolPsnInsDao.getNoUnitPsnNum(insId);
        this.put(key, num);
      }
      return num;
    } catch (Exception e) {
      logger.error("获取未分配部门人员数,key=" + key, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void clearTaskNotice(ClearTaskNoticeEvent event) throws ServiceException {

    this.clearTaskNotice(event, getLoginUserInfo());
  }

  @Override
  public void clearTaskNotice(ClearTaskNoticeEvent event, ClearTaskNoticeUserInfo userInfo) throws ServiceException {
    try {
      if (event.getKpiUnVlid() == 1) {
        String key = this.getCacheKey(PUB_KPI_UNVALID, userInfo);
        this.remove(key);
      }
      if (event.getPubMeger() == 1) {
        String key = this.getCacheKey(PUB_MERGE_PUB, userInfo);
        this.remove(key);
      }
      if (event.getPubPsnConfirm() == 1) {
        String key = this.getCacheKey(PUB_CONFIRM_PUBPSN, userInfo);
        this.remove(key);
      }
      if (event.getPsnApprove() == 1) {
        String key = this.getCacheKey(PSN_APPROVE_NUM, userInfo);
        this.remove(key);
      }
      if (event.getPsnNoUnit() == 1) {
        String key = this.getCacheKey(PSN_NO_UNIT, userInfo);
        this.remove(key);
      }
    } catch (Exception e) {
      logger.error("clearTaskNotice error ", e);
    }
  }

  public Object get(String key) {
    if (!isEnabled()) {
      return null;
    }
    Assert.notNull(key, "key参数不能为空");
    try {
      return this.cacheService.get(ROL_TASK_NOTICE_CACHE, key);
    } catch (IllegalStateException e) {
      logger.error("读取PublicationCache,key=" + key, e);
    } catch (CacheException e) {
      logger.error("读取PublicationCache,key=" + key, e);
    }
    return null;
  }

  private String getCacheKey(String pattent) {

    ClearTaskNoticeUserInfo userInfo = getLoginUserInfo();

    return this.getCacheKey(pattent, userInfo);
  }

  /**
   * 获取登录用户信息.
   * 
   * @return
   */
  private ClearTaskNoticeUserInfo getLoginUserInfo() {
    Long insId = SecurityUtils.getCurrentInsId();
    insId = insId == null ? 0 : insId;
    Long unitId = SecurityUtils.getCurrentUnitId();
    unitId = unitId == null ? 0 : unitId;
    Integer roleId = SecurityUtils.getCurrentUserRoleId();
    roleId = roleId == null ? 0 : roleId;
    ClearTaskNoticeUserInfo userInfo = ClearTaskNoticeUserInfo.getInstance(insId, roleId, unitId);
    return userInfo;
  }

  private String getCacheKey(String pattent, ClearTaskNoticeUserInfo userInfo) {

    Long insId = userInfo.getInsId();
    insId = insId == null ? 0 : insId;
    Long unitId = userInfo.getUnitId();
    unitId = unitId == null ? 0 : unitId;
    Integer roleId = userInfo.getRoleId();
    roleId = roleId == null ? 0 : roleId;

    return pattent.replace("ins_id", insId.toString()).replace("role_id", roleId.toString()).replace("unit_id",
        unitId.toString());
  }

  public void put(String key, Object value) {
    if (!isEnabled()) {
      return;
    }
    Assert.notNull(key, "key参数不能为空");
    if (value instanceof Serializable) {
      this.cacheService.put(ROL_TASK_NOTICE_CACHE, ROL_TASK_NOTICE_CACHE_TIMEOUT, key, (Serializable) value);
    }

  }

  public void remove(String key) {

    if (!isEnabled()) {
      return;
    }
    Assert.notNull(key, "key参数不能为空");
    logger.debug("Remove Cache:" + key);
    this.cacheService.remove(ROL_TASK_NOTICE_CACHE, key);
  }

  private boolean isEnabled() {
    // 不需要从数据库取，返回true
    // return AppSettingContext.getIntValue(AppSettingConstants.ROL_TASK_NOTICE_CACHE) == 1;
    return true;
  }

  @Override
  public void afterPropertiesSet() throws Exception {

  }
}
