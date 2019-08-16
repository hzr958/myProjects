package com.smate.center.batch.service.pub;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.pub.PubFundinfoDao;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.PubFundinfo;
import com.smate.center.batch.oldXml.prj.PrjRelatedPubContant;
import com.smate.center.batch.service.pub.mq.PrjRelatedPubRefreshMessageProducer;

/**
 * 成果基金标注SERVICE实现.
 * 
 * @author xys
 * 
 */
@Service("pubFundinfoService")
@Transactional(rollbackFor = Exception.class)
public class PubFundinfoServiceImpl implements PubFundinfoService {

  /**
   * 
   */
  private static final long serialVersionUID = -1598928635567576491L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubFundinfoDao pubFundinfoDao;
  @Autowired
  private PrjRelatedPubRefreshMessageProducer prjRelatedPubRefreshMessageProducer;

  // @Autowired
  // private PrjRelatedPubRefreshMessageProducer
  // prjRelatedPubRefreshMessageProducer;

  @Override
  public void syncPubFundinfo(Long pubId, Long psnId, Integer typeId, String fundinfo) throws ServiceException {
    try {
      boolean syncPrjRelPub = false;// 是否需要同步项目相关成果刷新
      PubFundinfo pubFundinfo = pubFundinfoDao.get(pubId);
      if (StringUtils.isBlank(fundinfo)) {
        if (pubFundinfo != null) {// 编辑/删除成果基金标注
          pubFundinfoDao.delete(pubFundinfo);
          syncPrjRelPub = true;
        }
      } else {
        fundinfo = fundinfo.length() > 1000 ? fundinfo.substring(0, 1000) : fundinfo;
        if (pubFundinfo != null) {// 编辑成果基金标注
          if (!fundinfo.equalsIgnoreCase(pubFundinfo.getFundinfo())) {
            syncPrjRelPub = true;
          }
          pubFundinfo.setTypeId(typeId);
          pubFundinfo.setFundinfo(fundinfo);
        } else {// 新增/编辑成果基金标注
          pubFundinfo = new PubFundinfo(pubId, psnId, typeId, fundinfo);
          syncPrjRelPub = true;
        }
        pubFundinfoDao.save(pubFundinfo);
      }
      try {
        if (syncPrjRelPub) {
          // 项目相关成果刷新同步消息
          // FIXME 2015-10-29 取消MQ -done
          prjRelatedPubRefreshMessageProducer.refreshPrjRelatedPub(null, pubId, psnId,
              PrjRelatedPubContant.REFRESH_SOURCE_PUB, 0);
        }
      } catch (Exception e) {
        logger.error("项目相关成果刷新同步消息出错pubId: " + pubId + ",psnId: " + psnId, e);
      }
    } catch (Exception e) {
      logger.error(
          "同步成果基金标注出错pubId: " + pubId + ",psnId: " + psnId + ",fundinfo: " + (fundinfo != null ? fundinfo : ""), e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Long> getPrjRelatedPubIds(Long psnId, String fundinfo) throws ServiceException {
    if (StringUtils.isBlank(fundinfo)) {
      return null;
    }
    try {
      return this.pubFundinfoDao.getPrjRelatedPubIds(psnId, fundinfo);
    } catch (DaoException e) {
      logger.error("getPrjRelatedPubIds根据人员id与基金标注匹配查找项目相关成果id出错psnId: " + psnId + ",fundinfo: " + fundinfo, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public PubFundinfo getPubFundinfo(Long pubId) throws ServiceException {
    try {
      return this.pubFundinfoDao.get(pubId);
    } catch (Exception e) {
      logger.error("获取成果基金标注出错pubId: " + pubId, e);
      throw new ServiceException(e);
    }
  }
}
