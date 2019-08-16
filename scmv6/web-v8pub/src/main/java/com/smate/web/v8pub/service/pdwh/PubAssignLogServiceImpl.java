package com.smate.web.v8pub.service.pdwh;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.pub.po.PubPdwhSnsRelationPO;
import com.smate.core.base.pub.po.dao.PubPdwhSnsRelationDAO;
import com.smate.core.base.utils.collections.ListUtils;
import com.smate.web.v8pub.dao.pdwh.PubPdwhDAO;
import com.smate.web.v8pub.dao.sns.PubAssignLogDAO;
import com.smate.web.v8pub.dao.sns.PubAssignLogDetailDao;
import com.smate.web.v8pub.dao.sns.psn.PsnCopartnerDao;
import com.smate.web.v8pub.enums.PubGenreConstants;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.sns.PubAssignLogDetail;
import com.smate.web.v8pub.po.sns.PubAssignLogPO;
import com.smate.web.v8pub.po.sns.psn.PsnCopartner;
import com.smate.web.v8pub.service.query.PubQueryDTO;

/**
 * 成果认领的服务类
 * 
 * @author aijiangbin
 * @date 2018年7月23日
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PubAssignLogServiceImpl implements PubAssignLogService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubAssignLogDAO pubAssignLogDao;
  @Autowired
  private PubPdwhDAO pubPdwhDAO;
  @Autowired
  private PubAssignLogDetailDao pubAssignLogDetailDao;
  @Autowired
  private PsnCopartnerDao psnCopartnerDao;
  @Autowired
  private PubPdwhSnsRelationDAO pubPdwhSnsRelationDAO;

  @Override
  public void queryPubConfirmIdList(PubQueryDTO pubQueryDTO) {
    pubAssignLogDao.queryPubConfirmIdList(pubQueryDTO);
  }

  @Override
  public void queryPubConfirmIdIgnoreStatusList(PubQueryDTO pubQueryDTO) {
    pubAssignLogDao.queryPubConfirmIdIgnoreStatusList(pubQueryDTO);
  }



  @Override
  public void updateAutoConfirmStatus(List<Long> dupPubIds, Long psnId, Long snsPubId, Integer pubGenre)
      throws ServiceException {
    if (CollectionUtils.isEmpty(dupPubIds)) {
      return;
    }
    try {
      List<Long> confirmPubIds = listNeedConfirmPubId(dupPubIds, psnId);
      if (CollectionUtils.isEmpty(confirmPubIds)) {
        return;
      }
      for (Long pdwhPubId : confirmPubIds) {
        PubAssignLogPO pubAssignLogPO = pubAssignLogDao.getPubAssign(pdwhPubId, psnId);
        if (pubAssignLogPO == null) {
          continue;
        }
        pubAssignLogPO.setIsAutoConfirm(1);
        pubAssignLogPO.setConfirmResult(1);
        pubAssignLogPO.setSnsPubId(snsPubId);
        pubAssignLogPO.setConfirmDate(new Date());
        pubAssignLogDao.save(pubAssignLogPO);
        if (pubGenre != PubGenreConstants.PDWH_SNS_PUB) {
          this.savePubPdwhSnsRelation(pdwhPubId, snsPubId);
        }
        // 自动认领成果后计算成果合作者
        psnPubCopartnerRcmd(pdwhPubId, psnId);
      }
    } catch (Exception e) {
      logger.error("PubAssignLogServiceImpl成果自动确认出错", e);
      throw new ServiceException(e);
    }

  }

  private void savePubPdwhSnsRelation(Long pdwhPubId, Long snsPubId) {
    PubPdwhSnsRelationPO pubPdwhSnsRelationPO = pubPdwhSnsRelationDAO.getByPubIdAndPdwhId(snsPubId, pdwhPubId);
    if (pubPdwhSnsRelationPO == null) {
      pubPdwhSnsRelationPO = new PubPdwhSnsRelationPO();
      pubPdwhSnsRelationPO.setPdwhPubId(pdwhPubId);
      pubPdwhSnsRelationPO.setSnsPubId(snsPubId);
      pubPdwhSnsRelationPO.setCreateDate(new Date());
      pubPdwhSnsRelationDAO.save(pubPdwhSnsRelationPO);
    }
  }

  public List<Long> listNeedConfirmPubId(List<Long> dupPubIds, Long psnId) throws ServiceException {
    List<Long> confirmPubIds = new ArrayList<Long>();
    if (CollectionUtils.isNotEmpty(dupPubIds)) {
      if (dupPubIds.size() > 1000) {
        List<Long>[] split = ListUtils.split(dupPubIds, 1000);
        List<Long> dupPubConfirmIds = null;
        for (List<Long> list : split) {
          dupPubConfirmIds = pubAssignLogDao.listNeedConfirmPubId(list, psnId);
          confirmPubIds.addAll(dupPubConfirmIds);
        }
      } else {
        confirmPubIds = pubAssignLogDao.listNeedConfirmPubId(dupPubIds, psnId);
      }
    }
    return confirmPubIds;
  }

  @Override
  public List<PubAssignLogPO> queryPubAssignLogByIds(PubQueryDTO pubQueryDTO) throws ServiceException {
    List<PubAssignLogPO> list = pubAssignLogDao.findByIds(pubQueryDTO);
    return list;
  }

  @Override
  public List<PubAssignLogPO> queryAllPubAssignLogByIds(PubQueryDTO pubQueryDTO) throws ServiceException {
    List<PubAssignLogPO> list = pubAssignLogDao.findAllByIds(pubQueryDTO);
    return list;
  }

  @Override
  public void queryPubAssignListForOpen(PubQueryDTO pubQueryDTO) throws ServiceException {
    pubAssignLogDao.queryPubAssignListForOpen(pubQueryDTO);
  }

  @Override
  public void queryAllPubAssignListForOpen(PubQueryDTO pubQueryDTO) throws ServiceException {
    pubAssignLogDao.queryAllPubAssignListForOpen(pubQueryDTO);
  }

  @Override
  public PubAssignLogPO findByPubConfirmId(Long pubConfirmId) throws ServiceException {
    PubAssignLogPO assignLogPO = pubAssignLogDao.getAssignLogByPubConfirmId(pubConfirmId);
    return assignLogPO;
  }

  @Override
  public Long queryPubConfirmCount(Long psnId) throws ServiceException {
    List<Long> confirmPubIds = pubAssignLogDao.queryPubConfirmCount(psnId);
    if (confirmPubIds != null && confirmPubIds.size() > 0) {
      return pubPdwhDAO.getPubCount(confirmPubIds);
    }
    return 0L;
  }

  @Override
  public void updateStatusByPdwhPubId(Long pdwhPubId, Integer status) throws ServiceException {
    try {
      pubAssignLogDao.updateStatus(pdwhPubId, status);
    } catch (Exception e) {
      logger.error("更新成果指派记录表status字段状态失败，pdwhPubId={},status={}", new Object[] {pdwhPubId, status}, e);
    }
  }

  public void psnPubCopartnerRcmd(Long pdwhPubId, Long psnId) throws ServiceException {
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
      logger.error("计算合作者出错--------pdwhPubId{},psnId{}", new Object[] {pdwhPubId, psnId}, e);
    }
  }
}
