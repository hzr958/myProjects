package com.smate.web.v8pub.service.sns;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.dao.psncnf.PsnConfigDao;
import com.smate.core.base.psn.dao.psncnf.PsnConfigPubDao;
import com.smate.core.base.pub.enums.PubSnsStatusEnum;
import com.smate.core.base.utils.exception.DAOException;
import com.smate.web.v8pub.dao.sns.PubSnsDAO;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.service.query.PubQueryDTO;

/**
 * @author houchuanjie
 * @date 2018/06/01 17:47
 */
@Service(value = "pubSnsService")
@Transactional(rollbackFor = Exception.class)
public class PubSnsServiceImpl implements PubSnsService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubSnsDAO pubSnsDAO;
  @Autowired
  private PsnConfigPubDao psnConfigPubDao;
  @Autowired
  private PsnConfigDao psnConfigDao;

  @Override
  public PubSnsPO get(Long pubId) throws ServiceException {
    try {
      return pubSnsDAO.get(pubId);
    } catch (DAOException e) {
      logger.error("查询成果出错！pubId={}", pubId);
      throw new ServiceException(e);
    }
  }

  @Override
  public void save(PubSnsPO pubSnsPO) throws ServiceException {
    try {
      pubSnsDAO.save(pubSnsPO);
    } catch (DAOException e) {
      logger.error("保存成果出错！{}", pubSnsPO);
      throw new ServiceException(e);
    }
  }

  @Override
  public void update(PubSnsPO pubSnsPO) throws ServiceException {
    try {
      pubSnsDAO.update(pubSnsPO);
    } catch (DAOException e) {
      logger.error("更新成果出错！{}", pubSnsPO);
      throw new ServiceException(e);
    }
  }

  @Override
  public void saveOrUpdate(PubSnsPO pubSnsPO) throws ServiceException {
    try {
      pubSnsDAO.saveOrUpdate(pubSnsPO);
    } catch (DAOException e) {
      logger.error("保存或更新成果出错！{}", pubSnsPO);
      throw new ServiceException(e);
    }
  }

  @Override
  public void deleteById(Long pubId) throws ServiceException {
    try {
      PubSnsPO pubSnsPO = pubSnsDAO.get(pubId);
      pubSnsPO.setStatus(PubSnsStatusEnum.DELETED);
      update(pubSnsPO);
    } catch (DAOException e) {
      logger.error("删除成果出错！pubId={}", pubId);
      throw new ServiceException(e);
    }
  }

  @Override
  public void delete(PubSnsPO pubSnsPO) throws ServiceException {
    pubSnsPO.setStatus(PubSnsStatusEnum.DELETED);
    update(pubSnsPO);
  }

  /**
   * 查询个人成果
   */
  @Override
  public void queryPubList(PubQueryDTO pubQueryDTO) {
    pubSnsDAO.queryPubList(pubQueryDTO);

  }

  @Override
  public Integer queryPubPermission(Long psnId, Long pubId) throws ServiceException {
    Long confId = psnConfigDao.getPsnConfId(psnId);
    return psnConfigPubDao.getAnyUser(confId, pubId);
  }

  @Override
  public void queryGrpPubList(PubQueryDTO pubQueryDTO) {
    pubSnsDAO.queryGrpPubList(pubQueryDTO);
  }

  @Override
  public void queryDynMyPub(PubQueryDTO pubQueryDTO) {
    pubSnsDAO.queryDynMyPub(pubQueryDTO);

  }

  @Override
  public void queryOpenPubList(PubQueryDTO pubQueryDTO) {

  }

  /**
   * 个人成果统计数
   */
  @Override
  public List<Map<String, Object>> querySnsPubCount(PubQueryDTO pubQueryDTO, int type) {
    List<Map<String, Object>> list = pubSnsDAO.querySnsPubCountList(pubQueryDTO, type);
    return list;
  }

  @Override
  public Long findPubCount(PubQueryDTO pubQueryDTO) {
    pubSnsDAO.queryPubCount(pubQueryDTO);
    return pubQueryDTO.getTotalCount();
  }

  /**
   * 群组成果统计数
   */
  @Override
  public List<Map<String, Object>> queryGrpPubCount(PubQueryDTO pubQueryDTO, int type) {
    List<Map<String, Object>> list = pubSnsDAO.queryGrpPubCountList(pubQueryDTO, type);
    return list;
  }

  /**
   * 是否存在标题相同的重复成果 1=是
   */
  @Override
  public Integer existRepGrpPub(String title, Long grpId) {
    Integer count = pubSnsDAO.existRepGrpPub(title, grpId);
    return count;
  }

  @Override
  public void queryPubListForOpen(PubQueryDTO pubQueryDTO) {
    pubSnsDAO.queryPubListForOpen(pubQueryDTO);
  }

  @Override
  public void queryGrpPubListForOpen(PubQueryDTO pubQueryDTO) {
    pubSnsDAO.queryGrpPubListForOpen(pubQueryDTO);

  }

  @Override
  public void queryAllPubListForOpen(PubQueryDTO pubQueryDTO) {
    pubSnsDAO.queryAllPubListForOpen(pubQueryDTO);

  }

  @Override
  public void queryByPubIds(PubQueryDTO pubQueryDTO) {
    pubSnsDAO.queryByPubIds(pubQueryDTO);
  }

  @Override
  public void queryPsnPublicPubForOpen(PubQueryDTO pubQueryDTO) {
    pubSnsDAO.queryPsnPublicPubForOpen(pubQueryDTO);

  }

  @Override
  public Long getPsnNotExistsResumePubCount(Long psnId) throws com.smate.core.base.exception.ServiceException {
    long count = pubSnsDAO.getPsnNotExistsResumePubCount(psnId);
    return count;
  }

  @Override
  public Long queryPubPsnId(Long pubId) throws com.smate.core.base.exception.ServiceException {
    return pubSnsDAO.getOwnerPubPsnId(pubId);
  }

  @Override
  public void queryLastUpdatePub(PubQueryDTO pubQueryDTO) {
    pubSnsDAO.queryLastUpdatePub(pubQueryDTO);

  }

  @Override
  public void queryLastUpdatePubByPubIds(PubQueryDTO pubQueryDTO) {
    pubSnsDAO.queryLastUpdatePubByPubIds(pubQueryDTO);
  }

  @Override
  public void getAllGrpPubRcmd(PubQueryDTO pubQueryDTO) {
    pubSnsDAO.getAllGrpPubRcmd(pubQueryDTO);
  }

  @Override
  public PubSnsPO queryPubSns(Long pubId) {
    return pubSnsDAO.get(pubId);
  }

  @Override
  public void queryPubListForZS(PubQueryDTO pubQueryDTO) {
    pubSnsDAO.queryPubListForZS(pubQueryDTO);
  }

  @Override
  public List<Integer> queryPublishYearList(PubQueryDTO pubQueryDTO) {
    return pubSnsDAO.queryPublishYear(pubQueryDTO);
  }

  @Override
  public void queryPubIndexAndTotalCount(PubQueryDTO pubQueryDTO) {
    pubSnsDAO.queryPubCountAndIndex(pubQueryDTO);
  }
}
