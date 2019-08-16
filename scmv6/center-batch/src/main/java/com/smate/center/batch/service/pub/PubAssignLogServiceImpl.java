package com.smate.center.batch.service.pub;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.smate.center.batch.constant.RcmdDynamicConstants;
import com.smate.center.batch.dao.pdwh.pubimport.PdwhPubDupDao;
import com.smate.center.batch.dao.sns.pub.PubAssignLogDao;
import com.smate.center.batch.model.sns.pub.PubAssignLog;
import com.smate.center.batch.service.pub.rcmd.DynRecomPsnTaskService;
import com.smate.core.base.utils.collections.ListUtils;

@Service("pubAssignLogService")
@Transactional(rollbackFor = Exception.class)
public class PubAssignLogServiceImpl implements PubAssignLogService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private DynRecomPsnTaskService dynRecomPsnTaskService;
  @Autowired
  private PubAssignLogDao pubAssignLogDao;
  @Autowired
  private PdwhPubDupDao pdwhPubDupDao;

  @Override
  public List<Long> getDupPubConfirm(Long zhTitleHashCode, Long enTitleHashCode, Long titleHashValue, Integer pubType,
      String pubYear, Long psnId) {
    Assert.notNull(pubType, "pubType不能为空");
    List<Long> dupPubIds =
        this.pdwhPubDupDao.getDupPubIds(zhTitleHashCode, enTitleHashCode, titleHashValue, pubType, pubYear);

    List<Long> dupPubConfirmList = new ArrayList<Long>();
    if (CollectionUtils.isNotEmpty(dupPubIds)) {
      if (dupPubIds.size() > 1000) {
        List<Long>[] split = ListUtils.split(dupPubIds, 1000);
        List<Long> dupPubConfirmIds = null;
        for (List<Long> list : split) {
          dupPubConfirmIds = pubAssignLogDao.getDupPubConfirmNew(list, psnId);
          dupPubConfirmList.addAll(dupPubConfirmIds);
        }
      } else {
        dupPubConfirmList = pubAssignLogDao.getDupPubConfirmNew(dupPubIds, psnId);
      }

    }

    return dupPubConfirmList;
  }

  @Override
  public void autoConfirmPubSimple(List<Long> dupPubConfirmIds, Long psnId) throws Exception {
    try {
      if (CollectionUtils.isEmpty(dupPubConfirmIds)) {
        return;
      }
      for (Long pdwhPubId : dupPubConfirmIds) {
        PubAssignLog pubAssignLog = pubAssignLogDao.getPubAssign(pdwhPubId, psnId);
        if (pubAssignLog == null) {
          continue;
        }
        pubAssignLog.setIsAutoConfirm(1);
        pubAssignLog.setConfirmResult(1);
        pubAssignLog.setConfirmDate(new Date());
        pubAssignLogDao.save(pubAssignLog);
        // MJG_SCM-5987.
        dynRecomPsnTaskService.saveDynRePsnTask(psnId, RcmdDynamicConstants.DYN_RECOMMEND_TYPE_PUB);
      }
    } catch (Exception e) {
      logger.error("PubAssignLogServiceImpl成果自动确认出错", e);
      throw new Exception(e);
    }

  }

}
