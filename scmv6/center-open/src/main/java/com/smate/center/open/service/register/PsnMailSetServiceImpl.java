package com.smate.center.open.service.register;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.dao.psn.PsnMailSetDao;
import com.smate.center.open.model.consts.ConstMailType;
import com.smate.center.open.model.psn.PsnMailSet;



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
