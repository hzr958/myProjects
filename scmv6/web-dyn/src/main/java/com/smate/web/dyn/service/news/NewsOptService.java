package com.smate.web.dyn.service.news;

import com.smate.core.base.exception.ServiceException;
import com.smate.web.dyn.form.news.NewsForm;

/**
 * 新闻操作服务接口
 * 
 * @author YHX
 *
 */
public interface NewsOptService {

  /**
   * 新闻赞操作
   * 
   * @param form
   * @throws ServiceException
   */
  void updateNewsAward(NewsForm form) throws ServiceException;

  /**
   * 更新统计表赞统计数
   * 
   * @param newsId
   * @param status
   * @throws ServiceException
   */
  void updateLikeStatistics(Long newsId, Integer status) throws ServiceException;

  /**
   * 增加新闻热度
   * 
   * @param newsId
   * @throws ServiceException
   */
  void addNewsHeat(Long newsId) throws ServiceException;

  /**
   * 减少新闻热度
   * 
   * @param newsId
   * @throws ServiceException
   */
  void subNewsHeat(Long newsId) throws ServiceException;

  /**
   * 检查新闻是否删除
   * 
   * @param newsId
   * @return
   * @throws ServiceException
   */
  public boolean checkNews(Long newsId) throws ServiceException;

  /**
   * 增加访问记录
   * 
   * @param form
   * @throws ServiceException
   */
  void addNewsView(NewsForm form) throws ServiceException;

  /**
   * 增加新闻统计表 访问数
   * 
   * @param newsId
   * @throws ServiceException
   */
  void updateViewStatistics(Long newsId) throws ServiceException;

  /**
   * 增加分享
   *
   * @param form
   * @throws ServiceException
   */
  void addNewsShare(NewsForm form) throws ServiceException;
}
