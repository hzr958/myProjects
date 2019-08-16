package com.smate.web.psn.service.setting;

import com.smate.core.base.consts.dao.InstitutionDao;
import com.smate.core.base.consts.model.Institution;
import com.smate.core.base.privacy.dao.PrivacySettingsDao;
import com.smate.core.base.privacy.model.PrivacySettings;
import com.smate.core.base.privacy.model.PrivacySettingsPK;
import com.smate.core.base.utils.constant.ConstDictionary;
import com.smate.core.base.utils.dao.consts.ConstDictionaryDao;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.psn.constants.DynType;
import com.smate.web.psn.dao.attention.AttPersonDao;
import com.smate.web.psn.dao.attention.AttSettingsDao;
import com.smate.web.psn.dao.dyn.DynRecvMessageDao;
import com.smate.web.psn.dao.friend.FriendDao;
import com.smate.web.psn.dao.privacy.PrivacyAttConfigDao;
import com.smate.web.psn.dao.psn.RecentSelectedDao;
import com.smate.web.psn.dao.recommend.FriendSysRecommendDao;
import com.smate.web.psn.exception.DaoException;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.form.AttPersonInfo;
import com.smate.web.psn.form.PsnSettingForm;
import com.smate.web.psn.model.attention.AttPerson;
import com.smate.web.psn.model.dynamic.DynamicHandlerService;
import com.smate.web.psn.model.friend.Friend;
import com.smate.web.psn.model.psninfo.RecentSelected;
import com.smate.web.psn.model.setting.UserSettings;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;

@Transactional(rollbackFor = Exception.class)
@Service("userSettingsService")
public class UserSettingsServiceImpl implements UserSettingsService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  // 隐私类别映射数组(sendMsg-发消息；reqAddFrd-加为好友；vFrdList-好友列表；vAttGroup-群组列表；vSendEva-评价动态；vAddFrd-添加好友动态；vPubDyn-成果动态；vMyLiter-文献动态；vUProfile-更新资料动态；vMyFile-文件动态；vMyPrj-项目动态；vMyMood-心情动态；vJoinGroupDyn-群组动态)
  private static final String[] PRAVICY_TYPE = new String[] {"sendMsg", "reqAddFrd", "vFrdList", "vAttGroup", "vPubAct",
      "vSendEva", "vAddFrd", "vUProfile", "vGroupInfo", "vPubDyn", "vMyLiter", "vMyFile", "vMyShare", "vAttendAct",
      "vMyPrj", "vMyMood", "vJoinGroupDyn"};
  @Autowired
  private ConstDictionaryDao constDictionaryDao;
  @Autowired
  private PrivacySettingsDao privacySettingsDao;
  @Autowired
  private PrivacyAttConfigDao privacyAttConfigDao;
  @Autowired
  private FriendSysRecommendDao friendSysRecommendDao;
  @Autowired
  private DynRecvMessageDao dynRecvMessageDao;
  @Autowired
  private FriendDao friendDao;
  @Autowired
  private AttPersonDao attPersonDao;

  @Autowired
  private AttSettingsDao attSettingsDao;

  @Autowired
  private PersonDao personDao;
  @Autowired
  private DynamicHandlerService dynamicHandlerService;

  @Autowired
  private RecentSelectedDao recentSelectedDao;
  @Autowired
  private InstitutionDao institutionDao;

  @Override
  public UserSettings getPrivacyConfig() throws ServiceException {

    try {
      UserSettings userSettings = new UserSettings();
      userSettings.setPravicyList(getPrivacyConstList());
      userSettings.setInitPrivateJson(loadPrivacySettings());

      return userSettings;

    } catch (Exception e) {

      logger.error("获取隐私页面配置信息失败！", e);
      throw new ServiceException();
    }
  }

  @Override
  public List<ConstDictionary> getPrivacyConstList() throws ServiceException {

    return getConstListByCategroy("privacy_permissions");
  }

  @Override
  public List<ConstDictionary> getConstListByCategroy(String category) throws ServiceException {
    try {

      return constDictionaryDao.findConstByCategory(category);
    } catch (Exception e) {
      logger.error("查询常量表数据失败！", e);
      throw new ServiceException();
    }
  }

  @Override
  public String loadPrivacySettings() throws ServiceException {

    try {
      boolean hasConfig = privacySettingsDao.hasConfigPs();
      String[] excludes = new String[] {"psnId"};
      if (hasConfig) { // 如果已经配置了，读取配置信息

        return JacksonUtils.configJsonObjectSerializer(privacySettingsDao.loadPs(), excludes, null);

      } else { // 没有配置的话，插入一份配置信息
        for (String privacyKey : PRAVICY_TYPE) {
          PrivacySettingsPK pk = new PrivacySettingsPK(SecurityUtils.getCurrentUserId(), privacyKey);
          if ("vMyFile".equalsIgnoreCase(privacyKey)) {
            privacySettingsDao.save(new PrivacySettings(pk, 2)); // 文件隐私类型设置为2，仅本人.
          } else if ("vMyLiter".equalsIgnoreCase(privacyKey)) {
            privacySettingsDao.save(new PrivacySettings(pk, 1)); // 文献隐私类型设置为1-好友(与页面限制保持一致)_MaoJianGuo_2012-10-08.
          } else {
            privacySettingsDao.save(new PrivacySettings(pk, 0));

          }
        }
        // 返回默认配置
        return JacksonUtils.configJsonObjectSerializer(privacySettingsDao.loadPs(), excludes, null);
      }

    } catch (Exception e) {

      logger.error("读取用户隐私配置出现异常！", e);
      throw new ServiceException();
    }

  }

  @Override
  public void savePrivacyConfig(String privacyConfig) throws ServiceException {

    try {
      JSONArray config = JSONArray.fromObject(privacyConfig);
      JSONArray privacyAtts = new JSONArray();
      boolean hasConfig = privacySettingsDao.hasConfigPs();
      if (!hasConfig) {
        logger.error("数据库中没有找到配置隐私配置数据！");
        throw new ServiceException();
      }
      List<String> attPrivacyKeys = privacyAttConfigDao.getPrivacyKeys();
      for (int i = 0; i < config.size(); i++) {
        JSONObject jsonObj = JSONObject.fromObject(config.get(i));
        String actionObj = jsonObj.getString("pk").toString().split("=")[1];
        // String actionObj = Object obj =
        // jsonObj.getJSONObject("pk").getString("privacyAction");
        int permission = jsonObj.getInt("permission");
        PrivacySettings ps = privacySettingsDao.get(new PrivacySettingsPK(SecurityUtils.getCurrentUserId(), actionObj));
        ps.setPermission(permission);
        privacySettingsDao.save(ps);
        if (attPrivacyKeys.contains(actionObj) && !"vMyMood".equalsIgnoreCase(actionObj)) {
          JSONObject tempJson = new JSONObject();
          tempJson.put("privacyKey", actionObj);
          tempJson.put("permission", permission);
          privacyAtts.add(tempJson);

        }
        // actionObj=reqAddFrd同步更新person_know
        if ("reqAddFrd".equalsIgnoreCase(actionObj)) {
          friendSysRecommendDao.syncPrivacySettingsToPersonKnow(SecurityUtils.getCurrentUserId(), permission);
        }
      }

      try {
        // 更新隐私设置和动态相关的记录
        if (privacyAtts.size() > 0) {
          snsPrivacyPermissionSyncDyn(SecurityUtils.getCurrentUserId(), privacyAtts,
              SecurityUtils.getCurrentAllNodeId().get(0));
        }
      } catch (Exception e) {
      }
      // 设置隐私时，更新好友群组
      try {
        // 更新这张表， 这张表好久没用了，这个逻辑注释到 GROUP_INVITE_PSNFRIEND
        // this.groupPrivacyChange();
      } catch (Exception e) {

        logger.error("设置隐私时，更新好友群组异常！");
      }

    } catch (Exception e) {

      logger.error("保存用户隐私配置失败！", e);
      throw new ServiceException();

    }

  }

  // 更新隐私设置和动态相关的记录
  void snsPrivacyPermissionSyncDyn(Long senderId, JSONArray privacys, Integer nodeId) {
    for (Object privacy : privacys) {
      JSONObject privacyJson = JSONObject.fromObject(privacy);
      try {
        String[] arr = new String[] {DynType.DYN_PRJ, DynType.DYN_PUB, DynType.DYN_REFRENCE};
        Assert.notNull(privacyJson, "privacyJson不允许为空！");
        String attType = this.privacyAttConfigDao.findAttTypeBy(privacyJson.getString("privacyKey"));
        if (attType == null || attType.equals(DynType.DYN_MOOD)) {
          logger.debug("attType={}隐私动态不需要同步动态！", attType);
          return;
        }
        if (Arrays.asList(arr).contains(attType)) {
          this.dynRecvMessageDao.updatePubDynByAttType(senderId, attType, privacyJson.getInt("permission"));
        } else {
          this.dynRecvMessageDao.updateDynByAttType(senderId, attType, privacyJson.getInt("permission"));
        }
      } catch (Exception e) {
        logger.error("同步隐私设置修改需要更新动态permission的操作失败！{}", privacyJson, e);
      }
    }
  }

  /**
   * 设置隐私时，更新好友群组 更新这张表， 这张表好久没用了，这个逻辑注释到 GROUP_INVITE_PSNFRIEND
   * 
   * @throws ServiceException
   */
  private void groupPrivacyChange() throws ServiceException {
    try {
      Long sendPsnId = SecurityUtils.getCurrentUserId();
      // 好友列表
      List<Friend> friendList = friendDao.getFriendsByPsnId(sendPsnId);
      for (Friend friend : friendList) {
        Integer sendNodeId = SecurityUtils.getCurrentAllNodeId().get(0);
        Integer receiveNodeId = SecurityUtils.getCurrentAllNodeId().get(0);
        // 通知好友群组信息已经变更
        // syncGroupInvitePsnChangeToSns第一个参数填写接收者的psnId，第二个参数填写发送者的psnId

        /*
         * GroupInvitePsnFriend groupInvitePsnFriend =
         * groupInvitePsnFriendDao.findGroupInvitePsnFriendList(friend.getFriendPsnId(), sendPsnId); if
         * (groupInvitePsnFriend != null) { groupInvitePsnFriendDao.delete(groupInvitePsnFriend); }
         */
      }
    } catch (Exception e) {

      logger.error("设置隐私时，更新好友群组失败！", e);
      throw new ServiceException();

    }

  }

  @Override
  public UserSettings getAttConfig() throws ServiceException {
    try {
      UserSettings userSettings = new UserSettings();
      userSettings.setAttList(getAttConstList());
      userSettings.setInitAttTypeJson(loadAttTypeConfig());
      userSettings.setDynEmail(getSubcribeDynEmailStatus() ? 1 : 0);
      return userSettings;
    } catch (Exception e) {

      logger.error("获取动态页面配置信息失败！", e);
      throw new ServiceException();

    }
  }

  public boolean getSubcribeDynEmailStatus() throws ServiceException {

    Integer dynEmailStatus = personDao.get(SecurityUtils.getCurrentUserId()).getDynEmail();
    if (dynEmailStatus == null || dynEmailStatus.equals(0)) {

      return false;
    }

    return true;
  }

  public String loadAttTypeConfig() throws ServiceException {
    try {
      String[] excludes = new String[] {"remark", "psnId"};

      return JacksonUtils.configJsonObjectSerializer(attSettingsDao.loadAttSettingsByPsnId(), excludes, null);
      /*
       * return JSONArray.fromObject(attSettingsDao.loadAttSettingsByPsnId(),
       * JsonUtils.configJson(excludes)) .toString();
       */
    } catch (Exception e) {

      logger.error("加载用户关注类型配置表失败！");
      throw new ServiceException();
    }

  }

  @Override
  public List<ConstDictionary> getAttConstList() throws ServiceException {
    return getConstListByCategroy("att_type", null);
  }

  @Override
  public List<ConstDictionary> getConstListByCategroy(String category, Integer size) throws ServiceException {
    try {

      return constDictionaryDao.findConstByCategory(category, size);
    } catch (Exception e) {
      logger.error("查询常量表数据失败！", e);
      throw new ServiceException();
    }
  }

  @Override
  public Long getAllAttPsnCount(Long psnId) throws ServiceException {
    try {
      return attPersonDao.getAllAttPsnCount(psnId);
    } catch (DaoException e) {
      throw new ServiceException(String.format("获取关注人员总数出错,获取人psnId=%s", psnId), e);

    }

  }

  @Override
  public List<AttPersonInfo> loadAttPersonList(UserSettings userSettings) throws ServiceException {
    try {
      List<AttPerson> list = attPersonDao.loadAttPersonList(userSettings);

      return buildAttPsnInfoList(list);

    } catch (Exception e) {

      logger.error("加载关注人员信息失败！", e);
      throw new ServiceException();

    }

  }

  private List<AttPersonInfo> buildAttPsnInfoList(List<AttPerson> list) {
    List<AttPersonInfo> psnInfoList = new ArrayList<AttPersonInfo>();
    AttPersonInfo psnInfo = null;
    Locale locale = LocaleContextHolder.getLocale();

    if (list != null && list.size() > 0) {
      for (AttPerson attPerson : list) {
        psnInfo = new AttPersonInfo();
        psnInfo.setRefDes3PsnId(Des3Utils.encodeToDes3(attPerson.getRefPsnId().toString()));
        psnInfo.setRefHeadUrl(attPerson.getRefHeadUrl());
        psnInfo.setAttPersonId(attPerson.getId());
        Person person = personDao.findPersonBaseIncludeIns(attPerson.getRefPsnId());
        psnInfo.setRefTitolo(person.getInsName());
        if (Locale.CHINA.equals(locale)) {
          if (StringUtils.isNotBlank(attPerson.getRefPsnName())) {
            psnInfo.setRefName(attPerson.getRefPsnName());
          } else {
            psnInfo.setRefName(attPerson.getRefFirstName() + " " + attPerson.getRefLastName());
          }
        } else {
          if (StringUtils.isNotBlank(attPerson.getRefFirstName())
              && StringUtils.isNotBlank(attPerson.getRefLastName())) {
            psnInfo.setRefName(attPerson.getRefFirstName() + " " + attPerson.getRefLastName());
          } else {
            psnInfo.setRefName(attPerson.getRefPsnName());
          }
          if (person.getInsId() != null) {
            Institution institution = institutionDao.get(person.getInsId());
            if (institution != null && StringUtils.isNotBlank(institution.getEnName())) {
              psnInfo.setRefTitolo(institution.getEnName());
            }
          }
        }
        psnInfoList.add(psnInfo);
      }
    }

    return psnInfoList;
  }

  @Override
  public void cancelAttPerson(UserSettings userSettings) throws ServiceException {

    try {
      Long id = userSettings.getCancelId();
      AttPerson attperson = attPersonDao.get(id);
      Long psnId = SecurityUtils.getCurrentUserId();
      if (id == null || id == 0L) {
        logger.error("关注人员参数错误！");
        throw new ServiceException();
      }
      if (psnId == null || psnId == 0L || psnId.longValue() != attperson.getPsnId().longValue()) {
        logger.error("你没有权限取消关注人！psnId=" + psnId + "被关注人id=" + attperson.getRefPsnId());
        throw new ServiceException();
      }
      attPersonDao.delete(id);
      // 处理动态.
      dynamicHandlerService.cancleAttentionVisible(attperson.getRefPsnId());

    } catch (Exception e) {

      logger.error("取消关注人操作失败！", e);
      throw new ServiceException(e);

    }

  }

  // 取消关注后刷新分页信息
  public UserSettings refreshUserSettings(UserSettings userSettings) throws ServiceException {
    try {
      List<AttPerson> attList = attPersonDao.getCurAttPersonList(userSettings);
      if (attList == null || attList.size() == 0) {
        if (userSettings.getBegin() > 0) {
          userSettings.setBegin(userSettings.getBegin() - userSettings.getFetchSize());
        }
      }
      return userSettings;
    } catch (Exception e) {
      logger.error("查询关注人员列表数据失败！", e);
      throw new ServiceException();
    }
  }

  /**
   * 保存关注人员列表
   * 
   * @return -1保存失败;0该该人已经被关注;>0保存成功，不需更新。
   */
  @Override
  public long saveAttFrdList(List<Long> psnIds) throws ServiceException {
    try {
      long result = 0;
      if (psnIds == null || psnIds.size() < 1) {
        return result;
      }
      Long currentPsnId = SecurityUtils.getCurrentUserId();
      for (int i = 0; i < psnIds.size(); i++) {
        Long attId = this.saveAttPrerson(currentPsnId, psnIds.get(i));
        if (attId != null && attId != 0L) {
          result = attId;
        }
        // 处理动态.
        dynamicHandlerService.addAttentionVisible(psnIds.get(i));
      }
      return result;
    } catch (Exception e) {
      logger.error("保存关注配置失败！", e);
      throw new ServiceException();
    }
  }

  /**
   * 保存关注人员配置.
   * 
   * @param psnId
   * @param refPsnId
   * @throws ServiceException
   * @return -1保存失败;>0保存成功，不需更新。
   */
  public long saveAttPrerson(Long psnId, Long refPsnId) throws ServiceException {
    try {

      AttPerson attPerson = this.attPersonDao.find(psnId, refPsnId);
      if (attPerson == null) {
        RecentSelected recentSelected = recentSelectedDao.getRecentSelected(psnId, refPsnId);
        if (recentSelected == null) {
          recentSelected = new RecentSelected();
          recentSelected.setPsnId(psnId);
          recentSelected.setSelectedPsnId(refPsnId);
        }
        recentSelected.setSelectedDate(new Date());
        recentSelectedDao.save(recentSelected);
        Person person = personDao.get(refPsnId);
        attPerson = new AttPerson(psnId, refPsnId);
        attPerson.setRefPsnName(person.getName());
        attPerson.setRefFirstName(person.getFirstName());
        attPerson.setRefLastName(person.getLastName());
        attPerson.setRefInsName(person.getInsName());
        attPerson.setRefHeadUrl(person.getAvatars());
        attPerson.setRefTitle(person.getTitolo());
        attPersonDao.save(attPerson);
      }
      return attPerson.getId();
      // return 0;
    } catch (Exception e) {
      logger.error("人员关注关系保存失败！", e);
      return -1;
    }

  }

  @Override
  public void getAttPersonId(PsnSettingForm form) throws Exception {
    Long userId = SecurityUtils.getCurrentUserId();
    if (StringUtils.isNotBlank(form.getDes3RefPsnId()) && userId != null) {
      AttPerson find = attPersonDao.find(userId, Long.parseLong(Des3Utils.decodeFromDes3(form.getDes3RefPsnId())));
      if (find != null) {
        form.setAttPersonId(find.getId());
      }

    }

  }

}
