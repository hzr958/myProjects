package com.smate.center.batch.service.rol.pub;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.constant.TemplateConstants;
import com.smate.center.batch.dao.rol.pub.PubHtmlDao;
import com.smate.center.batch.dao.rol.pub.PubHtmlRefreshDao;
import com.smate.center.batch.dao.sns.pub.HtmlExtendDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.PubHtml;
import com.smate.center.batch.model.rol.pub.PubHtmlRefresh;
import com.smate.center.batch.model.sns.pub.HtmlExtend;

/**
 * 成果HTML服务
 * 
 * @author zk
 * 
 */
@Service("pubHtmlService")
@Transactional(rollbackFor = Exception.class)
public class PubHtmlServiceImpl implements PubHtmlService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubHtmlRefreshDao pubHtmlRefreshDao;
  @Autowired
  private PubHtmlDao pubHtmlDao;
  @Autowired
  private HtmlExtendDao htmlExtendDao;

  /**
   * 保存成果Html
   */
  @Override
  public void savePubHtml(PubHtml pubHtml) throws ServiceException {

    this.updatePubHtmlExtend(pubHtml);
    this.updatePubHtml(pubHtml);
    this.checkPubHtmlRefresh(pubHtml);
  }

  // 先处理html是否超长
  private void updatePubHtmlExtend(PubHtml pubHtml) throws ServiceException {

    // 长度大于限定长度时，将html保存到扩展表clob字段中
    if (StringUtils.length(pubHtml.getHtmlZh()) > TemplateConstants.HTML_LENGTH
        || StringUtils.length(pubHtml.getHtmlEn()) > TemplateConstants.HTML_LENGTH) {
      HtmlExtend htmlExtend = new HtmlExtend();
      Long extendId = htmlExtendDao.getHtmlExtendId();
      htmlExtend.setId(extendId);
      htmlExtend.setHtmlZh(pubHtml.getHtmlZh());
      htmlExtend.setHtmlEn(pubHtml.getHtmlEn());
      htmlExtendDao.save(htmlExtend);
      // 清除prjhtml中数据，超过限制长度会报错
      pubHtml.setHtmlEn(null);
      pubHtml.setHtmlZh(null);
      pubHtml.setExtendId(extendId);
    }
  }

  // 处理html
  private void updatePubHtml(PubHtml pubHtml) throws ServiceException {

    // 查重、更新
    PubHtml ph = pubHtmlDao.findByPsnIdAndTempCode(pubHtml.getPubId(), pubHtml.getTempCode());
    if (ph == null) {
      pubHtmlDao.save(pubHtml);
    } else {
      // 删除旧扩展数据
      if (ph.getExtendId() != null) {
        htmlExtendDao.delete(ph.getExtendId());
      }
      ph.setHtmlZh(pubHtml.getHtmlZh());
      ph.setHtmlEn(pubHtml.getHtmlEn());
      ph.setExtendId(pubHtml.getExtendId());
      pubHtmlDao.save(ph);
    }
  }

  // 处理html刷新表
  private void checkPubHtmlRefresh(PubHtml pubHtml) throws ServiceException {

    PubHtmlRefresh pubHtmlRefresh = pubHtmlRefreshDao.findByPsnIdAndTempCode(pubHtml.getPubId(), pubHtml.getTempCode());
    if (pubHtmlRefresh == null) {
      pubHtmlRefresh = new PubHtmlRefresh();
      pubHtmlRefresh.setPubId(pubHtml.getPubId());
      pubHtmlRefresh.setTempCode(pubHtml.getTempCode());
      pubHtmlRefresh.setStatus(0);
      pubHtmlRefreshDao.save(pubHtmlRefresh);
    } else {
      if (pubHtmlRefresh.getStatus() == 1) {
        pubHtmlRefresh.setStatus(0);
        pubHtmlRefreshDao.save(pubHtmlRefresh);
      }
    }
  }

  /**
   * 获取人员ＨＴＭＬ
   * 
   * 
   */
  @Override
  public PubHtml findPubHtml(Long pubId, Integer tempCode) throws ServiceException {

    PubHtml pubHtml = pubHtmlDao.findPubHtml(pubId, tempCode);
    if (pubHtml == null) {
      return pubHtml;
    }
    if (pubHtml.getExtendId() != null) {
      HtmlExtend htmlExtend = htmlExtendDao.get(pubHtml.getExtendId());
      if (htmlExtend != null) {
        pubHtml.setHtmlZh(htmlExtend.getHtmlZh());
        pubHtml.setHtmlEn(htmlExtend.getHtmlEn());
      }
    }
    return pubHtml;
  }

  @Override
  public void updatePubHtmlRefresh(Long pubId, Integer tempCode) throws ServiceException {
    try {
      PubHtmlRefresh pubHtmlRefresh = pubHtmlRefreshDao.findByPsnIdAndTempCode(pubId, tempCode);
      if (pubHtmlRefresh == null) {
        pubHtmlRefresh = new PubHtmlRefresh();
        pubHtmlRefresh.setPubId(pubId);
        pubHtmlRefresh.setTempCode(tempCode);
      }
      pubHtmlRefresh.setStatus(1);
      pubHtmlRefreshDao.save(pubHtmlRefresh);
    } catch (Exception e) {
      logger.error("更新成果html刷新表出现错误,pubId=" + pubId + ",tempCode=" + tempCode, e);
      throw new ServiceException("更新成果html刷新表出现错误,pubId=" + pubId + ",tempCode=" + tempCode, e);
    }
  }

  @Override
  public List<PubHtmlRefresh> findNeedRefresh(Integer tempCode, int size) throws ServiceException {
    return pubHtmlRefreshDao.findNeedRefresh(tempCode, size);
  }

  @Override
  public List<PubHtmlRefresh> findNeedRefresh(int size) throws ServiceException {
    try {
      return pubHtmlRefreshDao.findNeedRefresh(size);
    } catch (Exception e) {
      logger.error("批量获取需要刷新的成果数据，出现错误！", e);
      throw new ServiceException("批量获取需要刷新的成果数据，出现错误！", e);
    }
  }

}
