package com.smate.web.v8pub.service.searchimport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.psn.consts.PsnCnfConst;
import com.smate.core.base.pub.consts.V8pubQueryPubConst;
import com.smate.core.base.pub.enums.PubSnsStatusEnum;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.v8pub.dom.sns.PubSnsDetailDOM;
import com.smate.web.v8pub.dto.PubFulltextDTO;
import com.smate.web.v8pub.enums.PubHandlerEnum;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.sns.PubAccessoryPO;
import com.smate.web.v8pub.po.sns.PubFullTextPO;
import com.smate.web.v8pub.po.sns.PubScienceAreaPO;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.service.match.PubAuthorMatchService;
import com.smate.web.v8pub.service.pubduplicate.PubCheckDuplicateService;
import com.smate.web.v8pub.service.sns.PubAccessoryService;
import com.smate.web.v8pub.service.sns.PubFullTextService;
import com.smate.web.v8pub.service.sns.PubScienceAreaService;
import com.smate.web.v8pub.service.sns.PubSnsDetailService;
import com.smate.web.v8pub.service.sns.PubSnsService;
import com.smate.web.v8pub.service.statistic.DownloadCollectStatisticsService;
import com.smate.web.v8pub.service.statistic.DownloadCollectStatisticsServiceImpl;
import com.smate.web.v8pub.utils.RestTemplateUtils;
import com.smate.web.v8pub.vo.searchimport.PubImportVO;
import com.smate.web.v8pub.vo.sns.PubDupResultVO;

/**
 * 导入他人成果服务类
 * 
 * @author aijiangbin
 * @date 2018年8月30日
 */
@Service("importOtherPubService")
@Transactional(rollbackFor = Exception.class)
public class ImportOtherPubServiceImpl implements ImportOtherPubService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Value("${domainscm}")
  private String domainScm;

  @Autowired
  private PubSnsService pubSnsService;
  @Autowired
  private PubFullTextService pubFullTextService;
  @Autowired
  private PubAccessoryService pubAccessoryService;
  @Autowired
  private PubSnsDetailService pubSnsDetailService;
  @Autowired
  private PubAuthorMatchService pubAuthorMatchService;
  @Autowired
  private PubScienceAreaService pubScienceAreaService;
  @Autowired
  private PubCheckDuplicateService pubCheckDuplicateService;
  @Autowired
  private DownloadCollectStatisticsService downloadCollectStatisticsService;


  @Override
  public Map<String, String> importPub(PubImportVO importVo) throws ServiceException {
    // 目前只导人一条成果
    Map<String, String> result = new HashMap<>();
    if (importVo.getArticleType() != 1) {
      return result;
    }
    List<Map<String, String>> paramList = JacksonUtils.jsonListUnSerializer(importVo.getPubJsonParams());
    for (Map<String, String> map : paramList) {
      Long pubId = buildPubId(map);
      Assert.notNull(pubId, "pubId为空！");
      PubSnsPO pubSnsPO = pubSnsService.get(pubId);
      Assert.notNull(pubSnsPO, "pubSnsPO为空！");
      if (PubSnsStatusEnum.DELETED.equals(pubSnsPO.getStatus())) {
        importVo.setPubImportType("isDel");
        result.put("result", "isDel");
        return result;
      }
      PubSnsDetailDOM pubSnsDOM = pubSnsDetailService.get(pubId);
      Assert.notNull(pubSnsDOM, "pubSnsDOM为空！");
      PubDupResultVO resVo = pubCheckDuplicateService.checkSnsPubDuplicate(pubSnsDOM, importVo.getCurrentPsnId());
      if (NumberUtils.isNotNullOrZero(resVo.getDupPubId())) {
        result.put("result", "dup");
        return result;
      }
      importVo.setSnsPubId(pubId);
      importVo.setOwnerPsnId(pubSnsPO.getCreatePsnId());
      importVo.setUpdateMark(pubSnsPO.getUpdateMark());
      importVo.setRecordFrom(pubSnsPO.getRecordFrom());
      result = importPubToMyLib(pubSnsDOM, importVo);
    }
    return result;
  }

  private Long buildPubId(Map<String, String> map) {
    Long pubId = NumberUtils.parseLong(map.get("pubId"));
    if (NumberUtils.isNullOrZero(pubId)) {
      pubId = NumberUtils.parseLong(Des3Utils.decodeFromDes3(map.get("des3PubId")));
    }
    return pubId;
  }

  // 导入成果到我的数据库
  @Override
  public Map<String, String> importPubToMyLib(PubSnsDetailDOM pubSnsDOM, PubImportVO importVo) throws ServiceException {
    Map<String, String> returnMap = new HashMap<>();
    // 处理成果数据
    String pubJson = JacksonUtils.mapToJsonStr(buildParamsMap(pubSnsDOM, importVo));
    // 保存成果
    String post = RestTemplateUtils.post(restTemplate, this.domainScm + V8pubQueryPubConst.PUBHANDLER_URL, pubJson);
    Map<String, String> postMap = JacksonUtils.jsonMapUnSerializer(post);
    if ("SUCCESS".equals(postMap.get("status"))) {
      returnMap.put("result", "success");
    } else {
      returnMap.put("result", "error");
    }
    // 导入记录保存
    saveDownloadCollect(importVo);
    return returnMap;
  }

  private void saveDownloadCollect(PubImportVO importVo) {
    try {
      downloadCollectStatisticsService.addRecord(importVo.getOwnerPsnId(), importVo.getSnsPubId(), 1,
          importVo.getCurrentPsnId(), DownloadCollectStatisticsServiceImpl.PUB_IMPORT, null, null);
    } catch (Exception e) {
      logger.error("导入到我的成果库记录保存失败！！", e);
    }
  }

  private Map<String, Object> buildParamsMap(PubSnsDetailDOM pubSnsDOM, PubImportVO importVo) {
    Map<String, Object> paramsMap = new HashMap<>();
    paramsMap.put("pubGenre", 1);
    paramsMap.put("pubHandlerName", PubHandlerEnum.PSNPUB_TO_SNS.getName());
    paramsMap.put("des3PsnId", Des3Utils.encodeToDes3(importVo.getCurrentPsnId() + ""));
    paramsMap.put("srcFulltextUrl", pubSnsDOM.getSrcFulltextUrl());
    paramsMap.put("updateMark", importVo.getUpdateMark());
    paramsMap.put("sourceId", pubSnsDOM.getSourceId());
    paramsMap.put("pubType", pubSnsDOM.getPubType());
    paramsMap.put("recordFrom", importVo.getRecordFrom());
    paramsMap.put("HCP", pubSnsDOM.isHCP() ? 1 : 0);
    paramsMap.put("HP", pubSnsDOM.isHP() ? 1 : 0);
    paramsMap.put("OA", pubSnsDOM.getOA());
    paramsMap.put("members", pubSnsDOM.getMembers());
    paramsMap.put("situations", pubSnsDOM.getSituations());
    paramsMap.put("title", pubSnsDOM.getTitle());
    paramsMap.put("publishDate", pubSnsDOM.getPublishDate());
    paramsMap.put("countryId", pubSnsDOM.getCountryId());
    paramsMap.put("fundInfo", pubSnsDOM.getFundInfo());
    paramsMap.put("citations", pubSnsDOM.getCitations());
    paramsMap.put("doi", pubSnsDOM.getDoi());
    paramsMap.put("summary", pubSnsDOM.getSummary());
    paramsMap.put("keywords", pubSnsDOM.getKeywords());
    paramsMap.put("organization", pubSnsDOM.getOrganization());
    paramsMap.put("sourceUrl", pubSnsDOM.getSourceUrl());
    paramsMap.put("citedUrl", pubSnsDOM.getCitedUrl());
    paramsMap.put("srcDbId", pubSnsDOM.getSrcDbId());
    paramsMap.put("dbId", pubSnsDOM.getSrcDbId());
    paramsMap.put("authorNames", pubSnsDOM.getAuthorNames());
    paramsMap.put("pubTypeInfo", pubSnsDOM.getTypeInfo());

    // 构建全文信息有权限才会构建
    rebuildFulltext(importVo.getSnsPubId(), paramsMap);
    // 构建附件信息，包括权限
    rebuildAccessorys(importVo.getSnsPubId(), paramsMap);
    // 处理成果权限,成果作者包含当前人的姓名则设置为公开，否则设置为隐私
    Integer permission = rebuildPubPermission(importVo.getOwnerPsnId(), pubSnsDOM.getAuthorNames());
    paramsMap.put("permission", permission);
    // 构建科技领域
    rebuildScienceAreas(importVo.getSnsPubId(), paramsMap);
    return paramsMap;
  }

  private void rebuildScienceAreas(Long pubId, Map<String, Object> paramsMap) {
    List<PubScienceAreaPO> areas = pubScienceAreaService.getScienceAreaList(pubId);
    List<Map<String, Object>> scienceAreas = new ArrayList<>();
    if (areas != null && areas.size() > 0) {
      for (PubScienceAreaPO scienceArea : areas) {
        Map<String, Object> map = new HashMap<>();
        map.put("scienceAreaId", scienceArea.getScienceAreaId());
        scienceAreas.add(map);
      }
    }
    paramsMap.put("scienceAreas", scienceAreas);
  }

  // 重构成果权限
  private Integer rebuildPubPermission(Long ownerPsnId, String authorNames) {
    Integer isMatch = pubAuthorMatchService.isMatch(authorNames, ownerPsnId);
    return isMatch == 1 ? PsnCnfConst.ALLOWS : PsnCnfConst.ALLOWS_SELF;
  }


  // 重构全文，
  public void rebuildFulltext(Long pubId, Map<String, Object> paramsMap) {
    PubFullTextPO pubFulltext = pubFullTextService.get(pubId);
    if (pubFulltext == null) {
      return;
    }
    // 全文下载权限，0所有人可下载，1好友可下载，2自己可下载
    Integer permission = pubFulltext.getPermission();
    if (permission == 0) {
      PubFulltextDTO fullText = new PubFulltextDTO();
      fullText.setPermission(pubFulltext.getPermission());
      fullText.setFileName(pubFulltext.getFileName());
      fullText.setSrcFulltextUrl(paramsMap.get("srcFulltextUrl") + "");
      fullText.setThumbnailPath(pubFulltext.getThumbnailPath());
      fullText.setFileId(pubFulltext.getFileId());
      fullText.setDes3fileId(Des3Utils.encodeToDes3(pubFulltext.getFileId() + ""));
      paramsMap.put("fullText", fullText);
    } else {
      paramsMap.put("srcFulltextUrl", "");
    }
  }

  // 重构附件
  public void rebuildAccessorys(Long pubId, Map<String, Object> paramsMap) {
    List<PubAccessoryPO> accessorys = pubAccessoryService.findByPubId(pubId);
    if (CollectionUtils.isNotEmpty(accessorys)) {
      // 附件下载权限，0所有人可下载，1好友可下载，2自己可下载
      accessorys =
          accessorys.stream().filter(a -> new Integer(0).equals(a.getPermission())).collect(Collectors.toList());
      paramsMap.put("accessorys", accessorys);
    }
  }

}
