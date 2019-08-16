package com.smate.web.v8pub.service.searchimport;

import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.pub.consts.V8pubQueryPubConst;
import com.smate.core.base.pub.dto.PubQueryDTO;
import com.smate.core.base.pub.vo.PubInfo;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.string.IrisNumberUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.v8pub.consts.PubDataUrlConstants;
import com.smate.web.v8pub.consts.V8pubConst;
import com.smate.web.v8pub.dao.pdwh.PubPdwhDAO;
import com.smate.web.v8pub.dao.pdwh.PubPdwhDetailDAO;
import com.smate.web.v8pub.dao.sns.PubCitationsDAO;
import com.smate.web.v8pub.dao.sns.PubSituationDAO;
import com.smate.web.v8pub.dao.sns.PubSnsDAO;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;
import com.smate.web.v8pub.enums.PubHandlerEnum;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.service.pubdetailquery.PubDetailHandleService;
import com.smate.web.v8pub.service.query.PubQueryhandlerService;
import com.smate.web.v8pub.service.sns.PubPdwhSnsRelationService;
import com.smate.web.v8pub.utils.RestTemplateUtils;
import com.smate.web.v8pub.vo.searchimport.PubCitedVo;

/**
 * 成果引用服务
 * 
 * @author wsn
 * @date 2018年8月28日
 */
@Service("pubCitedService")
@Transactional(rollbackFor = Exception.class)
public class PubCitedServiceImple implements PubCitedService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubQueryhandlerService pubQueryhandlerService;
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Autowired
  private PubCitationsDAO pubCitationsDAO;
  @Autowired
  private PubSituationDAO pubSituationDAO;
  @Autowired
  private PubPdwhDetailDAO pubPdwhDetailDAO;
  @Autowired
  private PubSnsDAO pubSnsDAO;
  @Autowired
  private PubDetailHandleService pubDetailHandleService;
  @Autowired
  private PubPdwhSnsRelationService pubPdwhSnsRelationService;
  @Autowired
  private PubPdwhDAO pubPdwhDAO;
  @Value("${domainscm}")
  private String scmDomain;

  @SuppressWarnings("unchecked")
  @Override
  public String getUpdatePubCitedParams(PubCitedVo vo) throws ServiceException {
    try {
      Long psnId = vo.getPsnId();
      if (psnId != null && psnId > 0L) {
        // 1. 获取人员所有成果
        PubQueryDTO queryDTO = new PubQueryDTO();
        queryDTO.searchPsnId = psnId;
        queryDTO.isQueryAll = false;
        queryDTO.setServiceType("pubList");
        queryDTO.setSearchDoi(true);
        queryDTO.setSelf(true);
        queryDTO.setNotFulltextAndSortUrl(true);
        queryDTO.setSearchDoi(true);
        String SERVER_URL = scmDomain + PubDataUrlConstants.SNS_PUB_LIST_QUERY_URL;
        // 设置请求头部
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> requestEntity =
            new HttpEntity<String>(JacksonUtils.jsonObjectSerializer(queryDTO), requestHeaders);
        Map<String, Object> object =
            (Map<String, Object>) restTemplate.postForObject(SERVER_URL, requestEntity, Object.class);
        if (object != null && object.get("status").equals(V8pubConst.SUCCESS)) {
          List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
          List<Map<String, Object>> resultList = (List<Map<String, Object>>) object.get("resultList");
          if (resultList != null && resultList.size() > 0) {
            for (Map<String, Object> map : resultList) {
              PubInfo pubInfo = new PubInfo();
              try {
                BeanUtils.populate(pubInfo, map);
                if (StringUtils.isBlank(pubInfo.getDOI())) {
                  pubInfo.setDOI(Objects.toString(map.get("doi"), ""));
                }
                // 构建更新引用所需参数
                this.buildUpdateCitedParamsByPubInfo(pubInfo, mapList, psnId);
              } catch (IllegalAccessException | InvocationTargetException e) {
                logger.error("复制属性异常", e);
              } catch (Exception e) {
                logger.error("构建更新引用所需参数异常， pubId = " + pubInfo.getPubId(), e);
              }

            }
          }
          return JacksonUtils.listToJsonStr(mapList);
        }
      }
    } catch (Exception e) {
      logger.error("获取更新引用所需参数出错， psnId = " + "", e);
      throw new ServiceException(e);
    }
    return null;
  }

  /**
   * 构建更新引用所需参数
   * 
   * @param pubInfo
   * @param mapList
   * @param psnId
   * @return true: 需要跳过， false: 正常处理
   */
  protected boolean buildUpdateCitedParamsByPubInfo(PubInfo pubInfo, List<Map<String, Object>> mapList, Long psnId) {
    Long pubId = pubInfo.getPubId();
    // 2. 获取成果的引用数更新时间，只有距离上次更新引用时间超过30分钟的才给重新更新
    Date updateCitedDate = pubCitationsDAO.findPubCitationModifyDate(pubId);
    if (updateCitedDate != null) {
      Date currentTime = new Date();
      Long time = currentTime.getTime() - updateCitedDate.getTime() - 30 * 60 * 1000;
      if (time < 0) {
        return true;
      }
    }
    String citeSourceUrl = "";
    Map<String, Object> resultMap = new HashMap<String, Object>();
    // 3. 拼接更新引用url，处理成果ID，人员ID
    String url = this.rebuildCitedURL(pubInfo);
    if (StringUtils.isBlank(url)) {
      return true;
    }
    String pid = String.valueOf(pubId);
    String pubIdStr = String.valueOf(pubId);
    pid = ServiceUtil.encodeToDes3(pid);
    citeSourceUrl = "$" + url;
    resultMap.put("pubIds", pid);
    resultMap.put("pubId", pubIdStr);
    resultMap.put("psnId", psnId);
    resultMap.put("citeUrl", citeSourceUrl);
    resultMap.put("srcDbId", pubInfo.getSrcDbId());
    mapList.add(resultMap);
    return false;
  }

  // 拼接更新引用url
  protected String rebuildCitedURL(PubInfo pubInfo) {
    String url = "";
    String pattern = "DOI.*";
    String cnkiPattern = "CNKI.*";
    String patternSci = "10.*";
    String title = null;
    // 优先拿srcId
    String ISIId = pubSituationDAO.findPubISIId(pubInfo.getPubId());
    // ISIId = "000439574700058";
    if (StringUtils.isNotBlank(ISIId)) {
      url += "UT=" + ISIId;
      return url;
    }
    // 没有srcId的尝试拿doi，且判断doi的正确性
    if (StringUtils.isNotBlank(pubInfo.getDOI()) && Pattern.matches(patternSci, pubInfo.getDOI())) {
      url += "DOI=(" + pubInfo.getDOI() + ")";
      return url;
    }
    // 取英文title
    if (StringUtils.isNotBlank(pubInfo.getTitle()) && isNotContainChinese(pubInfo.getTitle())) {
      title = pubInfo.getTitle();
    }
    if (StringUtils.isNotBlank(pubInfo.getDOI()) && Pattern.matches(pattern, pubInfo.getDOI().toUpperCase())) {
      String doi = pubInfo.getDOI();
      int index = doi.indexOf("10.");
      if (index == -1) {
        if (StringUtils.isNotBlank(title)) {
          url += "TI=(" + title + ")";
        }
      } else {
        doi = doi.substring(index);
        url += "DOI=(" + doi + ")";
      }
      return url;
    }
    boolean isCnkiMatch = (StringUtils.isNotBlank(title) && StringUtils.isNotBlank(pubInfo.getDOI())
        && Pattern.matches(cnkiPattern, pubInfo.getDOI().toUpperCase()));
    if (isCnkiMatch && StringUtils.isNotBlank(title)) {
      url += "TI=(" + title + ")";
      return url;
    }
    if (StringUtils.isBlank(url) && StringUtils.isNotBlank(title)) {
      url += "TI=(" + title + ")";
    }
    return url;
  }

  // 字符串是否包含中文字符
  protected boolean isNotContainChinese(String str) {
    Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
    Matcher m = p.matcher(str);
    if (m.find()) {
      return false;
    }
    return true;
  }

  @Override
  public String updatePubCited(PubCitedVo vo) throws ServiceException {
    String result = "{\"status\":\"SUCCESS\"}";
    try {
      if (StringUtils.isNotBlank(vo.getDes3PubId()) && !NumberUtils.isNullOrZero(vo.getPsnId())) {
        // 解析成果引用数据xml
        String citedXml = StringEscapeUtils.unescapeXml(vo.getCitedXml());
        citedXml = URLDecoder.decode(citedXml, "UTF-8");
        if (citedXml.contains("&") && !citedXml.contains("&amp;")) {
          citedXml = citedXml.replace("&", "&amp;");
        }
        Document doc = DocumentHelper.parseText(citedXml);
        List nodes = doc.selectNodes("//data/tc");
        // 解析加密的成果ID
        String[] des3PubIds = vo.getDes3PubId().split(",");
        // 遍历待更新的成果ID
        for (int i = 0; i < des3PubIds.length; i++) {
          Element em = (Element) nodes.get(i);
          Integer citedTimes = IrisNumberUtils.createInteger(em.attributeValue("count"));
          if (citedTimes != null && citedTimes > 0) {
            // 先更新当前的成果
            result = this.updateSnsPubCited(des3PubIds[i], citedTimes, vo);
            // 更新当前成果的引用次数的同时，也要更新关联的基准库成果以及个人库成果
            Long pdwhPubId =
                pubPdwhSnsRelationService.getPdwhIdBySnsId(Long.valueOf(Des3Utils.decodeFromDes3(des3PubIds[i])));
            if (pdwhPubId != null) {// 没有关联的基准库成果
              // 先判断成果是否已删除
              if (pubExists(pdwhPubId)) {
                // 1.更新基准库引用次数
                this.updatePdwhPubcited(pdwhPubId, citedTimes);
                // 1.更新基准库引用次数同时需要更新solr
                this.updatePubInfoInSolr(pdwhPubId);
              }
              List<Long> snsPubIds = pubPdwhSnsRelationService.getSnsPubIdsByPdwhId(pdwhPubId,
                  Long.valueOf(Des3Utils.decodeFromDes3(des3PubIds[i])));
              // 2.更新个人库引用次数
              for (Long snsPubId : snsPubIds) {
                this.updateSnsPubCited(Des3Utils.encodeToDes3(snsPubId.toString()), citedTimes, null);
              }
            }
          }
        }
      }
    } catch (Exception e) {
      result = "{\"status\":\"error\"}";
      logger.error("更新成果引用数出错， psnId = " + vo.getPsnId(), e);
      throw new ServiceException(e);
    }
    return result;
  }

  private boolean pubExists(Long pdwhPubId) {
    return pubPdwhDAO.pubExists(pdwhPubId);
  }

  private void updatePubInfoInSolr(Long pdwhPubId) {
    // 访问V8pub系统接口更新sorl索引
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("des3PubId", Des3Utils.encodeToDes3(pdwhPubId.toString()));
    String SERVER_URL = scmDomain + V8pubQueryPubConst.PDWHUPDATESORL_URL;
    restTemplate.postForObject(SERVER_URL, params, String.class);
  }

  private String updateSnsPubCited(String des3PubId, Integer citedTimes, PubCitedVo vo) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("pubHandlerName", PubHandlerEnum.UPDATE_SNS_CITATIONS.getName());
    params.put("des3PubId", des3PubId);
    params.put("citations", citedTimes);
    if (vo == null) {
      PubSnsPO pubSns = pubSnsDAO.getPubsnsById(Long.valueOf(Des3Utils.decodeFromDes3(des3PubId)));
      if (pubSns == null) {
        return null;
      }
      params.put("des3PsnId", Des3Utils.encodeToDes3(pubSns.getCreatePsnId().toString()));
      params.put("srcDbId", null);
      params.put("citedType", 0);

    } else {
      params.put("des3PsnId", Des3Utils.encodeToDes3(vo.getPsnId().toString()));
      params.put("srcDbId", vo.getSrcDbId());
      params.put("citedType", 1);
    }
    return RestTemplateUtils.post(restTemplate, scmDomain + V8pubQueryPubConst.PUBHANDLER_URL,
        JacksonUtils.mapToJsonStr(params));
  }

  private void updatePdwhPubcited(Long pdwhPubId, Integer citedTimes) {
    Map<String, Object> map = new HashMap<String, Object>();
    PubPdwhDetailDOM pubDetail = pubPdwhDetailDAO.findById(pdwhPubId);
    map.put("des3PubId", Des3Utils.encodeToDes3(pdwhPubId.toString()));
    map.put("citations", citedTimes);
    map.put("srcDbId", pubDetail.getSrcDbId());
    map.put("citedType", 0);
    map.put("pubHandlerName", "updatePdwhCitationsHandler");
    RestTemplateUtils.post(restTemplate, scmDomain + V8pubQueryPubConst.PUBHANDLER_URL, JacksonUtils.mapToJsonStr(map));
  }

}
