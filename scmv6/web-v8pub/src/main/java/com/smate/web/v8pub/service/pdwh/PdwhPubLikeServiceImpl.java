package com.smate.web.v8pub.service.pdwh;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.pub.enums.PubPdwhStatusEnum;
import com.smate.core.base.pub.po.dao.PubPdwhSnsRelationDAO;
import com.smate.web.v8pub.dao.pdwh.PdwhPubLikeDAO;
import com.smate.web.v8pub.dao.pdwh.PubPdwhDAO;
import com.smate.web.v8pub.enums.PubLikeStatusEnum;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.pdwh.PdwhPubLikePO;
import com.smate.web.v8pub.po.pdwh.PubPdwhPO;
import com.smate.web.v8pub.service.sns.PubLikeService;
import com.smate.web.v8pub.vo.pdwh.PdwhPubOperateVO;
import com.smate.web.v8pub.vo.sns.PubOperateVO;

/**
 * 基准库成果赞服务实现类
 * 
 * @author aijiangbin
 * @date 2018年6月1日
 */
@Service("pdwhPubLikeService")
@Transactional(rollbackFor = Exception.class)
public class PdwhPubLikeServiceImpl implements PdwhPubLikeService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PdwhPubStatisticsService newPdwhPubStatisticsService;
  @Autowired
  private PdwhPubLikeDAO pdwhPubLikeDAO;
  @Autowired
  private PubPdwhDAO pubPdwhDAO;
  @Autowired
  private PubPdwhSnsRelationDAO pubPdwhSnsRelationDAO;
  @Autowired
  private PubLikeService pubLikeService;

  @Override
  public void updatePubLike(Long pubId, Long psnId, PubLikeStatusEnum likeStatus) throws ServiceException {
    try {
      PdwhPubLikePO pubLikePO = pdwhPubLikeDAO.findByPubIdAndPsnId(pubId, psnId);
      if (pubLikePO == null) {
        pubLikePO = new PdwhPubLikePO();
        pubLikePO.setPsnId(psnId);
        pubLikePO.setPdwhPubId(pubId);
        pubLikePO.setGmtCreate(new Date());
      }
      pubLikePO.setStatus(likeStatus.getValue());
      pubLikePO.setGmtModified(new Date());
      pdwhPubLikeDAO.save(pubLikePO);
    } catch (Exception e) {
      logger.error("基准库成果赞服务类：更新赞状态异常,pubId=" + pubId, e);
      throw new ServiceException(e);
    }

  }

  /**
   * 基准库 赞/取消赞操作
   */
  @Override
  public void pdwhLikeOpt(PdwhPubOperateVO pdwhPubOperateVO) throws ServiceException {
    try {
      int result = pdwhLike(pdwhPubOperateVO);// 基准库操作自己的数据
      if (result == 0) {
        return;
      }
      // 个人库数据同步
      sysToSnsLike(pdwhPubOperateVO);
    } catch (Exception e) {
      logger.error("成果赞/取消赞异常,pdwhPubId=" + pdwhPubOperateVO.getPdwhPubId(), e);
      throw new ServiceException(e);
    }
  }

  @Override
  public int pdwhLike(PdwhPubOperateVO pdwhPubOperateVO) throws ServiceException {
    // 校验
    if (!checkParams(pdwhPubOperateVO)) {
      return 0;
    }
    Long pdwhPubId = pdwhPubOperateVO.getPdwhPubId();
    PubLikeStatusEnum status = PubLikeStatusEnum.valueOf(pdwhPubOperateVO.getOperate());
    if (status == PubLikeStatusEnum.PUB_LIKE) {// 点赞
      // 更新赞记录表
      updatePubLike(pdwhPubId, pdwhPubOperateVO.getPsnId(), PubLikeStatusEnum.PUB_LIKE);
    } else {// 取消赞
      PdwhPubLikePO pubLikePO = pdwhPubLikeDAO.findByPubIdAndPsnId(pdwhPubId, pdwhPubOperateVO.getPsnId());
      if (pubLikePO == null) {
        logger.error("取消赞时未查找到赞记录psnId=" + pdwhPubOperateVO.getPsnId() + " ,pdwhPubId= " + pdwhPubId);
        return 0;
      }
      pubLikePO.setStatus(status.getValue());
      pubLikePO.setGmtModified(new Date());
      pdwhPubLikeDAO.save(pubLikePO);
    }
    // 更新统计表赞统计数
    newPdwhPubStatisticsService.updateLikeStatistics(pdwhPubId, status);
    return 1;
  }

  public void sysToSnsLike(PdwhPubOperateVO pdwhPubOperateVO) throws Exception {
    List<Long> snsPubIds = pubPdwhSnsRelationDAO.getSnsOpenPubIds(pdwhPubOperateVO.getPdwhPubId(), 0L);
    PubOperateVO pubOperateVO = new PubOperateVO();
    pubOperateVO.setOperate(pdwhPubOperateVO.getOperate());
    if (CollectionUtils.isNotEmpty(snsPubIds)) {
      for (Long snsPubId : snsPubIds) {
        pubLikeService.updateSnsLikeStatistics(pubOperateVO, snsPubId, pdwhPubOperateVO.getPsnId());// 个人库数据同步
      }
    }
  }


  public boolean checkParams(PdwhPubOperateVO pdwhPubOperateVO) {
    int count = pdwhPubLikeDAO.isPdwhLike(pdwhPubOperateVO);
    long pdwhPubId = pdwhPubOperateVO.getPdwhPubId();
    if (count > 0) {
      logger.info("你已赞/取消赞过该条成果,psnId= " + pdwhPubOperateVO.getPsnId() + " ,pdwhPubId= " + pdwhPubId);
      return false;
    }
    PubPdwhPO pubPdwh = pubPdwhDAO.get(pdwhPubId);
    if (pubPdwh == null || pubPdwh.getStatus().equals(PubPdwhStatusEnum.DELETED)) {
      logger.error("成果不存在,PdwhPubId=" + pdwhPubId);
      return false;
    }
    return true;
  }

  @Override
  public boolean checkHasAwardPdwhPub(PdwhPubOperateVO pdwhPubOperateVO) throws ServiceException {
    boolean hasAward = false;
    long awardCount = pdwhPubLikeDAO.getLikeRecord(pdwhPubOperateVO.getPdwhPubId(), pdwhPubOperateVO.getPsnId());
    if (awardCount > 0) {
      hasAward = true;
    }
    return hasAward;
  }

}
