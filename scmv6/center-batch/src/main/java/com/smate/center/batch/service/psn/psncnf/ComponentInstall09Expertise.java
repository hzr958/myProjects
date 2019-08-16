package com.smate.center.batch.service.psn.psncnf;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.pub.PsnDisciplineKeyDao;
import com.smate.core.base.psn.enums.PsnCnfEnum;
import com.smate.core.base.psn.model.profile.PsnDisciplineKey;
import com.smate.core.base.psn.model.psncnf.PsnConfigExpertise;
import com.smate.core.base.psn.service.psncnf.PsnCnfService;
import com.smate.core.base.psn.utils.PsnCnfUtils;

@Service("install09Expertise")
@Transactional(rollbackFor = Exception.class)
public class ComponentInstall09Expertise implements ComponentInstall {
  @Autowired
  private PsnCnfService psnCnfService;

  @Resource(name = "install10Pub")
  private ComponentInstall next;

  @Autowired
  private PsnDisciplineKeyDao psnDisciplineKeyDao;

  @Override
  public void install(Long runs, Long cnfId, Long psnId) throws Exception {
    try {
      if (PsnCnfUtils.canRun(runs, PsnCnfEnum.DIRTY, PsnCnfEnum.EXPERTISE)) {
        // 创建新数据
        PsnConfigExpertise cnfExpertise = new PsnConfigExpertise(cnfId);
        PsnConfigExpertise cnfExpertiseExist = psnCnfService.get(cnfExpertise);
        if (cnfExpertiseExist == null) {
          List<PsnDisciplineKey> keyList = psnDisciplineKeyDao.findByPsnId(psnId);
          boolean hasText = CollectionUtils.isNotEmpty(keyList);
          // 查看权限 & 数据内容有无
          cnfExpertise.setAnyView(cnfExpertise.getAnyUser() & PsnCnfUtils.convertAnyView(hasText));
          psnCnfService.save(cnfExpertise);
        }
      }
    } catch (Exception e) {
      throw new Exception("个人配置：install09Expertise失败", e);
    }
    // 下个创建操作
    next.install(runs, cnfId, psnId);
  }
}
