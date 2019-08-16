package com.smate.sie.center.task.service;

/**
 * 部门合并历史记录
 * 
 * @author ztg
 *
 */
public interface SieInsUnitMergeService {


  // 根据当前参数beMergeUnitId(被合并部门), 查询merge_unit, 得到最终的(合并至部门) mergeToUnitId
  Long getFinalUnitId(Long insId, Long beMergeUnitId);

}
