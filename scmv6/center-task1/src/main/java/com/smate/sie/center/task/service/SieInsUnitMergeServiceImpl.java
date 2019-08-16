package com.smate.sie.center.task.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.sie.center.task.dao.SieInsUnitMergeDao;

@Service("sieInsUnitMergeService")
@Transactional(rollbackFor = Exception.class)
public class SieInsUnitMergeServiceImpl implements SieInsUnitMergeService {


  @Autowired
  private SieInsUnitMergeDao sieInsUnitMergeDao;

  /**
   * 根据当前参数beMergeUnitId(被合并部门), 查询merge_unit, 得到最终的(合并至部门) mergeToUnitId
   */
  @Override
  public Long getFinalUnitId(Long insId, Long beMergeUnitId) {
    Long mergeToUnitId = null;
    while (beMergeUnitId != null) {
      mergeToUnitId = beMergeUnitId;
      beMergeUnitId = sieInsUnitMergeDao.getMergeToUnitId(insId, beMergeUnitId);
    }
    // 返回最终合并至部门mergeToUnitId;
    return mergeToUnitId;
  }

}
