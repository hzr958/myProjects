package com.smate.center.batch.service.rol.pub;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.rol.psn.RolPsnInsDao;
import com.smate.center.batch.dao.rol.pub.InsRegionDao;
import com.smate.center.batch.dao.rol.pub.KpiInsInfDao;
import com.smate.center.batch.dao.rol.pub.KpiPubUnitDao;
import com.smate.center.batch.dao.rol.pub.KpiRefreshPubDao;
import com.smate.center.batch.dao.rol.pub.PubPsnRolDao;
import com.smate.center.batch.dao.rol.pub.PublicationRolDao;
import com.smate.center.batch.dao.sns.pub.PublicationListDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.KpiCityInf;
import com.smate.center.batch.model.rol.pub.KpiDistrictInf;
import com.smate.center.batch.model.rol.pub.KpiInsInf;
import com.smate.center.batch.model.rol.pub.KpiPubUnit;
import com.smate.center.batch.model.rol.pub.KpiRefreshPub;
import com.smate.center.batch.model.rol.pub.PublicationRol;



/**
 * 成果统计更新service.
 * 
 * @author liqinghua
 * 
 */
@Service("kpiRefreshPubService")
@Transactional(rollbackFor = Exception.class)
public class KpiRefreshPubServiceImpl implements KpiRefreshPubService {



  /**
   * 
   */
  private static final long serialVersionUID = -4018835681027660966L;

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private KpiPubUnitDao kpiPubUnitDao;
  @Autowired
  private KpiRefreshPubDao kpiRefreshPubDao;
  @Autowired
  private PubPsnRolDao pubPsnRolDao;
  @Autowired
  private PublicationRolDao publicationRolDao;
  @Autowired
  private InsRegionDao insRegionDao;
  @Autowired
  private PublicationListDao publicationListDao;
  @Autowired
  private KpiInsInfDao kpiInsInfDao;
  @Autowired
  private RolPsnInsDao rolPsnInsDao;
  @Autowired
  private PubRolMemberDao pubRolMemberDao;

  @Override
  public void removeExtPubUnit(Long pubId, Set<Long> remainIds) throws ServiceException {

    try {
      kpiPubUnitDao.removeExtPubUnit(pubId, remainIds);
    } catch (Exception e) {
      logger.error("删除指定部门之外的成果关联关系", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<KpiPubUnit> getKpiPubUnitByPubId(Long pubId) throws ServiceException {

    try {
      return this.kpiPubUnitDao.getKpiPubUnitByPubId(pubId);
    } catch (Exception e) {
      logger.error("获取成果关联的部门冗余列表", e);
      throw new ServiceException(e);
    }
  }



  @Override
  public void addPubRefresh(Long pubId, boolean isDel) throws ServiceException {

    try {
      KpiRefreshPub refPub = kpiRefreshPubDao.get(pubId);
      if (refPub == null) {
        refPub = new KpiRefreshPub(pubId);
      }
      refPub.setLastDate(new Date());
      refPub.setIsDel(isDel ? 1 : 0);
      this.kpiRefreshPubDao.save(refPub);
    } catch (Exception e) {
      logger.warn("添加成果统计更新重复pub_id" + pubId);
      if (isDel) {
        try {
          this.kpiRefreshPubDao.updatePubDel(pubId);
        } catch (Exception e1) {
          logger.warn("添加成果统计更新重复标记位为删除失败pub_id" + pubId);
        }
      }
    }
  }

  @Override
  public void addPubRefresh(PublicationRol pub, boolean isDel) throws ServiceException {

    if (!isDel) {
      this.validatePubKpi(pub);
    }
    this.addPubRefresh(pub.getId(), isDel);
  }

  @Override
  public PublicationRol validatePubKpi(PublicationRol pub) throws ServiceException {

    try {
      if (pub.getPublishYear() == null) {
        pub.setKpiValid(0);
      } else if (!this.pubRolMemberDao.hasMatchPsn(pub.getId())) {
        pub.setKpiValid(0);
      } else {
        pub.setKpiValid(1);
      }
      this.publicationRolDao.save(pub);
      return pub;
    } catch (Exception e) {
      logger.error("校验成果KPI统计完整信息pub_id:" + pub.getId(), e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Long> addRefreshUnitPub(Long unitId, Long insId, boolean isDel) throws ServiceException {

    try {
      List<Long> pubIds = pubPsnRolDao.getAllStatusUnitPubId(unitId, insId);
      if (CollectionUtils.isNotEmpty(pubIds)) {
        for (Long pubId : pubIds) {
          this.addPubRefresh(pubId, isDel);
        }
      }

      return pubIds;
    } catch (Exception e) {
      logger.error("添加部门关联成果更新(部门删除、合并部门).", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void addRefreshToUnitPub(List<Long> pubIds, boolean isDel) throws ServiceException {
    try {
      if (CollectionUtils.isNotEmpty(pubIds)) {
        for (Long pubId : pubIds) {
          this.addPubRefresh(pubId, isDel);
        }
      }
    } catch (Exception e) {
      logger.error("添加部门关联成果更新(合并部门).", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void addRefreshPsnPub(Long psnId, Long insId) throws ServiceException {
    try {
      List<Long> pubIds = pubPsnRolDao.getAllStatusPsnPubId(psnId, insId);
      for (Long pubId : pubIds) {
        this.addPubRefresh(pubId, false);
      }
    } catch (Exception e) {
      logger.error("添加人员关联成果更新（人员删除、移动部门）", e);
      throw new ServiceException(e);
    }

  }


  @Override
  public List<KpiRefreshPub> loadNeedRefreshPubId(Integer maxSize) throws ServiceException {

    try {
      return this.kpiRefreshPubDao.loadNeedRefreshPubId(maxSize);
    } catch (Exception e) {
      logger.error("获取需要更新成果统计数据冗余的成果ID", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void removeKpiRefreshPub(Long pubId) throws ServiceException {
    try {
      this.kpiRefreshPubDao.remove(pubId);
    } catch (Exception e) {
      logger.error("删除成果更新数据", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void addUnitPubRefresh(Set<Long> unitIds, Long indId) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public void addInsPubRefresh(Long insId) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public void addDisPubRefresh(Long disId) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public void addCityPubRefresh(Long cityId) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public void addPrvPubRefresh(Long prvId) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public void addRefreshInsPsnPub(Long psnId, Long insId) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public void refreshPubKpi(KpiRefreshPub refPub) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public void saveKpiRefreshError(Exception e, Long keyId, Integer type) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public KpiInsInf fillKpiInsInfoPub(KpiInsInf insInf) throws ServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public KpiDistrictInf fillKpiDistrictInfoPub(KpiDistrictInf disInf) throws ServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public KpiCityInf fillKpiCityInfoPub(KpiCityInf cyInf) throws ServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void refreshPrvPubDefault(Long prvId) throws ServiceException {
    // TODO Auto-generated method stub

  }



}
