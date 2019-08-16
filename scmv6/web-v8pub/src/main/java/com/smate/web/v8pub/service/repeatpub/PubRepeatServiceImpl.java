package com.smate.web.v8pub.service.repeatpub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.file.enums.FileTypeEnum;
import com.smate.core.base.file.service.FileDownloadUrlService;
import com.smate.core.base.pub.consts.V8pubQueryPubConst;
import com.smate.core.base.pub.dao.PubIndexUrlDao;
import com.smate.core.base.pub.model.PubIndexUrl;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.pubxml.ImportPubXmlUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.v8pub.enums.PubGenreConstants;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.service.sns.PubFullTextService;
import com.smate.web.v8pub.service.sns.PubSnsService;
import com.smate.web.v8pub.vo.PubDuplicateVO;
import com.smate.web.v8pub.vo.RepeatPubInfo;

/**
 * 新增成果查重服务类
 * 
 * @author YJ
 *
 *         2018年9月15日
 */

@Service("pubRepeatService")
@Transactional(rollbackFor = Exception.class)
public class PubRepeatServiceImpl implements PubRepeatService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubSnsService pubSnsService;
  @Autowired
  private PubFullTextService pubFullTextService;
  @Autowired
  private PubIndexUrlDao pubIndexUrlDao;
  @Autowired
  private FileDownloadUrlService fileDownUrlService;
  @Value("${domainscm}")
  private String domainscm;

  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;


  @SuppressWarnings("unchecked")
  @Override
  public List<Long> listPubIdByCheckDup(PubDuplicateVO dupVO) throws ServiceException {
    String SERVER_URL = domainscm + V8pubQueryPubConst.PUB_DUPCHECK_URL;

    // 设置请求头部
    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
    // 构建成果保存对象
    Map<String, Object> map = buildCheckPub(dupVO);
    HttpEntity<String> requestEntity = new HttpEntity<String>(JacksonUtils.jsonObjectSerializer(map), requestHeaders);
    Map<String, Object> res = (Map<String, Object>) restTemplate.postForObject(SERVER_URL, requestEntity, Object.class);
    if (!CollectionUtils.isEmpty(res) && "SUCCESS".equals(res.get("status"))) {
      String msgList = (String) res.get("msgList");
      if (StringUtils.isNotEmpty(msgList)) {
        List<Long> dupPubIds = ImportPubXmlUtils.parsePubIds(msgList);
        // 需要过滤当前需要编辑的成果的pubId
        if (StringUtils.isNotEmpty(dupVO.getDes3PubId())) {
          Long currentPubId = Long.valueOf(Des3Utils.decodeFromDes3(dupVO.getDes3PubId()));
          dupPubIds.remove(currentPubId);
        }
        return dupPubIds;
      }
    }
    return null;
  }


  private Map<String, Object> buildCheckPub(PubDuplicateVO dupVO) {
    Map<String, Object> map = new HashMap<>();
    map.put("des3PsnId", Des3Utils.encodeToDes3(String.valueOf(dupVO.getPsnId())));
    map.put("des3PubId", dupVO.getDes3PubId());
    map.put("pubGener", 1);
    map.put("title", dupVO.getTitle());
    map.put("pubYear", StringUtils.substring(dupVO.getPublishDate(), 0, 4));
    map.put("pubType", dupVO.getPubType());
    map.put("doi", dupVO.getDoi());
    map.put("sourceId", dupVO.getSourceId());
    map.put("srcDbId", dupVO.getSrcDbId());
    map.put("applicationNo", dupVO.getApplicationNo());
    map.put("publicationOpenNo", dupVO.getPublicationOpenNo());
    map.put("standardNo", dupVO.getStandardNo());
    map.put("registerNo", dupVO.getRegisterNo());
    return map;
  }

  @Override
  public List<RepeatPubInfo> listRepeatPubDetail(List<Long> dupPubIds) throws ServiceException {
    RepeatPubInfo repeatPub = null;
    List<RepeatPubInfo> repeatPubInfoList = new ArrayList<>();
    if (dupPubIds != null && dupPubIds.size() > 0) {
      for (Long pubId : dupPubIds) {
        PubSnsPO pubSnsPO = pubSnsService.get(pubId);
        if (pubSnsPO != null) {
          repeatPub = new RepeatPubInfo();
          repeatPub.setTitle(pubSnsPO.getTitle());
          repeatPub.setUpdateDate(pubSnsPO.getGmtModified());
          repeatPub.setDes3PubId(Des3Utils.encodeToDes3(pubSnsPO.getPubId() + ""));
          repeatPub.setBriefDesc(pubSnsPO.getBriefDesc());
          if (StringUtils.isNotBlank(pubSnsPO.getAuthorNames())) {
            repeatPub.setAuthorNamesNoTag(pubSnsPO.getAuthorNames().replaceAll("<([^>]*)>", ""));
          }
          repeatPub.setAuthorNames(pubSnsPO.getAuthorNames());
          // 成果全文图像资源地址
          String fullTextImagePath = pubFullTextService.getFulltextImageUrl(pubSnsPO.getPubId());
          if (StringUtils.isBlank(fullTextImagePath)) {
            repeatPub.setFullTextImagePath("/resmod/images_v5/images2016/file_img.jpg");
          } else {
            repeatPub.setFullTextImagePath(fullTextImagePath);
            // 下载地址
            String downloadUrl = fileDownUrlService.getDownloadUrl(FileTypeEnum.SNS_FULLTEXT, pubSnsPO.getPubId());
            repeatPub.setDownloadUrl(downloadUrl);
          }
          // 成果短地址路径，方便用戶查看成果全文
          PubIndexUrl pubIndexUrl = pubIndexUrlDao.get(pubId);
          if (pubIndexUrl != null && StringUtils.isNotBlank(pubIndexUrl.getPubIndexUrl())) {
            repeatPub.setPubIndexUrl(domainscm + "/" + ShortUrlConst.A_TYPE + "/" + pubIndexUrl.getPubIndexUrl());
          }
          repeatPubInfoList.add(repeatPub);
        }
      }
    }
    return repeatPubInfoList;
  }


  @SuppressWarnings("unchecked")
  @Override
  public Long getPdwhPubIdByCheckDup(String DOI) throws ServiceException {
    String SERVER_URL = domainscm + V8pubQueryPubConst.PUB_DUPCHECK_URL;
    Long pdwhPubId = null;
    try {
      if (StringUtils.isNotBlank(DOI)) {
        // 构造查重参数
        Map<String, Object> params = new HashMap<>();
        params.put("pubGener", PubGenreConstants.PDWH_SNS_PUB);
        params.put("doi", DOI);
        // 调用接口进行查重
        // 设置请求头部
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        // 构建成果保存对象
        HttpEntity<String> requestEntity =
            new HttpEntity<String>(JacksonUtils.jsonObjectSerializer(params), requestHeaders);
        Map<String, Object> res =
            (Map<String, Object>) restTemplate.postForObject(SERVER_URL, requestEntity, Object.class);
        if (!CollectionUtils.isEmpty(res) && "SUCCESS".equals(res.get("status"))) {
          pdwhPubId = NumberUtils.toLong((String) res.get("msg"));
        }
      }
      return pdwhPubId;
    } catch (Exception e) {
      logger.error("通过DOI查重基准库成果出错！DOI={}", DOI, e);
      throw new ServiceException(e);
    }
  }

}
