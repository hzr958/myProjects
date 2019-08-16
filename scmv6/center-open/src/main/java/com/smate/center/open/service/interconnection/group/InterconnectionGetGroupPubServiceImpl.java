package com.smate.center.open.service.interconnection.group;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.V8pubQueryPubConst;
import com.smate.center.open.dao.group.OpenGroupUnionDao;
import com.smate.center.open.dao.grp.GrpBaseInfoDao;
import com.smate.center.open.model.grp.GrpBaseinfo;
import com.smate.center.open.service.interconnection.ScmUnionDataHandleService;
import com.smate.center.open.service.interconnection.log.InterconnectionGetPubLogService;
import com.smate.center.open.service.interconnection.pub.AbstractCommonPubInfoService;
import com.smate.core.base.pub.dto.PubQueryDTO;
import com.smate.core.base.utils.date.DateUtils;
import com.smate.core.base.utils.json.JacksonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 互联互通 获取群组 成果
 * 
 * @author AiJiangBin
 *
 */
public class InterconnectionGetGroupPubServiceImpl extends AbstractCommonPubInfoService
    implements ScmUnionDataHandleService {
  protected Logger logger = LoggerFactory.getLogger(getClass());



  @Autowired
  private GrpBaseInfoDao grpBaseInfoDao;
  @Autowired
  private OpenGroupUnionDao openGroupUnionDao;
  @Autowired
  private InterconnectionGetPubLogService interconnectionGetPubLogService;


  /**
   * 检查： 参数
   * 
   * @param dataParamet
   * @return
   */
  private String checkParam(Map<String, Object> dataParamet) {
    Object groupCode = dataParamet.get("groupCode");
    if (groupCode == null || StringUtils.isBlank(groupCode.toString())) {
      logger.error("具体服务类型参数groupCode不能为空");
      return "groupCode缺少";
    }
    // 检查公共的参数
    return checkCommonParam(dataParamet);

  }

  /**
   * 具体业务
   */
  @Override
  public String handleUnionData(Map<String, Object> dataParamet) throws Exception {
    // 时间戳
    String lastGetPubDate = "";
    String currentGetPubDate = "";
    Date getPubDate = null;
    Object data = dataParamet.get(OpenConsts.MAP_DATA);
    Map<String, Object> serviceData = JacksonUtils.jsonToMap(data.toString());
    String temp = checkParam(serviceData);
    Object lastTimeObj = serviceData.get("lastGetPubDate");
    lastGetPubDate = lastTimeObj != null ? lastTimeObj.toString() : "";
    getPubDate = DateUtils.parseStringToDate(lastGetPubDate);
    if (temp != null) {
      return buildResultXml(temp, "4");
    }
    Long psnId = NumberUtils.toLong(dataParamet.get("psnId").toString());
    Long openId = NumberUtils.toLong(dataParamet.get("openid").toString());
    Integer pageNo = NumberUtils.toInt(serviceData.get("pageNo").toString());
    Integer pageSize = NumberUtils.toInt(serviceData.get("pageSize").toString());
    String pubTemplate = pubTemplateMap.get(serviceData.get("data_template_key").toString());
    String groupCode = serviceData.get("groupCode").toString();

    // ----------群组
    // 获取中的 groupId
    Long groupId = openGroupUnionDao.findGroupIdByGroupCode(serviceData.get("groupCode").toString(), psnId);
    if (groupId != null) {
      dataParamet.put("groupId", groupId);
    } else {
      return buildResultXml("groupCode无效 或者失效！", "4");
    }
    GrpBaseinfo grpBaseinfo = grpBaseInfoDao.get(groupId);
    if (grpBaseinfo == null) {
      return buildResultXml("访问的群组不存在,groupCode=" + serviceData.get("groupCode").toString(), "4");
    }
    // ----------
    currentGetPubDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    String SERVER_URL = domainscm + V8pubQueryPubConst.QUERY_PUB_URL;
    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    pubQueryDTO.setSearchGrpId(groupId);;
    pubQueryDTO.setPageNo(pageNo);
    pubQueryDTO.setPageSize(pageSize);
    if (getPubDate != null) {
      pubQueryDTO.setPubUpdateDate(getPubDate);
    }
    pubQueryDTO.setServiceType(V8pubQueryPubConst.CENTER_OPEN_GRP_PUB_LIST);
    Map<String, Object> result = (Map<String, Object>) getRemotePubInfo(pubQueryDTO, SERVER_URL);
    Long totalCount = 0L;
    if (result.get("status").equals("success")) {
      totalCount = Long.parseLong(result.get("totalCount").toString());
      if (totalCount == null || totalCount < 1) {
        // 没有成果
        if (getPubDate != null) {
          return buildResultXml("没有成果被更新", "7");
        }
        return buildResultXml("没有查询到成果!", "3");
      }
    } else {
      return buildResultXml("scm-111 系统异常,!", "3");
    }

    if (pageNo < 1) {
      pageNo = 1;
    }
    if (pageSize < 1) {
      pageSize = 1;
    }
    Long totalPages = 0L;
    if (totalCount % pageSize == 0) {
      totalPages = totalCount / pageSize;
    } else {
      totalPages = totalCount / pageSize + 1;
    }
    List<Map<String, Object>> resultList = (List<Map<String, Object>>) result.get("resultList");
    if (resultList != null && resultList.size() > 0) {
      // 记录日志
      interconnectionGetPubLogService.saveGetPubLog(2, resultList.size(), dataParamet);
      try {
        HashMap<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("openId", openId);
        dataMap.put("totalPages", totalPages);
        dataMap.put("count", totalCount);
        dataMap.put("pubTemplate", pubTemplate);
        dataMap.put("groupCode", groupCode);
        dataMap.put("currentGetPubDate", currentGetPubDate);
        return buildUnionPubListXmlStr(resultList, dataMap, dataParamet, true);
      } catch (Exception e) {
        // 吃掉异常，当做open系统的正确请求来处理
        logger.error(e.getMessage() + JacksonUtils.mapToJsonStr(dataParamet), e);
        this.saveAccessLog(dataParamet, e.toString(), 2,
            "获取成果历史数据异常，服务类型为：" + dataParamet.get(OpenConsts.MAP_SERVICE_TYPE).toString());
        return buildResultXml("构建项目群组 成果异常", "5");
      }
    }
    return buildResultXml("没有查询到成果!", "3");
  }


  public String buildResultXml(String result, String getPubStatus) {
    Document doc;
    try {
      doc = DocumentHelper.parseText("<?xml version=\"1.0\" encoding=\"UTF-8\"?><publications></publications>");
      Element rootNode = (Element) doc.selectSingleNode("/publications");
      rootNode.addElement("result").addText(result);
      rootNode.addElement("getPubStatus").addText(getPubStatus);
      return doc.getRootElement().asXML();
    } catch (DocumentException e) {
      logger.error("构造成果XML列表出现异常", e);
    }
    return "";
  }


}

