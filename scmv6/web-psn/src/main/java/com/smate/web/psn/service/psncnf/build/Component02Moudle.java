package com.smate.web.psn.service.psncnf.build;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.enums.PsnCnfEnum;
import com.smate.core.base.psn.model.psncnf.PsnConfigMoudle;
import com.smate.core.base.psn.service.psncnf.PsnCnfService;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.web.psn.exception.PsnCnfException;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.model.psncnf.PsnCnfBuild;

@Service("component02Moudle")
@Transactional(rollbackFor = Exception.class)
class Component02Moudle implements ComponentBase {

  @Autowired
  private PsnCnfService psnCnfService;

  @Resource(name = "component03List")
  private ComponentBase next;

  @SuppressWarnings("unchecked")
  @Override
  public void assemble(PsnCnfBuild cnfBuild) throws ServiceException, PsnCnfException {

    Long cnfId = cnfBuild.getCnfId();
    PsnConfigMoudle cnfMoudle = psnCnfService.get(new PsnConfigMoudle(cnfId));

    if (cnfMoudle == null) {
      throw new ServiceException("psn_config_moudle缺少数据，cnfId=" + cnfId);
    }

    // 添加到PsnCnfBuild，进行封装
    // seqno JSON转换
    Map<Integer, PsnCnfEnum> seqMap = (Map<Integer, PsnCnfEnum>) JacksonUtils.jsonObject(cnfMoudle.getSeqNos());
    cnfMoudle.setSeqMap(seqMap);
    cnfBuild.setCnfMoudle(cnfMoudle);

    // 下个封装操作
    next.assemble(cnfBuild);
  }
}
