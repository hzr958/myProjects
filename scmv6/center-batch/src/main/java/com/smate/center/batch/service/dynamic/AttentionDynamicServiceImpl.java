package com.smate.center.batch.service.dynamic;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.dynamic.DynamicRelationDao;
import com.smate.center.batch.dao.sns.pub.DynamicMsgDao;
import com.smate.center.batch.dao.sns.pub.PrivacySettingsDao;
import com.smate.center.batch.model.dynamic.DynamicMsg;
import com.smate.center.batch.model.dynamic.DynamicRelation;
import com.smate.center.batch.model.dynamic.DynamicRelationPk;
import com.smate.center.batch.model.sns.pub.PrivacySettings;
import com.smate.center.batch.model.sns.pub.PrivacySettingsPK;
import com.smate.center.batch.tasklet.dynamic.AttentionDynamicForm;

/**
 * 关注成员动态任务
 * 
 * @author aijiangbin
 *
 */
@Service("attentionDynamicService")
@Transactional(rollbackFor = Exception.class)
public class AttentionDynamicServiceImpl implements AttentionDynamicService {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private DynamicRelationDao dynamicRelationDao;

  @Autowired
  private DynamicMsgDao dynamicMsgDao;
  @Autowired
  private PrivacySettingsDao privacySettingsDao;



  /**
   * 添加动态
   */
  @Override
  public void addAttentionDynamic(AttentionDynamicForm form) {
    // 获取关注人 产生的dynId
    List<Long> dynIdList = dynamicMsgDao.getDynListByPsnId(form.getAttPsnId(), form.getSize(), form.getPageNo());
    DynamicRelationPk pk = null;

    if (dynIdList != null && dynIdList.size() > 0) {
      for (Long dynId : dynIdList) {
        // 检查动态权限
        DynamicMsg msg = dynamicMsgDao.get(dynId);
        if ("B3TEMP".equalsIgnoreCase(msg.getDynType()) && !checkAddRefDynPermit(msg.getProducer())) {
          logger.warn("动态关系建立任务中，动态产生者为隐私动态,psnId=" + msg.getProducer());
          continue;
        }
        pk = new DynamicRelationPk();
        pk.setReceiver(form.getPsnId());
        pk.setDynId(dynId);
        DynamicRelation dynRelation = dynamicRelationDao.getDynRelation(pk);
        if (dynRelation == null) {
          dynRelation = new DynamicRelation();
          dynRelation.setId(pk);
        }
        dynRelation.setDealStatus(0);
        dynamicRelationDao.save(dynRelation);
      }
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
   * 删除动态
   */
  @Override
  public void delAttentionDynamic(AttentionDynamicForm form) {
    // 获取关注人 产生的dynId
    List<Long> dynIdList = dynamicMsgDao.getDynListByPsnId(form.getAttPsnId(), form.getSize(), form.getPageNo());
    if (dynIdList != null && dynIdList.size() > 0) {
      for (Long dynId : dynIdList) {
        dynamicRelationDao.deleteDynRelation(dynId, form.getPsnId());
      }
    }
  }


  @Override
  public void getDynamicMsgCount(AttentionDynamicForm form) {
    Integer count = dynamicMsgDao.getDynListForPsnCount(form.getAttPsnId());
    form.setCount(count);
  }

  /**
   * 处理动态
   */
  @Override
  public void dealAttentionDynamic(AttentionDynamicForm form) {

    if (form.getStatus() == 0) {
      addAttentionDynamic(form);
    } else {
      delAttentionDynamic(form);
    }

  }


}
