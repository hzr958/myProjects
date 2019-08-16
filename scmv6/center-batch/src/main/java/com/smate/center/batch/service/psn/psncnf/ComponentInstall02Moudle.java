package com.smate.center.batch.service.psn.psncnf;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.batch.service.psn.PsnStatisticsService;
import com.smate.core.base.psn.consts.PsnCnfConst;
import com.smate.core.base.psn.enums.PsnCnfEnum;
import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.core.base.psn.model.psncnf.PsnConfigMoudle;
import com.smate.core.base.psn.service.psncnf.PsnCnfService;
import com.smate.core.base.psn.utils.PsnCnfUtils;
import com.smate.core.base.utils.json.JacksonUtils;

@Service("install02Moudle")
public class ComponentInstall02Moudle implements ComponentInstall {
  @Autowired
  private PsnCnfService psnCnfService;

  @Resource(name = "install03List")
  private ComponentInstall next;

  @Autowired
  private PsnStatisticsService psnStatisticsService;

  @Override
  public void install(Long runs, Long cnfId, Long psnId) throws Exception {
    try {
      PsnConfigMoudle cnfMoudle = new PsnConfigMoudle(cnfId);
      PsnConfigMoudle cnfMoudleExist = psnCnfService.get(cnfMoudle);
      if (PsnCnfUtils.canRun(runs, PsnCnfEnum.DIRTY)) {
        // 创建新数据
        Long anyMod = null;
        if (cnfMoudleExist == null) {
          PsnStatistics psnStatistics = psnStatisticsService.getPsnStatistics(psnId);
          // hindex需要大于5才显示
          if (psnStatistics != null && psnStatistics.getHindex() != null
              && psnStatistics.getHindex() >= PsnCnfConst.COND_MIN_HINDEX) {

            anyMod = Long.valueOf(PsnCnfEnum.ALL.toString());
          } else {
            anyMod = Long.valueOf(PsnCnfEnum.ALL.toString()) - Long.valueOf(PsnCnfEnum.HINDEX.toString());
          }
          cnfMoudle.setAnyMod(anyMod);
          cnfMoudle.setSeqNos(this.getSeqNos());// 默认顺序
          psnCnfService.save(cnfMoudle);
        }
      }
      // 重新调整顺序
      if (PsnCnfUtils.canRun(runs, PsnCnfEnum.MOVE)) {
        cnfMoudleExist.setSeqNos(this.getSeqNos());
        psnCnfService.save(cnfMoudleExist);
      }
    } catch (Exception e) {
      throw new Exception("个人配置：install02Moudle失败", e);
    }
    // 下个创建操作
    next.install(runs, cnfId, psnId);
  }

  private String getSeqNos() {
    Map<Integer, PsnCnfEnum> seqMap = new HashMap<Integer, PsnCnfEnum>();
    seqMap.put(1, PsnCnfEnum.BRIEF);
    seqMap.put(2, PsnCnfEnum.WORK);
    seqMap.put(3, PsnCnfEnum.TAUGHT);
    seqMap.put(4, PsnCnfEnum.EXPERTISE);
    seqMap.put(5, PsnCnfEnum.EDU);
    seqMap.put(6, PsnCnfEnum.CONTACT);
    seqMap.put(7, PsnCnfEnum.PRJ);
    seqMap.put(8, PsnCnfEnum.PUB);
    return JacksonUtils.jsonObjectSerializer(seqMap);
  }
}
