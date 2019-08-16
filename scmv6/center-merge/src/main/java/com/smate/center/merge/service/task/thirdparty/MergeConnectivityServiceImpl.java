package com.smate.center.merge.service.task.thirdparty;

import com.smate.center.merge.dao.thirdparth.OpenUserUnionDao;
import com.smate.center.merge.dao.thirdparth.OpenUserUnionHisDao;
import com.smate.center.merge.model.sns.thirdparty.OpenUserUnion;
import com.smate.center.merge.model.sns.thirdparty.OpenUserUnionHis;
import com.smate.center.merge.service.task.MergeBaseService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 互联互通关联关系 处理.
 * 
 * @author tsz
 *
 * @date 2018年9月12日
 */
@Transactional(rollbackFor = Exception.class)
public class MergeConnectivityServiceImpl extends MergeBaseService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private OpenUserUnionDao openUserUnionDao;
  @Autowired
  private OpenUserUnionHisDao openUserUnionHisDao;

  @Override
  public boolean checkRun(Long savePsnId, Long delPsnId) throws Exception {
    List<OpenUserUnion> list = openUserUnionDao.getUnionByPsnId(delPsnId);
    if (CollectionUtils.isNotEmpty(list)) {
      return true;
    }
    return false;
  }

  @Override
  public boolean dealMerge(Long savePsnId, Long delPsnId) throws Exception {
    List<OpenUserUnion> list = openUserUnionDao.getUnionByPsnId(delPsnId);
    Long saveOpenId = openUserUnionDao.getOpenIdByPsnId(savePsnId);
    for (OpenUserUnion delOu : list) {
      // 迁移历史表
      saveHist(delOu);
      OpenUserUnion saveOu = openUserUnionDao.getOpenUserUnionByPsnIdAndToken(savePsnId, delOu.getToken());
      if (saveOu == null) {
        // 更新openId 跟psnId
        saveOu = new OpenUserUnion();
        saveOu.setOpenId(saveOpenId);
        saveOu.setCreateType(6);
        saveOu.setCreateDate(new Date());
        saveOu.setPsnId(savePsnId);
        saveOu.setToken(delOu.getToken());
        openUserUnionDao.save(saveOu);
      } else {
        openUserUnionDao.delete(delOu);
      }
    }
    return false;
  }

  private void saveHist(OpenUserUnion delOu) {
    OpenUserUnionHis delOuHis = new OpenUserUnionHis();
    delOuHis.setId(delOu.getId());
    delOuHis.setCreateDate(delOu.getCreateDate());
    delOuHis.setCreateType(delOu.getCreateType());
    delOuHis.setDealDate(null);
    delOuHis.setDelDate(new Date());
    delOuHis.setOpenId(delOu.getOpenId());
    delOuHis.setPsnId(delOu.getPsnId());
    if ("11111111".equals(delOu.getToken()) || "00000000".equals(delOu.getToken())) {
      delOuHis.setStatus(1);
    } else {
      delOuHis.setStatus(0);
    }
    delOuHis.setToken(delOu.getToken());
    openUserUnionHisDao.save(delOuHis);
  }
}
