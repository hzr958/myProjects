package com.smate.center.batch.service.rol.pub;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.rol.pub.InstitutionRolDao;
import com.smate.center.batch.dao.sns.pub.PrjSchemeAgencyDao;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.InstitutionRol;
import com.smate.center.batch.model.sns.pub.PrjSchemeAgency;
import com.smate.center.batch.service.pub.ConstRegionService;
import com.smate.core.base.utils.file.FileService;
import com.smate.core.base.utils.model.InsPortal;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.security.TheadLocalInsId;


/**
 * 
 * @author new
 * 
 */
@Service("institutionRolService")
@Transactional(rollbackFor = Exception.class)
public class InstitutionRolServiceImpl implements InstitutionRolService {

  // logger.
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private InstitutionRolDao institutionRolDao;

  @Autowired
  private InsPortalManager insPortalManager;

  @Autowired
  private ConstRegionService constRegionService;
  @Autowired
  private PrjSchemeAgencyDao prjSchemeAgencyDao;
  @Autowired
  private BaseConstUtils baseConstUtils;
  @Autowired
  private FileService fileService;
  private Ehcache cache;


  /**
   * @param cache the cache to set
   */
  public void setCache(Ehcache cache) {
    this.cache = cache;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.scmweb.service.profile.InstitutionManager#findListByName (java.lang.String)
   */
  @Override
  public InstitutionRol findByName(String name) throws ServiceException {

    try {
      return institutionRolDao.findByName(name);
    } catch (DaoException e) {
      logger.error("findListByName查询单位出错:", e);
      throw new ServiceException(e);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.scmweb.service.profile.InstitutionManager#getInstitution (java.lang.Long)
   */
  @Override
  public InstitutionRol getInstitution(Long insId) throws ServiceException {
    try {

      return institutionRolDao.findById(insId);

    } catch (DaoException e) {
      logger.error("getInstitution取单位列表出错:", e);
      throw new ServiceException(e);
    }
  }


  private String getInsBrief(Long insId) throws ServiceException {
    try {
      Element element = cache.get(getInsBriefCacheKey(insId));
      if (element != null) {
        return (String) element.getValue();

      }

      String briefDir = baseConstUtils.getProps().get("file.root") + "/insbrief";

      return fileService.readTextTrimEmpty(getInsBriefFileName(insId), briefDir, "UTF-8");

    } catch (Exception e) {

      logger.error("读取单位简介文件失败！", e);
      return null;

    }

  }


  public String getInsBriefCacheKey(long insId) {
    return String.format("brief_%s", insId);
  }

  private String getInsBriefFileName(Long insId) throws ServiceException {
    Integer nodeId = SecurityUtils.getCurrentAllNodeId().get(0);
    String fileName = String.format("insbrief-%s-%s.txt", nodeId, insId);
    return fileName;
  }

  /**
   * 保存单位信息.
   * 
   * @param ins
   * @throws DaoException
   */
  public void saveInstitutionRol(InstitutionRol insParam) throws ServiceException {

    try {
      InstitutionRol ins = institutionRolDao.findById(insParam.getId());
      if (ins == null) {
        ins = insParam;
      } else {
        ins.setEnAddress(ins.getEnAddress());
        ins.setZhAddress(insParam.getZhAddress());
        ins.setPostcode(insParam.getPostcode());
        ins.setUrl(insParam.getUrl());
        ins.setTel(insParam.getTel());
        ins.setContactPerson(insParam.getContactPerson());
        ins.setServerTel(insParam.getServerTel());
        ins.setServerEmail(insParam.getServerEmail());
        ins.setRegionId(insParam.getRegionId());
        ins.setStatus(insParam.getStatus());
        ins.setNature(insParam.getNature());
        ins.setAbbreviation(insParam.getAbbreviation());
        ins.setLogoAddr(insParam.getLogoAddr());
        ins.setNearSnsNodeId(insParam.getNearSnsNodeId());
        ins.setZhName(insParam.getZhName());
        ins.setEnName(insParam.getEnName());
        institutionRolDao.saveInstitutionRol(ins);
      }

    } catch (Exception e) {
      logger.error("saveInstitutionRol保存单位信息出错:", e);
      throw new ServiceException("保存单位错误", e);
    }
  }

  @Override
  public String getInsName() throws ServiceException {

    return this.getInsName(TheadLocalInsId.getInsId());
  }

  @Override
  public String getInsName(Long insId) throws ServiceException {
    Locale local = LocaleContextHolder.getLocale();
    InstitutionRol ins = institutionRolDao.get(insId);
    if (ins == null) {
      return null;
    }
    String name = null;
    if (local.equals(Locale.US)) {
      name = ins.getEnName() == null ? ins.getZhName() : ins.getEnName();
    } else {
      name = ins.getZhName() == null ? ins.getEnName() : ins.getZhName();
    }
    return name;
  }

  @Override
  public void syncInsByOldIns(Map<String, Object> oldData) throws ServiceException {

    try {
      InstitutionRol ins = new InstitutionRol();
      Long insId = Long.valueOf(oldData.get("INS_ID").toString());
      String zhName = oldData.get("CNAME") == null ? null : oldData.get("CNAME").toString();
      String enName = oldData.get("ENAME") == null ? null : oldData.get("ENAME").toString();
      String abbr = oldData.get("ABBR") == null ? null : oldData.get("ABBR").toString();
      String contactPerson = oldData.get("CONTACT_PERSON") == null ? null : oldData.get("CONTACT_PERSON").toString();
      String zhAddress = oldData.get("CADDRESS") == null ? null : oldData.get("CADDRESS").toString();
      String enAddress = oldData.get("EADDRESS") == null ? null : oldData.get("EADDRESS").toString();
      String tel = oldData.get("TEL") == null ? null : oldData.get("TEL").toString();
      // String email = oldData.get("EMAIL") == null ? null :
      // oldData.get("EMAIL").toString();
      String url = oldData.get("URL") == null ? null : oldData.get("URL").toString();
      Integer oldRegionId =
          oldData.get("REGION_ID") == null ? null : Integer.valueOf(oldData.get("REGION_ID").toString());
      Long regionId = constRegionService.getOldMapingId(oldRegionId);
      // 单位状态
      String oldStatus = oldData.get("STATUS") == null ? null : oldData.get("STATUS").toString();
      Long status = 0L;
      if ("A".equalsIgnoreCase(oldStatus)) {
        status = 2L;
      } else if ("R".equalsIgnoreCase(oldStatus)) {
        status = 1L;
      } else if ("D".equalsIgnoreCase(oldStatus)) {
        status = 9L;
      }
      // 单位性质
      String oldNature = oldData.get("NATURE") == null ? null : oldData.get("NATURE").toString();
      Long nature = 99L;
      if ("01".equalsIgnoreCase(oldNature)) {
        nature = 1L;
      } else if ("02".equalsIgnoreCase(oldNature)) {
        nature = 2L;
      } else if ("03".equalsIgnoreCase(oldNature)) {
        nature = 3L;
      }
      String checkEmails = oldData.get("CHECK_EMAILS") == null ? null : oldData.get("CHECK_EMAILS").toString();
      String checkCode = oldData.get("CHECK_CODE") == null ? null : oldData.get("CHECK_CODE").toString();
      String oldIsCheckCode = oldData.get("IS_CHECKCODE") == null ? null : oldData.get("IS_CHECKCODE").toString();
      Integer isCheckCode = 0;
      if ("1".equals(oldIsCheckCode)) {
        isCheckCode = 1;
      }
      Integer isisOrgCode =
          oldData.get("ISIS_ORG_CODE") == null ? null : Integer.valueOf(oldData.get("ISIS_ORG_CODE").toString());
      // 从ins_portal同步过来
      String serverTel = oldData.get("SERVER_TEL") == null ? null : oldData.get("SERVER_TEL").toString();
      String serverEmail = oldData.get("SERVER_EMAIL") == null ? null : oldData.get("SERVER_EMAIL").toString();

      ins.setId(insId);
      ins.setZhName(zhName);
      ins.setEnName(enName);
      ins.setAbbreviation(abbr);
      ins.setContactPerson(contactPerson);
      ins.setZhAddress(zhAddress);
      ins.setEnAddress(enAddress);
      ins.setTel(tel);
      ins.setUrl(url);
      ins.setRegionId(regionId);
      ins.setStatus(status);
      ins.setNature(nature);
      ins.setCheckEmails(checkEmails);
      ins.setCheckCode(checkCode);
      ins.setIsCheckCode(isCheckCode);
      ins.setServerTel(serverTel);
      ins.setIsisOrgCode(isisOrgCode);
      ins.setServerEmail(serverEmail);
      ins.setServerTel(serverTel);

      this.institutionRolDao.save(ins);

      // 项目资助机构
      if (nature == 3) {
        PrjSchemeAgency prjSchemeAgency = new PrjSchemeAgency(insId, zhName, enName, null);
        prjSchemeAgencyDao.save(prjSchemeAgency);
      }
    } catch (Exception e) {
      logger.error("V2.6数据同步出错:", e);
      throw new ServiceException("V2.6数据同步错误", e);
    }
  }

  @Override
  public List<InstitutionRol> getAllInstitutionByPrvId(Long prvId) throws ServiceException {
    return this.institutionRolDao.getAllInstitutionByPrvId(prvId);
  }

  @Override
  public List<InstitutionRol> getAllInstitutionByCyId(Long cyId) throws ServiceException {
    return this.institutionRolDao.getAllInstitutionByCyId(cyId);
  }

  @Override
  public List<InstitutionRol> getAllInstitutionByDisId(Long disId) throws ServiceException {
    return this.institutionRolDao.getAllInstitutionByDisId(disId);
  }

  @Override
  public List<InstitutionRol> getInsByIds(List<Long> ids) throws ServiceException {
    return this.institutionRolDao.getInsByIds(ids);
  }

  @Override
  public InsPortal getJoinInsPortal(String insName) throws ServiceException {

    try {
      if (StringUtils.isBlank(insName)) {
        return null;
      }
      return this.institutionRolDao.getJoinInsPortal(insName);
    } catch (Exception e) {
      logger.error("查找加入科研在线单位域名", e);
      throw new ServiceException("查找加入科研在线单位域名", e);
    }
  }

  @Override
  public List<InstitutionRol> queryPrvInsList(Long prvId) throws ServiceException {
    try {
      if (prvId == null) {
        return null;
      }
      return this.institutionRolDao.queryPrvInsList(prvId);
    } catch (Exception e) {
      logger.error("查找指定省份单位列表", e);
      throw new ServiceException("查找指定省份单位列表", e);
    }
  }

  @Override
  public List<InstitutionRol> queryJoinInsList(String queryName) throws ServiceException {
    try {
      if (StringUtils.isBlank(queryName)) {
        return null;
      }
      return this.institutionRolDao.queryJoinInsList(queryName);
    } catch (Exception e) {
      logger.error("查找指定名称单位列表", e);
      throw new ServiceException("查找指定名称单位列表", e);
    }
  }

  @Override
  public InstitutionRol getInstitutionEdit() throws ServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public InstitutionRol getInstitutionCr() throws ServiceException {
    // TODO Auto-generated method stub
    return null;
  }

}
