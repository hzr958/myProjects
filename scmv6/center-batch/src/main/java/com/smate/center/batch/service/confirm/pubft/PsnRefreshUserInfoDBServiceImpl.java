package com.smate.center.batch.service.confirm.pubft;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.psn.inforefresh.PsnRefreshPubInfoDao;
import com.smate.center.batch.dao.sns.psn.inforefresh.PsnRefreshUserInfoDao;
import com.smate.center.batch.model.sns.psn.PsnRefreshPubInfo;
import com.smate.center.batch.model.sns.psn.PsnRefreshUserInfo;
import com.smate.center.batch.service.pub.mq.PsnRefreshInfoMessage;

@Service("psnRefreshUserInfoDBService")
@Transactional(rollbackFor = Exception.class)
public class PsnRefreshUserInfoDBServiceImpl implements PsnRefreshUserInfoDBService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PsnRefreshUserInfoDao psnRefreshUserInfoDao;
  @Autowired
  private PsnRefreshPubInfoDao psnRefreshPubInfoDao;

  @Override
  public void saveRefreshInfo(PsnRefreshInfoMessage msg) throws Exception {
    try {
      PsnRefreshUserInfo refInfo = psnRefreshUserInfoDao.get(msg.getPsnId());
      if (refInfo == null) {
        refInfo = new PsnRefreshUserInfo(msg.getPsnId());
      }
      refInfo.setIns(refInfo.getIns() == 0 ? msg.getIns() : 1);
      refInfo.setKwZt(refInfo.getKwZt() == 0 ? msg.getKwZt() : 1);
      refInfo.setPosition(refInfo.getPosition() == 0 ? msg.getPosition() : 1);
      refInfo.setPub(refInfo.getPub() == 0 ? msg.getPub() : 1);
      // 保存刷新的成果
      if (msg.getPub() == 1 && msg.getRefshPubId() != null && msg.getRefshPubId() > 0) {
        PsnRefreshPubInfo refPub = psnRefreshPubInfoDao.get(msg.getRefshPubId());
        if (refPub == null) {
          refPub = new PsnRefreshPubInfo(msg.getRefshPubId(), msg.getPsnId());
        }
        refPub.setIsDel(refPub.getIsDel() == 1 ? 1 : msg.getIsDelPub());
        refPub.setStatus(0);
        psnRefreshPubInfoDao.save(refPub);
      }

      refInfo.setNameFlag(refInfo.getNameFlag() == 0 ? msg.getNameFlag() : 1);
      refInfo.setEmailFlag(refInfo.getEmailFlag() == 0 ? msg.getEmailFlag() : 1);
      refInfo.setWorkFlag(refInfo.getWorkFlag() == 0 ? msg.getWorkFlag() : 1);
      refInfo.setPubToPdwh(refInfo.getPubToPdwh() == 0 ? msg.getPubToPdwh() : 1);
      refInfo.setDegree(refInfo.getDegree() == 0 ? msg.getDegree() : 1);
      refInfo.setAreaClf(refInfo.getAreaClf() == 0 ? msg.getAreaClf() : 1);
      refInfo.setRefc(refInfo.getRefc() == 0 ? msg.getRefc() : 1);
      refInfo.setCourseFlag(refInfo.getCourseFlag() == 0 ? msg.getCourseFlag() : 1);
      psnRefreshUserInfoDao.save(refInfo);
    } catch (Exception e) {
      logger.error("保存刷新结果psnId=" + msg.getPsnId(), e);
      throw new Exception("保存刷新结果psnId=" + msg.getPsnId(), e);
    }
  }

}
