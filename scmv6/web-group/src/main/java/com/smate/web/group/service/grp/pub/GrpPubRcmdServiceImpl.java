package com.smate.web.group.service.grp.pub;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
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

import com.smate.core.base.file.enums.FileTypeEnum;
import com.smate.core.base.file.service.FileDownloadUrlService;
import com.smate.core.base.pub.consts.V8pubQueryPubConst;
import com.smate.core.base.pub.enums.PubSnsRecordFromEnum;
import com.smate.core.base.pub.util.PubDetailVoUtil;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.group.action.grp.form.GrpPubRcmdForm;
import com.smate.web.group.action.grp.form.GrpPubShowInfo;
import com.smate.web.group.dao.group.pub.PubFulltextDao;
import com.smate.web.group.dao.grp.grpbase.GrpBaseInfoDao;
import com.smate.web.group.dao.grp.pub.GroupPubsDAO;
import com.smate.web.group.dao.grp.pub.GrpPubRcmdDao;
import com.smate.web.group.dao.grp.pub.PubSimpleDao;
import com.smate.web.group.model.group.GrpPubs;
import com.smate.web.group.model.grp.pub.GrpPubRcmd;
import com.smate.web.group.model.grp.pub.PubSimple;

/**
 * 群组成果推荐服务实现类
 * 
 * @author tsz
 *
 */
@Service("grpPubRcmdService")
@Transactional(rollbackFor = Exception.class)
public class GrpPubRcmdServiceImpl implements GrpPubRcmdService {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private GrpPubRcmdDao grpPubRcmdDao;
  @Autowired
  private PubSimpleDao pubSimpleDao;
  @Autowired
  private PubFulltextDao pubFulltextDao;
  @Autowired
  private GrpPubsService grpPubsService;
  @Autowired
  private GrpPubShowInfoService grpPubShowInfoService;
  @Autowired
  private GroupPubsDAO groupPubsDAO;
  @Autowired
  private GrpBaseInfoDao grpBaseInfoDao;
  @Autowired
  private FileDownloadUrlService fileDownloadUrlService;
  @Value("${domainscm}")
  private String domainscm;
  @Autowired
  private RestTemplate restTemplate;

  @Override
  public void getOneRcmdPub(GrpPubRcmdForm form) throws Exception {

    Long pubId = grpPubRcmdDao.getNextGrpPubRcmd(form.getGrpId());
    if (pubId != null) {
      PubSimple pubSimple = pubSimpleDao.get(pubId);
      if (pubSimple != null && pubSimple.getStatus() != 1) {
        // TODO 2018-08
        // form.setPubInfo(grpPubShowInfoService.buildShowPubInfo(pubSimple));
      }
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public void getAllRcmdPub(GrpPubRcmdForm form) throws Exception {
    List<GrpPubRcmd> grpPubs = this.grpPubRcmdDao.findGrpPubsRcmdByGroupId(form.getGrpId(), form.getPage());
    if (grpPubs == null || grpPubs.size() == 0) {
      return;
    }
    List<GrpPubShowInfo> showPubList = new ArrayList<GrpPubShowInfo>();
    Integer counts = 0;
    for (GrpPubRcmd pub : grpPubs) {
      GrpPubShowInfo pubInfo = new GrpPubShowInfo();
      Long pubId = pub.getPubId();
      // List<Map<String, Object>> mapList =
      // this.groupPubsDAO.findGrpPubInfoByPubId(pubId);
      Map<String, Object> pubMap = this.getRcmdPubDetailsFromPdwh(pubId);
      if (pubMap == null || pubMap.size() == 0) {
        continue;
      }
      counts++;
      pubInfo.setPubId(pub.getPubId());
      pubInfo.setAuthors(String.valueOf(pubMap.get("authorNames")));
      pubInfo.setShowBrif(String.valueOf(pubMap.get("briefDesc")));
      pubInfo.setShowTitle(String.valueOf(pubMap.get("title")));
      pubInfo.setPubIndexUrl(String.valueOf(pubMap.get("pubIndexUrl")));
      String fundInfoWithHighlight = StringUtils.trimToEmpty(String.valueOf(pubMap.get("fundInfo")));
      String fundNo = StringUtils.trimToEmpty(this.grpBaseInfoDao.getProjectNo(pub.getGrpId()));
      if (StringUtils.isNotEmpty(fundNo) && StringUtils.isNotEmpty(fundInfoWithHighlight)) {
        try {
          String fundInfoShow = this.highlightFundInfo(fundInfoWithHighlight, fundNo);
          if (StringUtils.isNotEmpty(fundInfoShow)) {
            pubInfo.setFundInfo(fundInfoShow);
            pubInfo.setFundInfoComplete(fundInfoWithHighlight);
          }
        } catch (Exception e) {
          logger.error("获取成果基金信息出错, pdwhPubId =" + pubInfo.getPubId(), e);
        }

      }
      if (pubMap.get("fullText") != null) {
        HashMap<String, String> fullTextMap = (HashMap<String, String>) pubMap.get("fullText");// 获取全文信息
        if (fullTextMap.get("fileId") == null || StringUtils.isEmpty(String.valueOf(fullTextMap.get("fileId")))) {
          pubInfo.setHasFulltext(0);
        } else {
          pubInfo.setFullTextImaUrl((String) pubMap.get("fulltextImageUrl"));
          pubInfo.setHasFulltext(1);
          pubInfo.setFullTextUrl(fileDownloadUrlService.getDownloadUrl(FileTypeEnum.PDWH_FULLTEXT, pub.getPubId()));
        }
      } else {
        pubInfo.setHasFulltext(0);
      }
      showPubList.add(pubInfo);
    }
    form.getPage().setResult(showPubList);
    // form.getPage().setTotalCount(counts);
  }

  @Override
  public void optionGrpPubRcmd(GrpPubRcmdForm form) throws Exception {
    if (StringUtils.isNoneBlank(form.getPubIds())) {
      String[] pubIdStrs = form.getPubIds().split(",");
      for (String pubIdStr : pubIdStrs) {
        if (NumberUtils.isNumber(pubIdStr)) {
          Long pubId = Long.parseLong(pubIdStr);
          GrpPubRcmd grpPubRcmd = grpPubRcmdDao.getGrpPubRcmd(pubId, form.getGrpId());
          if (grpPubRcmd != null) {
            if (form.getOptionType() == 1) {
              grpPubRcmd.setStatus(1);
              // 添加群组成果关联记录
              GrpPubs grpPubs = new GrpPubs();
              grpPubs.setGrpId(form.getGrpId());
              grpPubs.setCreatePsnId(form.getPsnId());
              grpPubs.setPubId(pubId);
              grpPubs.setIsProjectPub(1);
              grpPubsService.saveGrpPubs(grpPubs);
            } else if (form.getOptionType() == 2) {
              // 忽略
              grpPubRcmd.setStatus(9);
            }
            grpPubRcmd.setUpdateDate(new Date());
            grpPubRcmd.setUpdatePsnId(form.getPsnId());
          }
        }
      }
    }
  }

  @Override
  public Long getPendingConfirmedCount(Long grpId) {

    return grpPubRcmdDao.getPendingConfirmedCount(grpId);
  }

  @Override
  public Map<String, Object> getRcmdPubDetailsFromPdwh(Long pubId) throws Exception {
    String SERVER_URL = this.domainscm + V8pubQueryPubConst.QUERY_PUB_DETAIL_URL;
    Map<String, Object> rsMap = new HashMap<String, Object>();
    try {
      Map<String, Object> map = new HashMap<>();
      map.put("des3PubId", Des3Utils.encodeToDes3(pubId.toString()));
      map.put("serviceType", V8pubQueryPubConst.PDWH_PUB);
      // 设置请求头部
      HttpHeaders requestHeaders = new HttpHeaders();
      requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
      HttpEntity<String> requestEntity = new HttpEntity<String>(JacksonUtils.jsonObjectSerializer(map), requestHeaders);
      rsMap = (Map<String, Object>) restTemplate.postForObject(SERVER_URL, requestEntity, Object.class);
    } catch (Exception e) {
      logger.error("获取群组推荐成果详情信息出错,pubId=" + pubId, e);
    }
    return rsMap;
  }

  @Override
  public void rejectGrpPubRcmd(Long grpId, Long pubId, Long psnId) throws Exception {
    if (pubId == null || grpId == null || psnId == null) {
      return;
    }
    GrpPubRcmd grpPubRcmd = grpPubRcmdDao.getGrpPubRcmd(pubId, grpId);
    if (grpPubRcmd != null) {
      grpPubRcmd.setUpdateDate(new Date());
      grpPubRcmd.setStatus(9);
      grpPubRcmd.setUpdatePsnId(psnId);
      this.grpPubRcmdDao.save(grpPubRcmd);
    }
  }

  @Override
  public Long acceptGrpPubRcmd(Long grpId, Long pdwhpubId, Long psnId) throws Exception {
    Long snsPubId = this.grpImportPdwhPub(grpId, pdwhpubId, psnId);
    // 不需要新建立关键词，更新状态即可
    GrpPubRcmd grpPubRcmd = grpPubRcmdDao.getGrpPubRcmd(pdwhpubId, grpId);
    if (grpPubRcmd != null) {
      grpPubRcmd.setUpdateDate(new Date());
      grpPubRcmd.setStatus(1);
      grpPubRcmd.setUpdatePsnId(psnId);
      this.grpPubRcmdDao.save(grpPubRcmd);
    }
    return snsPubId;
  }

  public Long grpImportPdwhPub(Long grpId, Long pubId, Long psnId) throws Exception {
    if (pubId == null || grpId == null || psnId == null) {
      return null;
    }
    Map<String, Object> pubMap = this.getRcmdPubDetailsFromPdwh(pubId);
    if (pubMap == null || pubMap.size() == 0) {
      return null;
    }
    pubMap.put("pubHandlerName", "saveSnsPubHandler");
    pubMap.put("des3PdwhPubId", Des3Utils.encodeToDes3(pubId + ""));
    pubMap.put("des3PsnId", Des3Utils.encodeToDes3(String.valueOf(psnId)));
    pubMap.put("pubGenre", 2); // 设置为群组成果
    pubMap.put("grpId", grpId);
    pubMap.put("isPubConfirm", 1);// 设置这个操作为成果认领
    pubMap.put("des3GrpId", Des3Utils.encodeToDes3(String.valueOf(grpId)));
    pubMap.put("recordFrom", PubSnsRecordFromEnum.IMPORT_FROM_PDWH);// 设置为从基准库导入
    pubMap.put("isProjectPub", 1);// 群组成果
    String dupResult = this.getGroupDupPubs(pubMap, grpId, pubId, psnId);
    Map dupResultMap = JacksonUtils.jsonToMap(dupResult);
    Long newPubId = 0L;
    if ("SUCCESS".equals(dupResultMap.get("status").toString())) {
      if (dupResultMap.get("msg") != null) {// 成果已经存在
        return -1L;
      } else {
        // 2.导入成果到个人库
        // 调用保存个人成果接口
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> entity = new HttpEntity<String>(JacksonUtils.mapToJsonStr(pubMap), headers);
        String saveUrl = domainscm + V8pubQueryPubConst.PUBHANDLER_URL;
        String result = restTemplate.postForObject(saveUrl, entity, String.class);
        Map saveResultMap = JacksonUtils.jsonToMap(result);
        if ("SUCCESS".equals(saveResultMap.get("status").toString())) {
          String pubIdStr = Des3Utils.decodeFromDes3((String) saveResultMap.get("des3PubId"));
          newPubId = Long.parseLong(pubIdStr);
        } else {
          throw new Exception("推荐成果导入群组出错, GroupId=" + grpId + ", pdwhPubId=" + pubId);
        }
      }
    }
    return newPubId;
  }

  private String getGroupDupPubs(Map<String, Object> pubMap, Long grpId, Long pubId, Long psnId) {
    Map<String, Object> dupMap = new HashMap<String, Object>();
    String title = (String) pubMap.get("title");
    Integer pubType = (Integer) pubMap.get("pubType");
    if (pubMap.get("publishDate") != null && StringUtils.isNotEmpty((String) pubMap.get("publishDate"))) {
      dupMap.put("pubYear", Integer.valueOf(PubDetailVoUtil.parseDateForYear((String) pubMap.get("publishDate"))));
    }
    Integer srcDbId = (Integer) pubMap.get("srcDbId");
    String doi = (String) pubMap.get("doi");
    String sourceId = (String) pubMap.get("sourceId");
    String applicationNo = (String) pubMap.get("applicationNo");
    String publicationOpenNo = (String) pubMap.get("publicationOpenNo");
    String standardNo = (String) pubMap.get("standardNo");
    String registerNo = (String) pubMap.get("registerNo");
    dupMap.put("pubGener", 2);// 成果大类别，1代表个人成果 2代表群组成果 3代表基准库成果
    dupMap.put("title", title);
    dupMap.put("pubType", pubType);
    dupMap.put("doi", doi);
    dupMap.put("srcDbId", srcDbId);
    dupMap.put("sourceId", sourceId);
    dupMap.put("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
    dupMap.put("groupId", grpId);
    if (5 == pubType) {
      dupMap.put("applicationNo", applicationNo);
      dupMap.put("publicationOpenNo", publicationOpenNo);
    }
    if (12 == pubType) {
      dupMap.put("standardNo", standardNo);
    }
    if (13 == pubType) {
      dupMap.put("registerNo", registerNo);
    }
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
    HttpEntity<String> entity = new HttpEntity<String>(JacksonUtils.mapToJsonStr(dupMap), headers);
    String dupUrl = domainscm + V8pubQueryPubConst.PUB_DUPCHECK_URL;
    String result = restTemplate.postForObject(dupUrl, entity, String.class);
    return result;
  }

  private String highlightFundInfoComplete(String fundInfo, String fundNo) {
    if (StringUtils.isEmpty(fundNo) || StringUtils.isEmpty(fundInfo)) {
      return null;
    }
    fundInfo = fundInfo.replaceAll("\\s+", " ");
    String strongStart = "<strong>";
    String strongEnd = "</strong>";
    String fundNoCapital = fundNo.toUpperCase();
    String fundNoLower = fundNo.toLowerCase();
    String[] strs = null;
    if (fundInfo.indexOf(fundNoCapital) != -1) {
      fundInfo = fundInfo.replace(fundNoCapital, strongStart + fundNoCapital + strongEnd);
    } else if (fundInfo.indexOf(fundNoLower) != -1) {
      fundInfo = fundInfo.replace(fundNoLower, strongStart + fundNoLower + strongEnd);
    } else {
      return null;
    }
    return fundInfo;
  }

  private String highlightFundInfo(String fundInfo, String fundNo) throws Exception {
    if (StringUtils.isEmpty(fundNo) || StringUtils.isEmpty(fundInfo)) {
      return null;
    }
    fundInfo = fundInfo.replaceAll("\\s+", " ");
    Integer showLengthLimit = 28;
    String strongStart = "<strong>";
    String strongEnd = "</strong>";
    String fundNoCapital = fundNo.toUpperCase();
    String fundNoLower = fundNo.toLowerCase();
    String newFundInfo = fundInfo;
    String newFundNo = "";
    String[] strs = null;
    if (newFundInfo.indexOf(fundNoCapital) != -1) {
      newFundNo = strongStart + fundNoCapital + strongEnd;
      strs = fundInfo.split(fundNoCapital);
    } else if (newFundInfo.indexOf(fundNoLower) != -1) {
      newFundNo = strongStart + fundNoLower + strongEnd;
      strs = fundInfo.split(fundNoLower);
    } else {
      return null;
    }
    if (strs != null) {
      if (strs.length == 0) {
        strs = new String[] {"", ""};
      } else if (strs.length == 1) {
        if (fundInfo.indexOf(strs[0]) == 0) {
          strs = new String[] {strs[0], ""};
        } else {
          strs = new String[] {"", strs[0]};
        }
      }
      String strStart = StringUtils.trimToEmpty(strs[0]);
      String strEnd = StringUtils.trimToEmpty(strs[1]);
      Integer length = showLengthLimit - fundNo.length();
      return buildFundInfoStr(strStart, strEnd, newFundNo, length);
    }
    return null;
  }

  private String buildFundInfoStr(String strStart, String strEnd, String newFundNo, Integer lengthLimit) {
    if (StringUtils.isAllEmpty(strStart, strEnd)) {// 全为空时
      return StringUtils.trimToEmpty(newFundNo);
    } else if (StringUtils.isEmpty(strStart)) {
      return StringUtils.trimToEmpty(newFundNo + strEnd);
    } else {// 后面字符为空时和都不为空的处理一样
      if (XmlUtil.isChinese(strStart)) {
        if (strStart.length() > lengthLimit) {
          strStart = strStart.substring(strStart.length() - lengthLimit);
          strStart = StringUtils.trimToEmpty(strStart + newFundNo + strEnd);
          return "..." + strStart;
        }
        return StringUtils.trimToEmpty(strStart + newFundNo + strEnd);
      } else {
        if (strStart.length() > lengthLimit * 2) {
          // 找到最近的空格
          int start = strStart.length() - lengthLimit * 2;
          int first = strStart.indexOf(" ", start);
          strStart = strStart.substring(first);
          strStart = StringUtils.trimToEmpty(strStart + newFundNo + strEnd);
          return "..." + strStart;
        }
        return StringUtils.trimToEmpty(strStart + newFundNo + strEnd);
      }
    }
  }
}
