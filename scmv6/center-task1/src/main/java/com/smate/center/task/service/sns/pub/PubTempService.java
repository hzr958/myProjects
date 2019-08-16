package com.smate.center.task.service.sns.pub;

import java.util.List;

import com.smate.center.task.model.sns.pub.PubTemp;

/**
 * @description 会议论文数据统计服务类
 * @author xiexing
 * @date 2019年2月28日
 */
public interface PubTempService {
  /**
   * 保存查询的数据
   * 
   * @param pubTemp
   * @throws Exception
   */
  public void update(PubTemp pubTemp) throws Exception;

  /**
   * 从备份表中获取id 一次获取size条
   * 
   * @param size
   * @return
   */
  public List<Long> getIds(Integer size);
}
