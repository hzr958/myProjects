package com.smate.center.batch.service.pub;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.pub.DynMsgContentDao;
import com.smate.center.batch.dao.sns.pub.DynamicDao;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.DynMsgContent;
import com.smate.center.batch.model.sns.pub.Dynamic;
import com.smate.center.batch.util.pub.DynamicConstant;

/**
 * 动态消息同步实现.
 * 
 * @author chenxiangrong
 * 
 */
@Service("dynamicSyncService")
@Transactional(rollbackFor = Exception.class)
public class DynamicSyncServiceImpl implements DynamicSyncService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private DynamicExtendService dynamicExtendService;
  @Autowired
  private DynamicDao dynamicDao;
  @Autowired
  private DynMsgContentDao dynMsgContentDao;

  /**
   * 同步初始动态.
   */
  @Override
  public void syncDynamic(Dynamic dynamic, String extJson, Integer resType) throws ServiceException {
    if (dynamic.getStatus() == 0) {
      Dynamic dynamicSync = new Dynamic();
      dynamicSync.setDynParentId(dynamic.getDynParentId());
      dynamicSync.setProducer(dynamic.getProducer());
      dynamicSync.setReceiver(dynamic.getReceiver());
      dynamicSync.setObjectOwner(dynamic.getObjectOwner());
      dynamicSync.setGroupId(dynamic.getGroupId());
      dynamicSync.setTmpId(dynamic.getTmpId());
      dynamicSync.setDynType(dynamic.getDynType());
      dynamicSync.setSameFlag(dynamic.getSameFlag());
      dynamicSync.setDcId(dynamic.getDcId());
      dynamicSync.setDynJson(dynamic.getDynJson());
      dynamicSync.setRelation(dynamic.getRelation());
      if (dynamic.getProducer().longValue() == dynamic.getReceiver().longValue()) {
        dynamicSync.setVisible(DynamicConstant.DYN_VISIBLE_TRUE);
      } else {
        dynamicSync.setVisible(dynamic.getVisible());
      }
      dynamicSync.setPermission(dynamic.getPermission());
      dynamicSync.setDynDate(dynamic.getDynDate());
      dynamicSync.setUpdateDate(dynamic.getUpdateDate());
      dynamicSync.setStatus(dynamic.getStatus());
      dynamicSync.setSyncFlag(dynamic.getSyncFlag());
      this.dynamicDao.save(dynamicSync);

      this.saveDynMsgContent(dynamic);
      if (StringUtils.isNotBlank(extJson)) {// 保存DynamicExtend
        dynamicExtendService.saveDynamicExtends(extJson, dynamic.getDynId(), resType);
      }
    } else {
      // 删除动态.
      try {
        this.dynamicDao.updateAllDynamicStatus(dynamic.getDynId(), 1, 1);
      } catch (DaoException e) {
        logger.error("同步删除动态时出错啦！", dynamic.getDynId(), e);
      }
    }
  }

  private void saveDynMsgContent(Dynamic dyn) throws ServiceException {
    DynMsgContent dynMsgContent = this.dynMsgContentDao.get(dyn.getDcId());
    if (dynMsgContent == null) {
      dynMsgContent = new DynMsgContent(dyn.getDcId(), dyn.getDynJson());
      this.dynMsgContentDao.save(dynMsgContent);
    }
  }


}
