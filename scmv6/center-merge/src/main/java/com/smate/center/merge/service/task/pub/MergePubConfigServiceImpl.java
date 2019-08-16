package com.smate.center.merge.service.task.pub;

import com.smate.center.merge.dao.psnconf.PsnConfigDao;
import com.smate.center.merge.dao.psnconf.PsnConfigPubDao;
import com.smate.center.merge.model.sns.psnconf.PsnConfig;
import com.smate.center.merge.model.sns.psnconf.PsnConfigPub;
import com.smate.center.merge.model.sns.task.AccountsMergeData;
import com.smate.center.merge.service.task.MergeBaseService;
import com.smate.core.base.utils.json.JacksonUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 成果权限配置合并.优先于 psnPub 处理.
 * 
 * @author tsz
 *
 * @date 2018年9月11日
 */
@Transactional(rollbackFor = Exception.class)
public class MergePubConfigServiceImpl extends MergeBaseService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PsnConfigDao psnConfigDao;
  @Autowired
  private PsnConfigPubDao psnConfigPubDao;


  @Override
  public boolean checkRun(Long savePsnId, Long delPsnId) throws Exception {
    PsnConfig psnconfig = psnConfigDao.getByPsn(delPsnId);
    if (psnconfig == null) {
      return false;
    }
    List<PsnConfigPub> psnconfigPubs = psnConfigPubDao.gets(psnconfig.getCnfId());
    if (CollectionUtils.isEmpty(psnconfigPubs)) {
      return false;
    }
    return true;
  }

  @Override
  public boolean dealMerge(Long savePsnId, Long delPsnId) throws Exception {
    try {
      PsnConfig saveConfig = psnConfigDao.getByPsn(savePsnId);
      PsnConfig delConfig = psnConfigDao.getByPsn(delPsnId);
      List<PsnConfigPub> psnconfigPubs = psnConfigPubDao.gets(delConfig.getCnfId());
      for (PsnConfigPub pubconf : psnconfigPubs) {
        try {
          // 保存备份记录
          String desc = "合并成果权限配置 表， PSN_CONFIG_PUB ";
          AccountsMergeData accountsMergeData =
              super.saveAccountsMergeData(savePsnId, delPsnId, desc, super.DEAL_TYPE_MERGE, pubconf);
          psnConfigPubDao.updateConf(saveConfig.getCnfId(), delConfig.getCnfId(), pubconf.getId().getPubId());
          super.updateAccountsMergeDataStatus(accountsMergeData);
        } catch (Exception e) {
          logger.error(JacksonUtils.jsonObjectSerializer(pubconf), e);
          throw new Exception(e);
        }
      }
    } catch (Exception e) {
      logger.error("帐号合并->成果合并->合并成果权限配置 表 出错 savePsnId=" + savePsnId + ",delPsnId=" + delPsnId, e);
      throw new Exception("帐号合并->群组合并->合并成果权限配置 表  出错    savePsnId=" + savePsnId + ",delPsnId=" + delPsnId, e);
    }
    return true;
  }
}
