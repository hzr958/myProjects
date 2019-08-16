package com.smate.web.v8pub.service.sns;

import com.smate.core.base.psn.dao.PsnPubDAO;
import com.smate.core.base.pub.po.PubPdwhSnsRelationPO;
import com.smate.core.base.pub.po.dao.PubPdwhSnsRelationDAO;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.string.StringUtils;
import com.smate.web.v8pub.dao.sns.PubAssignLogDetailDao;
import com.smate.web.v8pub.exception.ServiceException;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service(value = "pubPdwhSnsRelationService")
@Transactional(rollbackFor = Exception.class)
public class PubPdwhSnsRelationServiceImpl implements PubPdwhSnsRelationService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubPdwhSnsRelationDAO pubPdwhSnsRelationDAO;
  @Autowired
  private PubAssignLogDetailDao pubAssignLogDetailDao;
  @Autowired
  private PsnPubDAO psnPubDAO;
  @Autowired
  private PersonDao personDao;

  @Override
  public PubPdwhSnsRelationPO get(Long id) throws ServiceException {
    // 传入的id既不是pubId也不是pdwhId而是逻辑主键id
    try {
      PubPdwhSnsRelationPO pubPdwhSnsRelationPO = pubPdwhSnsRelationDAO.get(id);
      return pubPdwhSnsRelationPO;
    } catch (Exception e) {
      logger.error("查询个人库成果与基准库成果关系表记录出错！id={}", id);
      throw new ServiceException(e);
    }
  }

  @Override
  public void save(PubPdwhSnsRelationPO pubPdwhSnsRelationPO) throws ServiceException {
    try {
      pubPdwhSnsRelationDAO.save(pubPdwhSnsRelationPO);
    } catch (Exception e) {
      logger.error("保存个人库成果与基准库成果关系表记录出错！对象信息为：={}", pubPdwhSnsRelationPO);
      throw new ServiceException(e);
    }
  }

  @Override
  public void update(PubPdwhSnsRelationPO pubPdwhSnsRelationPO) throws ServiceException {
    try {
      pubPdwhSnsRelationDAO.update(pubPdwhSnsRelationPO);
    } catch (Exception e) {
      logger.error("更新个人库成果与基准库成果关系表记录出错！对象信息为：={}", pubPdwhSnsRelationPO);
      throw new ServiceException(e);
    }
  }

  @Override
  public void saveOrUpdate(PubPdwhSnsRelationPO pubPdwhSnsRelationPO) throws ServiceException {
    try {
      pubPdwhSnsRelationDAO.saveOrUpdate(pubPdwhSnsRelationPO);
    } catch (Exception e) {
      logger.error("保存个人库成果与基准库成果关系表记录出错！对象信息为：={}", pubPdwhSnsRelationPO);
      throw new ServiceException(e);
    }
  }

  @Override
  public void deleteById(Long id) throws ServiceException {
    // 传入的id既不是pubId也不是pdwhId而是逻辑主键id
    try {
      pubPdwhSnsRelationDAO.delete(id);
    } catch (Exception e) {
      logger.error("根据id删除个人库成果与基准库成果关系表记录出错！id={}", id);
      throw new ServiceException(e);
    }
  }

  @Override
  public void delete(PubPdwhSnsRelationPO pubPdwhSnsRelationPO) throws ServiceException {
    try {
      pubPdwhSnsRelationDAO.delete(pubPdwhSnsRelationPO);
    } catch (Exception e) {
      logger.error("删除个人库成果与基准库成果关系表记录出错！对象信息为={}", pubPdwhSnsRelationPO);
      throw new ServiceException(e);
    }
  }

  @Override
  public PubPdwhSnsRelationPO getByPubIdAndPdwhId(Long pubId, Long pdwhId) throws ServiceException {
    try {
      PubPdwhSnsRelationPO pubPdwhSnsRelationPO = pubPdwhSnsRelationDAO.getByPubIdAndPdwhId(pubId, pdwhId);
      return pubPdwhSnsRelationPO;
    } catch (Exception e) {
      logger.error("sns成果与pdwh成果服务：根据pubId和pdwhId获取关系记录出错！pubId={},pdwhId={}", new Object[] {pubId, pdwhId});
      throw new ServiceException(e);
    }
  }

  @Override
  public Long getPdwhIdBySnsId(Long snsPubId) {
    try {
      Long pdwhPubId = pubPdwhSnsRelationDAO.getPdwhPubIdBySnsPubId(snsPubId);
      return pdwhPubId;
    } catch (Exception e) {
      logger.error("sns成果与pdwh成果服务：根据snsPubId找到对应的pdwhPubI出错！pubId={},pdwhId={}", snsPubId);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Long> getSnsIdByPdwhId(Long pdwhPubId) {
    try {
      List<Long> snsPubIds = pubPdwhSnsRelationDAO.getSnsPubIdListByPdwhId(pdwhPubId);
      return snsPubIds;
    } catch (Exception e) {
      logger.error("sns成果与pdwh成果服务：根据pdwhPubI找到对应的snsPubId出错！,pdwhId={}", pdwhPubId);
      throw new ServiceException(e);
    }

  }

  @Override
  public List<Long> getSnsPubIdsByPdwhId(Long pdwhPubId, Long snsPubId) {
    try {
      List<Long> snsPubIds = pubPdwhSnsRelationDAO.getSnsPubIdsByPdwhId(pdwhPubId, snsPubId);
      return snsPubIds;
    } catch (Exception e) {
      logger.error("sns成果与pdwh成果服务：根据pdwhPubI找到对应的snsPubId出错！pubId={},pdwhId={}", new Object[] {snsPubId, pdwhPubId});
      throw new ServiceException(e);
    }
  }

  /**
   * 查询关联的人员信息
   * 
   * @param pubId
   * @param keyword
   * @return
   */
  @Override
  public List<String> getPubRelationPsninfo(Long pubId, String keyword, Long ownerPsnId, Long currentUserId) {
    List<String> psnNameList = new ArrayList();
    List<Long> psnIds = new ArrayList<>();
    if (!ownerPsnId.equals(currentUserId)) {
      psnIds.add(ownerPsnId);
    }
    Long pdwhPubId = pubPdwhSnsRelationDAO.getPdwhPubIdBySnsPubId(pubId);
    if (NumberUtils.isNotNullOrZero(pdwhPubId)) {
      List<Long> psnIdByPubId = pubPdwhSnsRelationDAO.findSnsPsnIds(pdwhPubId);
      psnIdByPubId.remove(ownerPsnId);
      if (CollectionUtils.isNotEmpty(psnIdByPubId)) {
        psnIds.addAll(psnIdByPubId);
      }
    }
    if (CollectionUtils.isNotEmpty(psnIds)) {
      return searchPsnInfo(psnIds, keyword);
    }
    return psnNameList;
  }



  public List<String> searchPsnInfo(List<Long> psnIds, String keyword) {
    List<String> psnNameList = new ArrayList();
    // if(psnIds.size() >1000) psnIds = psnIds.subList(0,1000);
    // List<Person> list = personDao.findPersonList(psnIds);
    if (CollectionUtils.isNotEmpty(psnIds)) {
      for (Long psnId : psnIds) {
        Person person = personDao.get(psnId);
        if (person == null)
          continue;
        String s = searchNameFromPerson(person, keyword);
        if (StringUtils.isNotBlank(s)) {
          psnNameList.add(s);
        }
      }
    }
    return psnNameList;
  }

  protected String searchNameFromPerson(Person person, String keyword) {
    Locale locale = LocaleContextHolder.getLocale();
    if(Locale.US.equals(locale)){
      return  searchEnNameFromPerson( person,  keyword);
    }
    if (StringUtils.isBlank(keyword)) {
      if (StringUtils.isNotBlank(person.getName()))
        return person.getName();
      if (StringUtils.isNotBlank(person.getEname()))
        return person.getEname();
      if (StringUtils.isNotBlank(person.getFirstName()) && StringUtils.isNotBlank(person.getLastName())) {
        return person.getFirstName() + " " + person.getLastName();
      }
    } else {
      if (StringUtils.isNotBlank(person.getName()) && containName(person.getName(), keyword))
        return person.getName();
      if (StringUtils.isNotBlank(person.getEname()) && containName(person.getEname(), keyword))
        return person.getEname();
      if (StringUtils.isNotBlank(person.getFirstName()) && StringUtils.isNotBlank(person.getLastName())
          && containName(person.getFirstName() + " " + person.getLastName(), keyword)) {
        return person.getFirstName() + " " + person.getLastName();
      }
    }
    return "";
  }
  protected String searchEnNameFromPerson(Person person, String keyword) {
    if (StringUtils.isBlank(keyword)) {
      if (StringUtils.isNotBlank(person.getEname()))
        return person.getEname();
      if (StringUtils.isNotBlank(person.getFirstName()) && StringUtils.isNotBlank(person.getLastName())) {
        return person.getFirstName() + " " + person.getLastName();
      }
      if (StringUtils.isNotBlank(person.getName()))
        return person.getName();
    } else {
      if (StringUtils.isNotBlank(person.getEname()) && containName(person.getEname(), keyword))
        return person.getEname();
      if (StringUtils.isNotBlank(person.getFirstName()) && StringUtils.isNotBlank(person.getLastName())
          && containName(person.getFirstName() + " " + person.getLastName(), keyword)) {
        return person.getFirstName() + " " + person.getLastName();
      }
      if (StringUtils.isNotBlank(person.getName()) && containName(person.getName(), keyword))
        return person.getName();
    }
    return "";
  }

  protected boolean containName(String name, String cname) {
    if (StringUtils.isBlank(name) || StringUtils.isBlank(cname)) {
      return false;
    }
    name = name.toLowerCase();
    cname = cname.toLowerCase();
    return name.contains(cname);
  }

  @Override
  public void delPubPdwhSnsRelationByPdwhPubId(Long pdwhPubId) throws ServiceException {
    try {
      pubPdwhSnsRelationDAO.delPubPdwhSnsRelationByPdwhPubId(pdwhPubId);
    } catch (Exception e) {
      logger.error("根据pdwhPubId删除个人库成果与基准库成果关系表记录出错！pdwhPubId={}", pdwhPubId);
      throw new ServiceException(e);
    }
  }

}
