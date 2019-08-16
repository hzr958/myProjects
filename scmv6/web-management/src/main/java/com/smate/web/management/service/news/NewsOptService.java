package com.smate.web.management.service.news;


import com.smate.core.base.exception.ServiceException;
import com.smate.web.management.model.news.NewsForm;

/**
 *
 */
public interface NewsOptService {


  /**
   * 编辑新闻
   * 
   * @param form
   * @throws Exception
   */
  public void edit(NewsForm form) throws Exception;

  /**
   * 保存新闻
   * 
   * @param form
   * @throws Exception
   */
  public void save(NewsForm form) throws Exception;

  /**
   * 发布新闻
   * 
   * @param form
   * @throws Exception
   */
  public void publish(NewsForm form) throws Exception;

  /**
   * 检查新闻是否删除
   *
   * @param newsId
   * @return
   * @throws ServiceException
   */
  public boolean checkNews(Long newsId) throws ServiceException;

  /**
   * 删除新闻
   *
   * @param newsId
   * @throws ServiceException
   */
  public void deleteNews(Long newsId) throws ServiceException;

  /**
   * 新闻 上移/下移
   * 
   * @param newsId
   * @param nextNewsId
   * @throws ServiceException
   */
  public void changeNewsSeqno(Long newsId, Long nextNewsId) throws ServiceException;
}
