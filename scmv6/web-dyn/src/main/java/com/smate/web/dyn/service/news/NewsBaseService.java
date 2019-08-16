package com.smate.web.dyn.service.news;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.utils.model.security.UserRole;
import com.smate.web.dyn.form.news.NewsForm;
import com.smate.web.dyn.model.news.NewsBase;

import java.util.List;

/**
 * 
 * @author Administrator
 *
 */
public interface NewsBaseService {

  /**
   * 查找新闻列表
   *
   * @param form
   * @throws Exception
   */
  public void findNewsList(NewsForm form) throws Exception;

  /**
   * 获取用户的角色
   * 
   * @param form
   * @return
   * @throws Exception
   */
  public List<UserRole> getUserRole(NewsForm form) throws ServiceException;

  /**
   * 判断是否是管理员角色
   * 
   * @param form
   * @return
   * @throws Exception
   */
  public boolean isManageRole(NewsForm form) throws ServiceException;


  /**
   * 新闻详情页面
   *
   * @param form
   * @throws ServiceException
   */
  public void viewNewsDetails(NewsForm form) throws ServiceException;

  public void findNewsRcmd(NewsForm form) throws ServiceException;

  void insertNewsRecmRecord(Long psnId, Long newsId) throws ServiceException;

  public NewsBase get(Long Id)throws ServiceException;
}
