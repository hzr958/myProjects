package com.smate.web.v8pub.service.restful;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.pub.consts.V8pubQueryPubConst;
import com.smate.core.base.pub.util.PubDetailVoUtil;
import com.smate.core.base.utils.cache.SnsCacheService;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.v8pub.dao.pdwh.PubPdwhDetailDAO;
import com.smate.web.v8pub.dom.PatentInfoBean;
import com.smate.web.v8pub.dom.SoftwareCopyrightBean;
import com.smate.web.v8pub.dom.StandardInfoBean;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.service.fileimport.extract.PubFileInfo;
import com.smate.web.v8pub.vo.PendingImportPubVO;

@Service("publicPubDupCheckService")
@Transactional(rollbackFor = Exception.class)
public class PublicPubDupCheckServiceImpl implements PublicPubDupCheckService {
  private final String SEARCHIMPORTPUBCACHE = "searchImportPubCache";
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Value("${domainscm}")
  private String scmDomain;
  @Autowired
  private SnsCacheService cacheService;
  @Resource
  private PubPdwhDetailDAO pubPdwhDetailDAO;

  @Override
  public String getPdwhPubDupcheckStatus(Long psnId, Long pdwhPubId) {
    PubPdwhDetailDOM detailDom = pubPdwhDetailDAO.findById(pdwhPubId);
    String publishDate = detailDom.getPublishDate();
    Map<String, Object> dupMap = new HashMap<String, Object>();
    dupMap.put("pubGener", 1);// 成果大类别，1代表个人成果 2代表群组成果 3代表基准库成果
    dupMap.put("title", detailDom.getTitle());
    dupMap.put("pubType", detailDom.getPubType());
    dupMap.put("doi", detailDom.getDoi());
    dupMap.put("srcDbId", detailDom.getSrcDbId());
    dupMap.put("sourceId", detailDom.getSourceId());
    dupMap.put("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
    if (5 == detailDom.getPubType()) {
      PatentInfoBean infoBean = (PatentInfoBean) detailDom.getTypeInfo();
      if (infoBean != null) {
        dupMap.put("applicationNo", infoBean.getApplicationNo());
        dupMap.put("publicationOpenNo", infoBean.getPublicationOpenNo());
        Integer status = infoBean.getStatus();
        if (status != null && status == 1) {
          // 授权状态
          publishDate = infoBean.getStartDate();
        } else {
          // 申请状态
          publishDate = infoBean.getApplicationDate();
        }
      }
    }
    if (12 == detailDom.getPubType()) {
      StandardInfoBean infoBean = (StandardInfoBean) detailDom.getTypeInfo();
      if (infoBean != null) {
        dupMap.put("standardNo", infoBean.getStandardNo());
      }
    }
    if (13 == detailDom.getPubType()) {
      SoftwareCopyrightBean infoBean = (SoftwareCopyrightBean) detailDom.getTypeInfo();
      if (infoBean != null) {
        dupMap.put("registerNo", infoBean.getRegisterNo());
      }
    }
    dupMap.put("pubYear", PubDetailVoUtil.parseDateForYear(publishDate));
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
    HttpEntity<String> entity = new HttpEntity<String>(JacksonUtils.mapToJsonStr(dupMap), headers);
    String dupUrl = scmDomain + V8pubQueryPubConst.PUB_DUPCHECK_URL;
    String result = restTemplate.postForObject(dupUrl, entity, String.class);
    return result;
  }

  @Override
  public String getSnsPubDupcheckStatus(Long psnId, Long snsPubId) {
    // TODO Auto-generated method stub
    return null;
  }

  @SuppressWarnings("rawtypes")
  @Override
  public String getPubDupcheckStatus(Map map) {
    Map<String, Object> dupMap = new HashMap<String, Object>();
    dupMap.put("pubGener", 3);// 成果大类别，1代表个人成果 2代表群组成果 3代表基准库成果
    dupMap.put("title", map.get("title"));
    dupMap.put("pubYear", map.get("pubYear"));
    dupMap.put("pubType", map.get("pubType"));
    dupMap.put("doi", map.get("doi"));
    dupMap.put("srcDbId", map.get("srcDbId"));
    dupMap.put("sourceId", map.get("sourceId"));
    dupMap.put("applicationNo", map.get("applicationNo"));
    dupMap.put("publicationOpenNo", map.get("publicationOpenNo"));
    dupMap.put("standardNo", map.get("standardNo"));
    dupMap.put("registerNo", map.get("registerNo"));
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
    HttpEntity<String> entity = new HttpEntity<String>(JacksonUtils.mapToJsonStr(dupMap), headers);
    String dupUrl = scmDomain + V8pubQueryPubConst.PUB_DUPCHECK_URL;
    String result = restTemplate.postForObject(dupUrl, entity, String.class);
    return result;
  }

  @Override
  public String dupByPubType(Long psnId, Long pubId, Integer pubType) {
    PubPdwhDetailDOM detailDom = pubPdwhDetailDAO.findById(pubId);
    String publishDate = detailDom.getPublishDate();
    Map<String, Object> dupMap = new HashMap<String, Object>();
    dupMap.put("pubGener", 1);
    dupMap.put("title", detailDom.getTitle());
    dupMap.put("pubType", pubType);
    dupMap.put("doi", detailDom.getDoi());
    dupMap.put("srcDbId", detailDom.getSrcDbId());
    dupMap.put("sourceId", detailDom.getSourceId());
    dupMap.put("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
    if (5 == pubType && pubType.equals(detailDom.getPubType())) {
      PatentInfoBean infoBean = (PatentInfoBean) detailDom.getTypeInfo();
      if (infoBean != null) {
        dupMap.put("applicationNo", infoBean.getApplicationNo());
        dupMap.put("publicationOpenNo", infoBean.getPublicationOpenNo());
        Integer status = infoBean.getStatus();
        if (status != null && status == 1) {
          // 授权状态
          publishDate = infoBean.getStartDate();
        } else {
          // 申请状态
          publishDate = infoBean.getApplicationDate();
        }
      }
    }
    if (12 == detailDom.getPubType()) {
      StandardInfoBean infoBean = (StandardInfoBean) detailDom.getTypeInfo();
      if (infoBean != null) {
        dupMap.put("standardNo", infoBean.getStandardNo());
      }
    }
    if (13 == detailDom.getPubType()) {
      SoftwareCopyrightBean infoBean = (SoftwareCopyrightBean) detailDom.getTypeInfo();
      if (infoBean != null) {
        dupMap.put("registerNo", infoBean.getRegisterNo());
      }
    }
    dupMap.put("pubYear", PubDetailVoUtil.parseDateForYear(publishDate));
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
    HttpEntity<String> entity = new HttpEntity<String>(JacksonUtils.mapToJsonStr(dupMap), headers);
    String dupUrl = scmDomain + V8pubQueryPubConst.PUB_DUPCHECK_URL;
    String result = restTemplate.postForObject(dupUrl, entity, String.class);
    return result;
  }

  @SuppressWarnings("rawtypes")
  @Override
  public Map<String, Object> dupByPubType(Long psnId, String cacheKey, Integer pubType, Integer index) {
    Map<String, Object> resultMap = new HashMap<String, Object>();
    if (StringUtils.isBlank(cacheKey)) {
      // 获取缓存数据失败
      resultMap.put("result", "error");
      return resultMap;
    }
    if (pubType == null || index == null) {
      // 获取类型和索引值失败
      resultMap.put("result", "error");
      return resultMap;
    }
    // 根据缓存cacheKey获取缓存数据
    Map<Integer, PendingImportPubVO> cachePub = getImportfileVoInCache(psnId, cacheKey);
    if (cachePub != null) {
      PendingImportPubVO pubInfo = cachePub.get(index - 1);
      if (pubInfo != null) {
        pubInfo.setPubType(pubType);
        String dupResult = getPubDupcheckStatus(pubInfo, psnId);
        Map<Object, Object> dupMap = JacksonUtils.jsonToMap(dupResult);
        if ("SUCCESS".equals(dupMap.get("status").toString())) {
          if (dupMap.get("msg") != null) {
            Long dupPubId = Long.valueOf(dupMap.get("msg").toString());
            resultMap.put("result", Des3Utils.encodeToDes3(dupPubId + ""));
          } else {
            resultMap.put("result", "no_dup");
          }
        } else {
          resultMap.put("result", "no_dup");
        }
      }
    } else {
      resultMap.put("result", "error");
    }
    return resultMap;
  }


  @SuppressWarnings("rawtypes")
  private String getPubDupcheckStatus(PendingImportPubVO pubInfo, Long psnId) {
    String publishDate = pubInfo.getPublishDate();
    Map<String, Object> dupMap = new HashMap<String, Object>();
    dupMap.put("pubGener", 1);
    dupMap.put("title", pubInfo.getTitle());
    dupMap.put("pubType", pubInfo.getPubType());
    dupMap.put("doi", pubInfo.getDoi());
    dupMap.put("srcDbId", pubInfo.getSrcDbId());
    dupMap.put("sourceId", pubInfo.getSourceId());
    dupMap.put("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
    if (5 == pubInfo.getPubType()) {
      PubFileInfo pubFileInfo = pubInfo.getPubFileInfo();
      if (pubFileInfo != null) {
        dupMap.put("applicationNo", pubFileInfo.getApplicationNo());
        dupMap.put("publicationOpenNo", pubFileInfo.getPatentOpenNo());
        String status = pubFileInfo.getPatentStatus();
        if (status != null && status.equals("1")) {
          // 授权状态
          publishDate = pubFileInfo.getStartDate();
        } else {
          // 申请状态
          publishDate = pubFileInfo.getApplicationDate();
        }
      }
    }
    if (12 == pubInfo.getPubType()) {
      PubFileInfo pubFileInfo = pubInfo.getPubFileInfo();
      if (pubFileInfo != null) {
        dupMap.put("standardNo", pubFileInfo.getStandardNo());
      }
    }
    if (13 == pubInfo.getPubType()) {
      PubFileInfo pubFileInfo = pubInfo.getPubFileInfo();
      if (pubFileInfo != null) {
        dupMap.put("registerNo", pubFileInfo.getRegisterNo());
      }
    }
    dupMap.put("pubYear", PubDetailVoUtil.parseDateForYear(publishDate));
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
    HttpEntity<String> entity = new HttpEntity<String>(JacksonUtils.mapToJsonStr(dupMap), headers);
    String dupUrl = scmDomain + V8pubQueryPubConst.PUB_DUPCHECK_URL;
    String result = restTemplate.postForObject(dupUrl, entity, String.class);
    return result;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private Map<Integer, PendingImportPubVO> getImportfileVoInCache(Long psnId, String key) throws ServiceException {
    if (psnId > 0L) {
      return (Map<Integer, PendingImportPubVO>) cacheService.get(SEARCHIMPORTPUBCACHE, key);
    }
    return null;
  }

}
