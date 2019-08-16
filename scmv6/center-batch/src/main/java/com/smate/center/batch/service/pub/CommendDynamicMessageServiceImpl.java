package com.smate.center.batch.service.pub;

import java.util.Date;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.pub.DynMsgContentDao;
import com.smate.center.batch.dao.sns.pub.DynamicDao;
import com.smate.center.batch.dao.sns.pub.DynamicPsnRefreshDao;
import com.smate.center.batch.model.sns.pub.DynMsgContent;
import com.smate.center.batch.model.sns.pub.Dynamic;
import com.smate.center.batch.model.sns.pub.DynamicPsnRefresh;
import com.smate.center.batch.service.pub.mq.SnsDynamicSyncProducer;
import com.smate.center.batch.util.pub.DynamicConstant;
import com.smate.core.base.utils.constant.ServiceConstants;

/**
 * 生成动态 共用方法 抽取类2015.1.12_scm-6454
 * 
 * @author tsz
 * 
 */
@Service("commendDynamicMessageService")
@Transactional(rollbackFor = Exception.class)
public class CommendDynamicMessageServiceImpl implements CommendDynamicMessageService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  protected DynamicExtendService dynamicExtendService;
  @Resource(name = "snsDynamicSyncProducer")
  protected SnsDynamicSyncProducer snsDynamicSyncProducer;
  @Autowired
  protected DynamicDao dynamicDao;
  @Autowired
  protected DynamicPsnRefreshDao dynamicPsnRefreshDao;
  @Autowired
  private DynMsgContentDao dynMsgContentDao;

  /**
   * 重dynamicProduceAbstract抽取出来 tsz 生成动态公用入口.
   */
  @Override
  public void produceDynamic(JSONObject jsonObject, String dynJson, boolean extFlag) {
    try {
      // 保存初始动态信息.
      Long producer = jsonObject.getLong("producer");
      Dynamic dynamic = new Dynamic();
      dynamic.setDynParentId(-1l);
      dynamic.setProducer(producer);
      dynamic.setReceiver(producer);
      if (jsonObject.containsKey("objectOwner")) {
        dynamic.setObjectOwner(jsonObject.getLong("objectOwner"));
      }
      if (jsonObject.containsKey("groupId"))
        dynamic.setGroupId(jsonObject.getLong("groupId"));
      else
        dynamic.setGroupId(0l);

      int dynType = Integer.valueOf(jsonObject.get("dynType").toString());
      dynamic.setDynType(dynType);
      dynamic.setSameFlag(jsonObject.getString("sameFlag"));
      dynamic.setTmpId(jsonObject.getInt("tmpId"));

      dynamic.setRelation(DynamicConstant.DYN_RELATION_ME);
      dynamic.setVisible(DynamicConstant.DYN_VISIBLE_TRUE);
      int permission = jsonObject.getInt("permission");
      dynamic.setPermission(permission);
      Date nowDate = new Date();
      dynamic.setDynDate(nowDate);
      dynamic.setUpdateDate(nowDate);
      dynamic.setStatus(0);
      dynamic.setSyncFlag(jsonObject.getInt("syncFlag"));
      Long dcId = this.dynMsgContentDao.getDcId();
      dynamic.setDcId(dcId);

      dynamicDao.save(dynamic);
      DynMsgContent dynMsgContent = new DynMsgContent(dynamic.getDcId(), dynJson);
      this.dynMsgContentDao.save(dynMsgContent);
      if (extFlag) {// 保存DynamicExtend
        dynamicExtendService.saveDynamicExtends(jsonObject.getString("resDetails"), dynamic.getDynId(),
            jsonObject.getInt("resType"));
      }

      // 添加动态清理人员记录
      // TODO 临时处理，先把多线程的时，由于事务隔离级别不同造成 insert操作不同步，而违反唯一约束的错误捕捉到，不抛出
      try {
        DynamicPsnRefresh dynamicPsnRefresh = this.dynamicPsnRefreshDao.get(producer);
        if (dynamicPsnRefresh == null) {
          dynamicPsnRefresh = new DynamicPsnRefresh();
          dynamicPsnRefresh.setPsnId(producer);
          dynamicPsnRefresh.setUpdateDate(new Date());
          dynamicPsnRefreshDao.save(dynamicPsnRefresh);
        }
      } catch (Throwable e) {
        logger.error("个人-保存动态信息DYNAMIC_PSN_REFRESH出错", e);
      }
      // 同步初始动态信息(仅本人可见，不需要同步)======会造成重复生成动态，先注释。hzr
      /*
       * if (permission != DynamicConstant.DYN_PERMISSION_ME) { dynamic.setDynJson(dynJson); if (extFlag)
       * { snsDynamicSyncProducer.sendSyncMessage(ServiceConstants.SCHOLAR_NODE_ID_1, dynamic,
       * jsonObject.getString("resDetails"), jsonObject.getInt("resType")); } else {
       * snsDynamicSyncProducer.sendSyncMessage(ServiceConstants.SCHOLAR_NODE_ID_1, dynamic, null,
       * jsonObject.getInt("resType")); } }
       */

    } catch (Exception e) {
      logger.error("个人-保存动态信息出错", e);
    }
  }

}
