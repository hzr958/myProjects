package com.smate.web.dyn.service.dynamic.group;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.template.SmateFreeMarkerTemplateUtil;
import com.smate.web.dyn.constant.GroupDynConstant;
import com.smate.web.dyn.dao.dynamic.DynamicShareResDao;
import com.smate.web.dyn.dao.dynamic.group.GroupDynamicContentDao;
import com.smate.web.dyn.dao.dynamic.group.GroupDynamicMsgDao;
import com.smate.web.dyn.dao.dynamic.group.GroupDynamicStatisticDao;
import com.smate.web.dyn.exception.DynGroupException;
import com.smate.web.dyn.form.dynamic.group.GroupDynamicForm;
import com.smate.web.dyn.model.dynamic.group.GroupDynamicMsg;
import com.smate.web.dyn.model.dynamic.group.GroupDynamicStatistic;
import com.smate.web.dyn.model.mongodb.dynamic.group.GroupDynamicContent;
import com.smate.web.dyn.service.psn.PersonManager;

/**
 * 动态生成 处理接口
 * 
 * @author tsz
 *
 */
@Transactional(rollbackFor = Exception.class)
public class GroupDynamicRealtimeServiceImpl implements GroupDynamicRealtimeService {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private GroupDynamicMsgDao groupDynamicMsgDao;
  @Autowired
  private GroupDynamicContentDao groupDynamicContentDao;
  @Autowired
  private GroupDynamicStatisticDao groupDynamicStatisticDao;
  @Autowired
  private SmateFreeMarkerTemplateUtil smateFreeMarkerTemplateUtil;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private PersonManager personManager;
  @Autowired
  private DynamicShareResDao dynamicShareResDao;

  private Map<String, BuildResParametService> resParametMap;

  /**
   * map参数 {psnId,groupId,resType,resId,dynContent,tempType}
   * 
   * psnId 创建人id resType 动态资源类型 resId 动态资源id dynContent 动态文本内容 tempType 动态模版
   * 
   * resType ={pub/file/fund}
   * 
   * tempType={PUBLISHDYN(发布新动态)/ADDFILE(添加文件动态)/ADDPUB(添加成果动态)/SHARE(分享动态)/
   * SHAREPUB(分享文件动态)/SHAREFUND(分享基金动态)}
   * 
   */

  @Override
  public void groupDynRealtime(Map<String, Object> paramet) throws DynGroupException {
    checkParamet(paramet);
    GroupDynamicForm form = new GroupDynamicForm();
    // 产生动态id
    Long dynId = groupDynamicMsgDao.createDynId();
    form.setGroupDynId(dynId);
    buildParamet(form, paramet);
    saveGroupDynMsg(form);
    saveGroupDynStatistic(form);
    saveGroupDynContent(form);
  }

  // 校验关键参数方法
  private void checkParamet(Map<String, Object> data) throws DynGroupException {
    if (data.get(GroupDynConstant.MAP_DATA_PSNID) == null) {
      logger.error("生成动态 动态创建人参数 psnId 为空!!!" + data);
      throw new DynGroupException("生成动态 动态创建人参数 psnId 为空!!!" + data);
    }
    Long tempPsnIdLong = personDao.existsPerson(Long.parseLong(data.get(GroupDynConstant.MAP_DATA_PSNID).toString()));
    if (tempPsnIdLong == null) {
      logger.error("生成动态 动态创建人参数 psnId 不正确!!!" + data);
      throw new DynGroupException("生成动态 动态创建人参数 psnId 不正确!!!" + data);
    }
    if (data.get(GroupDynConstant.MAP_DATA_GROUPID) == null) {
      logger.error("生成动态 群组参数 groupId 为空!!!" + data);
      throw new DynGroupException("生成动态 群组参数 groupId 为空!!!" + data);
    }
    if (data.get(GroupDynConstant.MAP_DATA_TEMPTYPE) == null) {
      logger.error("生成动态 动态l模版类型参数 tempType 为空!!!" + data);
      throw new DynGroupException("生成动态 动态l模版类型参数 tempType 为空!!!" + data);
    }
    String tempType = data.get(GroupDynConstant.MAP_DATA_TEMPTYPE).toString();
    if (GroupDynamicTemplateEnum.valueOf(tempType) == null) {
      logger.error("生成动态 动态l模版类型参数 tempType 不正确!!!" + data);
      throw new DynGroupException("生成动态 动态l模版类型参数 tempType 不正确!!!" + data);
    }
  }

  // 构建参数对象方法
  @SuppressWarnings("unchecked")
  private void buildParamet(GroupDynamicForm form, Map<String, Object> data) {
    form.setDynContent(data.get(GroupDynConstant.MAP_DATA_DYNCONTENT) == null ? null
        : data.get(GroupDynConstant.MAP_DATA_DYNCONTENT).toString());
    form.setPsnId(Long.parseLong(data.get(GroupDynConstant.MAP_DATA_PSNID).toString()));
    form.setGroupId(Long.parseLong(data.get(GroupDynConstant.MAP_DATA_GROUPID).toString()));
    form.setTempType(data.get(GroupDynConstant.MAP_DATA_TEMPTYPE).toString());
    form.setResType(data.get(GroupDynConstant.MAP_DATA_RESTYPE) == null ? null
        : data.get(GroupDynConstant.MAP_DATA_RESTYPE).toString());
    form.setResId(data.get(GroupDynConstant.MAP_DATA_RESID) == null ? null
        : Long.parseLong(data.get(GroupDynConstant.MAP_DATA_RESID).toString()));
    form.setTemplate(
        GroupDynamicTemplateEnum.valueOf(data.get(GroupDynConstant.MAP_DATA_TEMPTYPE).toString()).toString());
    form.setPubSimpleMap(data.get(GroupDynConstant.MAP_DATA_PUBSIMPLEMAP) == null ? null
        : JacksonUtils.jsonToMap(data.get(GroupDynConstant.MAP_DATA_PUBSIMPLEMAP).toString()));
    form.setDatabaseType(data.get(GroupDynConstant.MAP_DATA_DATABASETYPE) == null ? null
        : NumberUtils.toInt(data.get(GroupDynConstant.MAP_DATA_DATABASETYPE).toString()));
    form.setDbId(data.get(GroupDynConstant.MAP_DATA_DBID) == null ? null
        : NumberUtils.toInt(data.get(GroupDynConstant.MAP_DATA_DBID).toString()));
    form.setResInfoJson(data.get("resInfoJson") == null ? null : data.get("resInfoJson").toString());

    Object obj = data.get("receiverGrpId");
    if (obj != null) {
      form.setReceiverGrpId(Long.parseLong(obj.toString()));
    }

  }

  // 保存群组动态信息
  private void saveGroupDynMsg(GroupDynamicForm form) {
    GroupDynamicMsg groupDynMsg = new GroupDynamicMsg();
    groupDynMsg.setDynId(form.getGroupDynId());
    groupDynMsg.setDynType(form.getTempType());
    groupDynMsg.setRelDealStatus(0);
    groupDynMsg.setStatus(0);
    groupDynMsg.setDynTmp(form.getTemplate());
    groupDynMsg.setCreateDate(new Date());
    groupDynMsg.setGroupId(form.getReceiverGrpId());
    groupDynMsg.setProducer(form.getPsnId());
    groupDynMsg.setResId(form.getResId());
    groupDynMsg.setResType(form.getResType());
    groupDynMsg.setUpdateDate(new Date());

    groupDynamicMsgDao.save(groupDynMsg);

  }

  // 保存群组动态内容
  private void saveGroupDynContent(GroupDynamicForm form) {
    // 构造模版参数
    Map<String, Object> data = new HashMap<String, Object>();

    buildTemplateMapData(data, form);

    try {
      String contentZh = smateFreeMarkerTemplateUtil.produceTemplate(data, form.getTemplate() + "_zh.ftl");
      String contentEn = smateFreeMarkerTemplateUtil.produceTemplate(data, form.getTemplate() + "_en.ftl");
      GroupDynamicContent groupDynamicContent = new GroupDynamicContent();
      groupDynamicContent.setDynId(form.getGroupDynId().toString());
      groupDynamicContent.setDynContentEn(contentEn);
      groupDynamicContent.setDynContentZh(contentZh);
      groupDynamicContent.setResDetails(JacksonUtils.mapToJsonStr(data));// 把map内容转换成json字符串存入表中
      groupDynamicContentDao.save(groupDynamicContent);
    } catch (Exception e) {
      logger.error("保存动态模版数据出错 " + form.toString(), e);
      throw new DynGroupException("保存动态模板数据出错..", e);
    }
  }

  /**
   * 构建群组 模版数据
   * 
   */
  private void buildTemplateMapData(Map<String, Object> data, GroupDynamicForm form) {
    data.put(GroupDynConstant.TEMPLATE_DATA_DYN_ID, Des3Utils.encodeToDes3(form.getGroupDynId().toString()));
    data.put(GroupDynConstant.TEMPLATE_DATA_NOTENCODE_DYN_ID, form.getGroupDynId().toString());
    data.put(GroupDynConstant.TEMPLATE_DATA_RES_TYPE, form.getResType());
    data.put(GroupDynConstant.TEMPLATE_DATA_DYN_TYPE, form.getTempType());
    Person psn = personDao.findPerson(form.getPsnId());
    data.put(GroupDynConstant.TEMPLATE_DATA_PSN_WORK_INFO, personManager.getPsnViewWorkHisInfo(form.getPsnId()));
    data.put(GroupDynConstant.TEMPLATE_DATA_AUTHOR_AVATAR, psn.getAvatars());
    data.put(GroupDynConstant.TEMPLATE_DATA_DES3_PSN_ID, Des3Utils.encodeToDes3(form.getPsnId().toString()));
    if (StringUtils.isNotBlank(form.getDynContent())) {
      data.put(GroupDynConstant.TEMPLATE_DATA_DYN_CONTENT, form.getDynContent());
    }
    // 部分中英文数据处理
    data.put(GroupDynConstant.TEMPLATE_DATA_EN_AUTHOR_NAME, getPersonName(psn, "en_US"));
    data.put(GroupDynConstant.TEMPLATE_DATA_ZH_AUTHOR_NAME, getPersonName(psn, "zh_CN"));

    data.put("RES_GRP_ID", form.getGroupId());

    if (form.getResId() != null) {
      BuildResParametService buildResParametService = resParametMap.get(form.getResType());
      if (buildResParametService != null) {
        buildResParametService.buildResParamet(data, form);
      } else {
        logger.error("生成动态 动态资源类型 参数 resType 不正确!!!" + form);
        throw new DynGroupException("生成动态 动态资源类型 参数 resType 不正确!!!" + form);
      }
    }
  }

  // 初始化动态统计信息
  private void saveGroupDynStatistic(GroupDynamicForm form) {
    GroupDynamicStatistic groupDynamicStatistic = new GroupDynamicStatistic();
    groupDynamicStatistic.setAwardCount(0);
    groupDynamicStatistic.setCommentCount(0);
    groupDynamicStatistic.setShareCount(0);
    groupDynamicStatistic.setDynId(form.getGroupDynId());

    groupDynamicStatisticDao.save(groupDynamicStatistic);
  }

  public static void main(String[] args) {
    System.out.println(GroupDynamicTemplateEnum.valueOf("123").toString());
  }

  public void setResParametMap(Map<String, BuildResParametService> resParametMap) {
    this.resParametMap = resParametMap;
  }

  /**
   * 优先显示中文名，没有中文名显示 lastname+firstname 还是没有的话取enName
   * 
   * @param person
   * @param language
   * @return
   */
  public String getPersonName(Person person, String language) {
    if (language.equals("zh_CN")) { // 中文
      if (StringUtils.isNotBlank(person.getName()))
        return person.getName();
      else if (StringUtils.isNotBlank(person.getLastName()) || StringUtils.isNotBlank(person.getFirstName()))
        return person.getFirstName() + " " + person.getLastName();
      else
        return person.getEname();
    } else {
      if (StringUtils.isNotBlank(person.getEname()))
        return person.getEname();
      else if (StringUtils.isNotBlank(person.getLastName()) || StringUtils.isNotBlank(person.getFirstName()))
        return person.getFirstName() + " " + person.getLastName();
      else
        return person.getName();
    }
  }

}
