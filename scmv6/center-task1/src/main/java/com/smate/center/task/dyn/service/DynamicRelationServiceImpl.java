package com.smate.center.task.dyn.service;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.task.dyn.dao.base.DynamicMsgDao;
import com.smate.center.task.dyn.dao.base.DynamicRelationDao;
import com.smate.center.task.dyn.dao.psn.AttPersonDao;
import com.smate.center.task.dyn.dao.psn.PrivacySettingsDao;
import com.smate.center.task.dyn.model.base.DynamicMsg;
import com.smate.center.task.dyn.model.base.DynamicRelation;
import com.smate.center.task.dyn.model.base.DynamicRelationPk;
import com.smate.center.task.model.sns.psn.PrivacySettings;
import com.smate.center.task.model.sns.psn.PrivacySettingsPK;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.dao.security.PsnPrivateDao;
import com.smate.core.base.utils.exception.DynException;

/**
 * 动态关系服务实现类
 * 
 * @author zk
 *
 */
@Service("dynamicRelationService")
@Transactional(rollbackOn = Exception.class)
public class DynamicRelationServiceImpl implements DynamicRelationService {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private DynamicMsgDao dynamicMsgDao;
  @Autowired
  private AttPersonDao attPersonDao;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private PsnPrivateDao psnPrivateDao;
  @Autowired
  private DynamicRelationDao dynamicRelationDao;
  @Autowired
  private PrivacySettingsDao privacySettingsDao;

  @Override
  public List<DynamicMsg> findDynNeedDeal(Integer size) throws DynException {
    return dynamicMsgDao.findDynNeedDeal(size);
  }

  @Override
  public void handleDynRelation(List<DynamicMsg> msgList) throws DynException {
    for (DynamicMsg msg : msgList) {
      this.handlePersonalDynRelation(msg);
    }
  }

  /**
   * 检查上传文献的权限， 2=仅本人
   * 
   * @param producer
   * @return
   */
  public Boolean checkAddRefDynPermit(Long producer) {
    PrivacySettingsPK pk = new PrivacySettingsPK();
    pk.setPsnId(producer);
    pk.setPrivacyAction("vMyLiter");
    PrivacySettings ps = privacySettingsDao.get(pk);
    if (ps != null && ps.getPermission() == 2) {
      return false;
    }
    return true;
  }

  /**
   * 处理具体人员动态关系
   * 
   * @param msg
   */
  private void handlePersonalDynRelation(DynamicMsg msg) {
    try {

      // 如果产生的是上传文献动态，则需要判断是否给 关注的人员生成动态
      if ("B3TEMP".equalsIgnoreCase(msg.getDynType()) && !checkAddRefDynPermit(msg.getProducer())) {
        logger.warn("动态关系建立任务中，动态产生者为隐私动态,psnId=" + msg.getProducer());
      } else if (psnPrivateDao.existsPsnPrivate(msg.getProducer())) {
        logger.warn("动态关系建立任务中，动态产生者为隐私人员,psnId=" + msg.getProducer());
      } else {
        List<Long> attPsnIds = attPersonDao.findAttPsnIds(msg.getProducer());
        if (CollectionUtils.isNotEmpty(attPsnIds)) {
          logger.info(msg.getProducer() + "存在" + attPsnIds.size() + "位好友");
          attPsnIds.remove(msg.getProducer());
          for (Long attPsnId : attPsnIds) {
            if (personDao.existsPerson(attPsnId) != null) {
              DynamicRelation dr = new DynamicRelation();
              DynamicRelationPk drPk = new DynamicRelationPk(msg.getDynId(), attPsnId);
              dr.setId(drPk);
              dr.setDealStatus(0);
              dynamicRelationDao.save(dr);
            }
          }
          logger.info(msg.getProducer() + "的动态已处理");
        }
      }
      msg.setRelDealStatus(1);
      dynamicMsgDao.updateDynRelDealStatus(msg.getDynId(), 1);
    } catch (Exception e) {
      logger.error("center-task1处理具体人员动态关系出错，msg=" + msg.getDynId(), e);
      dynamicMsgDao.updateDynRelDealStatus(msg.getDynId(), 2);
    }
  }
}
