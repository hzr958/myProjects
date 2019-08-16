package com.smate.sie.center.task.service;

import java.util.List;

import com.smate.sie.center.task.model.SieMergeInsReflush;

/***
 * 合并单位服务
 * 
 * @author 叶星源
 * @Date 201810
 */
public interface SieMergeInsMainService {
  List<SieMergeInsReflush> getNeedRefreshData(int size);

  // 合并单位的主逻辑
  void mergeInsMainMethod(SieMergeInsReflush sieMergeInsReflush);

  // 设置状态
  void finishDoneMethod(SieMergeInsReflush sieMergeInsReflush, Integer status);

  // 删除数据
  void deleteOriginalData(long mergeid);
}
