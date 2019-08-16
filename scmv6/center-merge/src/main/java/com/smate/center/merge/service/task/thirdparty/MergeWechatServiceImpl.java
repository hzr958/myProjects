package com.smate.center.merge.service.task.thirdparty;

import com.smate.center.merge.dao.thirdparth.OpenUserUnionDao;
import com.smate.center.merge.dao.thirdparth.WeChatRelationDao;
import com.smate.center.merge.model.sns.thirdparty.WeChatRelation;
import com.smate.center.merge.service.task.MergeBaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * 处理微信关联记录.优先于帐号互联互通
 * 
 * @author tsz
 *
 * @date 2018年9月12日
 */
@Transactional(rollbackFor = Exception.class)
public class MergeWechatServiceImpl extends MergeBaseService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private WeChatRelationDao weChatRelationDao;
  @Autowired
  private OpenUserUnionDao openUserUnionDao;

  @Override
  public boolean checkRun(Long savePsnId, Long delPsnId) throws Exception {
    Long saveOpenId = openUserUnionDao.getOpenIdByPsnId(savePsnId);
    Long delOpenId = openUserUnionDao.getOpenIdByPsnId(delPsnId);
    WeChatRelation saveWeChat = weChatRelationDao.getBySmateOpenId(saveOpenId);
    WeChatRelation delWeChat = weChatRelationDao.getBySmateOpenId(delOpenId);
    if (delWeChat != null && saveWeChat == null) {
      return true;
    }
    return false;
  }

  @Override
  public boolean dealMerge(Long savePsnId, Long delPsnId) throws Exception {
    Long saveOpenId = openUserUnionDao.getOpenIdByPsnId(savePsnId);
    Long delOpenId = openUserUnionDao.getOpenIdByPsnId(delPsnId);
    WeChatRelation delWeChat = weChatRelationDao.getBySmateOpenId(delOpenId);
    delWeChat.setSmateOpenId(saveOpenId);
    weChatRelationDao.save(delWeChat);
    return true;
  }
}
