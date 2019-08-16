package com.smate.center.batch.service.psn.psncnf;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.service.psn.PersonManager;
import com.smate.core.base.psn.enums.PsnCnfEnum;
import com.smate.core.base.psn.model.psncnf.PsnConfigContact;
import com.smate.core.base.psn.service.psncnf.PsnCnfService;
import com.smate.core.base.psn.utils.PsnCnfUtils;
import com.smate.core.base.utils.model.security.Person;

@Service("install08Contact")
@Transactional(rollbackFor = Exception.class)
public class ComponentInstall08Contact implements ComponentInstall {
  @Autowired
  private PsnCnfService psnCnfService;

  @Resource(name = "install09Expertise")
  private ComponentInstall next;

  @Autowired
  private PersonManager personManager;

  @Override
  public void install(Long runs, Long cnfId, Long psnId) throws Exception {
    try {
      if (PsnCnfUtils.canRun(runs, PsnCnfEnum.DIRTY, PsnCnfEnum.CONTACT)) {
        // 创建新数据
        PsnConfigContact cnfContact = new PsnConfigContact(cnfId);
        PsnConfigContact cnfContactExist = psnCnfService.get(cnfContact);
        if (cnfContactExist == null) {
          // 联系方式
          Person contact = personManager.getContact(psnId);
          boolean hasTextEmail = contact != null && StringUtils.isNotBlank(contact.getEmail());
          boolean hasTextMobile = contact != null && StringUtils.isNotBlank(contact.getMobile());
          boolean hasTextMsn = contact != null && StringUtils.isNotBlank(contact.getMsnNo());
          boolean hasTextTel = contact != null && StringUtils.isNotBlank(contact.getTel());
          boolean hasTextQq = contact != null && StringUtils.isNotBlank(contact.getQqNo());
          boolean hasTextSkype = contact != null && StringUtils.isNotBlank(contact.getSkype());
          // 查看权限 & 数据内容有无
          cnfContact.setAnyViewEmail(cnfContact.getAnyUserEmail() & PsnCnfUtils.convertAnyView(hasTextEmail));
          cnfContact.setAnyViewMobile(cnfContact.getAnyUserMobile() & PsnCnfUtils.convertAnyView(hasTextMobile));
          cnfContact.setAnyViewMsn(cnfContact.getAnyUserMsn() & PsnCnfUtils.convertAnyView(hasTextMsn));
          cnfContact.setAnyViewTel(cnfContact.getAnyUserTel() & PsnCnfUtils.convertAnyView(hasTextTel));
          cnfContact.setAnyViewQq(cnfContact.getAnyUserQq() & PsnCnfUtils.convertAnyView(hasTextQq));
          cnfContact.setAnyViewSkype(cnfContact.getAnyUserSkype() & PsnCnfUtils.convertAnyView(hasTextSkype));
          psnCnfService.save(cnfContact);
        }
      }
    } catch (Exception e) {
      throw new Exception("个人配置：install08Contact失败", e);
    }
    // 下个创建操作
    next.install(runs, cnfId, psnId);
  }
}
