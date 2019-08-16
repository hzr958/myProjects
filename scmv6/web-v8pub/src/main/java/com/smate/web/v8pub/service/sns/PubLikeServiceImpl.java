package com.smate.web.v8pub.service.sns;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.email.service.EmailSendService;
import com.smate.core.base.psn.dao.PsnStatisticsDao;
import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.core.base.pub.po.dao.PubPdwhSnsRelationDAO;
import com.smate.core.base.utils.exception.DAOException;
import com.smate.web.v8pub.dao.sns.PubLikeDAO;
import com.smate.web.v8pub.dao.sns.PubSnsDAO;
import com.smate.web.v8pub.enums.PubLikeStatusEnum;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.sns.PubLikePO;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.service.pdwh.PdwhPubLikeService;
import com.smate.web.v8pub.vo.pdwh.PdwhPubOperateVO;
import com.smate.web.v8pub.vo.sns.PubOperateVO;

/**
 * 成果赞实现类
 * 
 * @author aijiangbin
 * @date 2018年6月1日
 */

@Service("pubLikeService")
@Transactional(rollbackFor = Exception.class)
public class PubLikeServiceImpl implements PubLikeService {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubStatisticsService newPubStatisticsService;
  @Autowired
  private PubLikeDAO pubLikeDAO;
  @Autowired
  private PubSnsService pubSnsService;
  @Value("${domainscm}")
  private String domainscm;
  @Autowired
  private PubSnsDAO pubSnsDAO;
  @Autowired
  private EmailSendService likedYourPubEmailService;
  @Autowired
  private PsnStatisticsDao psnStatisticsDao;
  @Autowired
  private PubPdwhSnsRelationDAO pubPdwhSnsRelationDAO;
  @Autowired
  private PdwhPubLikeService pdwhPubLikeService;

  @Override
  public void updatePubLike(Long pubId, Long psnId, PubLikeStatusEnum likeStauts) throws ServiceException {
    try {
      PubLikePO pubLikePO = pubLikeDAO.findByPubIdAndPsnId(pubId, psnId);
      if (pubLikePO == null) {
        pubLikePO = new PubLikePO();
        pubLikePO.setPsnId(psnId);
        pubLikePO.setPubId(pubId);
        pubLikePO.setGmtCreate(new Date());
      }
      pubLikePO.setStatus(likeStauts.getValue());
      pubLikePO.setGmtModified(new Date());
      pubLikeDAO.save(pubLikePO);
    } catch (Exception e) {
      logger.error("成果赞服务类：更新赞状态异常,pubId=" + pubId, e);
      throw new ServiceException(e);
    }

  }

  /**
   * 个人库 赞/取消赞操作
   */
  @Override
  public void likeOpt(PubOperateVO pubOperateVO) throws ServiceException {
    Long pubId = pubOperateVO.getPubId();
    Long psnId = pubOperateVO.getPsnId();
    try {
      Long ownerPsnId = null;
      PubSnsPO pubSns = pubSnsService.get(pubId);
      if (pubSns != null) {
        ownerPsnId = pubSns.getCreatePsnId();
      }
      // SCM-23563 个人库成果先直接操作，如果是关联成果，再来同步数据
      int result = snsLikeOpt(pubOperateVO, pubId, ownerPsnId);
      if (result == 0) {
        return;
      }
      // SCM-23420 数据来源调整 跟基准库关联的成果，操作要通知所有关联成果的所属用户（注意一个用户 多条成果关联同一个基准库 成果的情况）
      Long pdwhPubId = pubPdwhSnsRelationDAO.getPdwhPubIdBySnsPubId(pubId);
      if (pdwhPubId != null && pdwhPubId > 0L) {
        pdwhLikeOpt(pubOperateVO, pdwhPubId);// 基准库数据备份
        List<Long> snsPubIds = pubPdwhSnsRelationDAO.getSnsOpenPubIds(pdwhPubId, 0L);
        if (CollectionUtils.isNotEmpty(snsPubIds)) {
          for (Long snsPubId : snsPubIds) {
            if (!snsPubId.equals(pubId)) {
              updateSnsLikeStatistics(pubOperateVO, snsPubId, psnId);// 个人库关联成果同步数据
            }
          }
          if (!psnId.equals(ownerPsnId)) {
            buildEmail(pubOperateVO, pubId, ownerPsnId, snsPubIds);
          }
        }
      }
    } catch (DAOException e) {
      logger.error("成果赞服务类：更新赞相关表异常,pubId=" + pubId, e);
      throw new ServiceException(e);
    }

  }

  public void buildEmail(PubOperateVO pubOperateVO, Long pubId, Long ownerPsnId, List<Long> snsPubIds) {
    PubLikeStatusEnum status = PubLikeStatusEnum.valueOf(pubOperateVO.getOperate());
    if (status == PubLikeStatusEnum.PUB_LIKE) {// 点赞
      Long psnId = pubOperateVO.getPsnId();
      Long count = pubLikeDAO.getLikeCount(pubId, psnId);
      if (count < 1 && !psnId.equals(ownerPsnId)) {
        List<Long> pubOwnerList = pubSnsDAO.getSnsPubList(snsPubIds);
        List<Long> newPubOwnerList = pubOwnerList.stream().distinct().collect(Collectors.toList());
        List<Map<Long, Long>> pubPsbList = pubSnsDAO.getSnsPub(snsPubIds);
        if (CollectionUtils.isNotEmpty(newPubOwnerList)) {
          for (Long ownerId : newPubOwnerList) {// 操作要通知所有关联成果的所属用户
            if (pubPsbList != null && pubPsbList.size() > 0) {
              Long newPubId = null;
              for (Map<Long, Long> map : pubPsbList) {
                if (ownerId.equals((Long) map.get("createPsnId"))) {
                  newPubId = map.get("pubId");
                }
              }
              if (!ownerPsnId.equals(ownerId)) {// 防止重复发送
                noticeAwardEmail(newPubId, psnId, ownerId);// 发送邮件通知用户
              }
            }
          }
        }
      }
    }
  }

  @Override
  public void updateSnsLikeStatistics(PubOperateVO pubOperateVO, Long pubId, Long psnId) {
    Long ownerPsnId = null;
    PubSnsPO pubSns = pubSnsService.get(pubId);
    if (pubSns != null) {
      ownerPsnId = pubSns.getCreatePsnId();
    }
    PubLikeStatusEnum status = PubLikeStatusEnum.valueOf(pubOperateVO.getOperate());
    if (status == PubLikeStatusEnum.PUB_LIKE) {// 点赞
      // 更新赞记录表
      updatePubLike(pubId, psnId, PubLikeStatusEnum.PUB_LIKE);
    } else {// 取消赞
      PubLikePO pubLikePO = pubLikeDAO.findByPubIdAndPsnId(pubId, psnId);
      if (pubLikePO != null) {
        pubLikePO.setStatus(status.getValue());
        pubLikePO.setGmtModified(new Date());
        pubLikeDAO.save(pubLikePO);
      }
    }
    // 更新统计表赞统计数
    newPubStatisticsService.updateLikeStatistics(pubId, status);
    // 更新人员统计表
    if (ownerPsnId != null) {
      psnStatisticSyncToSns(ownerPsnId, status);
    }
  }

  public void pdwhLikeOpt(PubOperateVO pubOperateVO, Long pdwhPubId) {
    PdwhPubOperateVO pdwhPubOperateVO = new PdwhPubOperateVO();
    pdwhPubOperateVO.setPdwhPubId(pdwhPubId);
    pdwhPubOperateVO.setPsnId(pubOperateVO.getPsnId());
    pdwhPubOperateVO.setOperate(pubOperateVO.getOperate());
    pdwhPubLikeService.pdwhLike(pdwhPubOperateVO);
  }

  public int snsLikeOpt(PubOperateVO pubOperateVO, Long pubId, Long ownerPsnId) {
    // 校验
    if (!checkParams(pubOperateVO)) {
      return 0;
    }
    Long psnId = pubOperateVO.getPsnId();
    PubLikeStatusEnum status = PubLikeStatusEnum.valueOf(pubOperateVO.getOperate());
    if (status == PubLikeStatusEnum.PUB_LIKE) {// 点赞
      snsLike(pubId, psnId, ownerPsnId);
    } else {// 取消赞
      PubLikePO pubLikePO = pubLikeDAO.findByPubIdAndPsnId(pubId, psnId);
      if (pubLikePO == null) {
        logger.error("取消赞时未查找到赞记录psnId=" + psnId + " ,pubId= " + pubId);
        return 0;
      }
      pubLikePO.setStatus(status.getValue());
      pubLikePO.setGmtModified(new Date());
      pubLikeDAO.save(pubLikePO);
    }
    // 更新统计表赞统计数
    newPubStatisticsService.updateLikeStatistics(pubId, status);
    // 更新人员统计表
    psnStatisticSyncToSns(ownerPsnId, status);
    return 1;
  }

  public void snsLike(Long pubId, Long psnId, Long ownerPsnId) {
    // 更新赞记录表
    updatePubLike(pubId, psnId, PubLikeStatusEnum.PUB_LIKE);
    Long count = pubLikeDAO.getLikeCount(pubId, psnId);
    // 发送点赞邮件
    if (count < 1) {
      noticeAwardEmail(pubId, psnId, ownerPsnId);
    }
  }

  public void noticeAwardEmail(Long pubId, Long psnId, Long ownerPsnId) {
    // 发送赞邮件 每人每天对同一个成果只能发一次邮件
    if (!psnId.equals(ownerPsnId)) {
      sendAwardEmail(psnId, ownerPsnId, pubId);
    }
  }

  private void psnStatisticSyncToSns(Long psnId, PubLikeStatusEnum status) {
    PsnStatistics psnStatistics = psnStatisticsDao.get(psnId);
    if (psnStatistics == null) {
      psnStatistics = new PsnStatistics();
      psnStatistics.setPsnId(psnId);
      psnStatistics.setPubAwardSum(0);
    }
    if (status == PubLikeStatusEnum.PUB_LIKE) {
      psnStatistics.setPubAwardSum(psnStatistics.getPubAwardSum() == null ? 1 : (psnStatistics.getPubAwardSum() + 1));
    } else {
      psnStatistics.setPubAwardSum((psnStatistics.getPubAwardSum() == null || psnStatistics.getPubAwardSum() <= 1) ? 0
          : (psnStatistics.getPubAwardSum() - 1));
    }
    psnStatisticsDao.save(psnStatistics);
  }

  private void sendAwardEmail(Long awarderPsnId, Long ownerPsnId, Long resId) {
    try {
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("psnId", ownerPsnId);
      map.put("likedPsnId", awarderPsnId);
      map.put("pubId", resId);
      likedYourPubEmailService.syncEmailInfo(map);
    } catch (Exception e) {
      logger.error("发送赞邮件错误：resId:" + resId + ",awarderPsnId:" + awarderPsnId, e);
    }
  }

  public boolean checkParams(PubOperateVO pubOperateVO) {
    int count = pubLikeDAO.isLike(pubOperateVO);
    long pubId = pubOperateVO.getPubId();
    if (count > 0) {
      logger.info("你已赞/取消赞过该条成果,psnId= " + pubOperateVO.getPsnId() + " ,pubId= " + pubId);
      return false;
    }
    return true;
  }

  @Override
  public boolean checkHasAwardPub(Long psnId, Long pubId) throws ServiceException {
    return pubLikeDAO.getLikeRecord(pubId, psnId) > 0;
  }

  @Override
  public PubLikePO findByPubIdAndPsnId(Long pubId, Long psnId) throws ServiceException {
    try {
      return pubLikeDAO.findByPubIdAndPsnId(pubId, psnId);
    } catch (Exception e) {
      logger.error("根据pubId和psnId获取成果赞记录出错！pubId={},psnId={}", pubId, psnId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public PubLikePO get(Long id) throws ServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void save(PubLikePO pubLikePO) throws ServiceException {
    pubLikeDAO.save(pubLikePO);
  }

  @Override
  public void update(PubLikePO entity) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public void saveOrUpdate(PubLikePO pubLikePO) throws ServiceException {
    pubLikeDAO.saveOrUpdate(pubLikePO);
  }

  @Override
  public void deleteById(Long id) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public void delete(PubLikePO entity) throws ServiceException {
    // TODO Auto-generated method stub

  }

}
