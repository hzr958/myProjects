package com.smate.center.task.service.sns.psn;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.sns.grp.GrpMemberDao;
import com.smate.center.task.dao.sns.psn.PsnCopartnerDao;
import com.smate.center.task.dao.sns.pub.PubAssignLogDetailDao;
import com.smate.center.task.dao.sns.quartz.AppSettingDao;
import com.smate.center.task.model.sns.psn.PsnCopartner;
import com.smate.center.task.model.sns.pub.PubAssignLogDetail;
import com.smate.core.base.psn.dao.PsnPubDAO;
import com.smate.core.base.pub.po.dao.PubPdwhSnsRelationDAO;

@Service("generatePsnCopartnerService")
@Transactional(rollbackFor = Exception.class)
public class GeneratePsnCopartnerServiceImpl implements GeneratePsnCopartnerService {
  @Autowired
  private PsnPubDAO psnPubDAO;
  @Autowired
  private PubPdwhSnsRelationDAO pubPdwhSnsRelationDAO;
  @Autowired
  private PubAssignLogDetailDao pubAssignLogDetailDao;
  @Autowired
  private PsnCopartnerDao psnCopartnerDao;
  @Autowired
  private GrpMemberDao grpMemberDao;
  @Autowired
  private AppSettingDao appSettingDao;

  @Override
  public List<Long> gethandlePubPsnId(Long lastPsnId, int batchSize) {
    return psnPubDAO.gethandlePsnId(lastPsnId, batchSize);
  }

  @Override
  public List<Long> gethandlePrjPsnId(Long lastPsnId, int batchSize) {
    return grpMemberDao.getHandlePsnId(lastPsnId, batchSize);

  }

  @Override
  public List<Long> getPsnPubIdList(Long psnId) {
    return psnPubDAO.getPubIdsByPsnId(psnId);
  }

  @Override
  public List<Long> getpdwhPubIds(List<Long> psnPubIdList) {
    return pubPdwhSnsRelationDAO.getPdwhPubIdsBySnsPubId(psnPubIdList);
  }

  @Override
  public void savePsnPubCopartner(Long psnId, List<Long> pdwhPubIds) {
    // 找到认领了该条基准库成果的psnId
    for (Long pdwhPubId : pdwhPubIds) {
      // 排除当前人
      List<PubAssignLogDetail> PubAssignLogDetails = pubAssignLogDetailDao.getAssignDetailByPdwhId(pdwhPubId, psnId);
      for (PubAssignLogDetail pubAssignLogDetail : PubAssignLogDetails) {
        PsnCopartner psnCopartner = psnCopartnerDao.getPsnCopartner(psnId, pubAssignLogDetail.getPsnId(), pdwhPubId, 1);
        if (psnCopartner == null) {
          psnCopartner = new PsnCopartner(psnId, pdwhPubId, 1);
          psnCopartner.setCoPsnId(pubAssignLogDetail.getPsnId());
        }
        psnCopartner.setPdwhPubName(pubAssignLogDetail.getPubMemberName());
        psnCopartner.setPdwhPubMemberId(pubAssignLogDetail.getPubMemberId());
        psnCopartnerDao.saveWithNewTransaction(psnCopartner);
      }
    }
  }

  @Override
  public List<Long> findPrjGroupIdsByPsnId(Long psnId) {
    return grpMemberDao.findPrjGroupIdsByPsnId(psnId);
  }

  @Override
  public List<Long> getPrjCoBygrpId(Long prjGroupId, Long psnId) {
    return grpMemberDao.getPrjCoBygrpId(prjGroupId, psnId);
  }

  @Override
  public void savePsnPrjCopartner(Long psnId, List<Long> coPsnIdList, Long prjGroupId) {
    for (Long coPsnId : coPsnIdList) {
      PsnCopartner psnCopartner = new PsnCopartner(psnId, coPsnId, prjGroupId, 2);
      psnCopartnerDao.saveOrUpdate(psnCopartner);
    }
  }

  @Override
  public void upAppSettingConstant(String snsPsnPrjCopartnerStart, Long lastPubId) {
    appSettingDao.updateAppSetting(snsPsnPrjCopartnerStart, lastPubId.toString());
  }

  @Override
  public void deletePsnCopartner(Long psnId, int coType) {
    psnCopartnerDao.deletePsnCopartner(psnId, coType);
  }


}
