package com.smate.center.batch.service.pub;

import java.util.Date;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.pub.DynamicGroupContentDao;
import com.smate.center.batch.dao.sns.pub.DynamicGroupDao;
import com.smate.center.batch.dao.sns.pub.DynamicPsnRefreshDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.DynamicGroup;
import com.smate.center.batch.model.sns.pub.DynamicGroupContent;
import com.smate.center.batch.model.sns.pub.DynamicPsnRefresh;
import com.smate.center.batch.util.pub.DynamicConstant;

/**
 * 生成群组动态 共用方法 抽取类2015.1.12_scm-6454
 * 
 * @author tsz
 * 
 */
@Service("commendGroupDynamicMessageService")
@Transactional(rollbackFor = Exception.class)
public class CommendGroupDynamicMessageServiceImpl implements CommendGroupDynamicMessageService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  protected DynamicExtendService dynamicExtendService;
  @Autowired
  protected DynamicBuildJsonService dynamicBuildJsonService;
  @Autowired
  protected DynamicGroupDao dynamicGroupDao;
  @Autowired
  protected DynamicPsnRefreshDao dynamicPsnRefreshDao;
  @Autowired
  private DynamicGroupContentDao dynamicGroupContentDao;

  /**
   * 重dynamicGroupProduceAbstract抽取出来 tsz 生成群组动态公用入口.
   */
  @Override
  public void produceGroupDynamic(JSONObject jsonObject, String dynJson, boolean extFlag) {
    if (!jsonObject.containsKey("groupId")) {
      return;
    }
    // 保存初始动态信息.
    DynamicGroup dynamic = this.buildDynamicGroup(jsonObject);
    dynamicGroupDao.saveDynamicGroup(dynamic);

    DynamicGroupContent dynamicContent = new DynamicGroupContent(dynamic.getGroupDynId(), dynJson);
    this.dynamicGroupContentDao.saveDynGroupCon(dynamicContent);
    if (extFlag) {// 保存DynamicExtend
      try {
        dynamicExtendService.saveDynamicExtends(jsonObject.getString("resDetails"), dynamic.getGroupDynId(),
            jsonObject.getInt("resType"));
      } catch (ServiceException e) {
        logger.error("保存动态扩展信息出错", e);
      }
    }

    // 添加动态清理人员记录
    DynamicPsnRefresh dynamicPsnRefresh = this.dynamicPsnRefreshDao.get(dynamic.getProducer());
    if (dynamicPsnRefresh == null) {
      dynamicPsnRefresh = new DynamicPsnRefresh();
      dynamicPsnRefresh.setPsnId(dynamic.getProducer());
      dynamicPsnRefresh.setUpdateDate(new Date());
      dynamicPsnRefreshDao.save(dynamicPsnRefresh);
    }

  }

  /**
   * 构建群组动态对象.
   * 
   * @param jsonObject
   * @return
   */
  private DynamicGroup buildDynamicGroup(JSONObject jsonObject) {
    Long producer = jsonObject.getLong("producer");
    DynamicGroup dynamic = new DynamicGroup();
    dynamic.setDynParentId(-1l);
    dynamic.setProducer(producer);
    dynamic.setReceiver(producer);
    if (jsonObject.containsKey("objectOwner")) {
      dynamic.setObjectOwner(jsonObject.getLong("objectOwner"));
    }
    dynamic.setGroupId(jsonObject.getLong("groupId"));

    int dynType = Integer.valueOf(jsonObject.get("dynType").toString());
    dynamic.setDynType(dynType);
    dynamic.setSameFlag(jsonObject.getString("sameFlag"));
    dynamic.setTmpId(jsonObject.getInt("tmpId"));

    dynamic.setRelation(DynamicConstant.DYN_RELATION_ME);
    dynamic.setVisible(DynamicConstant.DYN_VISIBLE_TRUE);
    int permission = jsonObject.getInt("permission");
    dynamic.setPermission(permission);
    Date nowDate = new Date();
    dynamic.setCreateDate(nowDate);
    dynamic.setUpdateDate(nowDate);
    dynamic.setStatus(0);
    dynamic.setSyncFlag(jsonObject.getInt("syncFlag"));
    return dynamic;
  }
}
