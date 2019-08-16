package com.smate.center.open.service.data.prj;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.connector.enums.BatchOpenCodeEnum;
import com.smate.center.batch.connector.enums.BatchWeightEnum;
import com.smate.center.batch.connector.model.job.BatchJobs;
import com.smate.center.batch.connector.service.job.BatchJobsService;
import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.dao.prj.OpenPrjMemberDao;
import com.smate.center.open.dao.prj.OpenProjectDao;
import com.smate.center.open.exception.OpenProjectDataJsonNullException;
import com.smate.center.open.exception.OpenProjectDataJsonStateException;
import com.smate.center.open.exception.OpenProjectDataJsonTypeException;
import com.smate.center.open.model.prj.OpenPrjMember;
import com.smate.center.open.model.prj.OpenProject;
import com.smate.center.open.service.data.ThirdDataTypeBase;

/**
 * 第三方项目数据处理服务
 * 
 * @author LXZ
 * @服务码:80e6e33e
 * @since 6.0.1
 * @version 6.0.1
 */
@Transactional(rollbackFor = Exception.class)
public class ThirdPushProjectInfoImpl extends ThirdDataTypeBase {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private OpenProjectDao openProjectDao;
  @Autowired
  private OpenPrjMemberDao openPrjMemberDao;
  @Autowired
  private BatchJobsService batchJobsService;

  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();
    if (paramet.get(OpenConsts.MAP_DATA) == null && StringUtils.isEmpty(paramet.get(OpenConsts.MAP_DATA).toString())) {
      logger.error("Open系统-接收接口-项目数据不能为空,服务码:80e6e33e");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_209, paramet, "Open系统-接收接口-项目数据不能为空,服务码:80e6e33e");
      return temp;
    } else {
      temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    }
    return temp;
  }

  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    try {
      Map<String, Object> result = new HashMap<String, Object>();
      result.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
      result.put(OpenConsts.RESULT_MSG, OpenMsgCodeConsts.SCM_000);// 响应成功
      List<OpenProject> projectList = convertData(paramet);
      saveData(projectList);
      return result;
    } catch (Exception e) {
      if (e instanceof OpenProjectDataJsonTypeException) {
        throw new OpenProjectDataJsonTypeException(e);
      } else if (e instanceof OpenProjectDataJsonStateException) {
        throw new OpenProjectDataJsonStateException(e);
      } else {
        logger.error("第三方接收项目数据解析错误,服务码:80e6e33e");
        throw new OpenProjectDataJsonNullException(e);
      }

    }

  }

  /**
   * 数据处理
   * 
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param paramet
   * @return
   */
  private List<OpenProject> convertData(Map<String, Object> paramet) throws Exception {
    // String receiveType = paramet.get(OpenConsts.RECEIVE_TYPE);
    List<OpenProject> projectList = null;
    // 暂只提供JSON数据形式,有时间再提供XML的数据形式解析
    // if (OpenConsts.RECEIVE_TYPE_RESTFUL.equals(receiveType)) {
    projectList = convertJsonData(paramet);
    // } else if (OpenConsts.RECEIVE_TYPE_WEBSERV.equals(receiveType)) {
    // projectList = convertXmlData(paramet);
    // }
    return projectList;
  }

  /**
   * 数据处理-JSON
   * 
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param paramet
   * @return
   */
  private List<OpenProject> convertJsonData(Map<String, Object> paramet) throws Exception {
    String data = paramet.get(OpenConsts.MAP_DATA).toString();
    String token = paramet.get(OpenConsts.MAP_TOKEN).toString();
    Long openId = Long.parseLong(paramet.get(OpenConsts.MAP_OPENID).toString());
    List<OpenProject> resultList = new ArrayList<OpenProject>();
    JSONObject dataJson = new JSONObject(data);
    JSONObject object = dataJson.optJSONObject(OpenConsts.PROJECT);
    if (object == null) {
      return resultList;
    } else {
      JSONObject obj = object;
      String objId = JSONToString(obj, "objId");
      if (excludeRepeat(objId, token)) {
        return resultList;
      } ;
      // String internalNo = JSONToString(obj,"internalNo");
      String internalNo = null;
      String externalNo = JSONToString(obj, "prjNo");
      if (externalNo != null) {
        externalNo = externalNo.replace(" ", "");
      }
      String amount = JSONToString(obj, "amount");
      BigDecimal bigAmout = "".equals(amount) ? null : new BigDecimal(amount);
      // Integer isPrincipalIns = JSONToInteger(obj,"isPrincipalIns");
      Integer isPrincipalIns = 1;
      String state = JSONToString(obj, "state");
      // 校验state 只能是01或者02 03 04
      if (state != null && (!state.trim().equals("01") && !state.trim().equals("02") && !state.trim().equals("03"))) {
        throw new OpenProjectDataJsonStateException();
      }
      Long schemeId = JSONToLong(obj, "schemeId");
      Integer fundingYear = JSONToInteger(obj, "fundingYear");
      String agencyName = JSONToString(obj, "agencyName");
      if (agencyName != null) {
        agencyName = agencyName.trim();
      }
      String schemeName = JSONToString(obj, "schemeName");
      String type = JSONToString(obj, "type");
      // 校验type 只能是1或者0
      if (type == null || type.trim().equals("")) {
        type = "0";
      } else if (type != null && (!type.trim().equals("1") && !type.trim().equals("0"))) {
        throw new OpenProjectDataJsonTypeException();
      }
      Integer startYear = JSONToInteger(obj, "startYear");
      Integer startMonth = JSONToInteger(obj, "startMonth");
      Integer startDay = JSONToInteger(obj, "startDay");
      Integer endYear = JSONToInteger(obj, "endYear");
      Integer endMonth = JSONToInteger(obj, "endMonth");
      Integer endDay = JSONToInteger(obj, "endDay");
      // Long insId = JSONToLong(obj,"insId");
      Long insId = null;
      String insName = JSONToString(obj, "insName");
      // Long psnId = obj.optLong("psnId");
      Long psnId = 0L;
      // Integer dbId = JSONToInteger(obj,"dbId");
      Integer dbId = 30;// const_ref_db表中的ID
      String zhTitle = JSONToString(obj, "zhTitle");
      if (zhTitle != null) {
        zhTitle = zhTitle.trim();
      }
      String enTitle = JSONToString(obj, "enTitle");
      if (enTitle != null) {
        enTitle = enTitle.trim();
      }
      // String authorNames = JSONToString(obj,"authorNames");
      String authorNames = null;
      // Long agencyId = JSONToLong(obj,"agencyId");
      Long agencyId = null;
      String amountUnit = JSONToString(obj, "amountUnit");
      // String authorNamesEn = JSONToString(obj,"authorNamesEn");
      String authorNamesEn = null;
      // String enAgencyName = JSONToString(obj,"enAgencyName");
      String enAgencyName = null;
      // String enSchemeName = JSONToString(obj,"enSchemeName");
      String enSchemeName = null;
      JSONArray membersArray = obj.optJSONArray("members");
      List<OpenPrjMember> members = null;
      if (membersArray != null) {
        members = new ArrayList<OpenPrjMember>();
        for (int j = 0; j < membersArray.length(); j++) {
          JSONObject member = membersArray.optJSONObject(j);
          String name = JSONToString(member, "name");
          String email = JSONToString(member, "email");
          String memberInsName = JSONToString(member, "insName");
          Integer seqNo = JSONToInteger(member, "seqNo") == null ? j : JSONToInteger(member, "seqNo");
          Integer notifyAuthor = JSONToInteger(member, "notifyAuthor");
          OpenPrjMember mem = new OpenPrjMember(name, email, memberInsName, seqNo, notifyAuthor);
          members.add(mem);
        }
      }
      OpenProject project = new OpenProject(objId, token, internalNo, externalNo, isPrincipalIns, state, schemeId,
          fundingYear, agencyName, schemeName, type, startYear, startMonth, startDay, endYear, endMonth, endDay, insId,
          insName, psnId, dbId, zhTitle, enTitle, authorNames, agencyId, bigAmout, amountUnit, authorNamesEn,
          enAgencyName, enSchemeName, openId);
      project.setMembers(members);
      project.setCreateDate(new Date());
      resultList.add(project);
    }
    return resultList;
  }

  /**
   * 数据处理-XML
   * 
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param paramet
   * @return
   */
  private List<OpenProject> convertXmlData(Map<String, Object> paramet) {
    // TODO 暂无解析方式
    return null;
  }

  /**
   * 数据保存
   * 
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param projectList
   */
  private void saveData(List<OpenProject> projectList) {
    if (CollectionUtils.isNotEmpty(projectList)) {
      for (OpenProject obj : projectList) {
        openProjectDao.save(obj);
        List<OpenPrjMember> members = obj.getMembers();
        if (CollectionUtils.isNotEmpty(members)) {
          for (OpenPrjMember mem : members) {
            mem.setOpenPrjId(obj.getId());
            openPrjMemberDao.save(mem);
          }
        }
        // 存储Spring Batch job
        // {"msg_id":2}
        String context = "{\"msg_id\":" + obj.getId() + "}";
        String Weight = BatchWeightEnum.A.toString();
        String strategy = BatchOpenCodeEnum.THIRD_PUSH_PROJECT_INFO.toString();
        BatchJobs job = new BatchJobs(context, Weight, strategy);
        batchJobsService.saveJob(job);
      }
    }
  }

  /**
   * int转化
   * 
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param obj
   * @param name
   * @return
   */
  private Integer JSONToInteger(JSONObject obj, String name) {
    Integer value = obj.optInt(name, OpenConsts.NO_ANY_INT_VALUE);
    return value.equals(OpenConsts.NO_ANY_INT_VALUE) ? null : value;
  }

  /**
   * String转化
   * 
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param obj
   * @param name
   * @return
   */
  private String JSONToString(JSONObject obj, String name) {
    String value = obj.optString(name, OpenConsts.NOT_ANY_MORE_VALUE);
    return OpenConsts.NOT_ANY_MORE_VALUE.equals(value) ? null : value;
  }

  /**
   * Long转化
   * 
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param obj
   * @param name
   * @return
   */
  private Long JSONToLong(JSONObject obj, String name) {
    Long value = obj.optLong(name, OpenConsts.NO_ANY_LONG_VALUE);
    return value.equals(new Long(OpenConsts.NO_ANY_LONG_VALUE)) ? null : value;
  }

  /**
   * 查重 逻辑 objId相同 return 重复
   * 
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param objId
   * @return
   */
  private boolean excludeRepeat(String objId, String token) {
    List<OpenProject> list = openProjectDao.queryByObjId(objId, token);
    if (list != null && list.size() > 0) {
      return true;
    }
    return false;
  }



}
