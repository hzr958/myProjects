package com.smate.center.open.service.interconnection.psn;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.smate.center.open.cache.OpenCacheService;
import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.dao.interconnection.UnionRefreshPsnLogDao;
import com.smate.center.open.model.interconnection.PsnInfo;
import com.smate.center.open.model.interconnection.UnionRefreshPsnLog;
import com.smate.center.open.service.interconnection.ScmUnionDataHandleService;
import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.dao.WorkHistoryDao;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.psn.model.WorkHistory;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.string.ServiceUtil;

public class GePsnInfoServiceImpl implements ScmUnionDataHandleService {
  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Value("${union.refresh.time}")
  public String unionRefreshTime;
  @Autowired
  private PersonDao personDao;
  @Autowired
  WorkHistoryDao workHistoryDao;
  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;
  @Autowired
  private UnionRefreshPsnLogDao unionRefreshPsnLogDao;
  @Autowired
  private OpenCacheService openCacheService;
  @Value("${domainscm}")
  private String domain;

  /**
   * 检查： 参数
   * 
   * @param dataParamet
   * @return
   */
  private String checkParam(Map<String, Object> dataParamet) {

    return null;
  }

  @Override
  public String handleUnionData(Map<String, Object> dataParamet) {
    Object data = dataParamet.get(OpenConsts.MAP_DATA);
    Map<String, Object> serviceData = JacksonUtils.jsonToMap(data.toString());
    String temp = checkParam(serviceData);
    if (temp != null) {
      return buildResultXml(temp, "4");
    }
    Long openId = NumberUtils.toLong(dataParamet.get("openid").toString());
    Long psnId = NumberUtils.toLong(dataParamet.get("psnId").toString());
    String token = dataParamet.get("token").toString();

    Person person = personDao.findUnionPerson(psnId);
    PsnInfo psnInfo = new PsnInfo();
    buildPsnInfo(person, psnInfo);
    String xmlData = "";
    try {
      xmlData = buildUnionPubListXmlStr(psnInfo);
    } catch (Exception e) {
      // 吃掉异常，当做open系统的正确请求来处理
      logger.error(e.getMessage() + JacksonUtils.mapToJsonStr(dataParamet), e);
      return buildResultXml("构建 成果列表xml数据异常！", "5");
    }

    return xmlData;
  }

  /**
   * 构建 perInfo
   * 
   * @param person
   * @param perInfo
   */
  private void buildPsnInfo(Person person, PsnInfo perInfo) {
    WorkHistory workHistory = workHistoryDao.findNewestWorkHistoryInsname(person.getPersonId());
    perInfo.setPsnId(person.getPersonId());
    perInfo.setDes3PsnId(ServiceUtil.encodeToDes3(person.getPersonId().toString()));
    perInfo.setPsnName(person.getName()); // 人员姓名
    perInfo.setAvatorUrl(person.getAvatars());// 头像地址
    perInfo.setFirstName(person.getFirstName());//
    perInfo.setLastName(person.getLastName());//
    if (person.getSex() == null) {
      perInfo.setSex("");// // 性别，1男，0女
    } else {
      perInfo.setSex(person.getSex() == 1 ? "男" : "女");// // 性别，1男，0女
    }
    perInfo.setInsId(person.getInsId());// 机构
    if (workHistory != null) {
      perInfo.setInsName(workHistory.getInsName());//
      perInfo.setDepartment_zh(workHistory.getDepartment());
      perInfo.setPosition_zh(workHistory.getPosition());// 职称
    }

  }

  /**
   * 构建xml数据
   * 
   * @param psnInfo
   * @return
   * @throws Exception
   */
  public String buildUnionPubListXmlStr(PsnInfo psnInfo) throws Exception {
    try {
      Document doc = DocumentHelper.parseText("<?xml version=\"1.0\" encoding=\"UTF-8\"?><psnInfo></psnInfo>");

      Element rootNode = (Element) doc.selectSingleNode("/psnInfo");
      rootNode.addElement("getPsnStatus").addText("0");
      rootNode.addElement("psnId").addText(psnInfo.getPsnId().toString());
      rootNode.addElement("des3PsnId").addText(psnInfo.getDes3PsnId());
      rootNode.addElement("firstName").addText(psnInfo.getFirstName() != null ? psnInfo.getFirstName() : "");
      rootNode.addElement("name").addText(psnInfo.getPsnName() != null ? psnInfo.getPsnName() : "");
      rootNode.addElement("lastName").addText(psnInfo.getLastName() != null ? psnInfo.getLastName() : "");
      rootNode.addElement("sex").addText(psnInfo.getSex() != null ? psnInfo.getSex() : "男");
      rootNode.addElement("insId").addText(psnInfo.getInsId() != null ? psnInfo.getInsId().toString() : "");
      rootNode.addElement("insName").addText(psnInfo.getInsName() != null ? psnInfo.getInsName() : "");
      rootNode.addElement("position").addText(psnInfo.getPosition_zh() != null ? psnInfo.getPosition_zh() : "");
      rootNode.addElement("outsidePsnUrl").addText(buildPsnOutsideUrl(psnInfo.getPsnId()));
      rootNode.addElement("atatorUrl").addText(psnInfo.getAvatorUrl() != null ? psnInfo.getAvatorUrl() : "");
      rootNode.addElement("department").addText(psnInfo.getDepartment_zh() != null ? psnInfo.getDepartment_zh() : "");
      return doc.getRootElement().asXML();
    } catch (Exception e) {
      throw new Exception(e);
    }
  }

  public String buildPsnOutsideUrl(Long psnId) {
    String url;
    PsnProfileUrl psnProfileUrl = psnProfileUrlDao.find(psnId);
    if (psnProfileUrl != null && StringUtils.isNotBlank(psnProfileUrl.getUrl())) {
      url = domain + "/profile/" + psnProfileUrl.getUrl();
    } else {
      url = domain + "/scmwebsns/in/view?des3PsnId=" + ServiceUtil.encodeToDes3(psnId + "");
    }
    return url;
  }

  public String buildResultXml(String result, String getPsnStatus) {
    Document doc;
    try {
      doc = DocumentHelper.parseText("<?xml version=\"1.0\" encoding=\"UTF-8\"?><psnInfo></psnInfo>");
      Element rootNode = (Element) doc.selectSingleNode("/psnInfo");
      rootNode.addElement("result").addText(result);
      rootNode.addElement("getPsnStatus").addText(getPsnStatus);
      return doc.getRootElement().asXML();
    } catch (DocumentException e) {
      logger.error("构造成果XML列表出现异常", e);
    }
    return "";
  }

  /**
   * 查找当天的刷新记录 true:已经刷新 false：没有刷新
   * 
   * @param openId
   * @param token
   * @return
   */
  public boolean hasAutoReflush(Long psnId) {
    UnionRefreshPsnLog unionRefreshPsnLog = unionRefreshPsnLogDao.findUnionRefreshPsnLog(psnId);
    Date date = new Date();
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    int nowYear = calendar.get(calendar.YEAR);
    int nowMonth = calendar.get(calendar.MONTH) + 1;
    int nowDay = calendar.get(calendar.DAY_OF_MONTH);

    if (unionRefreshPsnLog != null && unionRefreshPsnLog.getLogDate() != null) {
      calendar.setTime(unionRefreshPsnLog.getLogDate());
      int year = calendar.get(calendar.YEAR);
      int month = calendar.get(calendar.MONTH) + 1;
      int day = calendar.get(calendar.DAY_OF_MONTH);
      if (nowYear == year && nowMonth == month && nowDay == day)
        return true;
    }
    return false;
  }

  /**
   * 保存 或者 更新群组日志
   * 
   * @param psnId
   * @param groupId
   * @param openId
   * @param token
   * @param msg
   */
  private void saveRefreshGroupLog(Long psnId, Long openId, String token, String msg) {

    UnionRefreshPsnLog unionRefreshPsnLog = unionRefreshPsnLogDao.findUnionRefreshPsnLog(psnId);
    if (unionRefreshPsnLog == null) {
      unionRefreshPsnLog = new UnionRefreshPsnLog();
      unionRefreshPsnLog.setCreateDate(new Date());
    }
    unionRefreshPsnLog.setPsnId(psnId);
    unionRefreshPsnLog.setOpenId(openId);
    unionRefreshPsnLog.setToken(token);
    unionRefreshPsnLog.setMsg(msg);
    unionRefreshPsnLog.setLogDate(new Date());
    unionRefreshPsnLogDao.save(unionRefreshPsnLog);
  }

  /**
   * 刷新时间
   * 
   * @return
   */
  public Integer getRefreshTime() {
    if (StringUtils.isNotBlank(unionRefreshTime) && NumberUtils.isNumber(unionRefreshTime)) {
      return NumberUtils.toInt(unionRefreshTime);
    }
    return REFRESH_TIME;
  }
}
