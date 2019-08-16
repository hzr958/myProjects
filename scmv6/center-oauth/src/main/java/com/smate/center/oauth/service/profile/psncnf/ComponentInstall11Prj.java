package com.smate.center.oauth.service.profile.psncnf;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.oauth.dao.profile.SnsProjectDao;
import com.smate.core.base.project.model.Project;
import com.smate.core.base.psn.enums.PsnCnfEnum;
import com.smate.core.base.psn.model.psncnf.PsnConfigPrj;
import com.smate.core.base.psn.model.psncnf.PsnConfigPrjPk;
import com.smate.core.base.psn.service.psncnf.PsnCnfService;
import com.smate.core.base.psn.utils.PsnCnfUtils;

@Service("install11Prj")
@Transactional(rollbackFor = Exception.class)
public class ComponentInstall11Prj implements ComponentInstall {
  @Autowired
  private PsnCnfService psnCnfService;

  @Autowired
  private SnsProjectDao snsProjectDao;

  @Resource(name = "install12Position")
  private ComponentInstall next;

  @Override
  public void install(Long runs, Long cnfId, Long psnId) throws Exception {
    try {
      if (PsnCnfUtils.canRun(runs, PsnCnfEnum.DIRTY, PsnCnfEnum.PRJ)) {
        // 创建新数据据
        List<Project> prjList = snsProjectDao.findPrjIdsByPsnId(psnId);
        for (Project prj : prjList) {
          Long prjId = prj.getId();
          PsnConfigPrj cnfPrj = new PsnConfigPrj(new PsnConfigPrjPk(cnfId, prjId));
          PsnConfigPrj cnfPrjExist = psnCnfService.get(cnfPrj);
          if (cnfPrjExist == null) {
            psnCnfService.save(cnfPrj);
          }
        }
      }
    } catch (Exception e) {
      throw new Exception("个人配置：install11Prj失败", e);
    }
    // 下个创建操作
    next.install(runs, cnfId, psnId);
  }
}
