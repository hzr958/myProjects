package com.smate.web.v8pub.service.sns;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.v8pub.dao.sns.PubDuplicateDAO;
import com.smate.web.v8pub.dao.sns.group.GroupPubsDAO;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.sns.PubDuplicatePO;
import com.smate.web.v8pub.po.sns.group.GroupPubPO;

@Service("pubDuplicateService")
@Transactional(rollbackFor = Exception.class)
public class PubDuplicateServiceImpl implements PubDuplicateService {
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubDuplicateDAO pubDuplicateDAO;
  @Autowired
  private GroupPubsDAO groupPubsDAO;

  @Override
  public PubDuplicatePO get(Long pubId) throws ServiceException {
    try {
      PubDuplicatePO pubDuplicatePO = pubDuplicateDAO.get(pubId);
      return pubDuplicatePO;
    } catch (Exception e) {
      logger.error("查询成果查重表记录时出错！pubId={}", pubId);
      throw new ServiceException(e);
    }
  }

  @Override
  public void save(PubDuplicatePO pubDuplicatePO) throws ServiceException {
    try {
      pubDuplicatePO.setGmtCreate(new Date());
      pubDuplicateDAO.save(pubDuplicatePO);
    } catch (Exception e) {
      logger.error("保存成果查重表记录时出错！对象属性为={}", pubDuplicatePO);
      throw new ServiceException(e);
    }
  }

  @Override
  public void update(PubDuplicatePO pubDuplicatePO) throws ServiceException {
    try {
      pubDuplicatePO.setGmtModified(new Date());
      pubDuplicateDAO.update(pubDuplicatePO);
    } catch (Exception e) {
      logger.error("保存成果查重表记录时出错！对象属性为={}", pubDuplicatePO);
      throw new ServiceException(e);
    }
  }

  @Override
  public void saveOrUpdate(PubDuplicatePO pubDuplicatePO) throws ServiceException {
    try {
      pubDuplicateDAO.saveOrUpdate(pubDuplicatePO);
    } catch (Exception e) {
      logger.error("保存成果查重表记录时出错！对象属性为={}", pubDuplicatePO);
      throw new ServiceException(e);
    }
  }

  @Override
  public void deleteById(Long pubId) throws ServiceException {
    try {
      pubDuplicateDAO.delete(pubId);
    } catch (Exception e) {
      logger.error("根据pubId删除成果查重表记录时出错！pubId={}", pubId);
      throw new ServiceException(e);
    }

  }

  @Override
  public void delete(PubDuplicatePO pubDuplicatePO) throws ServiceException {
    try {
      pubDuplicateDAO.delete(pubDuplicatePO);
    } catch (Exception e) {
      logger.error("删除成果查重表记录时出错！对象属性为={}", pubDuplicatePO);
      throw new ServiceException(e);
    }
  }

  @Override
  public Boolean isPubChange(Long pubId, String newDetailsHash) throws ServiceException {
    PubDuplicatePO pubDuplicatePO = pubDuplicateDAO.get(pubId);
    if (pubDuplicatePO == null) {
      logger.error("获取成果查重对象为null！对象属性为={}", pubDuplicatePO);
      throw new ServiceException("获取成果查重对象为null");
    }
    Boolean isChange = pubDuplicatePO.getDetailsHash().equals(newDetailsHash) ? false : true;
    return isChange;
  }

  @Override
  public List<Long> getPsnPubDuplicate(String hashTP, String hashTPP) throws ServiceException {
    try {
      List<Long> dupPubs = pubDuplicateDAO.getPsnPubDuplicate(hashTP, hashTPP);
      return dupPubs;
    } catch (Exception e) {
      logger.error("删除成果查重表记录时出错！hashTP={},hashTPP={}", new Object[] {hashTP, hashTPP}, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Long> dupByDoi(Long hashDoi) throws ServiceException {
    try {
      return pubDuplicateDAO.dupByDoi(hashDoi);
    } catch (Exception e) {
      logger.error("根据hashDoi查重出错！hashDoi={}", hashDoi, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Long> dupByDoi(Long hashDoi, Long hashCleanDoi, Long psnId, Long pubId) throws ServiceException {
    try {
      return pubDuplicateDAO.dupByDoi(hashDoi, hashCleanDoi, psnId, pubId);
    } catch (Exception e) {
      logger.error("根据hashDoi查重出错！hashDoi={}", hashDoi, e);
      throw new ServiceException(e);
    }
  }


  @Override
  public List<Long> dupBySourceId(Long hashSourceId, Long psnId, Long pubId) throws ServiceException {
    try {
      return pubDuplicateDAO.dupBySourceId(hashSourceId, psnId, pubId);
    } catch (Exception e) {
      logger.error("根据hashSourceId查重出错！hashSourceId={}", hashSourceId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Long> dupBySourceId(Long hashSourceId) throws ServiceException {
    try {
      return pubDuplicateDAO.dupBySourceId(hashSourceId);
    } catch (Exception e) {
      logger.error("根据hashSourceId查重出错！hashSourceId={}", hashSourceId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Long> dupByPatentInfo(Long hashApplicationNo, Long hashPublicationOpenNo, Long psnId, Long pubId)
      throws ServiceException {
    try {
      return pubDuplicateDAO.dupByPatentInfo(hashApplicationNo, hashPublicationOpenNo, psnId, pubId);
    } catch (Exception e) {
      logger.error("根据专利信息查重出错！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Long> dupByPatentInfo(Long hashApplicationNo, Long hashPublicationOpenNo) throws ServiceException {
    try {
      return pubDuplicateDAO.dupByPatentInfo(hashApplicationNo, hashPublicationOpenNo);
    } catch (Exception e) {
      logger.error("根据专利信息查重出错！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Long> dupByPubInfo(String hashTP, String hashTPP, Long psnId, Long pubId) throws ServiceException {
    try {
      return pubDuplicateDAO.dupByPubInfo(hashTP, hashTPP, psnId, pubId);
    } catch (Exception e) {
      logger.error("根据成果信息查重出错！hashTP={},hashTPP={}", new Object[] {hashTP, hashTPP}, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Long> dupByPubInfo(String hashTP, String hashTPP) throws ServiceException {
    try {
      return pubDuplicateDAO.dupByPubInfo(hashTP, hashTPP);
    } catch (Exception e) {
      logger.error("根据成果信息查重出错！hashTP={},hashTPP={}", new Object[] {hashTP, hashTPP}, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public GroupPubPO getGroupDupPub(Long groupId, Long pubId) {
    return groupPubsDAO.findGrpPubByGrpIdAndPubId(groupId, pubId);
  }

  @Override
  public List<Long> dupByStandardNo(Long hashStandardNo, Long psnId, Long pubId) throws ServiceException {
    try {
      return pubDuplicateDAO.dupByStandardNo(hashStandardNo, psnId, pubId);
    } catch (Exception e) {
      logger.error("根据标准号进行查重出错！hashStandardNo={}，psnId={}", hashStandardNo, psnId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Long> dupByRegisterNo(Long hashRegisterNo, Long psnId, Long pubId) throws ServiceException {
    try {
      return pubDuplicateDAO.dupByRegisterNo(hashRegisterNo, psnId, pubId);
    } catch (Exception e) {
      logger.error("根据登记号进行查重出错！hashRegisterNo={}，psnId={}", hashRegisterNo, psnId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Long> dupByPubInfoNullAppNoAndOpenNo(String hashTP, String hashTPP, Long psnId, Long pubId) {
    try {
      return pubDuplicateDAO.dupByPubInfoNullAppNoAndOpenNo(hashTP, hashTPP, psnId, pubId);
    } catch (Exception e) {
      logger.error("排除专利号和申请号，根据TPP查重出错！hashTP={},hashTPP={}", new Object[] {hashTP, hashTPP}, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Long> dupByPubInfoNullStandardNo(String hashTP, String hashTPP, Long psnId, Long pubId) {
    try {
      return pubDuplicateDAO.dupByPubInfoNullStandardNo(hashTP, hashTPP, psnId, pubId);
    } catch (Exception e) {
      logger.error("排除标准号，根据TPP查重出错！hashTP={},hashTPP={}", new Object[] {hashTP, hashTPP}, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Long> dupByPubInfoNullRegisterNo(String hashTP, String hashTPP, Long psnId, Long pubId) {
    try {
      return pubDuplicateDAO.dupByPubInfoNullRegisterNo(hashTP, hashTPP, psnId, pubId);
    } catch (Exception e) {
      logger.error("排除登记号，根据TPP查重出错！hashTP={},hashTPP={}", new Object[] {hashTP, hashTPP}, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Long> dupByPubInfoNullDoi(String hashTP, String hashTPP, Long psnId, Long pubId) {
    try {
      return pubDuplicateDAO.dupByPubInfoNullDoi(hashTP, hashTPP, psnId, pubId);
    } catch (Exception e) {
      logger.error("排除DOI，根据TPP查重出错！hashTP={},hashTPP={}", new Object[] {hashTP, hashTPP}, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Long> dupByPubInfoNullSourceId(String hashTP, String hashTPP, Long psnId, Long pubId) {
    try {
      return pubDuplicateDAO.dupByPubInfoNullSourceId(hashTP, hashTPP, psnId, pubId);
    } catch (Exception e) {
      logger.error("排除souceId，根据TPP查重出错！hashTP={},hashTPP={}", new Object[] {hashTP, hashTPP}, e);
      throw new ServiceException(e);
    }
  }

}
