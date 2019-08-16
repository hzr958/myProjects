package com.smate.center.open.service.data.pub;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.consts.V8pubQueryPubConst;
import com.smate.center.open.dao.group.OpenGroupUnionDao;
import com.smate.center.open.dao.grp.GrpPubsDao;
import com.smate.center.open.dao.interconnection.log.InterconnectionImportPubLogDao;
import com.smate.center.open.model.interconnection.log.InterconnectionImportPubLog;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.center.open.service.data.group.GroupInfoConst;
import com.smate.center.open.service.interconnection.pub.InterconnectionIsisPubService;
import com.smate.center.open.service.util.PubDetailVoUtil;
import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * 获取成果详细信息接口 (可取多个) 需要判断成果与openid对应人员的关系,如果是群组成果 就要判断成果与群组的关系
 * 
 * @author tsz
 *
 */
@Transactional(rollbackFor = Exception.class)
public class GetPubDetailInfoServiceImpl extends ThirdDataTypeBase {

  protected Logger logger = LoggerFactory.getLogger(getClass());


  @Autowired
  private OpenGroupUnionDao openGroupUnionDao;
  @Autowired
  private InterconnectionImportPubLogDao interconnectionImportPubLogDao;

  @Autowired
  private GrpPubsDao grpPubsDao;
  @Resource(name = "isisPubService")
  private InterconnectionIsisPubService interconnectionIsisPubService;

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

    List<Long> pubIdList = handleParmaToPubIds(paramet);
    if (pubIdList == null || pubIdList.size() == 0) {
      logger.error("服务参数  pubIdList列表不能为空，或pubIdList列表不符合要求");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_268, paramet, "服务参数  pubIdList列表不能为空，或pubIdList列表不符合要求");
      return temp;
    }
    Object groupCode = paramet.get(GroupInfoConst.GROUP_CODE);
    if (groupCode != null && StringUtils.isNotBlank(groupCode.toString())) {
      // 群组成果Id权限判断
      Long psnId = (Long) paramet.get(OpenConsts.MAP_PSNID);
      Long groupId = openGroupUnionDao.findGroupIdByGroupCode(paramet.get(GroupInfoConst.GROUP_CODE).toString(), psnId);
      if (groupId != null) {
        analyzeGroupPubIds(paramet, pubIdList, groupId);

        paramet.put("groupId", groupId);
      } else {
        logger.error("服务参数   groupCode无效，或不属于当前人员的groupCode");
        temp = super.errorMap(OpenMsgCodeConsts.SCM_408, paramet, "scm-408  groupCode无效，或不属于当前人员的groupCode");
        return temp;
      }
    } else {
      // 个人成果ID权限判断
      analyzeOwnerPubIdList(paramet, pubIdList);
    }
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  /**
   * 分析群组成果id
   * 
   * @param paramet
   * @param pubIdList
   * @param groupId
   */
  private void analyzeGroupPubIds(Map<String, Object> paramet, List<Long> pubIdList, Long groupId) {
    // paramet.put("groupId", groupId);
    List<Long> errorPubIdList = new ArrayList<Long>();
    List<Long> ownerPubIdList = new ArrayList<Long>();
    for (int i = 0; i < pubIdList.size(); i++) {

      String SERVER_URL = domainscm + V8pubQueryPubConst.QUERY_PUB_DETAIL_URL;
      Map<String, Object> map = new HashMap<>();
      map.put(V8pubQueryPubConst.DES3_PUB_ID, Des3Utils.encodeToDes3(pubIdList.get(i).toString()));
      map.put("des3GrpId", Des3Utils.encodeToDes3(groupId.toString()));
      map.put("serviceType", V8pubQueryPubConst.OPEN_GRP_PUB);
      Map<String, Object> object = (Map<String, Object>) getRemotePubInfo(map, SERVER_URL);
      if (object != null && object.get("pubId") != null) {
        ownerPubIdList.add(pubIdList.get(i));
      } else {
        errorPubIdList.add(pubIdList.get(i));
      }
    }
    paramet.put(PubDetailConstant.FIND_PUB_ID_LIST, ownerPubIdList);
    paramet.put(PubDetailConstant.ERROR_PUB_ID_LIST, errorPubIdList);
  }

  /**
   * 分析个人成果id列表
   * 
   * @param paramet
   * @param pubIdList
   */
  private void analyzeOwnerPubIdList(Map<String, Object> paramet, List<Long> pubIdList) {
    List<Long> errorPubIdList = new ArrayList<Long>();
    List<Long> noAuthPubIdList = new ArrayList<Long>(); // 没有权限
    // List<Long> delPubIdList = new ArrayList<Long>(); // 删除的
    List<Long> ownerPubIdList = new ArrayList<Long>();
    Long psnId = (Long) paramet.get(OpenConsts.MAP_PSNID);
    for (int i = 0; i < pubIdList.size(); i++) {

      String SERVER_URL = domainscm + V8pubQueryPubConst.QUERY_PUB_DETAIL_URL;
      Map<String, Object> map = new HashMap<>();
      map.put(V8pubQueryPubConst.DES3_PUB_ID, Des3Utils.encodeToDes3(pubIdList.get(i).toString()));
      map.put("serviceType", V8pubQueryPubConst.QUERY_OPEN_SNS_PUB);
      Map<String, Object> pubInfoMap = (Map<String, Object>) getRemotePubInfo(map, SERVER_URL);
      if (pubInfoMap == null || (pubInfoMap.get("pubId")==null)) {
        errorPubIdList.add(pubIdList.get(i));
      } else if (pubInfoMap.get("pubOwnerPsnId") != null
          && pubInfoMap.get("pubOwnerPsnId").toString().equals(psnId.toString())) {
        ownerPubIdList.add(pubIdList.get(i));
      } else {
        noAuthPubIdList.add(pubIdList.get(i));
      }
    }
    paramet.put(PubDetailConstant.FIND_PUB_ID_LIST, ownerPubIdList);
    paramet.put(PubDetailConstant.ERROR_PUB_ID_LIST, errorPubIdList);
    paramet.put(PubDetailConstant.NO_AUTH_PUB_ID_LIST, noAuthPubIdList);
  }

  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    // 错误的的成果列表
    List<Long> errorPubIdList = (List<Long>) paramet.get(PubDetailConstant.ERROR_PUB_ID_LIST);
    if (errorPubIdList != null && errorPubIdList.size() > 0) {
      for (Long pubId : errorPubIdList) {
        Map<String, Object> errMap = new HashMap<String, Object>();
        errMap.put(PubDetailConstant.PUB_ID, pubId);
        errMap.put(PubDetailConstant.ERROR_MSG, OpenMsgCodeConsts.SCM_269);
        dataList.add(errMap);
      }
    }
    // 没有权限的成果列表
    List<Long> noAuthPubIdList = (List<Long>) paramet.get(PubDetailConstant.NO_AUTH_PUB_ID_LIST);
    if (noAuthPubIdList != null && noAuthPubIdList.size() > 0) {
      for (Long pubId : noAuthPubIdList) {
        Map<String, Object> errMap = new HashMap<String, Object>();
        errMap.put(PubDetailConstant.PUB_ID, pubId);
        errMap.put(PubDetailConstant.ERROR_MSG, OpenMsgCodeConsts.SCM_271);
        dataList.add(errMap);
      }
    }

    // 要查询的成果详情列表
    List<Long> pubIdList = (List<Long>) paramet.get(PubDetailConstant.FIND_PUB_ID_LIST);
    if (pubIdList != null && pubIdList.size() > 0) {
      for (Long pubId : pubIdList) {
        try {
          String SERVER_URL = domainscm + V8pubQueryPubConst.QUERY_PUB_DETAIL_URL;
          Map<String, Object> parmaMap = new HashMap<>();
          parmaMap.put(V8pubQueryPubConst.DES3_PUB_ID, Des3Utils.encodeToDes3(pubId.toString()));
          parmaMap.put("serviceType", V8pubQueryPubConst.QUERY_OPEN_SNS_PUB);
          Map<String, Object> pubInfo = (Map<String, Object>) getRemotePubInfo(parmaMap, SERVER_URL);
          PubDetailVO pubDetailVO = null;
          if (pubInfo != null) {
            pubDetailVO = PubDetailVoUtil.getBuilPubDetailVO(JacksonUtils.mapToJsonStr(pubInfo));
          }
          if (pubDetailVO != null) {
            dataList.add(interconnectionIsisPubService.parseXmlToMap1(pubDetailVO));
            InterconnectionImportPubLog log = new InterconnectionImportPubLog();
            log.setOpenId(Long.valueOf(paramet.get(OpenConsts.MAP_OPENID).toString()));
            log.setToken(paramet.get(OpenConsts.MAP_TOKEN).toString());
            log.setPubId(pubId);
            log.setGroupId(paramet.get("groupId") == null ? null : Long.parseLong(paramet.get("groupId").toString()));
            log.setImportDate(new Date());
            interconnectionImportPubLogDao.save(log);
          }
        } catch (Exception e) {
          logger.error("构建成果map数据出错 pubId=" + pubId, e);
        }
      }
    }
    return successMap(OpenMsgCodeConsts.SCM_000, dataList);
  }

  /**
   * 解析参数中的成果id
   * 
   * @return
   */
  public List<Long> handleParmaToPubIds(Map<String, Object> serviceData) {

    List<Long> pubIdList = new ArrayList<Long>();
    if (serviceData != null && serviceData.get("pubIdList") != null) {
      String pubIds = serviceData.get("pubIdList").toString();
      if (StringUtils.isNotBlank(pubIds)) {
        String[] pubIdArr = pubIds.split(",");
        for (int i = 0; i < pubIdArr.length; i++) {
          String pubId = StringUtils.trim(pubIdArr[i]);
          if (StringUtils.isNotBlank(pubId) && NumberUtils.isNumber(pubId)) {
            pubIdList.add(NumberUtils.toLong(pubId));
          }
        }
      }
    }
    return pubIdList;
  }



}
