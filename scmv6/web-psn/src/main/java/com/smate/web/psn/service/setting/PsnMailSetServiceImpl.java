package com.smate.web.psn.service.setting;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.psn.dao.setting.PsnMailSetDao;
import com.smate.web.psn.model.setting.ConstMailType;
import com.smate.web.psn.model.setting.PsnMailSet;


@Service("psnMailSetService")
@Transactional(rollbackFor = Exception.class)
public class PsnMailSetServiceImpl implements PsnMailSetService {
  private static Logger logger = LoggerFactory.getLogger(PsnMailSetServiceImpl.class);
  @Autowired
  private PsnMailSetDao psnMailSetDao;


  /**
   * 获取邮件类型列表 界面上有的
   * 
   * @return
   */
  @Override
  public List<ConstMailType> getMailTypeList() {
    return this.psnMailSetDao.getMailTypeList();
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

  /**
   * 新增或修改个人邮件设置
   * 
   * @param psnMailSet
   */
  @Override
  public void addOrMidify(PsnMailSet psnMailSet) {
    this.psnMailSetDao.save(psnMailSet);// 新增
    // 推荐库没有这张表，RCMD_SYNC_PSNINFO 先注释掉 2017-11-23-ajb
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

  @Override
  public String psnMailSet(String repsnId, String typeid) {
    Long psnId = Long.parseLong(repsnId);
    Long mailTypeId = Long.parseLong(typeid);

    PsnMailSet maiSet = psnMailSetDao.getByPsnIdAndMailTypeId(psnId, mailTypeId);
    if (maiSet == null || maiSet.getIsReceive() == null || ((Long) maiSet.getIsReceive()).equals(0L)) {
      return "0";// 表示该邮件已经不再接收此类邮件
    }
    psnMailSetDao.psnMailSet(psnId, mailTypeId);
    return "1"; // 退订成功

  }

  @Override
  public String getMailById(Long id) {
    // 通过id获取邮件
    return psnMailSetDao.getEmailByPsnId(id).toString();
  }


}
