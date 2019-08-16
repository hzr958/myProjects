package com.smate.core.base.psn.service.psncnf.impl;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.psn.dao.psncnf.PsnConfigMoudleDao;
import com.smate.core.base.psn.enums.PsnCnfEnum;
import com.smate.core.base.psn.model.psncnf.PsnConfigMoudle;
import com.smate.core.base.psn.service.psncnf.PsnCnfBase;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 个人配置：模块、排序
 * 
 * @author zhuangyanming
 * 
 */
@Service("psnCnfMoudleService")
@Transactional(rollbackFor = Exception.class)
class PsnConfigMoudleImpl extends PsnConfigAbstract {

  @Autowired
  private PsnConfigMoudleDao psnConfigMoudleDao;

  @Override
  public void doVerify(PsnCnfBase psnCnfBase) throws ServiceException {
    // 检查对象格式
    Assert.notNull(psnCnfBase);
    Assert.isInstanceOf(PsnConfigMoudle.class, psnCnfBase);
    // 检查主键格式
    boolean hasCnfId = psnCnfBase.getCnfId() != null && psnCnfBase.getCnfId() > 0;
    Assert.isTrue(hasCnfId);
  }

  @Override
  public void doSave(PsnCnfBase psnCnfBase) throws ServiceException {
    // 检查权限格式
    PsnConfigMoudle cnfMoudleNew = (PsnConfigMoudle) psnCnfBase;
    PsnConfigMoudle cnfMoudle = (PsnConfigMoudle) this.doGet(psnCnfBase);
    if (cnfMoudle != null) {// 更新
      if (!cnfMoudleNew.equals(cnfMoudle)) {// 存在数据变化
        if (cnfMoudleNew.getAnyMod() != null) {
          cnfMoudle.setAnyMod(cnfMoudleNew.getAnyMod());
        }
        if (StringUtils.isNotBlank(cnfMoudleNew.getSeqNos())) {// 后台处理
          this.checkSeqNos(cnfMoudleNew);
          cnfMoudle.setSeqNos(cnfMoudleNew.getSeqNos());
        } else {// 界面操作
          String[] seqChanges = cnfMoudleNew.getSeqChanges();
          if (seqChanges.length == 2 && seqChanges[0] != null && seqChanges[1] != null) {// 交换模块顺序
            String seqNos = cnfMoudle.getSeqNos();
            seqNos = seqNos.replaceAll("\"" + seqChanges[0] + "\"", "\"0\"");
            seqNos = seqNos.replaceAll("\"" + seqChanges[1] + "\"", "\"" + seqChanges[0] + "\"");
            seqNos = seqNos.replaceAll("\"0\"", "\"" + seqChanges[1] + "\"");
            cnfMoudleNew.setSeqNos(seqNos);
            this.checkSeqNos(cnfMoudleNew);
            cnfMoudle.setSeqNos(cnfMoudleNew.getSeqNos());
          }
        }

        cnfMoudle.setUpdateDate(cnfMoudleNew.getUpdateDate());
        psnConfigMoudleDao.save(cnfMoudle);
      }

    } else {// 新增
      Assert.state(cnfMoudleNew.getAnyMod() >= 0);
      this.checkSeqNos(cnfMoudleNew);
      psnConfigMoudleDao.save(cnfMoudleNew);
    }

  }

  @SuppressWarnings("unchecked")
  private void checkSeqNos(PsnConfigMoudle cnfMoudleNew) {
    // // 检查排序格式
    Assert.hasText(cnfMoudleNew.getSeqNos());
    // 必须是Map格式
    Map<Integer, PsnCnfEnum> seqMap = (Map<Integer, PsnCnfEnum>) JacksonUtils.jsonObject(cnfMoudleNew.getSeqNos());
    Assert.notNull(seqMap);
    Assert.state(seqMap.size() > 0);

    // 大于0的数字，必须连续且不重复
    Integer nextSeq = 0;
    do {
      nextSeq++;
    } while (seqMap.containsKey(nextSeq.toString()));

    Assert.state(nextSeq - 1 == seqMap.size());
  }

  @Override
  public void doDel(PsnCnfBase psnCnfBase) throws ServiceException {
    PsnConfigMoudle psnConfigMoudle = (PsnConfigMoudle) this.doGet(psnCnfBase);
    if (psnConfigMoudle != null) {// 存在对象，则删除
      psnConfigMoudleDao.delete(psnConfigMoudle);
    }
  }

  @Override
  public PsnCnfBase doGet(PsnCnfBase psnCnfBase) throws ServiceException {
    // 查询对象
    return psnConfigMoudleDao.get(psnCnfBase.getCnfId());
  }

}
