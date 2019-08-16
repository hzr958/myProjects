package com.smate.center.open.service.register;

import java.util.List;

import com.smate.center.open.model.consts.ConstMailType;
import com.smate.center.open.model.psn.PsnMailSet;



/**
 * 个人邮件设置接口
 * 
 * @author YPH
 * 
 */
public interface PsnMailSetService {

  /**
   * 获取邮件类型列表
   * 
   * @return
   */
  public List<ConstMailType> getMailTypeList();

  /**
   * 新增或修改个人邮件设置
   * 
   * @param psnMailSet
   */
  public void addOrMidify(PsnMailSet psnMailSet);

  /**
   * 初始化个人邮件设置
   * 
   * @param psnId
   * @return
   */
  public boolean iniPsnMailSet(Long psnId);
}
