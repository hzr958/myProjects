package com.smate.center.task.service.rol.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.smate.center.task.base.AppSettingConstants;
import com.smate.center.task.base.AppSettingContext;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.rol.quartz.ClearTaskNoticeEvent;
import com.smate.center.task.model.rol.quartz.ClearTaskNoticeUserInfo;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.security.SecurityUtils;

@Service("taskNoticeService")
@Transactional(rollbackFor = Exception.class)
public class TaskNoticeServiceImpl implements TaskNoticeService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private CacheService cacheService;
  private static final String PUB_KPI_UNVALID = "ins_id|role_id|unit_id|PUB_KPI_UNVALID";
  private static final String PUB_MERGE_PUB = "ins_id|role_id|unit_id|PUB_MERGE_PUB";
  private static final String PUB_CONFIRM_PUBPSN = "ins_id|role_id|unit_id|PUB_CONFIRM_PUBPSN";
  private static final String PSN_APPROVE_NUM = "ins_id|role_id|unit_id|PSN_APPROVE_NUM";
  private static final String PSN_NO_UNIT = "ins_id|role_id|unit_id|PSN_NO_UNIT";
  private final static String ROL_TASK_NOTICE_CACHE = "rol_task_notice_cache";

  @Override
  public void clearTaskNotice(ClearTaskNoticeEvent event) throws ServiceException {
    this.clearTaskNotice(event, getLoginUserInfo());
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

  public void remove(String key) {

    if (!isEnabled()) {
      return;
    }
    Assert.notNull(key, "key参数不能为空");
    logger.debug("Remove Cache:" + key);
    this.cacheService.remove(ROL_TASK_NOTICE_CACHE, key);
  }

  private boolean isEnabled() {
    return AppSettingContext.getIntValue(AppSettingConstants.ROL_TASK_NOTICE_CACHE) == 1;
  }

}
