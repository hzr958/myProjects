package com.smate.center.batch.service.user;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.psn.AttPersonDao;
import com.smate.center.batch.dao.sns.psn.AttSettingsDao;
import com.smate.center.batch.dao.sns.pub.ConstDictionaryCbDao;
import com.smate.center.batch.dao.sns.pub.PrivacySettingsDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.psn.AttSettings;
import com.smate.center.batch.model.psn.AttSettingsId;
import com.smate.center.batch.model.sns.pub.ConstDictionary;
import com.smate.center.batch.model.sns.pub.PrivacySettings;
import com.smate.center.batch.model.sns.pub.PrivacySettingsPK;
import com.smate.core.base.utils.model.security.Person;

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
  @Autowired
  private ConstDictionaryCbDao constDictionaryDao;
  @Autowired
  private AttSettingsDao attSettingsDao;
  @Autowired
  private AttPersonDao attPersonDao;

  @Override
  public PrivacySettings loadPsByPsnId(Long psnId, String privacyAction) throws ServiceException {

    try {
      return privacySettingsDao.loadPsByPsnId(psnId, privacyAction);
    } catch (Exception e) {
      logger.error("读取psnId={}的隐私配置失败！", psnId, e);
      return null;
    }
  }


  @Override
  public void initPrivacySettingsConfig(Long psnId) {
    try {
      boolean hasConfig = privacySettingsDao.hasConfigPs(psnId);
      if (!hasConfig) { // 没有配置的话，插入一份配置信息
        for (String privacyKey : PRAVICY_TYPE) {
          PrivacySettingsPK pk = new PrivacySettingsPK(psnId, privacyKey);
          if ("vMyFile".equalsIgnoreCase(privacyKey)) {
            privacySettingsDao.save(new PrivacySettings(pk, 2)); // 文件隐私类型设置为2，仅本人
          } else if ("vMyLiter".equalsIgnoreCase(privacyKey)) {
            privacySettingsDao.save(new PrivacySettings(pk, 1)); // 文献隐私类型设置为1-好友(与页面限制保持一致)_MaoJianGuo_2012-10-08.
          } else {
            privacySettingsDao.save(new PrivacySettings(pk, 0));

          }

        }

      }

    } catch (Exception e) {
      logger.error("初始化用户隐私配置出现异常！", e);
    }

  }



  @Override
  public void initAttTypeConfig(Long psnId) throws Exception {
    try {
      List<ConstDictionary> attTypeList = getAttConstList();
      if (attTypeList == null || attTypeList.size() == 0) {
        return;
      }
      for (ConstDictionary cd : attTypeList) {

        AttSettings attSettings = new AttSettings();
        AttSettingsId pk = new AttSettingsId(psnId, cd.getKey().getCode());
        attSettings.setAttSettingsId(pk);
        attSettingsDao.save(attSettings);
      }
    } catch (Exception e) {

      logger.error("初始化用户关注类型配置失败！", e);

    }
  }

  @Override
  public List<ConstDictionary> getAttConstList() throws Exception {
    return getConstListByCategroy("att_type", null);
  }


  @Override
  public List<ConstDictionary> getConstListByCategroy(String category, Integer size) throws Exception {
    try {

      return constDictionaryDao.findConstByCategory(category, size);
    } catch (Exception e) {
      logger.error("查询常量表数据失败！", e);
      throw new ServiceException();
    }
  }

  @Override
  public void syncPersonInfo(Person person) throws ServiceException {
    try {
      this.attPersonDao.updatePersonInfo(person);

    } catch (Exception e) {

      logger.error("同步关注人员psnId={}冗余数据失败", person.getPersonId(), e);
      throw new ServiceException(e);

    }

  }
}
