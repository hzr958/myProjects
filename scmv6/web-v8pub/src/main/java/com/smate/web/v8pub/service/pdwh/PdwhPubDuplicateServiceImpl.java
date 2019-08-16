package com.smate.web.v8pub.service.pdwh;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.v8pub.dao.pdwh.PdwhPubDuplicateDAO;
import com.smate.web.v8pub.dao.pdwh.PubPdwhDAO;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.pdwh.PdwhPubDuplicatePO;

@Service("pdwhPubDuplicateService")
@Transactional(rollbackFor = Exception.class)
public class PdwhPubDuplicateServiceImpl implements PdwhPubDuplicateService {
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PdwhPubDuplicateDAO pdwhDuplicateDAO;
  @Autowired
  private PubPdwhDAO pubPdwhDAO;

  @Override
  public PdwhPubDuplicatePO get(Long pdwhId) throws ServiceException {
    try {
      PdwhPubDuplicatePO pdwhDuplicatePO = pdwhDuplicateDAO.get(pdwhId);
      return pdwhDuplicatePO;
    } catch (Exception e) {
      logger.error("查询基准库成果查重表记录出错！pdwhId={}", pdwhId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void save(PdwhPubDuplicatePO pdwhDuplicatePO) throws ServiceException {
    try {
      pdwhDuplicateDAO.save(pdwhDuplicatePO);
    } catch (Exception e) {
      logger.error("保存基准库成果查重表记录出错！对象属性为={}", pdwhDuplicatePO, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void update(PdwhPubDuplicatePO pdwhDuplicatePO) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public void saveOrUpdate(PdwhPubDuplicatePO pdwhDuplicatePO) throws ServiceException {
    try {
      pdwhDuplicateDAO.saveOrUpdate(pdwhDuplicatePO);
    } catch (Exception e) {
      logger.error("保存基准库成果查重表记录出错！对象属性为={}", pdwhDuplicatePO, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void deleteById(Long pdwhId) throws ServiceException {
    try {
      pdwhDuplicateDAO.delete(pdwhId);
    } catch (Exception e) {
      logger.error("根据pdwhId删除基准库成果查重表记录出错！pdwhId={}", pdwhId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void delete(PdwhPubDuplicatePO pdwhDuplicatePO) throws ServiceException {
    try {
      pdwhDuplicateDAO.delete(pdwhDuplicatePO);
    } catch (Exception e) {
      logger.error("删除基准库成果查重表记录出错！对象属性为={}", pdwhDuplicatePO, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Long> listDuplicatePubByTPP(String hashTPP) throws ServiceException {
    try {
      List<Long> dupPubIds = pdwhDuplicateDAO.listDuplicatePubByTPP(hashTPP);
      if (CollectionUtils.isNotEmpty(dupPubIds)) {
        dupPubIds = pubPdwhDAO.findExistsPubIds(dupPubIds);
      }
      return dupPubIds;
    } catch (Exception e) {
      logger.error("根据hashTPP查找的成果id出错！hashTPP={}", hashTPP, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Long> dupByNotNullDoi(Long hashDoi, Long hashCleanDoi) throws ServiceException {
    try {
      List<Long> dupPubIds = pdwhDuplicateDAO.dupByNotNullDoi(hashDoi, hashCleanDoi);
      if (CollectionUtils.isNotEmpty(dupPubIds)) {
        dupPubIds = pubPdwhDAO.findExistsPubIds(dupPubIds);
      }
      return dupPubIds;
    } catch (Exception e) {
      logger.error("根据hashDoi查重出错！hashDoi={}", hashDoi, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Long> dupByPubInfoNullDoi(String hashTP, String hashTPP) throws ServiceException {
    try {
      List<Long> dupPubIds = pdwhDuplicateDAO.dupByPubInfoNullDoi(hashTP, hashTPP);
      if (CollectionUtils.isNotEmpty(dupPubIds)) {
        dupPubIds = pubPdwhDAO.findExistsPubIds(dupPubIds);
      }
      return dupPubIds;
    } catch (Exception e) {
      logger.error("根据成果信息查重出错！hashTP={},hashTPP={}", new Object[] {hashTP, hashTPP}, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Long> dupBySourceId(Long hashSourceId) throws ServiceException {
    try {
      List<Long> dupPubIds = pdwhDuplicateDAO.dupBySourceId(hashSourceId);
      if (CollectionUtils.isNotEmpty(dupPubIds)) {
        dupPubIds = pubPdwhDAO.findExistsPubIds(dupPubIds);
      }
      return dupPubIds;
    } catch (Exception e) {
      logger.error("根据hashSourceId查重出错！hashSourceId={}", hashSourceId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Long> dupByPatentInfo(Long hashApplicationNo, Long hashPublicationOpenNo) throws ServiceException {
    try {
      List<Long> dupPubIds = pdwhDuplicateDAO.dupByPatentInfo(hashApplicationNo, hashPublicationOpenNo);
      if (CollectionUtils.isNotEmpty(dupPubIds)) {
        dupPubIds = pubPdwhDAO.findExistsPubIds(dupPubIds);
      }
      return dupPubIds;
    } catch (Exception e) {
      logger.error("根据专利信息查重出错！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Long> dupByPubInfo(String hashTP, String hashTPP) throws ServiceException {
    try {
      List<Long> dupPubIds = pdwhDuplicateDAO.dupByPubInfo(hashTP, hashTPP);
      if (CollectionUtils.isNotEmpty(dupPubIds)) {
        dupPubIds = pubPdwhDAO.findExistsPubIds(dupPubIds);
      }
      return dupPubIds;
    } catch (Exception e) {
      logger.error("根据成果信息查重出错！hashTP={},hashTPP={}", new Object[] {hashTP, hashTPP}, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Long> dupByPubInfo(String hashT) throws ServiceException {
    try {
      List<Long> dupPubIds = pdwhDuplicateDAO.dupByPubInfo(hashT);
      if (CollectionUtils.isNotEmpty(dupPubIds)) {
        dupPubIds = pubPdwhDAO.findExistsPubIds(dupPubIds);
      }
      return dupPubIds;
    } catch (Exception e) {
      logger.error("根据成果信息查重出错！hashT={}", new Object[] {hashT}, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Long> dupByStandardNo(Long hashStandardNo) throws ServiceException {
    try {
      List<Long> dupPubIds = pdwhDuplicateDAO.dupByStandardNo(hashStandardNo);
      if (CollectionUtils.isNotEmpty(dupPubIds)) {
        dupPubIds = pubPdwhDAO.findExistsPubIds(dupPubIds);
      }
      return dupPubIds;
    } catch (Exception e) {
      logger.error("根据成果标准号查重出错！hashStandardNo={}", hashStandardNo, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Long> dupByRegisterNo(Long hashRegisterNo) throws ServiceException {
    try {
      List<Long> dupPubIds = pdwhDuplicateDAO.dupByRegisterNo(hashRegisterNo);
      if (CollectionUtils.isNotEmpty(dupPubIds)) {
        dupPubIds = pubPdwhDAO.findExistsPubIds(dupPubIds);
      }
      return dupPubIds;
    } catch (Exception e) {
      logger.error("根据成果登记号查重出错！hashRegisterNo={}", hashRegisterNo, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Long> dupByPubInfoNullAppNoAndOpenNo(String hashTP, String hashTPP) {
    try {
      List<Long> dupPubIds = pdwhDuplicateDAO.dupByPubInfoNullAppNoAndOpenNo(hashTP, hashTPP);
      if (CollectionUtils.isNotEmpty(dupPubIds)) {
        dupPubIds = pubPdwhDAO.findExistsPubIds(dupPubIds);
      }
      return dupPubIds;
    } catch (Exception e) {
      logger.error("排除有专利号和申请号的数据，根据TPP查重出错！hashTPP={}", hashTPP, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Long> dupByPubInfoNullStandardNo(String hashTP, String hashTPP) throws ServiceException {
    try {
      List<Long> dupPubIds = pdwhDuplicateDAO.dupByPubInfoNullStandardNo(hashTP, hashTPP);
      if (CollectionUtils.isNotEmpty(dupPubIds)) {
        dupPubIds = pubPdwhDAO.findExistsPubIds(dupPubIds);
      }
      return dupPubIds;
    } catch (Exception e) {
      logger.error("排除有标准号的数据，根据TPP查重出错！hashTPP={}", hashTPP, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Long> dupByPubInfoNullRegisterNo(String hashTP, String hashTPP) throws ServiceException {
    try {
      List<Long> dupPubIds = pdwhDuplicateDAO.dupByPubInfoNullRegisterNo(hashTP, hashTPP);
      if (CollectionUtils.isNotEmpty(dupPubIds)) {
        dupPubIds = pubPdwhDAO.findExistsPubIds(dupPubIds);
      }
      return dupPubIds;
    } catch (Exception e) {
      logger.error("排除有登记号的数据，根据TPP查重出错！hashTPP={}", hashTPP, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Long> dupByPubInfoNullSourceId(String hashTP, String hashTPP) throws ServiceException {
    try {
      List<Long> dupPubIds = pdwhDuplicateDAO.dupByPubInfoNullSourceId(hashTP, hashTPP);
      if (CollectionUtils.isNotEmpty(dupPubIds)) {
        dupPubIds = pubPdwhDAO.findExistsPubIds(dupPubIds);
      }
      return dupPubIds;
    } catch (Exception e) {
      logger.error("排除有souceId的数据，根据TPP查重出错！hashTPP={}", hashTPP, e);
      throw new ServiceException(e);
    }
  }

}
