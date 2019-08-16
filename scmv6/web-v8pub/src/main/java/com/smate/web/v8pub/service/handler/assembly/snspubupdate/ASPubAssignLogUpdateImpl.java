package com.smate.web.v8pub.service.handler.assembly.snspubupdate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.pub.consts.V8pubQueryPubConst;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.web.v8pub.dom.PatentInfoBean;
import com.smate.web.v8pub.dom.SoftwareCopyrightBean;
import com.smate.web.v8pub.dom.StandardInfoBean;
import com.smate.web.v8pub.enums.PubGenreConstants;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.pdwh.PubAssignLogService;

/**
 * 成果指派自动认领状态更新
 * 
 * @author YJ
 *
 *         2018年8月10日
 */
@Transactional(rollbackFor = Exception.class)
public class ASPubAssignLogUpdateImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Value("${domainscm}")
  private String domainScm;
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Autowired
  private PubAssignLogService pubAssignLogService;

  @Override
  public void checkSourcesParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // TODO Auto-generated method stub

  }

  @Override
  public void checkRebuildParameter(PubDTO pub) throws PubHandlerAssemblyException {

  }

  @Override
  public Map<String, String> excute(PubDTO pub) throws PubHandlerAssemblyException {
    if (pub.pubGenre == PubGenreConstants.GROUP_PUB) {
      return null;
    }
    // 成果认领的成果，不走成果自动认领的逻辑
    if (pub.isPubConfirm != null && pub.isPubConfirm == 1) {
      return null;
    }
    if (NumberUtils.isNullOrZero(pub.psnId)) {
      return null;
    }
    try {
      // 调用查重接口进行查重
      List<Long> dupPubIds = getDupPub(pub);
      // 更新成果认领状态
      pubAssignLogService.updateAutoConfirmStatus(dupPubIds, pub.psnId, pub.pubId, pub.pubGenre);
    } catch (Exception e) {
      logger.error("更新成果自动认领状态出错！", e);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "更新成果自动认领状态出错！", e);
    }
    return null;
  }

  /**
   * 进行查重
   * 
   * @param pub
   * @return
   */
  private List<Long> getDupPub(PubDTO pub) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
    HttpEntity<String> entity = new HttpEntity<String>(buildDupJson(pub), headers);
    String dupUrl = domainScm + V8pubQueryPubConst.PUB_DUPCHECK_URL;
    String dupResult = restTemplate.postForObject(dupUrl, entity, String.class);
    Map<String, Object> map = JacksonUtils.jsonToMap(dupResult);
    if (!"SUCCESS".equals(String.valueOf(map.get("status")))) {
      return null;
    }
    if (map.get("msgList") == null) {
      return null;
    }
    String msgList = String.valueOf(map.get("msgList"));
    return changePubList(msgList);
  }

  /**
   * 将string变为Long数组
   * 
   * @param msgList
   * @return
   */
  private List<Long> changePubList(String msgList) {
    if (StringUtils.isBlank(msgList)) {
      return null;
    }
    List<Long> dupList = new ArrayList<>();
    String[] strArr = msgList.split(",");
    if (strArr != null && strArr.length > 0) {
      for (int i = 0; i < strArr.length; i++) {
        Long dupPubId = NumberUtils.toLong(strArr[i]);
        dupList.add(dupPubId);
      }
    }
    return dupList;
  }

  /**
   * 构建查重参数
   * 
   * @param pub
   * @return
   */
  private String buildDupJson(PubDTO pub) {
    Map<String, Object> dupMap = new HashMap<>();
    dupMap.put("pubGener", 3);// 成果大类别，1代表个人成果 2代表群组成果 3代表基准库成果
    dupMap.put("title", pub.title);
    dupMap.put("pubYear", pub.publishYear);
    dupMap.put("pubType", pub.pubType);
    dupMap.put("doi", pub.doi);
    dupMap.put("srcDbId", pub.srcDbId);
    dupMap.put("sourceId", pub.sourceId);
    switch (pub.pubType) {
      case 5:
        PatentInfoBean patentInfoBean = (PatentInfoBean) pub.pubTypeInfoBean;
        if (patentInfoBean != null) {
          dupMap.put("applicationNo", patentInfoBean.getApplicationNo());
          dupMap.put("publicationOpenNo", patentInfoBean.getPublicationOpenNo());
        }
        break;
      case 12:
        StandardInfoBean standInfoBean = (StandardInfoBean) pub.pubTypeInfoBean;
        if (standInfoBean != null) {
          dupMap.put("standardNo", standInfoBean.getStandardNo());
        }
        break;
      case 13:
        SoftwareCopyrightBean typeInfo = (SoftwareCopyrightBean) pub.pubTypeInfoBean;
        if (typeInfo != null) {
          dupMap.put("registerNo", typeInfo.getRegisterNo());
        }
        break;
    }
    return JacksonUtils.mapToJsonStr(dupMap);
  }



}
