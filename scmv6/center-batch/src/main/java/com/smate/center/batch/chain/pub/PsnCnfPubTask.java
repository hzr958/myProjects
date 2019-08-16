package com.smate.center.batch.chain.pub;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.pub.IPubXmlTask;
import com.smate.core.base.psn.consts.PsnCnfConst;
import com.smate.core.base.psn.model.psncnf.PsnConfigPub;
import com.smate.core.base.psn.service.psncnf.PsnCnfService;
import com.smate.core.base.utils.constant.PubXmlConstants;

/**
 * 个人配置：成果权限
 * 
 * @author zhuangyanming
 * 
 */
public class PsnCnfPubTask implements IPubXmlTask {

  private final String name = "add_psnCnfPub";

  @Autowired
  private PsnCnfService psnCnfService;

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public boolean can(PubXmlDocument xmlDocument, PubXmlProcessContext context) {
    return true;
  }

  @Override
  public boolean run(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception {

    Long pubId = context.getCurrentPubId();
    Long psnId = context.getCurrentUserId();
    if (pubId != null && psnId != null && pubId > 0 && psnId > 0) {

      // 个人成果权限设置
      String authority = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "authority");
      if (StringUtils.isBlank(authority)) {
        authority = PsnCnfConst.ALLOWS.toString();
      }
      // lcw 如果成果作者未精确和模糊匹配到当前用户，则将权限设置本人，fzq意见
      String isAdd = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "isAdd");
      if ("true".equals(isAdd)) {
        String authorMatchOwner = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "match_owner");
        // 文件导入不执行以下规则
        String sourceDbId = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "source_db_id");
        if (StringUtils.isBlank(sourceDbId) || !"-1".equals(sourceDbId)) {
          if ("false".equals(authorMatchOwner)) {// 没有匹配上则设置为本人可见
            authority = PsnCnfConst.ALLOWS_SELF.toString();
          } else if ("true".equals(authorMatchOwner)) {// 已匹配上则公开
            authority = PsnCnfConst.ALLOWS.toString();
          }
        }
      }
      Integer anyUser = Integer.parseInt(authority);
      // 构造权限对象
      PsnConfigPub cnfPub = new PsnConfigPub();
      cnfPub.getId().setPubId(pubId);
      cnfPub.setAnyUser(anyUser);
      cnfPub.setAnyView(cnfPub.getAnyUser());
      psnCnfService.save(psnId, cnfPub);// 保存权限
    }
    return true;
  }

}
