package com.smate.center.batch.service.pub;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.pub.RcmdSyncGroupInfoDao;
import com.smate.center.batch.dao.sns.pub.RcmdSyncPsnInfoDao;
import com.smate.center.batch.dao.sns.pub.RcmdSyncPubInfoDao;
import com.smate.center.batch.dao.sns.pub.RcmdSyncRefInfoDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.RcmdSyncGroupInfo;
import com.smate.center.batch.model.sns.pub.RcmdSyncPsnInfo;
import com.smate.center.batch.model.sns.pub.RcmdSyncPubInfo;
import com.smate.center.batch.model.sns.pub.RcmdSyncRefInfo;
import com.smate.center.batch.service.pub.mq.RcmdSyncFlagMessage;

/**
 * 推荐信息同步服务.
 * 
 * @author lqh
 * 
 */
@Service("rcmdSyncDBService")
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
public class RcmdSyncDBServiceImpl implements RcmdSyncDBService {

  /**
   * 
   */
  private static final long serialVersionUID = 4047361745806272337L;
  @Autowired
  private RcmdSyncPsnInfoDao rcmdSyncPsnInfoDao;
  @Autowired
  private RcmdSyncPubInfoDao rcmdSyncPubInfoDao;
  @Autowired
  private RcmdSyncRefInfoDao rcmdSyncRefInfoDao;
  @Autowired
  private RcmdSyncGroupInfoDao rcmdSyncGroupPsnInfoDao;

  @Override
  public synchronized void saveSyncInfo(RcmdSyncFlagMessage msg) throws ServiceException {

    /*
     * TODO 2015-11-19 线程同步,因此不能够负载,如果有负载需求,需解决RCMD_SYNC_PSNINFO表的不同线程之间的脏数据问题
     */
    RcmdSyncPsnInfo rsp = rcmdSyncPsnInfoDao.get(msg.getPsnId());
    if (rsp == null) {
      rsp = new RcmdSyncPsnInfo(msg.getPsnId());
    }
    rsp.setAdditinfoFlag(msg.getAdditinfoFlag() == 0 ? rsp.getAdditinfoFlag() : msg.getAdditinfoFlag());
    rsp.setAttFlag(msg.getAttFlag() == 0 ? rsp.getAttFlag() : msg.getAttFlag());
    rsp.setContactFlag(msg.getContactFlag() == 0 ? rsp.getContactFlag() : msg.getContactFlag());
    rsp.setEduFlag(msg.getEduFlag() == 0 ? rsp.getEduFlag() : msg.getEduFlag());
    rsp.setEmailFlag(msg.getEmailFlag() == 0 ? rsp.getEmailFlag() : msg.getEmailFlag());
    rsp.setExperienceFlag(msg.getExperienceFlag() == 0 ? rsp.getExperienceFlag() : msg.getExperienceFlag());
    rsp.setFriendFlag(msg.getFriendFlag() == 0 ? rsp.getFriendFlag() : msg.getFriendFlag());
    rsp.setGroupFlag(msg.getGroupFlag() == 0 ? rsp.getGroupFlag() : msg.getGroupFlag());
    rsp.setInsFlag(msg.getInsFlag() == 0 ? rsp.getInsFlag() : msg.getInsFlag());
    rsp.setKwztFlag(msg.getKwztFlag() == 0 ? rsp.getKwztFlag() : msg.getKwztFlag());
    rsp.setNameFlag(msg.getNameFlag() == 0 ? rsp.getNameFlag() : msg.getNameFlag());
    rsp.setPrivacyFlag(msg.getPrivacyFlag() == 0 ? rsp.getPrivacyFlag() : msg.getPrivacyFlag());
    rsp.setPsnSetFlag(msg.getPsnSetFlag() == 0 ? rsp.getPsnSetFlag() : msg.getPsnSetFlag());
    rsp.setPubFlag(msg.getPubFlag() == 0 ? rsp.getPubFlag() : msg.getPubFlag());
    rsp.setTaughtFlag(msg.getTaughtFlag() == 0 ? rsp.getTaughtFlag() : msg.getTaughtFlag());
    rsp.setWorkFlag(msg.getWorkFlag() == 0 ? rsp.getWorkFlag() : msg.getWorkFlag());
    rsp.setRefcFlag(msg.getRefcFlag() == 0 ? rsp.getRefcFlag() : msg.getRefcFlag());
    // SCM-15465_start
    if (msg.getPubFlag() > 0 && rsp.getStatus() == 9) {
      rsp.setStatus(0);
    }
    // SCM-15465_end
    rcmdSyncPsnInfoDao.save(rsp);
    // 变更的成果
    if (msg.getPubFlag() > 0 && MapUtils.isNotEmpty(msg.getPubs())) {
      for (Long pubId : msg.getPubs().keySet()) {
        Integer isDel = msg.getPubs().get(pubId);
        RcmdSyncPubInfo rp = rcmdSyncPubInfoDao.get(pubId);
        if (rp == null) {
          rp = new RcmdSyncPubInfo(pubId, msg.getPsnId(), isDel);
        } else {
          rp.setIsDel(isDel);
          rp.setStatus(0);
        }
        rcmdSyncPubInfoDao.save(rp);
      }
    }
    // 变更的参考文献
    if (msg.getRefcFlag() > 0 && MapUtils.isNotEmpty(msg.getRefcs())) {
      for (Long refcId : msg.getRefcs().keySet()) {
        Integer isDel = msg.getRefcs().get(refcId);
        RcmdSyncRefInfo rr = rcmdSyncRefInfoDao.get(refcId);
        if (rr == null) {
          rr = new RcmdSyncRefInfo(refcId, msg.getPsnId(), isDel);
        } else {
          rr.setIsDel(isDel);
          rr.setStatus(0);
        }
        rcmdSyncRefInfoDao.save(rr);
      }
    }
    // 变更的群组
    if (msg.getGroupFlag() > 0 && MapUtils.isNotEmpty(msg.getGroups())) {
      for (Long groupId : msg.getGroups().keySet()) {
        Integer isDel = msg.getGroups().get(groupId);
        RcmdSyncGroupInfo rg = rcmdSyncGroupPsnInfoDao.get(groupId);
        if (rg == null) {
          rg = new RcmdSyncGroupInfo(groupId, msg.getPsnId(), isDel);
        } else {
          rg.setIsDel(isDel);
          rg.setStatus(0);
        }
        rcmdSyncGroupPsnInfoDao.save(rg);
      }
    }

  }

  /**
   * 群组信息和成员变动，同步到推荐服务
   */
  @Override
  public void saveRcmdSyncInfo(RcmdSyncFlagMessage msg) throws ServiceException {
    // 变更的成员
    RcmdSyncPsnInfo rsp = updateRcmdSyncPsnInfo(msg);
    // 变更的成果
    if (msg.getPubFlag() > 0 && MapUtils.isNotEmpty(msg.getPubs())) {
      updateRcmdSyncPubInfo(msg);
    }
    // 变更的参考文献
    if (msg.getRefcFlag() > 0 && MapUtils.isNotEmpty(msg.getRefcs())) {
      updateRcmdSyncRefInfo(msg);
    }
    // 变更的群组
    if (msg.getGroupFlag() > 0 && MapUtils.isNotEmpty(msg.getGroups())) {
      updateRcmdSyncGroupInfo(msg);
    }
  }

  /**
   * 变更的成员
   * 
   * @return
   */
  private RcmdSyncPsnInfo updateRcmdSyncPsnInfo(RcmdSyncFlagMessage msg) {
    RcmdSyncPsnInfo rsp = rcmdSyncPsnInfoDao.get(msg.getPsnId());
    if (rsp == null) {
      rsp = new RcmdSyncPsnInfo(msg.getPsnId());
    }
    rsp.setAdditinfoFlag(msg.getAdditinfoFlag() == 0 ? rsp.getAdditinfoFlag() : msg.getAdditinfoFlag());
    rsp.setAttFlag(msg.getAttFlag() == 0 ? rsp.getAttFlag() : msg.getAttFlag());
    rsp.setContactFlag(msg.getContactFlag() == 0 ? rsp.getContactFlag() : msg.getContactFlag());
    rsp.setEduFlag(msg.getEduFlag() == 0 ? rsp.getEduFlag() : msg.getEduFlag());
    rsp.setEmailFlag(msg.getEmailFlag() == 0 ? rsp.getEmailFlag() : msg.getEmailFlag());
    rsp.setExperienceFlag(msg.getExperienceFlag() == 0 ? rsp.getExperienceFlag() : msg.getExperienceFlag());
    rsp.setFriendFlag(msg.getFriendFlag() == 0 ? rsp.getFriendFlag() : msg.getFriendFlag());
    rsp.setGroupFlag(msg.getGroupFlag() == 0 ? rsp.getGroupFlag() : msg.getGroupFlag());
    rsp.setInsFlag(msg.getInsFlag() == 0 ? rsp.getInsFlag() : msg.getInsFlag());
    rsp.setKwztFlag(msg.getKwztFlag() == 0 ? rsp.getKwztFlag() : msg.getKwztFlag());
    rsp.setNameFlag(msg.getNameFlag() == 0 ? rsp.getNameFlag() : msg.getNameFlag());
    rsp.setPrivacyFlag(msg.getPrivacyFlag() == 0 ? rsp.getPrivacyFlag() : msg.getPrivacyFlag());
    rsp.setPsnSetFlag(msg.getPsnSetFlag() == 0 ? rsp.getPsnSetFlag() : msg.getPsnSetFlag());
    rsp.setPubFlag(msg.getPubFlag() == 0 ? rsp.getPubFlag() : msg.getPubFlag());
    rsp.setTaughtFlag(msg.getTaughtFlag() == 0 ? rsp.getTaughtFlag() : msg.getTaughtFlag());
    rsp.setWorkFlag(msg.getWorkFlag() == 0 ? rsp.getWorkFlag() : msg.getWorkFlag());
    rsp.setRefcFlag(msg.getRefcFlag() == 0 ? rsp.getRefcFlag() : msg.getRefcFlag());
    // SCM-15465_start
    if (msg.getPubFlag() > 0 && rsp.getStatus() == 9) {
      rsp.setStatus(0);
    }
    // SCM-15465_end
    rcmdSyncPsnInfoDao.save(rsp);
    return rsp;

  }

  /**
   * 变更的成果
   * 
   * @param msg
   */
  private void updateRcmdSyncPubInfo(RcmdSyncFlagMessage msg) {
    for (Long pubId : msg.getPubs().keySet()) {
      Integer isDel = msg.getPubs().get(pubId);
      RcmdSyncPubInfo rp = rcmdSyncPubInfoDao.get(pubId);
      if (rp == null) {
        rp = new RcmdSyncPubInfo(pubId, msg.getPsnId(), isDel);
      } else {
        rp.setIsDel(isDel);
        rp.setStatus(0);
      }
      rcmdSyncPubInfoDao.save(rp);
    }
  }

  /**
   * 变更的参考文献
   * 
   * @param msg
   */
  private void updateRcmdSyncRefInfo(RcmdSyncFlagMessage msg) {
    for (Long refcId : msg.getRefcs().keySet()) {
      Integer isDel = msg.getRefcs().get(refcId);
      RcmdSyncRefInfo rr = rcmdSyncRefInfoDao.get(refcId);
      if (rr == null) {
        rr = new RcmdSyncRefInfo(refcId, msg.getPsnId(), isDel);
      } else {
        rr.setIsDel(isDel);
        rr.setStatus(0);
      }
      rcmdSyncRefInfoDao.save(rr);
    }
  }

  /**
   * 变更的群组
   * 
   * @param msg
   */
  private void updateRcmdSyncGroupInfo(RcmdSyncFlagMessage msg) {
    for (Long groupId : msg.getGroups().keySet()) {
      Integer isDel = msg.getGroups().get(groupId);
      RcmdSyncGroupInfo rg = rcmdSyncGroupPsnInfoDao.get(groupId);
      if (rg == null) {
        rg = new RcmdSyncGroupInfo(groupId, msg.getPsnId(), isDel);
      } else {
        rg.setIsDel(isDel);
        rg.setStatus(0);
      }
      rcmdSyncGroupPsnInfoDao.save(rg);
    }
  }
}
