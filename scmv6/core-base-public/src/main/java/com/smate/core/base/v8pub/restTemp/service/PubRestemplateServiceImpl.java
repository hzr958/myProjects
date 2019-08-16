package com.smate.core.base.v8pub.restTemp.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.pub.consts.RestTemplateUtils;
import com.smate.core.base.pub.consts.V8pubQueryPubConst;
import com.smate.core.base.pub.dto.PubQueryDTO;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;

@Service("pubRestemplateService")
public class PubRestemplateServiceImpl implements PubRestemplateService {
  public final static String SNS_QUERY_PUB = "/data/pub/query/list";
  public final static String DETAIL_URL = "/data/pub/query/detail";// 成果编辑查询
  public final static String RESUME_PUB = "/data/pub/resume/pubinfo"; // 简历代表成果和其他成果

  @Value("${domainscm}")
  private String domainscm;
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;

  @Override
  public String pubListQueryByPubIds(List<Long> pubIds, Long psnId) throws Exception {
    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    pubQueryDTO.setServiceType(V8pubQueryPubConst.PUB_LIST_QUERY_BY_PUB_IDS);
    pubQueryDTO.setSearchPubIdList(pubIds);
    pubQueryDTO.setDes3SearchPsnId(Des3Utils.encodeToDes3(Objects.toString(psnId)));
    pubQueryDTO.setOrderBy("citations");
    String pubJson =
        RestTemplateUtils.post(restTemplate, domainscm + SNS_QUERY_PUB, JacksonUtils.jsonObjectSerializer(pubQueryDTO));
    return pubJson;
  }

  @Override
  public String psnRepresentPubList(Long currentPsnId, String des3PsnId) throws Exception {
    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    pubQueryDTO.setServiceType(V8pubQueryPubConst.QUERY_PSN_REPRESENT_PUBS);
    pubQueryDTO.setDes3SearchPsnId(des3PsnId);
    pubQueryDTO.setPsnId(currentPsnId);
    String pubJson =
        RestTemplateUtils.post(restTemplate, domainscm + SNS_QUERY_PUB, JacksonUtils.jsonObjectSerializer(pubQueryDTO));
    return pubJson;
  }

  @Override
  public String delPsnPub(Long pubId, Long psnId) throws Exception {
    Map<String, Object> paramMap = new HashMap<>();
    paramMap.put("des3PubId", Des3Utils.encodeToDes3(pubId.toString()));
    paramMap.put("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
    paramMap.put("pubHandlerName", V8pubQueryPubConst.DELETE_PSN_PUBHANDLER);
    paramMap.put("pubGenre", "1");
    String result = RestTemplateUtils.post(restTemplate, domainscm + V8pubQueryPubConst.PUBHANDLER_URL,
        JacksonUtils.jsonObjectSerializer(paramMap));
    return result;
  }

  @Override
  public String saveOrUpdatePubJson(String pubJson) {
    return RestTemplateUtils.post(restTemplate, domainscm + V8pubQueryPubConst.PUBHANDLER_URL, pubJson);

  }

  @Override
  public String snsEditPubQuery(String des3PubId) {
    String pubJson = "";
    Map<String, String> param = new HashMap<String, String>();
    param.put("des3PubId", des3PubId);
    param.put("serviceType", V8pubQueryPubConst.QUERY_SNS_EDIT_PUB_DETAIL_BY_PUB_ID_SERVICE);
    String requestJson = JacksonUtils.mapToJsonStr(param);
    pubJson = RestTemplateUtils.post(restTemplate, domainscm + DETAIL_URL, requestJson);
    return pubJson;
  }

  @Override
  public String psnResumePubInfo(Long psnId, Long resumeId, Integer moduleId) {
    String pubJson = "";
    Map<String, Object> param = new HashMap<String, Object>();
    param.put("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
    param.put("resumeId", Des3Utils.encodeToDes3(resumeId.toString()));
    param.put("moduleId", moduleId);
    param.put("serviceType", V8pubQueryPubConst.QUERY_SNS_EDIT_PUB_DETAIL_BY_PUB_ID_SERVICE);
    String requestJson = JacksonUtils.mapToJsonStr(param);
    pubJson = RestTemplateUtils.post(restTemplate, domainscm + RESUME_PUB, requestJson);
    return pubJson;
  }

}
