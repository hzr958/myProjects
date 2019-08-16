package com.smate.web.management.service.news;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.management.dao.news.NewsBaseDao;
import com.smate.web.management.model.news.NewsBase;
import com.smate.web.management.model.news.NewsForm;
import com.smate.web.management.model.news.NewsShowInfo;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

/**
 * 新闻服务类
 * 
 * @author Administrator
 *
 */
@Service("newsOptService")
@Transactional(rollbackOn = Exception.class)
public class NewsOptServiceImpl implements NewsOptService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private NewsBaseDao newsBaseDao;

  @Override
  public void edit(NewsForm form) throws Exception {
    NewsBase newsBase = newsBaseDao.get(form.getNewsId());
    if (newsBase != null && newsBase.getStatus() != 9) {
      NewsShowInfo info = new NewsShowInfo();
      info.setId(newsBase.getId());
      info.setDes3NewsId(form.getDes3NewsId());
      info.setTitle(newsBase.getTitle());
      info.setBrief(newsBase.getBrief());
      info.setContent(newsBase.getContent());
      info.setImage(newsBase.getImage());
      info.setPublish(newsBase.getStatus() == 1 ? true : false);
      form.setNewsShowInfo(info);
    }
  }

  @Override
  public void save(NewsForm form) throws Exception {
    NewsBase newsBase = null;
    if (NumberUtils.isNotNullOrZero(form.getNewsShowInfo().getId())) {
      newsBase = newsBaseDao.get(form.getNewsShowInfo().getId());
    }
    if (newsBase != null && form.getNewsShowInfo().isPublish()) {
      newsBase.setGmtUpdate(new Date());
      newsBase.setStatus(1);
      newsBase.setGmtPublish(newsBase.getGmtUpdate());
    } else {
      if(newsBase == null){
        newsBase = new NewsBase();
        Long id = newsBaseDao.getId();
        newsBase.setId(id);
        newsBase.setSeqNo(id);
      }
      newsBase.setGmtCreate(new Date());
      newsBase.setGmtUpdate(newsBase.getGmtCreate());
      if(form.getNewsShowInfo().isPublish()){
        newsBase.setGmtPublish(newsBase.getGmtUpdate());
        newsBase.setStatus(1);
      }
    }
    newsBase.setTitle(StringEscapeUtils.unescapeHtml4(form.getNewsShowInfo().getTitle()));
    newsBase.setBrief(StringEscapeUtils.unescapeHtml4(form.getNewsShowInfo().getBrief()));
    newsBase.setContent(form.getNewsShowInfo().getContent());
    newsBase.setImage(form.getNewsShowInfo().getImage());
    newsBase.setCreatePsnId(form.getPsnId());
    newsBaseDao.save(newsBase);
    form.setDes3NewsId(Des3Utils.encodeToDes3(newsBase.getId().toString()));
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
  public void deleteNews(Long newsId) throws ServiceException {
    NewsBase news = newsBaseDao.get(newsId);
    if (news != null) {
      news.setStatus(9);
      news.setGmtUpdate(new Date());
      newsBaseDao.saveOrUpdate(news);
    }
  }

  @Override
  public void publish(NewsForm form) throws Exception {
    NewsBase news = newsBaseDao.get(form.getNewsId());
    if (news != null) {
      news.setStatus(1);
      news.setGmtPublish(new Date());
      newsBaseDao.saveOrUpdate(news);
    }
  }

  @Override
  public void changeNewsSeqno(Long newsId, Long nextNewsId) throws ServiceException {
    NewsBase news = newsBaseDao.get(newsId);
    NewsBase nextNews = newsBaseDao.get(nextNewsId);
    if (news != null && nextNews != null) {
      Long seqNo = news.getSeqNo();
      news.setSeqNo(nextNews.getSeqNo());
      news.setGmtUpdate(new Date());
      nextNews.setSeqNo(seqNo);
      nextNews.setGmtUpdate(new Date());
      newsBaseDao.saveOrUpdate(news);
      newsBaseDao.saveOrUpdate(nextNews);
    }
  }
}
