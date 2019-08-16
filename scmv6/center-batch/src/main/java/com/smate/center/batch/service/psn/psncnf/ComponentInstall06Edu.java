package com.smate.center.batch.service.psn.psncnf;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.dao.EducationHistoryDao;
import com.smate.core.base.psn.enums.PsnCnfEnum;
import com.smate.core.base.psn.model.psncnf.PsnConfigEdu;
import com.smate.core.base.psn.model.psncnf.PsnConfigEduPk;
import com.smate.core.base.psn.service.psncnf.PsnCnfService;
import com.smate.core.base.psn.utils.PsnCnfUtils;

@Service("install06Edu")
@Transactional(rollbackFor = Exception.class)
public class ComponentInstall06Edu implements ComponentInstall {
  @Autowired
  private PsnCnfService psnCnfService;

  @Resource(name = "install07Taught")
  private ComponentInstall next;

  @Autowired
  private EducationHistoryDao educationHistoryDao;

  @Override
  public void install(Long runs, Long cnfId, Long psnId) throws Exception {
    try {
      if (PsnCnfUtils.canRun(runs, PsnCnfEnum.DIRTY, PsnCnfEnum.EDU)) {
        // 创建新数据
        List<Long> eduIdList = educationHistoryDao.findEduIdList(psnId);
        for (Long eduId : eduIdList) {
          PsnConfigEdu cnfEdu = new PsnConfigEdu(new PsnConfigEduPk(cnfId, eduId));
          PsnConfigEdu cnfEduExist = psnCnfService.get(cnfEdu);
          if (cnfEduExist == null) {
            psnCnfService.save(cnfEdu);
          }
        }
      }
    } catch (Exception e) {
      throw new Exception("个人配置：install06Edu失败", e);
    }
    // 下个创建操作
    next.install(runs, cnfId, psnId);
  }
}
