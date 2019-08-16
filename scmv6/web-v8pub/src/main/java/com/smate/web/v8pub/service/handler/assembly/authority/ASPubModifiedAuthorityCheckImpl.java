package com.smate.web.v8pub.service.handler.assembly.authority;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.pub.util.DisposeDes3IdUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.web.v8pub.enums.PubGenreConstants;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.sns.GroupPubService;
import com.smate.web.v8pub.service.sns.PubSnsService;

/**
 * 人员对成果修改的权限检查
 * 
 * @author YJ
 *
 *         2018年9月11日
 */
@Transactional(rollbackFor = Exception.class)
public class ASPubModifiedAuthorityCheckImpl implements PubHandlerAssemblyService {
  @Autowired
  private GroupPubService groupPubService;
  @Autowired
  private PubSnsService pubSnsService;

  @Override
  public void checkSourcesParameter(PubDTO pub) throws PubHandlerAssemblyException {
    Long pubId = DisposeDes3IdUtils.disposeDes3Id(pub.pubId, pub.des3PubId);
    Long psnId = DisposeDes3IdUtils.disposeDes3Id(pub.psnId, pub.des3PsnId);
    Long grpId = DisposeDes3IdUtils.disposeDes3Id(pub.grpId, pub.des3GrpId);
    if (NumberUtils.isNullOrZero(psnId)) {
      throw new PubHandlerAssemblyException("人员不存在");
    }
    // 个人成果
    if (pub.pubGenre == PubGenreConstants.PSN_PUB) {
      // 只有成果拥有者才可以进行对成果进行修改
      // 1.通过成果pubId取拥有者的psnId
      if (!NumberUtils.isNullOrZero(pubId)) {
        // 暂时修改
        // Long ownerPsnId = psnPubService.getPubOwnerId(pubId);
        Long ownerPsnId = null;
        PubSnsPO pubSns = pubSnsService.get(pubId);
        if (pubSns != null) {
          ownerPsnId = pubSns.getCreatePsnId();
          // 2.对比psnId是否为当前传入的人员psnId
          if (!NumberUtils.isNullOrZero(ownerPsnId) && psnId.compareTo(ownerPsnId) != 0) {
            throw new PubHandlerAssemblyException("没有权限修改此条成果信息");
          }
        } else {
          throw new PubHandlerAssemblyException("此成果不存在，pubId=" + pubId + ",psnId=" + psnId);
        }
      }
    }
    // 群组成果
    if (pub.pubGenre == PubGenreConstants.GROUP_PUB) {
      if (NumberUtils.isNullOrZero(grpId)) {
        throw new PubHandlerAssemblyException("群组成果保存，grpId为null值");
      }
      // 只有成果拥有者才可以进行对成果进行修改(群组成果也是)
      if (!NumberUtils.isNullOrZero(pubId)) {
        Long ownerPsnId = groupPubService.getPubOwnerPsnId(grpId, pubId);
        // 2.对比psnId是否为当前传入的人员psnId
        if (psnId.compareTo(ownerPsnId) != 0) {
          throw new PubHandlerAssemblyException("没有权限修改此条成果信息");
        }
      }
    }
  }

  @Override
  public void checkRebuildParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // TODO Auto-generated method stub
  }

  @Override
  public Map<String, String> excute(PubDTO pub) throws PubHandlerAssemblyException {
    return null;
  }
}
