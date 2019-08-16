package com.smate.center.open.service.interconnection.group;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
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
import com.smate.center.open.dao.group.OpenGroupUnionDao;
import com.smate.center.open.dao.grp.GrpBaseInfoDao;
import com.smate.center.open.dao.grp.GrpMemberDao;
import com.smate.center.open.dao.interconnection.UnionRefreshGroupLogDao;
import com.smate.center.open.model.grp.GrpBaseinfo;
import com.smate.center.open.model.grp.GrpMember;
import com.smate.center.open.model.interconnection.UnionRefreshGroupLog;
import com.smate.center.open.service.interconnection.ScmUnionDataHandleService;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;

/**
 * 互联互通 获取群组信息
 * 
 * @author AiJiangBin
 *
 */
public class InterconnectionGetGroupInfoServiceImpl implements ScmUnionDataHandleService {
  protected Logger logger = LoggerFactory.getLogger(getClass());


  @Value("${union.refresh.time}")
  public String unionRefreshTime;

  @Autowired
  private OpenCacheService openCacheService;
  @Autowired
  private OpenGroupUnionDao openGroupUnionDao;
  @Autowired
  private UnionRefreshGroupLogDao unionRefreshGroupLogDao;

  @Autowired
  private GrpBaseInfoDao grpBaseInfoDao;
  @Autowired
  private GrpMemberDao grpMemberDao;
  @Autowired
  private PersonDao personDao;
  @Value("${domainscm}")
  private String domainscm;

  /**
   * 检查： 参数
   * 
   * @param dataParamet
   * @return
   */
  private String checkParam(Map<String, Object> dataParamet) {

    Object groupCode = dataParamet.get("groupCode");
    Object autoRefresh = dataParamet.get("autoRefresh");
    if (groupCode == null || StringUtils.isBlank(groupCode.toString())) {
      logger.error("具体服务类型参数groupCode不能为空");
      return "groupCode缺少";
    }
    return null;
  }

  /**
   * 具体业务
   */
  @Override
  public String handleUnionData(Map<String, Object> dataParamet) {

    // data 在前面已经 验证不为空 ，并且是json格式的
    Object data = dataParamet.get(OpenConsts.MAP_DATA);
    Map<String, Object> serviceData = JacksonUtils.jsonToMap(data.toString());
    String temp = checkParam(serviceData);
    if (temp != null) {
      return buildResultXml(temp, "4");
    }
    Long psnId = NumberUtils.toLong(dataParamet.get("psnId").toString());
    Long openId = NumberUtils.toLong(dataParamet.get("openid").toString());
    String token = dataParamet.get("token").toString();
    String groupCode = serviceData.get("groupCode").toString();

    // 获取中的 groupId
    Long groupId = openGroupUnionDao.findGroupIdByGroupCode(groupCode, psnId);
    if (groupId != null) {
      dataParamet.put("groupId", groupId);
    } else {
      return buildResultXml("groupCode无效 或者失效！", "7");
    }

    GrpBaseinfo grpBaseinfo = grpBaseInfoDao.get(groupId);
    if (grpBaseinfo != null) {
      List<GrpMember> grpMemberList = grpMemberDao.findFiveGrpMemberByGrpId(groupId);

      if (grpMemberList != null && grpMemberList.size() > 0) {
        try {
          return buildUnionPubListXmlStr(grpBaseinfo, grpMemberList, serviceData);
        } catch (Exception e) {
          logger.error("构建群组xml错误！");
          return buildResultXml("构建群组xml异常！", "5");
        }
      }
    }
    return buildResultXml("没有查询到群组信息！", "3");
  }

  /**
   * 构建xml数据
   * 
   * @param psnInfo
   * @return
   * @throws Exception
   */
  public String buildUnionPubListXmlStr(GrpBaseinfo grpBaseinfo, List<GrpMember> grpMemberList,
      Map<String, Object> serviceData) throws Exception {
    try {
      Document doc = DocumentHelper.parseText("<?xml version=\"1.0\" encoding=\"UTF-8\"?><group></group>");

      Element rootNode = (Element) doc.selectSingleNode("/group");
      rootNode.addElement("getGroupStatus").addText("0");
      rootNode.addElement("group_code").addText(serviceData.get("groupCode").toString());
      rootNode.addElement("group_name").addText(grpBaseinfo.getGrpName() == null ? "" : grpBaseinfo.getGrpName());
      if (StringUtils.isNotBlank(grpBaseinfo.getGrpAuatars())) {

        if (grpBaseinfo.getGrpAuatars().startsWith("http")) {
          rootNode.addElement("group_url").addText(grpBaseinfo.getGrpAuatars());
        } else {
          rootNode.addElement("group_url").addText(domainscm + grpBaseinfo.getGrpAuatars());
        }
      } else {
        rootNode.addElement("group_url").addText(domainscm + "/resscmwebsns/images_v5/50X50g.gif");
      }
      Element adminElement = rootNode.addElement("admin");
      for (GrpMember grpMember : grpMemberList) {
        adminElement.addElement("admin_name").addText(getPersonName(grpMember.getPsnId()));
      }
      return doc.getRootElement().asXML();
    } catch (Exception e) {
      throw new Exception(e);
    }
  }

  public String getPersonName(Long psnId) {
    Person person = personDao.getPersonNameNotId(psnId);
    if (person != null) {
      if (StringUtils.isNotBlank(person.getName())) {
        return person.getName();
      } else if (StringUtils.isNotBlank(person.getFirstName()) && StringUtils.isNotBlank(person.getLastName())) {
        return person.getFirstName() + person.getLastName();
      } else if (StringUtils.isNotBlank(person.getEname())) {
        return person.getEname();
      }
    }
    return "";
  }


  public String buildResultXml(String result, String getGroupStatus) {
    Document doc;
    try {
      doc = DocumentHelper.parseText("<?xml version=\"1.0\" encoding=\"UTF-8\"?><group></group>");
      Element rootNode = (Element) doc.selectSingleNode("/group");
      rootNode.addElement("result").addText(result);
      rootNode.addElement("getGroupStatus").addText(getGroupStatus);
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
  public boolean hasAutoReflush(String groupCode) {
    UnionRefreshGroupLog unionRefreshGroupLog = unionRefreshGroupLogDao.findUnionRefreshGroupLog(groupCode);
    Date date = new Date();
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    int nowYear = calendar.get(calendar.YEAR);
    int nowMonth = calendar.get(calendar.MONTH) + 1;
    int nowDay = calendar.get(calendar.DAY_OF_MONTH);

    if (unionRefreshGroupLog != null && unionRefreshGroupLog.getLogDate() != null) {
      calendar.setTime(unionRefreshGroupLog.getLogDate());
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
  private void saveRefreshGroupLog(Long psnId, Long groupId, Long openId, String token, String msg) {
    String groupCode = DigestUtils.md5Hex(openId.toString() + "_" + groupId.toString());
    UnionRefreshGroupLog unionRefreshGroupLog = unionRefreshGroupLogDao.findUnionRefreshGroupLog(groupCode);
    if (unionRefreshGroupLog == null) {
      unionRefreshGroupLog = new UnionRefreshGroupLog();
      unionRefreshGroupLog.setCreateDate(new Date());
    }
    unionRefreshGroupLog.setPsnId(psnId);
    unionRefreshGroupLog.setGroupCode(groupCode);
    unionRefreshGroupLog.setOpenId(openId);
    unionRefreshGroupLog.setToken(token);
    unionRefreshGroupLog.setMsg(msg);
    unionRefreshGroupLog.setLogDate(new Date());
    unionRefreshGroupLogDao.save(unionRefreshGroupLog);
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
