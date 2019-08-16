package com.smate.center.batch.service.pub.dyn;

import net.sf.json.JSONObject;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.PrivacySettings;
import com.smate.center.batch.service.pub.DynamicProduceAbstract;
import com.smate.center.batch.service.user.UserSettingsService;
import com.smate.center.batch.util.pub.DynamicConstant;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 添加成果动态.
 * 
 * @author cxr
 * 
 */
@Service("dynamicProduceAddPubService")
@Transactional(rollbackFor = Exception.class)
public class DynamicProduceAddPubServiceImpl extends DynamicProduceAbstract {


  /**
   * 
   */
  private static final long serialVersionUID = -7651222263468207984L;
  @Autowired
  private UserSettingsService userSettingsService;

  @Override
  public void produceDynamic(String jsonParam) {
    JSONObject jsonObject = JSONObject.fromObject(jsonParam);
    try {
      Long currentPsnId =
          NumberUtils.createLong(jsonObject.get("producer") == null ? null : jsonObject.get("producer").toString());
      if (currentPsnId == null) {
        currentPsnId = SecurityUtils.getCurrentUserId();
        jsonObject.accumulate("producer", currentPsnId);
      }
      int resType = jsonObject.getInt("resType");

      if (resType == DynamicConstant.RES_TYPE_REF) {// 文献
        PrivacySettings ps = userSettingsService.loadPsByPsnId(currentPsnId, DynamicConstant.DYN_SETTING_ADDREF);
        jsonObject.remove("permission");
        jsonObject.accumulate("permission", ps == null ? 0 : ps.getPermission());
      }
      jsonObject.accumulate("syncFlag", 2);
      jsonObject.accumulate("objectOwner", currentPsnId);
      jsonObject.accumulate("dynType", DynamicConstant.DYN_TYPE_ADD + resType);
      jsonObject.accumulate("tmpId", DynamicConstant.DYNMSG_TEMPLATE_ADDRES);
      produceDynamic(jsonObject, dynamicBuildJsonService.addPubRefDynamic(currentPsnId, jsonObject), true);
    } catch (ServiceException e) {
      logger.error("个人-添加成果动态出错", e);
    }
  }


}
