package com.smate.center.open.service.psnconf;

import com.smate.center.open.service.publication.PublicationService;
import com.smate.core.base.psn.enums.PsnCnfEnum;
import com.smate.core.base.psn.model.psncnf.PsnConfigPub;
import com.smate.core.base.psn.model.psncnf.PsnConfigPubPk;
import com.smate.core.base.psn.service.psncnf.PsnCnfService;
import com.smate.core.base.psn.utils.PsnCnfUtils;
import com.smate.core.base.pub.model.Publication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service("install10Pub")
@Transactional(rollbackFor = Exception.class)
public class ComponentInstall10Pub implements ComponentInstall {
  @Autowired
  private PsnCnfService psnCnfService;

  @Resource(name = "install11Prj")
  private ComponentInstall next;

  @Autowired
  private PublicationService publicationService;

  @Override
  public void install(Long runs, Long cnfId, Long psnId) throws Exception {
    try {
      if (PsnCnfUtils.canRun(runs, PsnCnfEnum.DIRTY, PsnCnfEnum.PUB)) {
        // 创建新数据
        List<Publication> pubList = new ArrayList<>();
        //TODO 2018-12-14
        //List<Publication> pubList = publicationService.findPubIdsByPsnId(psnId);
        for (Publication pub : pubList) {
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
