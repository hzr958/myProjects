package com.smate.center.batch.service.pub;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.pub.PubOwnerMatchDao;
import com.smate.center.batch.dao.sns.pub.PublicationDao;
import com.smate.center.batch.dao.sns.pub.PublicationPdwhExtDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.PubMember;
import com.smate.center.batch.model.sns.pub.PublicationPdwh;
import com.smate.center.batch.service.pub.mq.PubSyncToPubFtSrvProducer;
import com.smate.center.batch.util.pub.PsnPmIsiNameUtils;
import com.smate.core.base.psn.dao.PersonProfileDao;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.model.security.Person;

/**
 * 成果作者匹配表，用于确定用户与作者的关系.
 * 
 * @author liqinghua
 * 
 */
@Service("pubOwnerMatchService")
@Transactional(rollbackFor = Exception.class)
public class PubOwnerMatchServiceImpl implements PubOwnerMatchService {

  /**
   * 
   */
  private static final long serialVersionUID = 4890027176921322084L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubOwnerMatchDao pubOwnerMatchDao;
  @Autowired
  private PublicationPdwhExtDao publicationPdwhDao;
  @Autowired
  private PersonProfileDao personProfileDao;
  @Autowired
  private PublicationDao publicationDao;
  @Autowired
  private PubSyncToPubFtSrvProducer pubSyncToPubFtSrvProducer;

  // @Autowired
  // private PubSyncToPubFtSrvProducer pubSyncToPubFtSrvProducer;

  @Override
  public void pubOwnerMatch(Long pubId, Long psnId) throws ServiceException {

    try {
      Person person = personProfileDao.getPsnAllName(psnId);
      String zhName = person.getName();
      String firstName = person.getFirstName();
      String lastName = person.getLastName();
      String otherName = person.getOtherName();
      List<PubMember> pmList = publicationDao.getPubMembersByPubId(pubId);
      if (CollectionUtils.isEmpty(pmList)) {
        this.pubOwnerMatchDao.savePubOwnerMatch(pubId, psnId, 0, 0);
        // FIXME 2015-10-29 取消MQ -done
        this.pubSyncToPubFtSrvProducer.sendUpdatePubOwnerMatch(pubId, psnId, 0);
        return;
      }
      List<String> enNameList = builderEnNames(firstName, lastName, otherName);
      // 先确认用户自己选的作者
      for (PubMember pm : pmList) {
        if (!psnId.equals(pm.getPsnId())) {
          continue;
        }
        // 精确、模糊匹配上
        if (this.matchPubAthorExact(pm, zhName, enNameList)
            || this.matchPubAthorFuzzy(pm, firstName, lastName, otherName)) {
          this.pubOwnerMatchDao.savePubOwnerMatch(pubId, psnId, pm.getSeqNo(), pm.getAuthorPos());
          // FIXME 2015-10-29 取消MQ -done
          this.pubSyncToPubFtSrvProducer.sendUpdatePubOwnerMatch(pubId, psnId, 1);
          return;
        }
      }
      // 如果匹配不上，再试试其他人员是否精确匹配上
      for (PubMember pm : pmList) {
        // 精确、模糊匹配上
        if (this.matchPubAthorExact(pm, zhName, enNameList)) {
          this.pubOwnerMatchDao.savePubOwnerMatch(pubId, psnId, pm.getSeqNo(), pm.getAuthorPos());
          // FIXME 2015-10-29 取消MQ -done
          this.pubSyncToPubFtSrvProducer.sendUpdatePubOwnerMatch(pubId, psnId, 1);
          return;
        }
      }
      // 如果匹配不上，再试试其他人员是否模糊匹配上
      for (PubMember pm : pmList) {
        // 精确、模糊匹配上
        if (this.matchPubAthorFuzzy(pm, firstName, lastName, otherName)) {
          this.pubOwnerMatchDao.savePubOwnerMatch(pubId, psnId, pm.getSeqNo(), pm.getAuthorPos());
          // FIXME 2015-10-29 取消MQ -done
          this.pubSyncToPubFtSrvProducer.sendUpdatePubOwnerMatch(pubId, psnId, 1);
          return;
        }
      }
      // 匹配不上
      this.pubOwnerMatchDao.savePubOwnerMatch(pubId, psnId, 0, 0);
      // FIXME 2015-10-29 取消MQ -done
      this.pubSyncToPubFtSrvProducer.sendUpdatePubOwnerMatch(pubId, psnId, 0);
    } catch (Exception e) {
      logger.error("成果所有人作者与成果匹配 pubId:" + pubId, e);
      throw new ServiceException("成果所有人作者与成果匹配 pubId:" + pubId, e);
    }
  }

  /**
   * 精确匹配成果作者.
   * 
   * @param pm
   * @param zhName
   * @param enNameList
   * @return
   */
  public boolean matchPubAthorExact(PubMember pm, String zhName, List<String> enNameList) {
    String pmName = pm.getName();
    if (StringUtils.isBlank(pmName)) {
      return false;
    }
    // 匹配中文名
    if (pmName.equalsIgnoreCase(zhName)) {
      return true;
    }
    // 匹配英文名
    pmName = XmlUtil.getCleanAuthorName(pmName);
    for (String enName : enNameList) {
      if (pmName.equalsIgnoreCase(enName)) {
        return true;
      }
    }
    return false;
  }

  /**
   * 模糊匹配成果作者.
   * 
   * @param pm
   * @param zhName
   * @param enNameList
   * @return
   */
  public boolean matchPubAthorFuzzy(PubMember pm, String firstName, String lastName, String otherName) {
    String pmName = pm.getName();
    if (StringUtils.isBlank(pmName)) {
      return false;
    }
    pmName = pmName.toLowerCase();
    firstName = XmlUtil.getCleanAuthorName(firstName);
    lastName = XmlUtil.getCleanAuthorName(lastName);
    otherName = XmlUtil.getCleanAuthorName(otherName);
    if (StringUtils.isBlank(firstName) || StringUtils.isBlank(lastName)) {
      return false;
    }
    String preF = firstName.substring(0, 1).toLowerCase();
    lastName = lastName.toLowerCase();
    // 尝试z lin 是否匹配上alen z lin或者 z alen lin
    int index = pmName.indexOf(preF);
    if (index > -1 && pmName.substring(index).endsWith(lastName)) {
      return true;
    }
    // 尝试lin z是否匹配上lin z alen或者lin alen z
    index = pmName.lastIndexOf(preF);
    if (index > 0 && pmName.substring(0, index).startsWith(lastName)) {
      return true;
    }
    if (StringUtils.isBlank(otherName)) {
      return false;
    }
    String preO = otherName.substring(0, 1).toLowerCase();
    // 尝试a lin 是否匹配上a zhen lin或者 zhen a lin
    index = pmName.indexOf(preO);
    if (index > -1 && pmName.substring(index).endsWith(lastName)) {
      return true;
    }
    // 尝试lin a是否匹配上lin zhen a或者lin a zhen
    index = pmName.lastIndexOf(preO);
    if (index > 0 && pmName.substring(0, index).startsWith(lastName)) {
      return true;
    }
    return false;
  }

  /**
   * 构造用户英文名.
   * 
   * @param firstName
   * @param lastName
   * @param otherName
   * @return
   */
  public List<String> builderEnNames(String firstName, String lastName, String otherName) {

    firstName = XmlUtil.getCleanAuthorName(firstName);
    lastName = XmlUtil.getCleanAuthorName(lastName);
    otherName = XmlUtil.getCleanAuthorName(otherName);
    List<String> enNameList = new ArrayList<String>();
    if (StringUtils.isBlank(firstName) || StringUtils.isBlank(lastName)) {
      return enNameList;
    }

    // 构造全称，简称
    Set<String> ifNameList = PsnPmIsiNameUtils.buildAllName(firstName, lastName, otherName);
    if (ifNameList != null) {
      enNameList.addAll(ifNameList);
    }
    return enNameList;
  }

  @Override
  public void delPubOwnerMatch(Long pubId) throws ServiceException {

    try {
      this.pubOwnerMatchDao.delPubOwnerMatch(pubId);
    } catch (Exception e) {
      logger.error("删除数据 pubId:" + pubId, e);
      throw new ServiceException("删除数据PubId:" + pubId, e);
    }
  }

  @Override
  public List<Long> findPdwhIsiId(Long psnId) throws ServiceException {
    try {
      return this.publicationPdwhDao.findPdwhIsiId(psnId);
    } catch (Exception e) {
      logger.error("通过成果作者匹配表查找isi_pubId失败 psnId:" + psnId, e);
      throw new ServiceException("通过成果作者匹配表查找isi_pubId失败psnId:" + psnId, e);
    }
  }

  @Override
  public List<Long> findPdwhCnkiId(Long psnId) throws ServiceException {
    try {
      return this.publicationPdwhDao.findPdwhCnkiId(psnId);
    } catch (Exception e) {
      logger.error("通过成果作者匹配表查找cnki_pubId失败 psnId:" + psnId, e);
      throw new ServiceException("通过成果作者匹配表查找cnki_pubId失败psnId:" + psnId, e);
    }
  }

  @Override
  public List<PublicationPdwh> findByPsnId(Long psnId) throws ServiceException {
    try {
      return this.publicationPdwhDao.findByPsnId(psnId);
    } catch (Exception e) {
      logger.error("通过成果作者匹配表失败 psnId:" + psnId, e);
      throw new ServiceException("通过成果作者匹配表失败psnId:" + psnId, e);
    }
  }

  @Override
  public boolean isPubOwnerMatch(Long pubId, Long psnId) throws ServiceException {

    try {
      return this.pubOwnerMatchDao.isPubOwnerMatch(pubId, psnId);
    } catch (Exception e) {
      logger.error("成果所有人是否与成果作者匹配 pubId:" + pubId, e);
      throw new ServiceException("成果所有人是否与成果作者匹配 pubId:" + pubId, e);
    }
  }

}
