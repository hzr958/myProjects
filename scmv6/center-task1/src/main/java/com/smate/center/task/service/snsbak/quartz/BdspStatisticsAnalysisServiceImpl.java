package com.smate.center.task.service.snsbak.quartz;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * 
 * @author zzx
 *
 */

import com.smate.center.task.dao.pdwh.quartz.PubCategoryDao;
import com.smate.center.task.dao.pdwh.quartz.PubCategoryPatentDao;
import com.smate.center.task.dao.snsbak.PubCategorySnsbakDao;
import com.smate.center.task.dao.snsbak.bdsp.BdspProjectDao;
import com.smate.center.task.dao.snsbak.bdsp.BdspProjectOrgDao;
import com.smate.center.task.dao.snsbak.bdsp.BdspProjectTempDao;
import com.smate.center.task.dao.snsbak.bdsp.BdspProposalDao;
import com.smate.center.task.dao.snsbak.bdsp.BdspProposalOrgDao;
import com.smate.center.task.dao.snsbak.bdsp.BdspProposalTempDao;
import com.smate.center.task.dao.snsbak.bdsp.PubPdwhAddrStandardDao;
import com.smate.center.task.dao.snsbak.bdsp.PubPdwhAddrStandardTempDao;
import com.smate.center.task.model.snsbak.bdsp.BdspProject;
import com.smate.center.task.model.snsbak.bdsp.BdspProjectOrg;
import com.smate.center.task.model.snsbak.bdsp.BdspProjectTemp;
import com.smate.center.task.model.snsbak.bdsp.BdspProposal;
import com.smate.center.task.model.snsbak.bdsp.BdspProposalOrg;
import com.smate.center.task.model.snsbak.bdsp.BdspProposalTemp;
import com.smate.center.task.model.snsbak.bdsp.PubPdwhAddrStandard;
import com.smate.center.task.model.snsbak.bdsp.PubPdwhAddrStandardTemp;

@Service("bdspStatisticsAnalysisService")
@Transactional(rollbackFor = Exception.class)
public class BdspStatisticsAnalysisServiceImpl implements BdspStatisticsAnalysisService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private BdspProjectDao bdspProjectDao;
  @Autowired
  private BdspProposalDao bdspProposalDao;
  @Autowired
  private BdspProjectOrgDao bdspProjectOrgDao;
  @Autowired
  private BdspProjectTempDao bdspProjectTempDao;
  @Autowired
  private BdspProposalOrgDao bdspProposalOrgDao;
  @Autowired
  private BdspProposalTempDao bdspProposalTempDao;
  @Autowired
  private PubPdwhAddrStandardDao pubPdwhAddrStandardDao;
  @Autowired
  private PubPdwhAddrStandardTempDao pubPdwhAddrStandardTempDao;
  @Autowired
  private PubCategoryDao pubCategoryDao;
  @Autowired
  private PubCategorySnsbakDao pubCategorySnsbakDao;
  @Autowired
  private PubCategoryPatentDao pubCategoryPatentDao;

  @Override
  public List<BdspProject> findProjectList(int batchSize) throws Exception {
    return bdspProjectDao.findlist(batchSize);
  }

  @Override
  public List<BdspProposal> findProposalList(int batchSize) throws Exception {
    return bdspProposalDao.findlist(batchSize);
  }

  @Override
  public void doProjectRegister(BdspProject one) throws Exception {

    BdspProjectTemp bpt = new BdspProjectTemp();
    bpt.setPrjCode(one.getPrjCode());
    bpt.setStatYear(one.getStatYear());
    bpt.setGrantTypeId(one.getGrantCode());// 5大领域
    bpt.setTechfiledId(one.getSubjectId());// 7大领域
    bpt.setReqAmt(one.getReqAmt());
    BdspProjectOrg bdspProjectOrg = bdspProjectOrgDao.findOrg(one.getPrjCode(), one.getOrgName());
    if (bdspProjectOrg != null) {
      bpt.setCityId(bdspProjectOrg.getCityId());
      bpt.setProvId(bdspProjectOrg.getProvinceId());
      bpt.setOrgName(StringUtils.isBlank(bdspProjectOrg.getOrgZhName()) ? bdspProjectOrg.getOrgEnName()
          : bdspProjectOrg.getOrgZhName());
      bpt.setOrgId(bdspProjectOrg.getOriginalId());
    }
    bdspProjectTempDao.save(bpt);

  }

  @Override
  public void doProposalRegister(BdspProposal one) throws Exception {
    BdspProposalTemp bpt = new BdspProposalTemp();
    bpt.setPrpCode(one.getPrpCode());
    bpt.setStatYear(one.getStatYear());
    bpt.setGrantTypeId(one.getGrantCode());
    bpt.setTechfiledId(one.getSubjectId());
    bpt.setReqAmt(one.getReqAmt());
    BdspProposalOrg bdspProposalOrg = bdspProposalOrgDao.findOrg(one.getPrpCode(), one.getOrgName());
    if (bdspProposalOrg != null) {
      bpt.setCityId(bdspProposalOrg.getCityId());
      bpt.setProvId(bdspProposalOrg.getProvinceId());
      bpt.setOrgName(StringUtils.isBlank(bdspProposalOrg.getOrgZhName()) ? bdspProposalOrg.getOrgEnName()
          : bdspProposalOrg.getOrgZhName());
      bpt.setOrgId(bdspProposalOrg.getOriginalId());
    }
    bdspProposalTempDao.save(bpt);
  }

  @Override
  public List<PubPdwhAddrStandard> findPaperPatentList(int batchSize) throws Exception {
    return pubPdwhAddrStandardDao.findlist(batchSize);
  }

  @Override
  public void doPaperPatentRegister(PubPdwhAddrStandard one) throws Exception {
    PubPdwhAddrStandardTemp t = new PubPdwhAddrStandardTemp();
    t.setId(one.getId());
    t.setCityId(one.getCityId());
    t.setOrgId(one.getInsId());
    t.setProvId(one.getProvinceId());
    t.setStatYear(one.getPubYear());
    t.setPubType(one.getPubType());
    if (one.getPubType() == 5) {
      t.setTechfiledId(pubCategoryPatentDao.findTechfiledIdByPubId(one.getPdwhPubId()));
    } else {
      t.setTechfiledId(pubCategorySnsbakDao.findTechfiledIdByPubId(one.getPdwhPubId()));
    }
    t.setPubId(one.getPdwhPubId());
    pubPdwhAddrStandardTempDao.save(t);

  }

  @Override
  public void initDelOldData() throws Exception {
    // 删除统计分析和对比分析表的数据
    pubPdwhAddrStandardDao.delOldData();


  }

  @Override
  public void initDealTempData() throws Exception {
    // 删除PUB_PDWH_ADDR_STANDARD_TEMP中pubId重复的数据
    pubPdwhAddrStandardDao.delRepeatData();

  }

  @Override
  public void dealPrjData() throws Exception {
    pubPdwhAddrStandardDao.dealPrjData();
    pubPdwhAddrStandardDao.dealPrjAmt();

  }

  @Override
  public void dealPrpData() throws Exception {
    pubPdwhAddrStandardDao.dealPrpData();
    pubPdwhAddrStandardDao.dealPrpAmt();

  }

  @Override
  public void dealFundRateData() throws Exception {
    pubPdwhAddrStandardDao.dealFundRateData();

  }

  @Override
  public void dealPaperData() throws Exception {
    pubPdwhAddrStandardDao.dealPaperData();

  }

  @Override
  public void dealPatentData() throws Exception {
    pubPdwhAddrStandardDao.dealPatentData();

  }

  @Override
  public void initDelTempData() {
    try {
      // 删除统计分析和对比分析表的数据
      pubPdwhAddrStandardDao.delTempData();
    } catch (Exception e) {
      logger.error("统计对比分析任务 -清理临时数据 -出错", e);
    }

  }
}
