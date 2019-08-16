package com.smate.center.task.service.sns.pubAssign.handler;

import java.util.Date;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.center.task.dao.pdwh.quartz.PdwhPubAuthorSnsPsnRecordDao;
import com.smate.center.task.dao.sns.psn.PsnCopartnerDao;
import com.smate.center.task.dao.sns.pub.PubAssignLogDao;
import com.smate.center.task.dao.sns.pub.PubAssignLogDetailDao;
import com.smate.center.task.model.pdwh.pub.PdwhPubAuthorSnsPsnRecord;
import com.smate.center.task.model.sns.psn.PsnCopartner;
import com.smate.center.task.model.sns.pub.PubAssginMatchContext;
import com.smate.center.task.model.sns.pub.PubAssignLog;
import com.smate.center.task.model.sns.pub.PubAssignLogDetail;
import com.smate.center.task.v8pub.dao.sns.PubPdwhDetailDAO;
import com.smate.core.base.pub.consts.V8pubQueryPubConst;
import com.smate.core.base.pub.po.PubPdwhSnsRelationPO;
import com.smate.core.base.pub.po.dao.PubPdwhSnsRelationDAO;
import com.smate.core.base.pub.util.PubDetailVoUtil;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.v8pub.dom.PatentInfoBean;
import com.smate.web.v8pub.dom.SoftwareCopyrightBean;
import com.smate.web.v8pub.dom.StandardInfoBean;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;


@Service("pdwhPubAssignService")
@Transactional(rollbackFor = Exception.class)
public class PdwhPubAssignServiceImpl implements PdwhPubAssignService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Value("${domainscm}")
  private String scmDomain;
  @Autowired
  private PubAssignLogDao pubAssignLogDao;
  @Autowired
  private PubAssignLogDetailDao pubAssignLogDetailDao;
  @Autowired
  private PubPdwhDetailDAO pubPdwhDetailDAO;
  @Autowired
  private PsnCopartnerDao psnCopartnerDao;
  @Autowired
  private PdwhPubAuthorSnsPsnRecordDao PsnRecordDao;
  @Autowired
  private PubPdwhSnsRelationDAO pubPdwhSnsRelationDAO;

  @Override
  public void deletePubAssign(Long pdwhPubId, Long psnId) {
    pubAssignLogDao.remove(pdwhPubId, psnId);
  }

  @Override
  public String getPubDupucheckStatus(Long psnId, Long pdwhPubId) {
    PubPdwhDetailDOM detailDom = pubPdwhDetailDAO.findById(pdwhPubId);
    Map<String, Object> dupMap = new HashMap<String, Object>();
    dupMap.put("pubGener", 1);// 成果大类别，1代表个人成果 2代表群组成果 3代表基准库成果
    dupMap.put("title", detailDom.getTitle());
    dupMap.put("pubYear", PubDetailVoUtil.parseDateForYear(detailDom.getPublishDate()));
    dupMap.put("pubType", detailDom.getPubType());
    dupMap.put("doi", detailDom.getDoi());
    dupMap.put("sourceId", detailDom.getSourceId());
    dupMap.put("srcDbId", detailDom.getSrcDbId());
    dupMap.put("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
    if (5 == detailDom.getPubType()) {
      PatentInfoBean infoBean = (PatentInfoBean) detailDom.getTypeInfo();
      dupMap.put("applicationNo", infoBean.getApplicationNo());
      dupMap.put("publicationOpenNo", infoBean.getPublicationOpenNo());
    }
    if (12 == detailDom.getPubType()) {
      StandardInfoBean infoBean = (StandardInfoBean) detailDom.getTypeInfo();
      dupMap.put("standardNo", infoBean.getStandardNo());
    }
    if (13 == detailDom.getPubType()) {
      SoftwareCopyrightBean infoBean = (SoftwareCopyrightBean) detailDom.getTypeInfo();
      dupMap.put("registerNo", infoBean.getRegisterNo());
    }
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
    HttpEntity<String> entity = new HttpEntity<String>(JacksonUtils.mapToJsonStr(dupMap), headers);
    String dupUrl = scmDomain + V8pubQueryPubConst.PUB_DUPCHECK_URL;
    String result = restTemplate.postForObject(dupUrl, entity, String.class);
    return result;
  }

  @Override
  public void dealpubAssignLogDetail(PubAssginMatchContext context) {
    List<PubAssignLogDetail> assignDetailList =
        pubAssignLogDetailDao.getAssignDeatil(context.getPdwhPubId(), context.getPsnId());
    if (assignDetailList.size() > 0) {
      pubAssignLogDetailDao.deleteAssignDetail(context.getPdwhPubId(), context.getPsnId());
    }
    PubAssignLogDetail assignDetail = new PubAssignLogDetail(context.getPdwhPubId(), context.getPsnId(),
        context.getMatchedEmail(), context.getMatchedInsId(), new Date());
    assignDetail.setMatchedEmail(context.getMatchedEmail());
    assignDetail.setMatchedInsId(context.getMatchedInsId());
    assignDetail.setPubMemberId(context.getPubMemberId());
    assignDetail.setPubMemberName(context.getPubMemberName());
    if (StringUtils.isBlank(context.getMatchedFullName()) && StringUtils.isBlank(context.getMatchedInitName())
        && StringUtils.isBlank(context.getMatchedEmail())) {
      return;
    }
    if (StringUtils.isNotBlank(context.getMatchedFullName()) && StringUtils.isNotBlank(context.getMatchedInitName())) {
      assignDetail.setMatchedName(context.getMatchedFullName());
      assignDetail.setMatchedNameType(1);
      pubAssignLogDetailDao.saveWithNewTransaction(assignDetail);
      assignDetail = new PubAssignLogDetail(context.getPdwhPubId(), context.getPsnId(), context.getMatchedEmail(),
          context.getMatchedInsId(), new Date());
      assignDetail.setMatchedName(context.getMatchedInitName());
      assignDetail.setMatchedNameType(0);
    } else if (StringUtils.isNotBlank(context.getMatchedFullName())) {
      assignDetail.setMatchedName(context.getMatchedFullName());
      assignDetail.setMatchedNameType(1);
    } else if (StringUtils.isNotBlank(context.getMatchedInitName())) {
      assignDetail.setMatchedName(context.getMatchedInitName());
      assignDetail.setMatchedNameType(0);
    }
    pubAssignLogDetailDao.saveWithNewTransaction(assignDetail);
  }

  @Override
  public PubAssignLog getPubAssignLog(Long pdwhPubId, Long psnId) {
    return pubAssignLogDao.getPubAssignLog(pdwhPubId, psnId);
  }

  @Override
  public void removeAllContext(PubAssginMatchContext context) {
    context.setEmailResult(0);
    context.setFriendResult(0);
    context.setInsResult(0);
    context.setKeywordsResult(0);
    context.setMatchedEmail(null);
    context.setMatchedFullName(null);
    context.setMatchedInitName(null);
    context.setInitNameMatch(null);
    context.setPubMemberName(null);
    context.setPubMemberId(null);
  }

  @Override
  public void PsnPubCopartnerRcmd(Long psnId, Long pdwhPubId) {
    try {
      // 排除当前人
      List<PubAssignLogDetail> PubAssignLogDetails = pubAssignLogDetailDao.getAssignDetailByPdwhId(pdwhPubId, psnId);
      for (PubAssignLogDetail pubAssignLogDetail : PubAssignLogDetails) {
        // 把认领了这条成果的人推给当前人当合作者
        PsnCopartner psnCopartner = psnCopartnerDao.getPsnCopartner(psnId, pubAssignLogDetail.getPsnId(), pdwhPubId, 1);
        if (psnCopartner == null) {
          psnCopartner = new PsnCopartner(psnId, pdwhPubId, 1);
          psnCopartner.setCoPsnId(pubAssignLogDetail.getPsnId());
        }
        psnCopartner.setPdwhPubName(pubAssignLogDetail.getPubMemberName());
        psnCopartner.setPdwhPubMemberId(pubAssignLogDetail.getPubMemberId());
        psnCopartnerDao.saveWithNewTransaction(psnCopartner);
        // 把当前人推给认领了这条成果的人当合作者
        PsnCopartner psnCopartner2 =
            psnCopartnerDao.getPsnCopartner(pubAssignLogDetail.getPsnId(), psnId, pdwhPubId, 1);
        if (psnCopartner2 == null) {
          psnCopartner2 = new PsnCopartner(pubAssignLogDetail.getPsnId(), pdwhPubId, 1);
          psnCopartner2.setCoPsnId(psnId);
        }
        psnCopartner2.setPdwhPubName(pubAssignLogDetail.getPubMemberName());
        psnCopartner2.setPdwhPubMemberId(pubAssignLogDetail.getPubMemberId());
        psnCopartnerDao.saveWithNewTransaction(psnCopartner2);
      }
    } catch (Exception e) {
      logger.error("自动认领推荐合作者出错===================", e);
    }
  }

  @Override
  public List<PdwhPubAuthorSnsPsnRecord> getPsnRecordByPubId(Long pdwhPubId) {
    return PsnRecordDao.getPsnRecordByPubId(pdwhPubId);
  }

  @Override
  public void savePubPdwhSnsRelation(Long snsPubId, Long pdwhPubId) {
    PubPdwhSnsRelationPO pubPdwhSnsRelationPO = pubPdwhSnsRelationDAO.getByPubIdAndPdwhId(snsPubId, pdwhPubId);
    if (pubPdwhSnsRelationPO == null) {
      pubPdwhSnsRelationPO = new PubPdwhSnsRelationPO();
      pubPdwhSnsRelationPO.setPdwhPubId(pdwhPubId);
      pubPdwhSnsRelationPO.setSnsPubId(snsPubId);
      pubPdwhSnsRelationPO.setCreateDate(new Date());
      pubPdwhSnsRelationDAO.save(pubPdwhSnsRelationPO);
    }
  }



}
