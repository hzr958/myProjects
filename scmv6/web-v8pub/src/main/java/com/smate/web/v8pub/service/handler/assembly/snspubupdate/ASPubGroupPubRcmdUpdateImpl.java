package com.smate.web.v8pub.service.handler.assembly.snspubupdate;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.string.StringUtils;
import com.smate.web.v8pub.dao.sns.group.GrpPubRcmdDao;
import com.smate.web.v8pub.enums.PubGenreConstants;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.po.sns.group.GrpPubRcmd;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.pdwh.PdwhPubDuplicateService;

/**
 * 用于群组导入成果时，将与该成果查重得到的所有推荐给该群组的记录，进行成果自动认领。
 * 
 * @author SYL
 * 
 *         2019年4月8日
 *
 */
@Transactional(rollbackFor = Exception.class)
public class ASPubGroupPubRcmdUpdateImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PdwhPubDuplicateService pdwhPubDuplicateService;

  @Autowired
  private GrpPubRcmdDao grpPubRcmdDao;

  @Override
  public void checkSourcesParameter(PubDTO pub) throws PubHandlerAssemblyException {

  }

  @Override
  public void checkRebuildParameter(PubDTO pub) throws PubHandlerAssemblyException {

  }

  @Override
  public Map<String, String> excute(PubDTO pub) throws PubHandlerAssemblyException {
    // 只有导入群组成果才走这个逻辑
    if (pub.pubGenre != PubGenreConstants.GROUP_PUB || NumberUtils.isNullOrZero(pub.grpId)) {
      return null;
    }
    // 成果认领的成果，不走成果自动认领的逻辑
    if (pub.isPubConfirm != null && pub.isPubConfirm == 1) {
      return null;
    }
    Set<Long> dupPubIds = new HashSet<Long>();
    try {
      if (StringUtils.isNotBlank(pub.hashDoi)) {
        dupPubIds
            .addAll(pdwhPubDuplicateService.dupByNotNullDoi(Long.valueOf(pub.hashDoi), Long.valueOf(pub.hashCleanDoi)));
      }
      Long hashSourceId = null;
      if (StringUtils.isNotBlank(pub.hashEiSourceId)) {
        hashSourceId = Long.valueOf(pub.hashEiSourceId);
      } else if (StringUtils.isNotBlank(pub.hashIsiSourceId)) {
        hashSourceId = Long.valueOf(pub.hashIsiSourceId);
      }
      if (hashSourceId != null) {
        dupPubIds.addAll(pdwhPubDuplicateService.dupBySourceId(hashSourceId));
      }
      Long hashPublicationOpenNo = null;
      Long hashApplicationNo = null;
      if (StringUtils.isNotBlank(pub.hashPublicationOpenNo)) {
        hashPublicationOpenNo = Long.valueOf(pub.hashPublicationOpenNo);
      }
      if (StringUtils.isNotBlank(pub.hashApplicationNo)) {
        hashApplicationNo = Long.valueOf(pub.hashApplicationNo);
      }
      if (hashPublicationOpenNo != null || hashApplicationNo != null) {
        dupPubIds.addAll(pdwhPubDuplicateService.dupByPatentInfo(hashPublicationOpenNo, hashApplicationNo));// 进行基准库查重
      }
      if (StringUtils.isNotEmpty(pub.hashTP)) {
        dupPubIds.addAll(pdwhPubDuplicateService.dupByPubInfo(pub.hashTP, pub.hashTPP));// 进行基准库查重
      }
      if (CollectionUtils.isNotEmpty(dupPubIds)) {
        for (Long dupPubId : dupPubIds) {
          GrpPubRcmd grpPubRcmd = grpPubRcmdDao.getGrpPubRcmd(dupPubId, pub.grpId);
          if (grpPubRcmd != null) {// 存在与当前导入的成果相同的推荐成果,将该推荐成果自动设置为已经确认
            grpPubRcmd.setStatus(1);
            grpPubRcmd.setUpdateDate(new Date());
            grpPubRcmdDao.update(grpPubRcmd);
          }
        }
      }
    } catch (Exception e) {
      logger.error("更新群组成果自动认领状态出错！", e);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "更新群组成果自动认领状态出错！", e);
    }
    return null;
  }

}
