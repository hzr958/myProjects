package com.smate.web.psn.service.setting;

import java.util.List;

import com.smate.web.psn.model.setting.ConstMailType;
import com.smate.web.psn.model.setting.PsnMailSet;


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
   * 根据个人Id删除该人的邮件设置
   * 
   * @param psnId
   * @return
   */
  public int removes(Long psnId);

  /**
   * 新增或修改个人邮件设置
   * 
   * @param psnMailSet
   */
  public void addOrMidify(PsnMailSet psnMailSet);

  /**
   * 根据个人Id取得该人的邮件设置信息
   * 
   * @param psnId
   * @return
   */
  public List<PsnMailSet> list(Long psnId);

  public String psnMailSet(String repsnId, String typeid);

  public String getMailById(Long id);

}
