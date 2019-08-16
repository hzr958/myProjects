package com.smate.center.oauth.service.profile.psncnf;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.enums.PsnCnfEnum;
import com.smate.core.base.psn.model.PsnPubPO;
import com.smate.core.base.psn.model.psncnf.PsnConfigPub;
import com.smate.core.base.psn.model.psncnf.PsnConfigPubPk;
import com.smate.core.base.psn.service.psncnf.PsnCnfService;
import com.smate.core.base.psn.service.psnpub.PsnPubService;
import com.smate.core.base.psn.utils.PsnCnfUtils;

@Service("install10Pub")
@Transactional(rollbackFor = Exception.class)
public class ComponentInstall10Pub implements ComponentInstall {
  @Autowired
  private PsnCnfService psnCnfService;

  @Resource(name = "install11Prj")
  private ComponentInstall next;

  @Autowired
  private PsnPubService psnPubService;

  @Override
  public void install(Long runs, Long cnfId, Long psnId) throws Exception {
    try {
      if (PsnCnfUtils.canRun(runs, PsnCnfEnum.DIRTY, PsnCnfEnum.PUB)) {
        // 创建新数据
        List<PsnPubPO> pubList = psnPubService.getPubsByPsnId(psnId);
        for (PsnPubPO pub : pubList) {
          Long pubId = pub.getPubId();
          PsnConfigPub cnfPub = new PsnConfigPub(new PsnConfigPubPk(cnfId, pubId));
          PsnConfigPub cnfPubExist = psnCnfService.get(cnfPub);
          if (cnfPubExist == null) {
            psnCnfService.save(cnfPub);
          }
        }
      }
    } catch (Exception e) {
      throw new Exception("个人配置：install10Pub失败", e);
    }
    // 下个创建操作
    next.install(runs, cnfId, psnId);
  }
}
