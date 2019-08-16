package com.smate.center.open.service.data.group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.cache.OpenCacheService;
import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.dao.group.OpenGroupUnionDao;
import com.smate.center.open.dao.grp.GrpBaseInfoDao;
import com.smate.center.open.dao.grp.GrpMemberDao;
import com.smate.center.open.dao.grp.GrpStatisticsDao;
import com.smate.center.open.dao.interconnection.UnionRefreshGroupLogDao;
import com.smate.center.open.model.grp.GrpBaseinfo;
import com.smate.center.open.model.grp.GrpMember;
import com.smate.center.open.model.grp.GrpStatistics;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.Des3Utils;

/**
 * 重构 互联互通获取群组信息服务实现
 * 
 * @author ajb
 *
 */

@Transactional(rollbackFor = Exception.class)
public class InterconnetcionRefactorGetGroupInfoServiceImpl extends ThirdDataTypeBase {

  protected Logger logger = LoggerFactory.getLogger(getClass());

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
  private GrpStatisticsDao grpStatisticsDao;

  @Autowired
  private PersonDao personDao;
  @Value("${domainscm}")
  private String domainscm;

  @SuppressWarnings("unchecked")
  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    // 没有 参数校验 直接返回成功
    Map<String, Object> temp = new HashMap<String, Object>();

    Object data = paramet.get(OpenConsts.MAP_DATA);
    if (data != null && data.toString().length() > 0) {
      Map<String, Object> dataMap = JacksonUtils.jsonToMap(data.toString());
      if (dataMap != null) {
        paramet.putAll(dataMap);
      }
    }
    Object groupCode = paramet.get(GroupInfoConst.GROUP_CODE);
    if (groupCode == null) {
      groupCode = paramet.get("des3GrpId");
    }
    if (groupCode == null || StringUtils.isBlank(groupCode.toString())) {
      logger.error("服务参数  群组的groupCode不能为空");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_254, paramet, "服务参数  群组的groupCode不能为空");
      return temp;
    }
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);

    return temp;
  }

  /**
     * 
     */
  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {

    Map<String, Object> temp = new HashMap<String, Object>();
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    Object psnIdObj = paramet.get(OpenConsts.MAP_PSNID);
    Long psnId = (Long) psnIdObj;
    String groupCode = "";
    Long groupId = null;
    // 获取中的 groupId
    if (paramet.get(OpenConsts.MAP_GROUP_CODE) == null) {
      groupId = Long.parseLong(Des3Utils.decodeFromDes3(ObjectUtils.toString(paramet.get("des3GrpId"))));
    } else {
      groupCode = paramet.get(OpenConsts.MAP_GROUP_CODE).toString();
      groupId = openGroupUnionDao.findGroupIdByGroupCode(groupCode, psnId);
    }
    Map<String, Object> infoMap = new HashMap<String, Object>();
    if (groupId == null) {
      logger.error("获取群组信息，groupCode无效 或者失效");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_402, paramet, "获取群组信息，groupCode无效 或者失效");
      return temp;
    }

    GrpBaseinfo grpBaseinfo = grpBaseInfoDao.get(groupId);

    if (grpBaseinfo != null) {
      List<GrpMember> grpMemberList = grpMemberDao.findFiveGrpMemberByGrpId(groupId);
      if (grpMemberList != null && grpMemberList.size() > 0) {
        try {
          infoMap = buildInfoMap(grpBaseinfo, grpMemberList, paramet);
          dataList.add(infoMap);
          temp = super.successMap(OpenMsgCodeConsts.SCM_000, dataList);
          return temp;
        } catch (Exception e) {
          logger.error("构建群组xml错误！");
          temp = super.errorMap(OpenMsgCodeConsts.SCM_531, paramet, "构建群组xml错误！");
          return temp;
        }
      }
    }
    temp = super.errorMap(OpenMsgCodeConsts.SCM_403, paramet, "没有查询到群组信息！");
    return temp;
  }

  /**
   * 构建返回的数据
   * 
   * @param groupBaseInfo
   * @param groupInvitePsnList
   * @param serviceData
   * @return
   */
  public Map<String, Object> buildInfoMap(GrpBaseinfo grpBaseinfo, List<GrpMember> grpMemberList,
      Map<String, Object> serviceData) {

    Map<String, Object> mapInfo = new HashMap<String, Object>();
    if (serviceData.get(GroupInfoConst.GROUP_CODE) != null) {
      mapInfo.put(GroupInfoConst.GROUP_CODE, serviceData.get(GroupInfoConst.GROUP_CODE).toString());
    }
    mapInfo.put("des3GrpId", Des3Utils.encodeToDes3(grpBaseinfo.getGrpId().toString()));
    mapInfo.put(GroupInfoConst.GROUP_NAME, grpBaseinfo.getGrpName() == null ? "" : grpBaseinfo.getGrpName());

    if (StringUtils.isNotBlank(grpBaseinfo.getGrpAuatars())) {
      if (grpBaseinfo.getGrpAuatars().startsWith("http")) {
        mapInfo.put(GroupInfoConst.GROUP_URL, grpBaseinfo.getGrpAuatars());
      } else {
        mapInfo.put(GroupInfoConst.GROUP_URL, domainscm + grpBaseinfo.getGrpAuatars());
      }
    } else {
      mapInfo.put(GroupInfoConst.GROUP_URL, domainscm + "/resscmwebsns/images_v5/50X50g.gif");
    }

    // 管理员
    List<Map<String, Object>> adminList = new ArrayList<Map<String, Object>>();
    for (GrpMember grpMember : grpMemberList) {
      Map<String, Object> admin = new HashMap<String, Object>();
      admin.put(GroupInfoConst.ADMIN_NAME, getPersonName(grpMember.getPsnId()));

      adminList.add(admin);
    }
    mapInfo.put(GroupInfoConst.ADMIN, adminList);
    // 添加统计数
    GrpStatistics grpStatistics = grpStatisticsDao.get(grpBaseinfo.getGrpId());
    if (grpStatistics != null) {
      mapInfo.put(GroupInfoConst.PUB_SUM, grpStatistics.getSumPubs());
    } else {
      mapInfo.put(GroupInfoConst.PUB_SUM, "");
    }
    return mapInfo;
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

}
