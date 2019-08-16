package com.smate.web.v8pub.service.handler.assembly.snspubsave;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.number.NumberUtils;
import com.smate.web.v8pub.enums.PubGenreConstants;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.po.sns.group.GroupPubPO;
import com.smate.web.v8pub.po.sns.group.GrpBaseinfo;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.sns.GroupPubService;
import com.smate.web.v8pub.service.sns.group.GrpBaseInfoService;

/**
 * 群组与个人库成果关系 保存
 * 
 * @author YJ
 *
 *         2018年7月20日
 */
@Transactional(rollbackFor = Exception.class)
public class ASPubGroupPubSaveImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private GroupPubService groupPubService;
  @Autowired
  private GrpBaseInfoService grpBaseInfoService;

  @Override
  public void checkSourcesParameter(PubDTO pub) throws PubHandlerAssemblyException {

  }

  @Override
  public void checkRebuildParameter(PubDTO pub) throws PubHandlerAssemblyException {
    String className = this.getClass().getSimpleName();
    if (pub.pubGenre != PubGenreConstants.GROUP_PUB) {
      return;
    }
    if (NumberUtils.isNullOrZero(pub.grpId)) {
      String error = className + "保存或更新群组成果信息，grpId不能为空，grpId=" + pub.grpId;
      logger.error(error);
      throw new PubHandlerAssemblyException(error);
    }
    // 检查是否有效的grpId
    GrpBaseinfo grpBaseinfo = grpBaseInfoService.getByGrpId(pub.grpId);
    if (grpBaseinfo == null) {
      String error = className + "保存或更新群组成果信息，无效的grpId，grpId=" + pub.grpId;
      logger.error(error);
      throw new PubHandlerAssemblyException(error);
    }
    // 检查isProjectPub是否有效
    if (pub.isProjectPub == null) {
      String error = className + "保存或更新群组成果信息，isProjectPub不能为空，isProjectPub=" + pub.isProjectPub;
      logger.error(error);
      throw new PubHandlerAssemblyException(error);
    }
  }

  @Override
  public Map<String, String> excute(PubDTO pub) throws PubHandlerAssemblyException {
    if (pub.pubGenre != PubGenreConstants.GROUP_PUB) {
      return null;
    }
    GroupPubPO groupPubPO = null;
    try {
      if (!NumberUtils.isNullOrZero(pub.grpId)) {
        groupPubPO = groupPubService.getByGrpIdAndPubId(pub.grpId, pub.pubId);
        if (groupPubPO == null) {
          groupPubPO = new GroupPubPO();
          groupPubPO.setGrpId(pub.grpId);
          groupPubPO.setPubId(pub.pubId);
          groupPubPO.setCreateDate(new Date());
          groupPubPO.setCreatePsnId(pub.psnId);
          groupPubPO.setStatus(0);
          groupPubPO.setOwnerPsnId(pub.psnId);
        }
        groupPubPO.setIsProjectPub(pub.isProjectPub);
        groupPubPO.setLabeled(pub.labeled);
        groupPubPO.setRelevance(pub.relevance);
        groupPubPO.setUpdatePsnId(pub.psnId);
        groupPubPO.setUpdateDate(new Date());
        groupPubService.saveOrUpdate(groupPubPO);
        logger.debug("保存或更新sns库群组与成果关系记录成功");
      }
    } catch (Exception e) {
      logger.error("更新或者保存sns库群组成果关系表记录出错！GroupPubPO={}", groupPubPO, e);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "保存或更新sns库成果与群组关系记录出错!", e);
    }

    return null;
  }

}
