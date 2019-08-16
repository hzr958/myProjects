package com.smate.center.batch.service.psn;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.psn.PsnMailSetDao;
import com.smate.center.batch.model.psn.ConstMailType;
import com.smate.center.batch.model.psn.PsnMailSet;

@Service("psnMailSetService")
@Transactional(rollbackFor = Exception.class)
public class PsnMailSetServiceImpl implements PsnMailSetService {
  private static Logger logger = LoggerFactory.getLogger(PsnMailSetServiceImpl.class);
  @Autowired
  private PsnMailSetDao psnMailSetDao;


  /**
   * 新增或修改个人邮件设置
   * 
   * @param psnMailSet
   */
  @Override
  public void addOrMidify(PsnMailSet psnMailSet) {
    this.psnMailSetDao.saveOrUpdate(psnMailSet);
    // TODO 是直接使用同步任务呢 还是直接 跳过任务 直接保存差异数据
    // rcmdSyncFlagMessageProducer.syncPsnMailSet(psnMailSet.getPsnId());
  }


  /**
   * 根据个人Id取得该人的邮件设置信息
   * 
   * @param psnId
   * @return
   */
  @Override
  public List<PsnMailSet> list(Long psnId) {
    return this.psnMailSetDao.list(psnId);
  }

  /**
   * 根据个人Id删除该人的邮件设置
   * 
   * @param psnId
   * @return
   */
  @Override
  public int removes(Long psnId) {
    return this.psnMailSetDao.removes(psnId);
  }

  public void setPsnMailSetDao(PsnMailSetDao psnMailSetDao) {
    this.psnMailSetDao = psnMailSetDao;
  }

  /**
   * 获取邮件类型列表
   * 
   * @return
   */
  @Override
  public List<ConstMailType> getMailTypeList() {
    return this.psnMailSetDao.getMailTypeList();
  }

  /**
   * 是否关闭了发送邮件的功能,关闭返回true，否则返回false
   * 
   * @param psnId 个人Id
   * @param mailTypeId 邮件类型Id
   * @return
   */
  @Override
  public boolean isClosed(Long psnId, Long mailTypeId) {

    return false;
    // return this.psnMailSetDao.isClosed(psnId, mailTypeId);
  }

  /**
   * 初始化个人邮件设置
   * 
   * @param psnId
   * @return
   */
  @Override
  public boolean iniPsnMailSet(Long psnId) {
    boolean isOk = true;
    List<ConstMailType> list = this.getMailTypeList();
    try {
      for (ConstMailType mailType : list) {
        PsnMailSet psnMailSet = new PsnMailSet(psnId, mailType.getMailTypeId(), 1l);
        this.addOrMidify(psnMailSet);
      }
    } catch (Exception ex) {
      logger.error("初始化个人邮件设置失败： " + ex.getMessage());
      isOk = false;
    }
    return isOk;
  }
}
