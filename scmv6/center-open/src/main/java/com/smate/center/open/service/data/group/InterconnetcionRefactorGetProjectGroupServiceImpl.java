package com.smate.center.open.service.data.group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.math.NumberUtils;
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
import com.smate.center.open.dao.grp.GrpStatisticsDao;
import com.smate.center.open.model.grp.GrpStatistics;
import com.smate.center.open.model.interconnection.UnionGroupCodeCache;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;

/**
 * 重构 互联互通 获取项目群组
 * 
 * @author ajb
 */

@Transactional(rollbackFor = Exception.class)
public class InterconnetcionRefactorGetProjectGroupServiceImpl extends ThirdDataTypeBase {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private GrpBaseInfoDao grpBaseInfoDao;
  @Autowired
  private GrpStatisticsDao grpStatisticsDao;

  @Autowired
  private OpenGroupUnionDao openGroupUnionDao;
  @Autowired
  private OpenCacheService openCacheService;
  @Value("${domainscm}")
  private String domain;

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
    Map<String, Object> infoMap = new HashMap<String, Object>();
    Long psnId = NumberUtils.toLong(paramet.get(OpenConsts.MAP_PSNID).toString());

    List<HashMap<String, Object>> groupBaseInfoList = grpBaseInfoDao.findMyProjectGrp(psnId);
    // 没有项目群组也要返回success，而不是调用数据交互失败
    /*
     * if (groupBaseInfoList == null || groupBaseInfoList.size() == 0) { temp =
     * super.errorMap(OpenMsgCodeConsts.SCM_404, paramet, "没有查询到没有项目群组！"); return temp; }
     */
    infoMap = buildInfoMap(groupBaseInfoList, paramet);
    dataList.add(infoMap);
    temp = super.successMap(OpenMsgCodeConsts.SCM_000, dataList);
    return temp;

  }

  /**
   * 构建返回的数据
   * 
   * @param groupBaseInfoList
   * @param serviceData
   * @return
   */
  public Map<String, Object> buildInfoMap(List<HashMap<String, Object>> groupBaseInfoList,
      Map<String, Object> serviceData) {
    Map<String, Object> mapInfo = new HashMap<String, Object>();
    List<Map<String, Object>> groupMapList = new ArrayList<Map<String, Object>>();
    mapInfo.put("groups", groupMapList);

    Long psnId = NumberUtils.toLong(serviceData.get(OpenConsts.MAP_PSNID).toString());
    Long openId = NumberUtils.toLong(serviceData.get(OpenConsts.MAP_OPENID).toString());
    List<HashMap<String, Object>> groupBaseInfoListHasUnion = new ArrayList<HashMap<String, Object>>();
    if (groupBaseInfoList != null && groupBaseInfoList.size() > 0) {
      for (HashMap<String, Object> groupBaseInfo : groupBaseInfoList) {
        String groupId = (String) groupBaseInfo.get("GRP_ID").toString(); // 群组id
        String groupCode = DigestUtils.md5Hex(openId.toString() + "_" + groupId);
        Boolean hasUnion = false;
        hasUnion = isOrNotUnion(psnId, groupCode);
        if (hasUnion) {
          groupBaseInfoListHasUnion.add(groupBaseInfo);
          continue;
        }
        Map<String, Object> groupMap = buildGroupMap(groupBaseInfo, openId, 0);
        groupMapList.add(groupMap);
      }
    }
    // 最后添加已经关联的
    if (groupBaseInfoListHasUnion != null && groupBaseInfoListHasUnion.size() > 0) {
      for (HashMap<String, Object> groupBaseInfo : groupBaseInfoListHasUnion) {
        Map<String, Object> groupMap = buildGroupMap(groupBaseInfo, openId, 1);
        groupMapList.add(groupMap);
      }
    }
    return mapInfo;
  }

  private Map<String, Object> buildGroupMap(HashMap<String, Object> groupBaseInfo, Long openId, int hasUnion) {
    Map<String, Object> groupMap = new HashMap<String, Object>();
    String groupName = (String) groupBaseInfo.get("GRP_NAME"); // 群组名称
    String groupId = (String) groupBaseInfo.get("GRP_ID").toString(); // 群组id
    String groupCode = DigestUtils.md5Hex(openId.toString() + "_" + groupId);
    String grpAuatars = (String) groupBaseInfo.get("GRP_AUATARS"); // 群组头像地址
    String grpIndexUrl = (String) groupBaseInfo.get("GRP_INDEX_URL"); // 群组短地址
    GrpStatistics grpStatistics = grpStatisticsDao.get(NumberUtils.toLong(groupId));
    UnionGroupCodeCache groupCodeCache = new UnionGroupCodeCache();
    groupCodeCache.setGroupId(NumberUtils.toLong(groupId));
    groupCodeCache.setOpenId(openId);
    openCacheService.put(OpenConsts.UNION_GROUP_CODE_CACHE, openCacheService.EXP_HOUR_1, groupCode, groupCodeCache);
    groupMap.put(GroupInfoConst.GROUP_CODE, groupCode);
    groupMap.put(GroupInfoConst.GROUP_NAME, groupName);
    groupMap.put(GroupInfoConst.HAS_UNION, hasUnion);
    /* 群组头像地址 */
    if (grpAuatars != null) {
      groupMap.put(GroupInfoConst.GRP_AUATARS,
          grpAuatars.startsWith("http") ? grpAuatars : this.domain + grpAuatars.toString());
    } else {
      groupMap.put(GroupInfoConst.GRP_AUATARS, "");
    }
    if (grpIndexUrl != null) {
      groupMap.put(GroupInfoConst.GROUP_URL, domain + "/G/" + grpIndexUrl);
    } else {
      groupMap.put(GroupInfoConst.GROUP_URL, "");
    }
    if (grpStatistics != null) {
      groupMap.put(GroupInfoConst.PUB_SUM, grpStatistics.getSumPubs()); // 文献数
      groupMap.put(GroupInfoConst.MEMBER_SUM, grpStatistics.getSumMember()); // 成员数
    } else {
      groupMap.put(GroupInfoConst.PUB_SUM, 0);
      groupMap.put(GroupInfoConst.MEMBER_SUM, 0);
    }
    groupMap.put("des3GrpId", Des3Utils.encodeToDes3(groupId));
    return groupMap;
  }

  /**
   * 判断是否关联
   * 
   * @param ownerPsnId
   * @param groupCode
   * @return
   */
  private Boolean isOrNotUnion(Long ownerPsnId, String groupCode) {
    Long id = openGroupUnionDao.findIdByOwnPsnIdAndGroupCode(ownerPsnId, groupCode);
    if (id != null) {
      return true;
    }
    return false;
  }

}
