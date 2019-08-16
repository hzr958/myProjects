package com.smate.center.batch.service.pub.pubgrouping;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.rol.pub.PublicationRolPdwhDao;
import com.smate.center.batch.dao.sns.pub.PubGroupingDao;
import com.smate.center.batch.dao.sns.pub.PublicationDao;
import com.smate.center.batch.dao.sns.pub.PublicationPdwhDao;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.PublicationRolPdwh;
import com.smate.center.batch.model.sns.pub.PubFulltext;
import com.smate.center.batch.model.sns.pub.PubGrouping;
import com.smate.center.batch.model.sns.pub.PublicationPdwh;
import com.smate.center.batch.service.pubfulltexttoimage.PubFullTextService;


/**
 * 成果分组服务实现类 分组就是为了区分 相关成果
 * 
 * @author tsz
 * 
 */
@Service("pubGroupingService")
@Transactional(rollbackFor = Exception.class)
public class PubGroupingServiceImpl implements PubGroupingService {
  private static Logger logger = LoggerFactory.getLogger(PubGroupingServiceImpl.class);
  @Autowired
  private PubGroupingDao pubGroupingDao;
  @Autowired
  private PubFullTextService pubFulltextService;
  @Autowired
  private PublicationPdwhDao publicationPdwhDao;
  @Autowired
  private PublicationRolPdwhDao publicationRolPdwhDao;
  @Autowired
  private PublicationDao publicationDao;

  @Override
  public PubGrouping findPubGroup(Long pubId) throws ServiceException {
    return pubGroupingDao.findById(pubId);
  }

  @Override
  public Long findPubGroupId(Long pubId) throws ServiceException {
    return pubGroupingDao.findGroupIdById(pubId);
  }

  @Override
  public PubGrouping addPubPdwhGroup(PubGrouping pubGroup) throws ServiceException {
    pubGroupingDao.save(pubGroup);
    return pubGroup;
  }

  @Override
  public List<Long> findSnsPubIdsByGroupId(Long groupId) throws ServiceException {

    return pubGroupingDao.getSnsPubIdsByGroupId(groupId);
  }

  @Override
  public void pubGroupingFor(List<PublicationPdwh> list) {
    if (list != null && list.size() > 0) {
      for (PublicationPdwh p : list) {
        try {
          pubGroupingBuilder(p);

        } catch (Exception e) {
          e.printStackTrace();
          logger.error("pubId=" + p.getPubId() + "成果分组出错", e);
        }
      }
      // 记录开始
      setStartFlag(list.size());
    }
  }

  @Override
  public void pubGroupingBuilder(PublicationPdwh pubPdwh) throws Exception {
    try {
      Long pubId = pubPdwh.getPubId();
      // 先判断是否存在分组 如果有 直接退出；
      PubGrouping pubGrouping = pubGroupingDao.findById(pubId);
      if (pubGrouping != null && pubGrouping.getGroupId() > 0) {
        return;
      }
      // 获取成果所有人 当前的登录人 是成果所有者吗？
      // Long psnId = SecurityUtils.getCurrentUserId();
      Long psnId = publicationDao.getPubOwner(pubId);
      PubGrouping pubGroup = new PubGrouping();
      pubGroup.setPsnId(psnId);
      pubGroup.setPubId(pubId);
      // 获取相关成果ids
      List<Long> listPubIds = getIds(pubPdwh);
      // 获取相关成果分组信息
      Long groupId = null;
      if (CollectionUtils.isNotEmpty(listPubIds)) {
        List<Long> groupIds = pubGroupingDao.getGroupIdByPubIds(listPubIds);
        if (CollectionUtils.isNotEmpty(groupIds)) {
          // 有分组信息
          groupId = groupIds.get(0);

        } else { // 相关成果没有分组 自成一组
          groupId = pubGroupingDao.getNewGroupingId();
        }
      } else {// 没有相关成果 自成一组
        groupId = pubGroupingDao.getNewGroupingId();
      }
      pubGroup.setGroupId(groupId);
      // 保存分组
      pubGroupingDao.save(pubGroup);

      // 成果全文附件同步分组
      PubFulltext pubFulltext = pubFulltextService.getPubFulltextByPubId(pubId);
      if (pubFulltext != null) {
        pubFulltext.setGroupId(groupId);
        // 更新分组信息
        pubFulltextService.updatePubFulltext(pubFulltext);
      }

    } catch (Exception e) {
      logger.error("pubId=" + pubPdwh.getPubId() + "成果分组出错", e);
    }
  }

  @Override
  public void pubGroupingBuilder(PublicationRolPdwh pubRolPdwh) {
    try {
      Long pubId = pubRolPdwh.getPubId();
      // 先判断是否存在分组 如果有 直接退出；
      PubGrouping pubGrouping = pubGroupingDao.findById(pubId);
      if (pubGrouping != null && pubGrouping.getGroupId() > 0) {
        return;
      }
      // 获取成果所有人 当前的登录人 是成果所有者吗？
      // Long psnId = SecurityUtils.getCurrentUserId();
      Long psnId = publicationDao.getPubOwner(pubId);
      PubGrouping pubGroup = new PubGrouping();
      pubGroup.setPsnId(psnId);
      pubGroup.setPubId(pubId);
      // 获取相关成果ids
      List<Long> listPubIds = getpubRolPdwhIds(pubRolPdwh);
      // 获取相关成果分组信息
      Long groupId = null;
      if (CollectionUtils.isNotEmpty(listPubIds)) {
        List<Long> groupIds = pubGroupingDao.getGroupIdByPubIds(listPubIds);
        if (CollectionUtils.isNotEmpty(groupIds)) {
          // 有分组信息
          groupId = groupIds.get(0);

        } else { // 相关成果没有分组 自成一组
          groupId = pubGroupingDao.getNewGroupingId();
        }
      } else {// 没有相关成果 自成一组
        groupId = pubGroupingDao.getNewGroupingId();
      }
      pubGroup.setGroupId(groupId);
      // 保存分组
      pubGroupingDao.save(pubGroup);

      // 成果全文附件同步分组
      PubFulltext pubFulltext = pubFulltextService.getPubFulltextByPubId(pubId);
      if (pubFulltext != null) {
        pubFulltext.setGroupId(groupId);
        // 更新分组信息
        pubFulltextService.updatePubFulltext(pubFulltext);
      }

    } catch (Exception e) {
      logger.error("pubId=" + pubRolPdwh.getPubId() + "成果分组出错", e);
    }
  }

  /**
   * 得到所有基准库成果id
   * 
   * @param pubRolPdwh ConstPdwhPubRefDb
   * @return
   * @throws DaoException
   */
  private List<Long> getpubRolPdwhIds(PublicationRolPdwh pubRolPdwh) throws DaoException {
    List<Long> list = new ArrayList<Long>();
    if (pubRolPdwh.getIsiId() != null && pubRolPdwh.getIsiId().longValue() > 0) {
      list.addAll(publicationRolPdwhDao.findRolPubIds(pubRolPdwh.getIsiId(), "isiId"));
    }
    if (pubRolPdwh.getSpsId() != null && pubRolPdwh.getSpsId().longValue() > 0) {
      list.addAll(publicationRolPdwhDao.findRolPubIds(pubRolPdwh.getSpsId(), "spsId"));
    }
    if (pubRolPdwh.getEiId() != null && pubRolPdwh.getEiId().longValue() > 0) {
      list.addAll(publicationRolPdwhDao.findRolPubIds(pubRolPdwh.getEiId(), "eiId"));
    }
    if (pubRolPdwh.getCnkiId() != null && pubRolPdwh.getCnkiId().longValue() > 0) {
      list.addAll(publicationRolPdwhDao.findRolPubIds(pubRolPdwh.getCnkiId(), "cnkiId"));
    }
    if (pubRolPdwh.getCniprId() != null && pubRolPdwh.getCniprId().longValue() > 0) {
      list.addAll(publicationRolPdwhDao.findRolPubIds(pubRolPdwh.getCniprId(), "cniprId"));
    }
    if (pubRolPdwh.getWfId() != null && pubRolPdwh.getWfId().longValue() > 0) {
      list.addAll(publicationRolPdwhDao.findRolPubIds(pubRolPdwh.getWfId(), "wfId"));
    }
    if (pubRolPdwh.getPubmedId() != null && pubRolPdwh.getPubmedId().longValue() > 0) {
      list.addAll(publicationRolPdwhDao.findRolPubIds(pubRolPdwh.getPubmedId(), "pubmedId"));
    }
    if (pubRolPdwh.getIeeeXpId() != null && pubRolPdwh.getIeeeXpId().longValue() > 0) {
      list.addAll(publicationRolPdwhDao.findRolPubIds(pubRolPdwh.getIeeeXpId(), "ieeeXpId"));
    }
    if (pubRolPdwh.getScdId() != null && pubRolPdwh.getScdId().longValue() > 0) {
      list.addAll(publicationRolPdwhDao.findRolPubIds(pubRolPdwh.getScdId(), "scdId"));
    }
    if (pubRolPdwh.getBaiduId() != null && pubRolPdwh.getBaiduId().longValue() > 0) {
      list.addAll(publicationRolPdwhDao.findRolPubIds(pubRolPdwh.getBaiduId(), "baiduId"));
    }
    if (pubRolPdwh.getCnkiPatId() != null && pubRolPdwh.getCnkiPatId().longValue() > 0) {
      list.addAll(publicationRolPdwhDao.findRolPubIds(pubRolPdwh.getCnkiPatId(), "cnkiPatId"));
    }
    return list;
  }

  /**
   * 得到所有基准库成果id
   * 
   * @param pubPdwh ConstPdwhPubRefDb
   * @return
   * @throws DaoException
   */
  private List<Long> getIds(PublicationPdwh pubPdwh) throws DaoException {
    List<Long> list = new ArrayList<Long>();
    if (pubPdwh.getIsiId() != null && pubPdwh.getIsiId().longValue() > 0) {
      list.addAll(publicationPdwhDao.findSnsPubIds(pubPdwh.getIsiId(), "isiId"));
    }
    if (pubPdwh.getSpsId() != null && pubPdwh.getSpsId().longValue() > 0) {
      list.addAll(publicationPdwhDao.findSnsPubIds(pubPdwh.getSpsId(), "spsId"));
    }
    if (pubPdwh.getEiId() != null && pubPdwh.getEiId().longValue() > 0) {
      list.addAll(publicationPdwhDao.findSnsPubIds(pubPdwh.getEiId(), "eiId"));
    }
    if (pubPdwh.getCnkiId() != null && pubPdwh.getCnkiId().longValue() > 0) {
      list.addAll(publicationPdwhDao.findSnsPubIds(pubPdwh.getCnkiId(), "cnkiId"));
    }
    if (pubPdwh.getCniprId() != null && pubPdwh.getCniprId().longValue() > 0) {
      list.addAll(publicationPdwhDao.findSnsPubIds(pubPdwh.getCniprId(), "cniprId"));
    }
    if (pubPdwh.getWfId() != null && pubPdwh.getWfId().longValue() > 0) {
      list.addAll(publicationPdwhDao.findSnsPubIds(pubPdwh.getWfId(), "wfId"));
    }
    if (pubPdwh.getPubmedId() != null && pubPdwh.getPubmedId().longValue() > 0) {
      list.addAll(publicationPdwhDao.findSnsPubIds(pubPdwh.getPubmedId(), "pubmedId"));
    }
    if (pubPdwh.getIeeeXpId() != null && pubPdwh.getIeeeXpId().longValue() > 0) {
      list.addAll(publicationPdwhDao.findSnsPubIds(pubPdwh.getIeeeXpId(), "ieeeXpId"));
    }
    if (pubPdwh.getScdId() != null && pubPdwh.getScdId().longValue() > 0) {
      list.addAll(publicationPdwhDao.findSnsPubIds(pubPdwh.getScdId(), "scdId"));
    }
    if (pubPdwh.getBaiduId() != null && pubPdwh.getBaiduId().longValue() > 0) {
      list.addAll(publicationPdwhDao.findSnsPubIds(pubPdwh.getBaiduId(), "baiduId"));
    }
    if (pubPdwh.getCnkiPatId() != null && pubPdwh.getCnkiPatId().longValue() > 0) {
      list.addAll(publicationPdwhDao.findSnsPubIds(pubPdwh.getCnkiPatId(), "cnkiPatId"));
    }
    return list;
  }

  /**
   * 取出重复数据
   * 
   * @param list
   * @return
   */
  private List<Long> buildList(List<Long> list) {
    if (list == null || list.size() < 1) {
      return null;
    }
    for (int i = 0; i < list.size(); i++) { // 外循环是循环的次数
      for (int j = list.size() - 1; j > i; j--) { // 内循环是 外循环一次比较的次数
        if (list.get(i) == list.get(j)) {
          list.remove(j);
        }

      }
    }
    return list;
  }

  /**
   * 获取初始化刷新数据
   */
  @Override
  public List<PublicationPdwh> getInitList(int maxsize) throws ServiceException {
    int start = 0;
    PubGrouping pubg = pubGroupingDao.findById(0L);
    if (pubg == null) { // 做特殊标记 标示 任务重哪里开始 tsz
      pubg = new PubGrouping();
      pubg.setPsnId(0L);
      pubg.setGroupId(0L);
      pubg.setPubId(0L);
      pubGroupingDao.save(pubg);
    } else {
      start = Integer.valueOf((pubg.getPsnId().toString()));
    }
    return pubGroupingDao.getInitGroupingList(start, maxsize);
  }

  public void setStartFlag(int size) {
    // 一个标记 不是真的psnId
    PubGrouping pubg = pubGroupingDao.findById(0L);
    pubg.setPsnId((Long.valueOf(pubg.getPsnId() + size)));
    pubGroupingDao.save(pubg);
  }

  @Override
  public boolean checkIsHasSnsOtherRelPub(Long pubId, Long psnId) throws ServiceException {
    boolean isHasOtherRelPub = false;
    try {
      Long groupId = pubGroupingDao.findGroupIdById(pubId);// 查找成果所在分组id
      if (groupId != null && groupId.longValue() > 0) {
        // 获取其他版本相关成果数
        Long count = pubGroupingDao.countSnsOtherRelPub(groupId, pubId, psnId);
        if (count != null && count.longValue() > 0) {
          isHasOtherRelPub = true;
        }
      }
    } catch (Exception e) {
      logger.error("判断是否有其他版本相关成果出错pubId=" + pubId + ",psnId=" + psnId, e);
    }
    return isHasOtherRelPub;
  }

  /**
   * 删除分组记录
   */
  @Override
  public void delGroupingByPubId(Long pubId) throws Exception {
    pubGroupingDao.delGroupindByPubId(pubId);
  }



}
