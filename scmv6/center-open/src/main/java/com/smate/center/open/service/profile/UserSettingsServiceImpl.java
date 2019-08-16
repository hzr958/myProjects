package com.smate.center.open.service.profile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.privacy.dao.PrivacySettingsDao;
import com.smate.core.base.privacy.model.PrivacySettings;
import com.smate.core.base.privacy.model.PrivacySettingsPK;

@Transactional(rollbackFor = Exception.class)
@Service("userSettingsService")
public class UserSettingsServiceImpl implements UserSettingsService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  // 隐私类别映射数组(sendMsg-发消息；reqAddFrd-加为好友；vFrdList-好友列表；vAttGroup-群组列表；vSendEva-评价动态；vAddFrd-添加好友动态；vPubDyn-成果动态；vMyLiter-文献动态；vUProfile-更新资料动态；vMyFile-文件动态；vMyPrj-项目动态；vMyMood-心情动态；vJoinGroupDyn-群组动态)
  private static final String[] PRAVICY_TYPE = new String[] {"sendMsg", "reqAddFrd", "vFrdList", "vAttGroup", "vPubAct",
      "vSendEva", "vAddFrd", "vUProfile", "vGroupInfo", "vPubDyn", "vMyLiter", "vMyFile", "vMyShare", "vAttendAct",
      "vMyPrj", "vMyMood", "vJoinGroupDyn"};
  @Autowired
  private PrivacySettingsDao privacySettingsDao;

  @Override
  public void initPrivacySettingsConfig(Long psnId) throws Exception {
    try {
      boolean hasConfig = privacySettingsDao.hasConfigPs(psnId);
      if (!hasConfig) { // 没有配置的话，插入一份配置信息
        for (String privacyKey : PRAVICY_TYPE) {
          PrivacySettingsPK pk = new PrivacySettingsPK(psnId, privacyKey);
          if ("vMyFile".equalsIgnoreCase(privacyKey)) {
            privacySettingsDao.save(new PrivacySettings(pk, 2)); // 文件隐私类型设置为2，仅本人
          } else if ("vMyLiter".equalsIgnoreCase(privacyKey)) {
            // 20181119修改,页面显示是假如不为2(仅本人),就是4(关注我的人)
            privacySettingsDao.save(new PrivacySettings(pk, 4)); // 文献隐私类型设置为1-好友(与页面限制保持一致)_MaoJianGuo_2012-10-08.
          } else {
            privacySettingsDao.save(new PrivacySettings(pk, 0));
          }
        }
      }
    } catch (Exception e) {
      logger.error("初始化用户隐私配置出现异常！", e);
      throw new Exception("初始化用户隐私配置出现异常！", e);
    }
  }
}
