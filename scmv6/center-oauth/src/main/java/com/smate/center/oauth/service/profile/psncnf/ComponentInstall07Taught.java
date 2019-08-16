package com.smate.center.oauth.service.profile.psncnf;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.oauth.dao.profile.PersonTaughtDao;
import com.smate.center.oauth.model.profile.PersonTaught;
import com.smate.core.base.psn.enums.PsnCnfEnum;
import com.smate.core.base.psn.model.psncnf.PsnConfigTaught;
import com.smate.core.base.psn.service.psncnf.PsnCnfService;
import com.smate.core.base.psn.utils.PsnCnfUtils;

@Service("install07Taught")
@Transactional(rollbackFor = Exception.class)
public class ComponentInstall07Taught implements ComponentInstall {
  @Autowired
  private PsnCnfService psnCnfService;

  @Resource(name = "install08Contact")
  private ComponentInstall next;

  @Autowired
  private PersonTaughtDao personTaughtDao;

  @Override
  public void install(Long runs, Long cnfId, Long psnId) throws Exception {
    try {
      if (PsnCnfUtils.canRun(runs, PsnCnfEnum.DIRTY, PsnCnfEnum.TAUGHT)) {
        // 创建新数据
        PsnConfigTaught cnfTaught = new PsnConfigTaught(cnfId);
        PsnConfigTaught cnfTaughtExist = psnCnfService.get(cnfTaught);
        if (cnfTaughtExist == null) {
          // 所教课程
          PersonTaught personTaught = personTaughtDao.get(psnId);
          boolean hasText = personTaught != null && StringUtils.isNotBlank(personTaught.getContent());
          // 查看权限 & 数据内容有无
          cnfTaught.setAnyView(cnfTaught.getAnyUser() & PsnCnfUtils.convertAnyView(hasText));
          psnCnfService.save(cnfTaught);
        }
      }
    } catch (Exception e) {
      throw new Exception("个人配置：install07Taught失败", e);
    }
    // 下个创建操作
    next.install(runs, cnfId, psnId);
  }
}
