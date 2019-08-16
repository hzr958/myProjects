package com.smate.web.dyn.service.psn;

import java.util.Map;

import com.smate.core.base.utils.exception.DynException;

/**
 * 单位信息服务接口
 * 
 * @author zk
 *
 */
public interface InsInfoService {


  public static String INS_INFO_ZH = "ins_info_zh"; // 中文单位信息
  public static String INS_INFO_EN = "ins_info_en"; // 英文单位信息

  /**
   * 获取人员首要单位名称+人员职称 支持获取指定语言，默认中英文都返回
   * 
   * @param psnId
   * @return
   * @throws DynException
   */
  Map<String, String> findPsnInsInfo(Long psnId) throws DynException;
}
