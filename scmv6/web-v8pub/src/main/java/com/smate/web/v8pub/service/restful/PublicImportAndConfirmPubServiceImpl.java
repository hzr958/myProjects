package com.smate.web.v8pub.service.restful;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.pub.consts.V8pubQueryPubConst;
import com.smate.core.base.pub.enums.PubSnsRecordFromEnum;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.v8pub.dao.pdwh.PubPdwhDetailDAO;
import com.smate.web.v8pub.dom.AwardsInfoBean;
import com.smate.web.v8pub.dom.BookInfoBean;
import com.smate.web.v8pub.dom.ConferencePaperBean;
import com.smate.web.v8pub.dom.JournalInfoBean;
import com.smate.web.v8pub.dom.OtherInfoBean;
import com.smate.web.v8pub.dom.PatentInfoBean;
import com.smate.web.v8pub.dom.PubSituationBean;
import com.smate.web.v8pub.dom.PubTypeInfoBean;
import com.smate.web.v8pub.dom.ThesisInfoBean;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;
import com.smate.web.v8pub.dto.PubFulltextDTO;
import com.smate.web.v8pub.enums.PubGenreConstants;
import com.smate.web.v8pub.enums.PubHandlerEnum;
import com.smate.web.v8pub.po.pdwh.PdwhPubFullTextPO;
import com.smate.web.v8pub.service.pdwh.PdwhPubFullTextService;

@Service("publicImportAndConfirmPubService")
@Transactional(rollbackFor = Exception.class)
public class PublicImportAndConfirmPubServiceImpl implements PublicImportAndConfirmPubService {
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Value("${domainscm}")
  private String scmDomain;
  @Autowired
  private PdwhPubFullTextService pdwhPubFullTextService;
  @Autowired
  private PubPdwhDetailDAO pubPdwhDetailDAO;

  @Override
  public String importAndConfirmPdwhPub(Long pdwhPubId, Long psnId, Integer pubType,
      Set<PubSituationBean> situationList, Integer isPubConfirm) {
    PubPdwhDetailDOM pdwhDetail = pubPdwhDetailDAO.findById(pdwhPubId);
    if (pdwhDetail == null) {
      return null;
    }
    // 构建成果保存对象
    Map<String, Object> pub = buildPub(pdwhDetail, psnId, pubType, situationList, isPubConfirm);
    // 调用保存个人成果接口
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
    String pubJson = JacksonUtils.mapToJsonStr(pub);
    // 不需要进行xss过滤，因为基准库数据进来的时候就已经经过xss过滤了，存在没有过滤的数据，是其他问题，这里不需要进行再过滤
    // pubJson = XssUtils.transferJson(pubJson);
    HttpEntity<String> entity = new HttpEntity<String>(pubJson, headers);
    String saveUrl = scmDomain + V8pubQueryPubConst.PUBHANDLER_URL;
    String result = restTemplate.postForObject(saveUrl, entity, String.class);
    return result;
  }

  private Map<String, Object> buildPub(PubPdwhDetailDOM pdwhDetail, Long psnId, Integer pubType,
      Set<PubSituationBean> situationList, Integer isPubConfirm) {
    Map<String, Object> map = new HashMap<>();
    Long pdwhPubId = pdwhDetail.getPubId();
    map.put("pubHandlerName", PubHandlerEnum.SAVE_SNS.getName());
    map.put("des3PdwhPubId", Des3Utils.encodeToDes3(pdwhPubId + ""));
    map.put("des3PsnId", Des3Utils.encodeToDes3(String.valueOf(psnId)));
    map.put("pubGenre", PubGenreConstants.PDWH_SNS_PUB);
    map.put("title", pdwhDetail.getTitle());
    map.put("isPubConfirm", isPubConfirm);
    map.put("publishDate", pdwhDetail.getPublishDate());
    map.put("countryId", pdwhDetail.getCountryId());
    map.put("fundInfo", pdwhDetail.getFundInfo());
    map.put("citations", pdwhDetail.getCitations());
    map.put("doi", pdwhDetail.getDoi());
    map.put("summary", pdwhDetail.getSummary());
    map.put("keywords", pdwhDetail.getKeywords());
    map.put("srcFulltextUrl", pdwhDetail.getSrcFulltextUrl());
    map.put("pubType", pubType == null ? pdwhDetail.getPubType() : pubType);
    map.put("recordFrom", PubSnsRecordFromEnum.IMPORT_FROM_PDWH);
    map.put("organization", pdwhDetail.getOrganization());
    map.put("sourceUrl", pdwhDetail.getSourceUrl());
    map.put("citedUrl", pdwhDetail.getCitedUrl());
    map.put("permission", 7);
    map.put("sourceId", pdwhDetail.getSourceId());
    map.put("srcDbId", pdwhDetail.getSrcDbId());
    map.put("dbId", pdwhDetail.getSrcDbId());
    map.put("fullText", constructFullText(pdwhPubId));
    map.put("pubTypeInfo", buildPubTypeInfo(pdwhDetail, pubType));
    map.put("HCP", pdwhDetail.isHCP() ? 1 : 0);
    map.put("HP", pdwhDetail.isHP() ? 1 : 0);
    map.put("OA", pdwhDetail.getOA());
    // 作者名不需要再传过去，基准库的members 直接到 个人库的members
    // map.put("authorNames", pdwhDetail.getAuthorNames());
    map.put("members", pdwhDetail.getMembers());
    map.put("situations", situationList == null ? pdwhDetail.getSituations() : situationList);
    return map;
  }

  private PubTypeInfoBean buildPubTypeInfo(PubPdwhDetailDOM pdwhDetail, Integer pubType) {
    if (pubType != null && !pubType.equals(pdwhDetail.getPubType())) {
      switch (pubType) {
        case 1:
          return new AwardsInfoBean();
        case 2:
          return new BookInfoBean();
        case 3:
          return new ConferencePaperBean();
        case 4:
          return new JournalInfoBean();
        case 5:
          return new PatentInfoBean();
        case 7:
          return new OtherInfoBean();
        case 8:
          return new ThesisInfoBean();
        case 10:
          return new BookInfoBean();
      }
    }
    return pdwhDetail.getTypeInfo();
  }

  private PubFulltextDTO constructFullText(Long pdwhPubId) {
    PdwhPubFullTextPO p = pdwhPubFullTextService.getPdwhPubfulltext(pdwhPubId);
    if (p != null) {
      PubFulltextDTO pubFulltext = new PubFulltextDTO();
      pubFulltext.setDes3fileId(Des3Utils.encodeToDes3(String.valueOf(p.getFileId())));
      pubFulltext.setFileName(p.getFileName());
      pubFulltext.setPermission(p.getPermission());
      return pubFulltext;
    }
    return null;
  }
}
