package com.smate.web.dyn.service.news;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.utils.date.DateUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.dyn.dao.news.*;
import com.smate.web.dyn.form.news.NewsForm;
import com.smate.web.dyn.model.news.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

/**
 * 新闻操作服务
 * 
 * @author YHX
 *
 */
@Service("newsOptService")
@Transactional(rollbackOn = Exception.class)
public class NewsOptServiceImpl implements NewsOptService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private NewsBaseDao newsBaseDao;
  @Autowired
  private NewsLikeDao newsLikeDao;
  @Autowired
  private NewsStatisticsDao newsStatisticsDao;
  @Autowired
  private NewsViewDao newsViewDao;
  @Autowired
  private NewsShareDao newsShareDao;

  @Override
  public void updateNewsAward(NewsForm form) throws ServiceException {
    // 校验
    if (!checkParams(form)) {
      return;
    }
    Long newsId = form.getNewsId();
    Long psnId = form.getPsnId();
    Integer operate = form.getOperate();
    // 新增修改赞记录表
    updateAward(newsId, psnId, operate);
    // 更新统计表赞记录数
    updateLikeStatistics(newsId, operate);
    // 更新热度
    if (operate == 1) {
      addNewsHeat(newsId);// 赞-增加热度
    } else {
      subNewsHeat(newsId);// 取消赞-减少热度
    }
    form.setAwardTimes(getAwardCounts(newsId));
  }

  @Override
  public void updateLikeStatistics(Long newsId, Integer status) throws ServiceException {
    try {
      NewsStatistics newsStatistics = newsStatisticsDao.get(newsId);
      if (newsStatistics == null) {
        newsStatistics = new NewsStatistics();
        newsStatistics.setNewsId(newsId);
      }
      Integer awardCount = newsStatistics.getAwardCount() == null ? 0 : newsStatistics.getAwardCount();
      switch (status) {
        case 1:
          newsStatistics.setAwardCount(awardCount + 1);
          break;
        case 0:
          newsStatistics.setAwardCount(awardCount - 1 < 0 ? 0 : newsStatistics.getAwardCount() - 1);
          break;
      }
      newsStatisticsDao.save(newsStatistics);
    } catch (Exception e) {
      logger.error("更新新闻统计表赞统计数异常,newsId=" + newsId, e);
      throw new ServiceException(e);
    }

  }

  @Override
  public boolean checkNews(Long newsId) throws ServiceException {
    if (NumberUtils.isNullOrZero(newsId)) {
      return false;
    }
    NewsBase news = newsBaseDao.get(newsId);
    if (news == null || news.getStatus() == 9) {
      return false;
    }
    return true;
  }

  @Override
  public void addNewsHeat(Long newsId) throws ServiceException {
    NewsBase news = newsBaseDao.get(newsId);
    news.setHeat(news.getHeat() + 1);
    newsBaseDao.save(news);

  }

  @Override
  public void subNewsHeat(Long newsId) throws ServiceException {
    NewsBase news = newsBaseDao.get(newsId);
    int heat = news.getHeat() - 1 < 0 ? 0 : news.getHeat() - 1;
    news.setHeat(heat);
    newsBaseDao.save(news);
  }

  public boolean checkParams(NewsForm form) {
    int count = newsLikeDao.isLike(form.getNewsId(), form.getPsnId(), form.getOperate());
    if (count > 0) {
      logger.info("你已赞/取消赞过该条新闻,psnId= " + form.getPsnId() + " ,newsId= " + form.getNewsId());
      return false;
    }
    return true;
  }

  public void updateAward(Long newsId, Long psnId, Integer operate) {
    NewsLike newsLike = newsLikeDao.findByNewsIdAndPsnId(newsId, psnId);
    if (operate == 1) {// 点赞
      if (newsLike == null) {
        newsLike = new NewsLike();
        newsLike.setNewsId(newsId);
        newsLike.setLikePsnId(psnId);
        newsLike.setGmtCreate(new Date());
      }
    } else {// 取消赞
      if (newsLike == null) {
        logger.error("取消赞时未查找到赞记录psnId=" + psnId + " ,newsId= " + newsId);
        return;
      }
    }
    newsLike.setStatus(operate);
    newsLike.setGmtUpdate(new Date());
    newsLikeDao.save(newsLike);
  }

  public int getAwardCounts(Long newsId) throws ServiceException {
    int awardCount = 0;
    NewsStatistics newsStatistics = newsStatisticsDao.get(newsId);
    if (newsStatistics != null) {
      awardCount = newsStatistics.getAwardCount() == null ? 0 : newsStatistics.getAwardCount();
    }
    return awardCount;
  }

  @Override
  public void addNewsView(NewsForm form) throws ServiceException {
    Long newsId = form.getNewsId();
    try {
      // 增加新闻访问记录
      insertView(form);
      // 更新新闻统计表 访问数
      updateViewStatistics(newsId);
      // 增加新闻热度
      addNewsHeat(newsId);
    } catch (Exception e) {
      logger.error("新闻增加查看记录异常,newsId=" + newsId, e);
      throw new ServiceException(e);
    }

  }

  private void insertView(NewsForm form) {
    try {
      long formateDate = DateUtils.getDateTime(new Date());
      String ip = Struts2Utils.getRemoteAddr();
      NewsView newsView = newsViewDao.findNewsView(form.getNewsId(), form.getPsnId(), formateDate, ip);
      if (newsView == null) {
        newsView = new NewsView();
        newsView.setNewsId(form.getNewsId());
        newsView.setViewPsnId(form.getPsnId());
        newsView.setIp(ip);
        newsView.setGmtCreate(new Date());
        newsView.setFormateDate(formateDate);
        newsView.setTotalCount(1l);
      } else {
        newsView.setGmtCreate(new Date());
        newsView.setFormateDate(formateDate);
        long viewCount = newsView.getTotalCount() == null ? 0 : newsView.getTotalCount();
        newsView.setTotalCount(viewCount + 1);
      }
      newsViewDao.saveOrUpdate(newsView);
    } catch (Exception e) {
      logger.error("新闻查看记录插入异常,newsId=" + form.getNewsId(), e);
    }
  }

  @Override
  public void updateViewStatistics(Long newsId) throws ServiceException {
    try {
      NewsStatistics newsStatistics = newsStatisticsDao.get(newsId);
      if (newsStatistics == null) {
        newsStatistics = new NewsStatistics();
        newsStatistics.setNewsId(newsId);
        newsStatistics.setViewCount(1);
      } else {
        int viewCount = newsStatistics.getViewCount() == null ? 0 : newsStatistics.getViewCount();
        newsStatistics.setViewCount(viewCount + 1);
      }
      newsStatisticsDao.save(newsStatistics);
    } catch (Exception e) {
      logger.error("更新访问统计数出错！newsId={}", newsId);
      throw new ServiceException(e);
    }
  }

  @Override
  public void addNewsShare(NewsForm form ) throws ServiceException {
    Long newsId = form.getNewsId();
    try {
      insertShare(form);
      // 更新新闻统计表 访问数
      updateShareStatistics(newsId);
      // 增加新闻热度
      addNewsHeat(newsId);
    } catch (Exception e) {
      logger.error("新闻增加查看记录异常,newsId=" + newsId, e);
      throw new ServiceException(e);
    }

  }
  private  void insertShare(NewsForm form){
    // 增加新闻分享记录
    NewsShare share = new NewsShare();
    share.setNewsId(form.getNewsId());
    share.setSharePsnId(form.getPsnId());
    share.setContent(form.getContent());
    share.setStatus(0);
    share.setPlatform(form.getPlatform());
    share.setBeSharedId(form.getBeSharedId());
    share.setGmtCreate(new Date());
    share.setGmtUpdate(share.getGmtCreate());
    newsShareDao.save(share);
  }

  public void updateShareStatistics(Long newsId) throws ServiceException {
    try {
      NewsStatistics newsStatistics = newsStatisticsDao.get(newsId);
      if (newsStatistics == null) {
        newsStatistics = new NewsStatistics();
        newsStatistics.setNewsId(newsId);
        newsStatistics.setShareCount(1);
      } else {
        int count = newsStatistics.getShareCount() == null ? 0 : newsStatistics.getShareCount();
        newsStatistics.setShareCount(count + 1);
      }
      newsStatisticsDao.save(newsStatistics);
    } catch (Exception e) {
      logger.error("更新访问统计数出错！newsId={}", newsId);
      throw new ServiceException(e);
    }
  }
}
