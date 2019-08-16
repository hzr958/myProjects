package com.smate.center.task.single.service.pub;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.smate.center.task.dao.innocity.InnoCityPatRcmdForReqDao;
import com.smate.center.task.dao.innocity.InnoCityPatRcmdForReqResultDao;
import com.smate.center.task.dao.innocity.InnoCityReqRcmdForPatDao;
import com.smate.center.task.dao.innocity.InnoCityReqRcmdForPatResultDao;
import com.smate.center.task.dao.innocity.InnoCityRequirementDao;
import com.smate.center.task.dao.innocity.JyPatentDao;
import com.smate.center.task.model.innocity.InnoCityPatRcmdForReq;
import com.smate.center.task.model.innocity.InnoCityPatRcmdForReqResult;
import com.smate.center.task.model.innocity.InnoCityReqRcmdForPat;
import com.smate.center.task.model.innocity.InnoCityReqRcmdForPatResult;
import com.smate.center.task.model.innocity.InnoCityRequirement;
import com.smate.center.task.model.innocity.JyPatent;
import com.smate.center.task.model.sns.pub.KeywordSplit;
import com.smate.center.task.model.sns.pub.PubInfo;
import com.smate.center.task.service.solrindex.SolrIndexDifService;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.json.JacksonUtils;

@Service("patentAndRequirementRcmdService")
@Transactional(rollbackFor = Exception.class)
public class PatentAndRequirementRcmdServiceImpl implements PatentAndRequirementRcmdService {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private KeywordsDicService keywordsDicService;

  @Autowired
  private InnoCityReqRcmdForPatDao innoCityReqRcmdForPatDao;

  @Autowired
  private InnoCityPatRcmdForReqDao innoCityPatRcmdForReqDao;

  @Autowired
  private InnoCityPatRcmdForReqResultDao innoCityPatRcmdForReqResultDao;

  @Autowired
  private InnoCityReqRcmdForPatResultDao innoCityReqRcmdForPatResultDao;

  @Autowired
  private InnoCityRequirementDao innoCityRequirementDao;

  @Autowired
  private JyPatentDao jyPatentDao;

  @Autowired
  private SolrIndexDifService solrIndexDifService;

  @Resource(name = "pubKeywordsWeightSetProcess")
  private KeywordsWeightSetProcess keywordsWeightSetProcess;

  @Override
  public List<InnoCityPatRcmdForReq> getToHandleRequirementList() {
    return innoCityPatRcmdForReqDao.getListByStatus(0);
  }

  @Override
  public String patRcmdForRequirement(InnoCityPatRcmdForReq req) throws Exception {
    Assert.notNull(req);
    Long reqId = req.getReqId();
    InnoCityRequirement requirement = innoCityRequirementDao.get(reqId);
    Assert.notNull(requirement, "为需求推荐专利时，需求书不能为空");
    String title = requirement.getTitle();
    String abs = requirement.getDescription();

    this.innoCityPatRcmdForReqResultDao.deleteAutoRcmdResult(reqId);
    String extractKwStr = this.extractKeywordsStr(title, abs);
    if (StringUtils.isEmpty(extractKwStr)) {
      return null;
    }

    String searchStr = extractKwStr.replace(",", "");
    Map<String, Object> rsMap = solrIndexDifService.getRcmdPatents(1, 20, searchStr, null);
    if (rsMap == null) {
      // 标记推荐结果为空
      return "rsNull||" + extractKwStr;
    }
    List<Map<String, Object>> list = JacksonUtils.jsonToList((String) rsMap.get("items"));
    if (CollectionUtils.isEmpty(list)) {
      // 标记推荐结果为空
      return "rsNull||" + extractKwStr;
    }

    for (Map<String, Object> rs : list) {
      String idStr = String.valueOf(rs.get("patIdRcmd"));
      if (!XmlUtil.isNumeric(idStr)) {
        continue;
      }
      Long id = Long.parseLong(idStr);
      if (id != null && id != 0L) {
        InnoCityPatRcmdForReqResult rcmdRs = new InnoCityPatRcmdForReqResult();
        rcmdRs.setReqId(reqId);
        rcmdRs.setPatentId(id);
        rcmdRs.setStatus(1);// 后台任务推荐标记为1
        this.innoCityPatRcmdForReqResultDao.save(rcmdRs);
      }
    }

    return extractKwStr;
  }

  @Override
  public List<InnoCityReqRcmdForPat> getToHandlePatentList() {
    return this.innoCityReqRcmdForPatDao.getListByStatus(0);
  }

  @Override
  public String reqRcmdForPatent(InnoCityReqRcmdForPat pat) throws Exception {
    Assert.notNull(pat);
    Long patId = pat.getPatentId();
    JyPatent patent = this.jyPatentDao.get(patId);
    Assert.notNull(patent, "为专利推荐需求时，专利不能为空");

    String patTitle = patent.getZhTitle();
    String zhKw = patent.getZhKeyWords();
    String brief = patent.getAbStract();

    this.innoCityReqRcmdForPatResultDao.deleteAutoRcmdResult(patId);

    String extractKwStr = this.extractKeywordsStr(patTitle, brief, zhKw);
    if (StringUtils.isEmpty(extractKwStr)) {
      return null;
    }

    String searchStr = extractKwStr.replace(",", "");
    Map<String, Object> rsMap = solrIndexDifService.getRequestRcmdFromPatent(1, 20, searchStr, null);
    if (rsMap == null) {
      return "rsNull||" + extractKwStr;
    }
    List<Map<String, Object>> list = JacksonUtils.jsonToList((String) rsMap.get("items"));
    if (CollectionUtils.isEmpty(list)) {
      return "rsNull||" + extractKwStr;
    }

    for (Map<String, Object> rs : list) {
      String idStr = String.valueOf(rs.get("reqId"));
      if (!XmlUtil.isNumeric(idStr)) {
        continue;
      }
      Long id = Long.parseLong(idStr);
      if (id != null && id != 0L) {
        InnoCityReqRcmdForPatResult rcmdRs = new InnoCityReqRcmdForPatResult();
        rcmdRs.setPatentId(patId);
        rcmdRs.setReqId(id);
        rcmdRs.setStatus(1);// 标识为后台任务推荐
        this.innoCityReqRcmdForPatResultDao.save(rcmdRs);
      }

    }
    return extractKwStr;
  }

  @Override
  public void updateReqRcmdForPatentStatus(InnoCityReqRcmdForPat req, Integer status, String extractKws) {
    req.setStatus(status);
    req.setRcmdTime(new Date());
    req.setExtractKws(extractKws.length() > 200 ? extractKws.substring(0, 200) : extractKws);
    this.innoCityReqRcmdForPatDao.save(req);
  }

  @Override
  public void updateReqRcmdForPatentStatus(InnoCityReqRcmdForPat req, Integer status) {
    req.setStatus(status);
    req.setRcmdTime(new Date());
    this.innoCityReqRcmdForPatDao.save(req);
  }

  @Override
  public void updatePatRcmdForReqStatus(InnoCityPatRcmdForReq req, Integer status, String extractKws) {
    req.setStatus(status);
    req.setRcmdTime(new Date());
    req.setExtractKws(extractKws.length() > 200 ? extractKws.substring(0, 200) : extractKws);
    this.innoCityPatRcmdForReqDao.save(req);
  }

  @Override
  public void updatePatRcmdForReqStatus(InnoCityPatRcmdForReq req, Integer status) {
    req.setStatus(status);
    req.setRcmdTime(new Date());
    this.innoCityPatRcmdForReqDao.save(req);
  }

  public String extractKeywordsStr(String title, String abs) throws Exception {
    PubInfo pubInfo = new PubInfo();
    pubInfo.setZhTitle(title);
    pubInfo.setZhAbs(abs);
    List<KeywordSplit> kwList = keywordsDicService.findPubKeywords(pubInfo, keywordsWeightSetProcess);

    if (kwList == null || kwList.size() == 0) {
      return null;
    }
    // 保留5个关键词
    kwList = kwList.size() > 5 ? kwList.subList(0, 4) : kwList;
    StringBuilder sb = new StringBuilder();
    for (KeywordSplit ks : kwList) {
      if (StringUtils.isNotBlank(ks.getKeyword())) {
        sb.append("," + ks.getKeyword());
      }
    }
    return sb.toString();
  }

  public String extractKeywordsStr(String title, String abs, String kw) throws Exception {
    PubInfo pubInfo = new PubInfo();
    pubInfo.setZhTitle(title);
    pubInfo.setZhAbs(abs);
    pubInfo.setZhKws(kw);
    List<KeywordSplit> kwList = keywordsDicService.findPubKeywords(pubInfo, keywordsWeightSetProcess);

    if (kwList == null || kwList.size() == 0) {
      return null;
    }
    // 保留5个关键词
    kwList = kwList.size() > 5 ? kwList.subList(0, 4) : kwList;
    StringBuilder sb = new StringBuilder();
    for (KeywordSplit ks : kwList) {
      if (StringUtils.isNotBlank(ks.getKeyword())) {
        sb.append("," + ks.getKeyword());
      }
    }
    return sb.toString();
  }

}


