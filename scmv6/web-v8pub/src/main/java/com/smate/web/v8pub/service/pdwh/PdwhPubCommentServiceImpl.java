package com.smate.web.v8pub.service.pdwh;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import com.smate.core.base.pub.po.dao.PubPdwhSnsRelationDAO;
import com.smate.web.v8pub.dao.pdwh.PdwhPubCommentDAO;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.pdwh.PdwhPubCommentPO;
import com.smate.web.v8pub.service.sns.PubCommentService;
import com.smate.web.v8pub.vo.pdwh.PdwhPubOperateVO;
import com.smate.web.v8pub.vo.sns.PubOperateVO;

/**
 * 基准库成果评论服务实现类
 * 
 * @author aijiangbin
 * @date 2018年6月1日
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PdwhPubCommentServiceImpl implements PdwhPubCommentService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PdwhPubCommentDAO pdwhPubCommentDAO;
  @Autowired
  private PdwhPubStatisticsService newPdwhPubStatisticsService;
  @Autowired
  private PubPdwhSnsRelationDAO pubPdwhSnsRelationDAO;
  @Autowired
  private PubCommentService pubCommentService;

  @Override
  public List<PdwhPubCommentPO> findByPubId(Long pubId) throws ServiceException {
    try {
      List<PdwhPubCommentPO> list = pdwhPubCommentDAO.findByPubId(pubId);
      return list;
    } catch (Exception e) {
      logger.error("准库成果评论服务：查询评论列表异常，pubId=" + pubId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void saveOrUpdate(PdwhPubCommentPO entity) throws ServiceException {
    try {
      pdwhPubCommentDAO.save(entity);
    } catch (Exception e) {
      logger.error("准库成果评论服务：保存或者更新评论异常，pubId=" + entity.getPdwhPubId(), e);
      throw new ServiceException(e);
    }

  }

  @Override
  public void deleteById(Long id) throws ServiceException {
    try {
      pdwhPubCommentDAO.delete(id);
    } catch (Exception e) {
      logger.error("准库成果评论服务：通过id删除评论异常，id=" + id, e);
      throw new ServiceException(e);
    }

  }

  /**
   * 基准库 成果评论操作
   */
  @Override
  public void pdwhCommentOpt(PdwhPubOperateVO pdwhPubOperateVO) throws ServiceException {
    try {
      pdwhComment(pdwhPubOperateVO);// 基准库操作
      // 个人库数据同步
      sysToSnsComment(pdwhPubOperateVO);
    } catch (Exception e) {
      logger.error("成果评论异常,pdwhPubId=" + pdwhPubOperateVO.getPdwhPubId(), e);
      throw new ServiceException(e);
    }

  }

  @Override
  public void pdwhComment(PdwhPubOperateVO pdwhPubOperateVO) throws ServiceException {
    // 插入成果评论表
    insertPdwhComment(pdwhPubOperateVO);
    // 更新基准库成果统计表评论数
    newPdwhPubStatisticsService.updateCommentStatistics(pdwhPubOperateVO.getPdwhPubId());
  }

  public void sysToSnsComment(PdwhPubOperateVO pdwhPubOperateVO) throws Exception {
    List<Long> snsPubIds = pubPdwhSnsRelationDAO.getSnsOpenPubIds(pdwhPubOperateVO.getPdwhPubId(), 0L);
    PubOperateVO pubOperateVO = new PubOperateVO();
    pubOperateVO.setPsnId(pdwhPubOperateVO.getPsnId());
    pubOperateVO.setContent(pdwhPubOperateVO.getContent());
    if (CollectionUtils.isNotEmpty(snsPubIds)) {
      for (Long snsPubId : snsPubIds) {
        pubCommentService.updateSnsCommentStatistics(pubOperateVO, snsPubId);// 个人库数据同步
      }
    }
  }

  public void insertPdwhComment(PdwhPubOperateVO pdwhPubOperateVO) {
    try {
      PdwhPubCommentPO pdwhPubCommentPO = new PdwhPubCommentPO();
      pdwhPubCommentPO.setContent(HtmlUtils.htmlUnescape(pdwhPubOperateVO.getContent()));
      pdwhPubCommentPO.setPdwhPubId(pdwhPubOperateVO.getPdwhPubId());
      pdwhPubCommentPO.setPsnId(pdwhPubOperateVO.getPsnId());
      pdwhPubCommentPO.setStatus(0);// 状态 0=正常 ; 9=删除
      pdwhPubCommentPO.setGmtCreate(new Date());
      pdwhPubCommentPO.setGmtModified(new Date());
      saveOrUpdate(pdwhPubCommentPO);
    } catch (Exception e) {
      logger.error("成果评论插入异常,pdwhPubId=" + pdwhPubOperateVO.getPdwhPubId(), e);
    }
  }

}
