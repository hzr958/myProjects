package com.smate.center.open.service.proposal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.dao.proposal.NsfcProposalDao;
import com.smate.center.open.dao.proposal.NsfcPrpPubDao;
import com.smate.center.open.model.consts.ConstPubType;
import com.smate.center.open.model.nsfc.SyncProposalModel;
import com.smate.center.open.model.proposal.NsfcProposal;
import com.smate.center.open.model.proposal.NsfcPrpPub;
import com.smate.center.open.model.rule.GrantRule;
import com.smate.center.open.service.consts.ConstPubTypeService;
import com.smate.center.open.service.rule.GrantRuleService;
import com.smate.core.base.utils.constant.ConstDictionary;
import com.smate.core.base.utils.service.consts.ConstDictionaryManage;

/**
 * 杰青申报成果模块
 * 
 * @author tsz
 * 
 */
@Service("nsfcPrpPubService")
@Transactional(rollbackFor = Exception.class)
public class NsfcPrpPubServiceImpl implements NsfcPrpPubService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private NsfcProposalDao nsfcProposalDao;
  @Autowired
  private NsfcPrpPubDao nsfcPrpPubDao;

  @Autowired
  private GrantRuleService grantRuleService;

  @Autowired
  private ConstPubTypeService constPubTypeService;

  @Autowired
  private ConstDictionaryManage constDictionaryManage;

  @Override
  public void syncSaveIsisGuid(SyncProposalModel model) throws Exception {
    try {

      NsfcProposal prp = new NsfcProposal();
      prp.setIsisGuid(model.getGuid());
      prp.setPrpPsnId(model.getPsnId());
      prp.setStatus(2);
      this.nsfcProposalDao.save(prp);
    } catch (Exception e) {
      logger.error("生成isisGuid={}的记录失败！", model.getGuid());
      throw new Exception(e);
    }
  }

  @Override
  public void syncSaveProposal(SyncProposalModel model) throws Exception {
    if ("2".equals(model.getStatus())) { // 暂存状态
      try {
        syncSaveIsisGuid(model);
      } catch (Exception e) {
        logger.error("初始化sns杰青申请书信息失败！", e);
        throw new Exception("初始化sns杰青申请书信息", e);
      }
      return;
    } else if ("3".equals(model.getStatus())) { // 删除项目
      try {
        this.nsfcProposalDao.deleteByGuids(model.getGuid(), model.getPsnId());
        this.nsfcPrpPubDao.deletePrpPub(model.getGuid());
      } catch (Exception e) {
        logger.error("删除isisGuid={}项目失败", model.getGuid(), e);
        throw new Exception("删除杰青申报书信息", e);
      }
    } else {
      syncSaveProposal1(model);
    }
  }

  private void syncSaveProposal1(SyncProposalModel model) throws Exception {
    try {
      NsfcProposal prp = this.nsfcProposalDao.getPrpByIsisGuid(model.getGuid(), model.getPsnId());

      if (model.getIsRollBack() != null && model.getIsRollBack().booleanValue()) {
        prp.setIsisGuid(model.getGuid());
        prp.setPrpPsnId(model.getPsnId());
        prp.setStatus(NumberUtils.toInt(model.getStatus()));
      } else {
        if (prp == null) {
          prp = new NsfcProposal();
        }
        prp.setEtitle(model.getEtitle());
        if (model.getCode() != null && !"".equals(model.getCode()))
          prp.setCode(Long.valueOf(model.getCode()));
        prp.setCtitle(model.getCtitle());
        prp.setIsisGuid(model.getGuid());
        prp.setPrpPsnId(model.getPsnId());
        prp.setPrpYear(NumberUtils.toInt(model.getYear()));
        prp.setStatus(NumberUtils.toInt(model.getStatus()));
        prp.setVersion(0);
      }
      this.nsfcProposalDao.save(prp);
    } catch (Exception e) {
      logger.error("同步isisGuid={}的杰青申报书信息失败！", model.getGuid());
      throw new Exception("同步杰青申报书信息", e);
    }
  }

  @Override
  public NsfcProposal getPrpByIsisGuid(String isisGuid, Long userId) throws Exception {
    try {
      NsfcProposal nsfcProposal = this.nsfcProposalDao.getPrpByIsisGuid(isisGuid, userId);
      if (nsfcProposal == null)
        return null;
      if ("en_US".equals(LocaleContextHolder.getLocale().toString())) {
        nsfcProposal.setTitle(nsfcProposal.getEtitle());
      } else {
        nsfcProposal.setTitle(nsfcProposal.getCtitle());
      }
      return nsfcProposal;
    } catch (Exception e) {
      logger.error("获取杰青isisGuid={失败！", isisGuid, e);
      throw new Exception("获取杰青isisGuid={失败！", e);
    }
  }

  @Override
  public List<NsfcPrpPub> loadPrpPubsByGuid(SyncProposalModel model) throws Exception {
    GrantRule rule = this.grantRuleService.getGrantRule(Long.valueOf(model.getCode()));
    try {
      List<NsfcPrpPub> prpPubs = null;
      if (rule != null && !rule.getIsUserDefined()) {// 好像国际合作类的页面
        prpPubs = this.nsfcPrpPubDao.getPubsByGuid(model.getGuid(), model.getPsnId());
      } else {
        prpPubs = this.nsfcPrpPubDao.getPubsOrderByType(model.getGuid(), model.getPsnId());
      }
      this.wrapPubTypeName(prpPubs, rule); // ok
      if (StringUtils.isNotEmpty(model.getStatus())) { // 修改申报书状态
        NsfcProposal prp = this.nsfcProposalDao.getPrpByIsisGuid(model.getGuid(), model.getPsnId());
        prp.setStatus(new Integer(model.getStatus()));
        prp.setCtitle(model.getCtitle());
        this.nsfcProposalDao.save(prp);

      }

      return prpPubs;
    } catch (Exception e) {

      logger.error("读取杰青成果列表失败！", e);
      throw new Exception("读取杰青成果列表失败！", e);
    }

  }

  private List<NsfcPrpPub> wrapPubTypeName(List<NsfcPrpPub> prpPubs, GrantRule rule) throws Exception {
    Map<Integer, ConstPubType> cptsMap = new HashMap<Integer, ConstPubType>();
    List<ConstPubType> cpts = constPubTypeService.getAll();
    for (ConstPubType cpt : cpts) {
      cptsMap.put(cpt.getId(), cpt);
    }

    if (rule.getIsUserDefined()) {
      Map<String, ConstDictionary> pubTypeMap =
          this.constDictionaryManage.findConstByCategoryAndCodes("nsfc_pub_type", rule.getTypeValues());
      for (NsfcPrpPub nsfcPrpPub : prpPubs) {
        ConstDictionary constDictionary = pubTypeMap.get(nsfcPrpPub.getTreatiseType().toString());
        nsfcPrpPub.setTreatiseTypeZhName(constDictionary.getZhCnName());
        nsfcPrpPub.setTreatiseTypeEnName(constDictionary.getEnUsName());

        if (nsfcPrpPub.getPubType() != null && !"".equals(nsfcPrpPub.getPubType())) {
          nsfcPrpPub.setPubTypeZhName(cptsMap.get(nsfcPrpPub.getPubType()).getZhName());
          nsfcPrpPub.setPubTypeEnName(cptsMap.get(nsfcPrpPub.getPubType()).getEnName());
        }
      }
    } else {
      for (NsfcPrpPub prpPub : prpPubs) {
        if (prpPub.getPubType() != null && !"".equals(prpPub.getPubType())) {
          prpPub.setPubTypeZhName(cptsMap.get(prpPub.getPubType()).getZhName());
          prpPub.setPubTypeEnName(cptsMap.get(prpPub.getPubType()).getEnName());
        }
      }
    }
    return prpPubs;
  }


}
