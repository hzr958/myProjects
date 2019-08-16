package com.smate.web.v8pub.service.sns;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.pub.enums.PubDbEnum;
import com.smate.web.v8pub.consts.V8pubConst;
import com.smate.web.v8pub.dao.pdwh.PdwhPubStatisticsDAO;
import com.smate.web.v8pub.dao.pdwh.PubPdwhDAO;
import com.smate.web.v8pub.dao.sns.CollectedPubDao;
import com.smate.web.v8pub.dao.sns.PubSnsDAO;
import com.smate.web.v8pub.dao.sns.PubStatisticsDAO;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.pdwh.PubPdwhPO;
import com.smate.web.v8pub.po.sns.CollectedPub;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.service.query.PubQueryhandlerService;
import com.smate.web.v8pub.vo.PubListResult;
import com.smate.web.v8pub.vo.PubListVO;
import com.smate.web.v8pub.vo.sns.PubOperateVO;

@Service("collectedPubService")
@Transactional(rollbackFor = Exception.class)
public class CollectedPubServiceImpl implements CollectedPubService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Value("${domainscm}")
  private String domainscm;
  @Autowired
  private PubQueryhandlerService pubQueryhandlerService;
  @Autowired
  private CollectedPubDao collectedPubDao;
  @Autowired
  private PubSnsDAO pubSnsDAO;
  @Autowired
  private PubPdwhDAO pubPdwhDAO;
  @Autowired
  private PdwhPubStatisticsDAO pdwhPubStatisticsDAO;
  @Autowired
  private PubStatisticsDAO pubStatisticsDAO;

  @Override
  public boolean isDelPub(CollectedPub pub) throws ServiceException {
    boolean isDel = false;
    if (PubDbEnum.SNS.equals(pub.getPubDb())) {
      PubSnsPO pubSns = pubSnsDAO.get(pub.getPubId());
      if (pubSns == null || (pubSns != null && "DELETED".equals(pubSns.getStatus().toString()))) {
        isDel = true;
      }
    } else {
      PubPdwhPO pdwh = pubPdwhDAO.get(pub.getPubId());
      if (pdwh == null) {
        isDel = true;
      }
    }
    return isDel;
  }

  @Override
  public String savePub(CollectedPub pub) {
    // 查重
    try {
      if (checkParams(pub)) {
        collectedPubDao.save(pub);
        return "success";
      } else {
        return "exist";
      }
    } catch (Exception e) {
      logger.error("收藏论文信息出错", e);
      return "fail";
    }
  }

  /**
   * 校验
   * 
   * @param pub
   * @return true - 校验通过
   */
  private boolean checkParams(CollectedPub pub) {
    List<CollectedPub> pubs = collectedPubDao.getCollectedPubs(pub.getPsnId());
    // 查重
    if (CollectionUtils.isNotEmpty(pubs)) {
      for (CollectedPub p : pubs) {
        if (p.equals(pub)) {
          logger.error("论文已被收藏");
          return false;
        }
      }
    }
    return true;
  }

  @Override
  public boolean delPub(CollectedPub pub) {
    try {
      collectedPubDao.delCollectedPub(pub.getPsnId(), pub.getPubId(), pub.getPubDb());
      return true;
    } catch (Exception e) {
      logger.error(e.getMessage());
      return false;
    }

  }

  @Override
  public Map<String, String> dealCollectedPub(PubOperateVO pubOperateVO) throws ServiceException {
    Map<String, String> map = new HashMap<>();
    Long pubId = pubOperateVO.getPubId();
    CollectedPub pub = new CollectedPub();
    pub.setPsnId(pubOperateVO.getPsnId());
    pub.setPubId(pubId);
    pub.setPubDb(PubDbEnum.valueOf(pubOperateVO.getPubDb().replace("\"", "")));
    if (pubOperateVO.getCollectOperate() == 0) {// 收藏
      // 成果是否已删除
      boolean isDel = isDelPub(pub);
      if (isDel) {
        map.put("result", "isDel");
      } else {
        Date createTime = new Date();
        pub.setCreateDate(createTime);
        String message = savePub(pub);// 收藏到数据库
        map.put("result", message);
      }
    } else if (pubOperateVO.getCollectOperate() == 1) {// 取消收藏
      boolean delStatus = false;
      delStatus = delPub(pub);// 删除数据库记录
      map.put("result", delStatus ? "success" : "fails");
    }
    return map;
  }

  @Override
  public void getShowPubList(PubListVO pubListVO) {
    try {
      PubListResult result = pubQueryhandlerService.queryPub(pubListVO.getPubQueryDTO());
      if (result.status.equals(V8pubConst.SUCCESS)) {
        pubListVO.setResultList(result.resultList);
        pubListVO.setTotalCount(result.totalCount);
      }
      if (result.getResultList() != null && result.getResultList().size() > 0) {

      }
      logger.debug("success");
    } catch (Exception e) {
      logger.error("查询论文收藏列表 异常", e);
    }
  }

  @Override
  public boolean hasCollectedPub(Long psnId, Long pubId, PubDbEnum pubDB) throws ServiceException {
    return collectedPubDao.isCollectedPub(psnId, pubId, pubDB);
  }

}
