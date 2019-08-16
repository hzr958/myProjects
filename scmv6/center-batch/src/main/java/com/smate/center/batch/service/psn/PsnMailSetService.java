package com.smate.center.batch.service.psn;

import java.util.List;

import com.smate.center.batch.model.psn.ConstMailType;
import com.smate.center.batch.model.psn.PsnMailSet;

/**
 * 个人邮件设置接口
 * 
 * @author YPH
 * 
 */
public interface PsnMailSetService {

  /**
   * 新增或修改个人邮件设置
   * 
   * @param psnMailSet
   */
  public void addOrMidify(PsnMailSet psnMailSet);

  /**
   * 根据个人Id删除该人的邮件设置
   * 
   * @param psnId
   * @return
   */
  public int removes(Long psnId);

  /**
   * 根据个人Id取得该人的邮件设置信息
   * 
   * @param psnId
   * @return
   */
  public List<PsnMailSet> list(Long psnId);

  /**
   * 获取邮件类型列表
   * 
   * @return
   */
  public List<ConstMailType> getMailTypeList();

  /**
   * 是否关闭了发送邮件的功能,关闭返回true，否则返回false
   * 
   * @param psnId 个人Id
   * @param mailTypeId 邮件类型Id
   * @return
   */
  public boolean isClosed(Long psnId, Long mailTypeId);

  /**
   * 初始化个人邮件设置
   * 
   * @param psnId
   * @return
   */
  public boolean iniPsnMailSet(Long psnId);

}
