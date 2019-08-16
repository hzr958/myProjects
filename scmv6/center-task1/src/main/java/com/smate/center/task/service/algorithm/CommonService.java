package com.smate.center.task.service.algorithm;

import java.util.List;

/**
 * 推荐算法基类
 * 
 * @author lichangwen
 * 
 */
public interface CommonService {

  /**
   * 清理原推荐数据
   * 
   * @param id {psnId或pubId}
   * @param list 满足必要条件的结果集
   */
  void clear(Long id, List<?> list);

  /**
   * 推荐数据保存
   * 
   * @param id {psnId或pubId}
   * @param scoreList 根据算法得分排序后的结果集
   */
  void save(Long id, List<?> scoreList);

  /**
   * 推荐度
   * 
   * @param id {psnId或pubId}
   */
  void degrees(Long id);

  /**
   * 推荐产品
   * 
   * @param id {psnId或pubId}
   * @param list 满足必要条件的结果集
   */
  void product(Long id, List<?> list);

}
