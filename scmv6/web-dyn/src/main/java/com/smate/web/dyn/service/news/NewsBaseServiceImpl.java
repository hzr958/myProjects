package com.smate.web.dyn.service.news;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.utils.dao.security.UserRoleDao;
import com.smate.core.base.utils.date.DateUtils;
import com.smate.core.base.utils.model.security.UserRole;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.dyn.dao.news.NewsBaseDao;
import com.smate.web.dyn.dao.news.NewsLikeDao;
import com.smate.web.dyn.dao.news.NewsRecommendRecordDAO;
import com.smate.web.dyn.dao.news.NewsStatisticsDao;
import com.smate.web.dyn.form.news.NewsForm;
import com.smate.web.dyn.form.news.NewsShowInfo;
import com.smate.web.dyn.model.news.NewsBase;
import com.smate.web.dyn.model.news.NewsRecommendRecord;
import com.smate.web.dyn.model.news.NewsStatistics;

/**
 * 新闻服务类
 * 
 * @author Administrator
 *
 */
@Service("newsBaseService")
@Transactional(rollbackOn = Exception.class)
public class NewsBaseServiceImpl implements NewsBaseService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private NewsBaseDao newsBaseDao;
  @Autowired
  private NewsStatisticsDao newsStatisticsDao;
  @Autowired
  private UserRoleDao userRoleDao;
  @Autowired
  private NewsLikeDao newsLikeDao;
  @Autowired
  private NewsRecommendRecordDAO newsRecommendRecordDAO;

  @Override
  public void findNewsList(NewsForm form) throws Exception {
    form.setManager(isManageRole(form));
    newsBaseDao.findNewsList(form);
    if (CollectionUtils.isNotEmpty(form.getPage().getResult())) {
      for (Object obj : form.getPage().getResult()) {
        NewsShowInfo info = new NewsShowInfo();
        buildNewsShowInfo((NewsBase) obj, info);
        form.getList().add(info);
      }
    }
  }

  @Override
  public List<UserRole> getUserRole(NewsForm form) throws ServiceException {
    return userRoleDao.getUserRole(form.getPsnId());
  }

  /**
   * 判断是否是管理员
   * 
   * @param form
   * @return
   * @throws Exception
   */
  @Override
  public boolean isManageRole(NewsForm form) throws ServiceException {
    List<UserRole> userRoles = getUserRole(form);
    if (CollectionUtils.isNotEmpty(userRoles)) {
      for (UserRole role : userRoles) {
        // 新闻管理角色
        if (role.getId().getRolId() == 9L || role.getId().getRolId() == 2L) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * 构建显示信息
   *
   * @param base
   */
  private void buildNewsShowInfo(NewsBase base, NewsShowInfo info) {
    Long newsId = base.getId();
    info.setTitle(base.getTitle());
    info.setBrief(base.getBrief());
    info.setId(newsId);
    info.setImage(base.getImage());
    info.setGmtCreate(DateUtils.getDateFormat(base.getGmtCreate(), "yyyy-MM-dd HH:mm"));
    info.setDes3NewsId(Des3Utils.encodeToDes3(newsId.toString()));
    info.setContent(base.getContent());
    info.setGmtPublish(DateUtils.getDateFormat(base.getGmtPublish(), "yyyy-MM-dd HH:mm"));
    info.setGmtUpdate(DateUtils.getDateFormat(base.getGmtUpdate(), "yyyy-MM-dd HH:mm"));
    info.setPublish(base.getStatus() == 1 ? true : false);
    info.setSeqNo(base.getSeqNo());
    NewsStatistics newsStatistics = newsStatisticsDao.get(base.getId());
    if (newsStatistics != null) {
      info.setAwardCount(newsStatistics.getAwardCount());
      info.setShareCount(newsStatistics.getShareCount());
      info.setViewCount(newsStatistics.getViewCount());
    }
    long count = newsLikeDao.getLikeRecord(newsId, SecurityUtils.getCurrentUserId());
    if (count > 0) {
      info.setIsAward(1);
    }
  }

  @Override
  public void viewNewsDetails(NewsForm form) throws ServiceException {
    form.setIsManager(isManageRole(form));
    NewsBase news = newsBaseDao.get(form.getNewsId());
    NewsShowInfo info = new NewsShowInfo();
    buildNewsShowInfo(news, info);
    // 上一篇 下一篇 新闻
    Long priorId = newsBaseDao.findPriorNews(form.getNewsId());
    if (priorId != null && priorId != 0L) {
      form.setPriorNews(newsBaseDao.get(priorId));
    }
    Long nextId = newsBaseDao.findNextNews(form.getNewsId());
    if (nextId != null) {
      form.setNextNews(newsBaseDao.get(nextId));
    }

    // 增加SEO
    form.setSeoTitle(news.getTitle());
    form.setSeoDescription(news.getBrief());
    form.setNewsShowInfo(info);
  }

  @Override
  public void findNewsRcmd(NewsForm form) throws ServiceException {
    List<Long> excludeNewsIds = buildExcludeNewsIdList(form.getDes3NewsId(), form.getPsnId());
    form.setNewsIds(excludeNewsIds);
    newsBaseDao.findNewsRcmd(form);
    if (CollectionUtils.isNotEmpty(form.getPage().getResult())) {
      for (Object obj : form.getPage().getResult()) {
        NewsShowInfo info = new NewsShowInfo();
        buildNewsShowInfo((NewsBase) obj, info);
        form.getList().add(info);
      }
    }
  }

  /**
   * 构建排除的新闻id
   * 
   */
  private List<Long> buildExcludeNewsIdList(String newsIds, Long psnId) {
    // 先排除用户不感兴趣的推荐论文
    List<Long> excludeNewsIds = newsRecommendRecordDAO.getNewsIdsByPsnId(psnId, 1);
    if (StringUtils.isNotBlank(newsIds)) {
      String[] splitNewsIds = newsIds.split(",");
      for (String newsIdStr : splitNewsIds) {
        long newsId = NumberUtils.toLong(Des3Utils.decodeFromDes3(newsIdStr));
        excludeNewsIds.add(newsId);
      }
      if (excludeNewsIds.size() > 1000) {
        excludeNewsIds.subList(0, 1000);
      }
    }
    return excludeNewsIds;
  }

  @Override
  public void insertNewsRecmRecord(Long psnId, Long newsId) throws ServiceException {
    try {
      NewsRecommendRecord newsRecm = newsRecommendRecordDAO.findRecordByNewsIdAndPsnId(psnId, newsId);
      if (newsRecm == null) {
        newsRecm = new NewsRecommendRecord();
        newsRecm.setPsnId(psnId);
        newsRecm.setNewsId(newsId);
        newsRecm.setStatus(1);// 状态：0正常，1不感兴趣
        newsRecm.setGmtCreate(new Date());
        newsRecm.setGmtModified(new Date());
        newsRecommendRecordDAO.save(newsRecm);
      }
    } catch (Exception e) {
      logger.error("插入V_NEWS_RECOMMEND_RECORD表出错，psnId=" + psnId + ",newsId=" + newsId, e);
    }

  }

  @Override
  public NewsBase get(Long newsId) throws ServiceException {
    try {
      return newsBaseDao.get(newsId);
    } catch (Exception e) {
      logger.error("查询新闻消息异常,newsId=" + newsId, e);
    }
    return null;
  }
}
