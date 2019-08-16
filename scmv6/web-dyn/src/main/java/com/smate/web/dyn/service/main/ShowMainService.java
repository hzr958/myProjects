package com.smate.web.dyn.service.main;

import com.smate.web.dyn.form.main.MainForm;

/**
 * 主页显示服务接口
 * 
 * @author zzx
 *
 */
public interface ShowMainService {
  /**
   * 显示主页
   * 
   * @param form
   * @throws Exception
   */
  void main(MainForm form) throws Exception;

  /**
   * 显示主页常用功能
   * 
   * @param form
   * @throws Exception
   */
  void mainShortcuts(MainForm form) throws Exception;

}
