package com.smate.web.psn.service.ins;

/**
 * 个人工作、教育经历的机构信息统计服务类
 * 
 * @author xiexing
 * @date 2019年1月24日
 */
public interface InsPsnCountService {
  /**
   * 添加(删除)工作、教育经历都要增加(减少)此数字, 1表示添加、2表示删除
   * 
   * @param insId
   * @throws Exception
   */
  public void addPsnCount(String insName, Integer type) throws Exception;
}
