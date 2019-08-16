package com.smate.center.task.service.sns.quartz;

import java.util.List;

public interface PsnKwRmcService {

  // --获取5个小时前的用户刷新数据
  public List<Long> getRefreshData();

  public int getRefreshFlag();

  public void deleteFromTmp();

  public void handlePsnKwRmc(Long psnId);

  public void updateRefreshFlag();

  public void updateRefreshData(Long psnId);

  public void handleKwRcmdScore(Long psnId);

  public void HandlesupplementPsnKwRmc(Long psnId);

  public void ItoratorZhKw(Long psnId);

  public void ItoratorEnKw(Long psnId);

  /**
   * 获取推荐研究领域的人员ID任务列表<推荐研究领域邮件功能用到此方法>
   * 
   * @param maxSize
   * @return
   */
  List<Long> getPsnIdList(Long startPsnId, Integer maxSize);
}
