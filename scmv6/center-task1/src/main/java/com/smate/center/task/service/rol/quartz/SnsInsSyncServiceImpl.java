package com.smate.center.task.service.rol.quartz;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.sns.psn.PsnInsDao;
import com.smate.center.task.dao.sns.quartz.ConstRegionDao;
import com.smate.center.task.dao.sns.quartz.InsDataFromSnsDao;
import com.smate.center.task.dao.sns.quartz.InsRegionSnsDao;
import com.smate.center.task.dao.sns.quartz.InstitutionDao;
import com.smate.center.task.dao.sns.quartz.PsnSidDao;
import com.smate.center.task.exception.DaoException;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.bpo.InsInfo;
import com.smate.center.task.model.common.InsDataFrom;
import com.smate.center.task.model.common.InsDataFromId;
import com.smate.center.task.model.common.InsReg;
import com.smate.center.task.model.sns.psn.PsnInsSns;
import com.smate.center.task.model.sns.psn.PsnSid;
import com.smate.center.task.model.sns.pub.ConstRegion;
import com.smate.center.task.model.sns.quartz.Institution;
import com.smate.core.base.psn.dao.PersonProfileDao;
import com.smate.core.base.utils.dao.security.InsPortalDao;
import com.smate.core.base.utils.dao.security.InsRoleDao;
import com.smate.core.base.utils.model.InsPortal;
import com.smate.core.base.utils.model.security.InsRole;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.model.security.PsnInsPk;

/**
 * 同步单位信息 sns
 * 
 * @author hd
 *
 */
@Service("snsInsSyncService")
@Transactional(rollbackFor = Exception.class)
public class SnsInsSyncServiceImpl implements SnsInsSysService {
  Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ConstRegionDao constRegionDao;
  @Autowired
  private InstitutionDao institutionDao;
  @Autowired
  private InsPortalDao InsPortalDao;
  @Autowired
  private PersonProfileDao personpDao;
  @Autowired
  private InsRoleDao insRoleDao;
  @Autowired
  private PsnInsDao psnInsDao;
  @Autowired
  private InsDataFromSnsDao insDataFromSnsDao;
  @Autowired
  private InsRegionSnsDao insRegionSnsDao;
  @Autowired
  private PsnSidDao psnSidDao;

  @Override
  public Long findRegionId(String regionName) throws ServiceException {
    try {
      if (regionName == null) {
        return 0L;
      }
      ConstRegion region = constRegionDao.getConstRegionByName(regionName);
      if (region == null) {
        return 0L;
      } else {
        return region.getId();
      }
    } catch (DaoException e) {
      logger.error("同步单位信息不正确，查询prvId失败", e);
      throw new ServiceException("查询prvId失败", e);
    }
  }

  @Override
  public Long addIns(InsInfo info) throws ServiceException {
    if (!NumberUtils.isNumber(info.getOrgCode().toString()) || info.getToken() == null) {
      throw new ServiceException("同步单位信息不正确，orgcode=" + info.getOrgCode() + " token=" + info.getToken());
    }
    Long orgCode = info.getOrgCode();
    try {
      // 通过orgcode和token，查询现有单位id
      InsDataFrom insByCode = insDataFromSnsDao.get(new InsDataFromId(orgCode, info.getToken()));
      // 通过单位名称，查询现有单位
      Institution insByName = institutionDao.findInsByName(info.getZhName());
      if (insByCode == null && insByName == null) {// 不存在单位需要新增单位
        // 查询新单位的ins_id
        Long insId = info.getInsId();
        if (insId == null || insId == 0) {
          logger.error("同步单位信息不正确，新增或获取ins_id失败");
          throw new ServiceException("同步单位信息不正确，新增或获取ins_id失败");
        }
        // INS_REGION
        this.addInsReg(insId, info.getPrvId(), info.getCyId(), info.getDisId());
        info.setInsId(insId);
        Institution ins = new Institution(info);
        /*
         * // 判断是否来自isis系统的数据 if (info.getToken().equalsIgnoreCase("2bcca485")) {
         * ins.setIsisOrgCode(Integer.valueOf(info.getOrgCode().toString())); }
         */
        institutionDao.save(ins);
        insDataFromSnsDao.save(new InsDataFrom(info.getInsId(), info.getOrgCode(), info.getToken(), new Date()));
        return insId;
      } else {// 已存在单位，更新单位信息
        if (insByName == null) {
          logger.error("仅匹配到orgcode，且无名称相同的记录，不考虑单位名称修改的情况，insId=" + insByCode.getInsId() + ",orgCode+token="
              + info.getOrgCode() + info.getToken());
          throw new ServiceException("仅匹配到orgcode，且无名称相同的记录，不考虑单位名称修改的情况");
        }
        // 需要考虑更新的字段：联系电话、单位网址、单位地址（这些字段均为原先为空时更新，否则不更新）
        String contactTel = StringUtils.isBlank(insByName.getTel()) ? info.getContactTel() : insByName.getTel();
        String url = StringUtils.isBlank(insByName.getUrl()) ? info.getUrl() : insByName.getUrl();
        String address = StringUtils.isBlank(insByName.getZhAddress()) ? info.getAddress() : insByName.getZhAddress();
        Long regionId = 0L;
        // INS_REGION
        InsReg r = this.addInsReg(insByName.getId(), info.getPrvId(), info.getCyId(), info.getDisId());
        // 从ins_region取值更新，取值顺序：市->省，即优先取“市”，若“市”无值（为空或等于0）则取“省”更新）
        if (r.getCyId() != null && r.getCyId() > 0) {
          regionId = r.getCyId();
        } else {
          regionId = r.getPrvId();
        }
        if (insByCode != null && insByName != null) {
          // 比较ins_id是否相同，相同更新，不同报异常，名字被占用
          if (insByCode.getInsId().equals(insByName.getId())) {// 同一条记录，直接更新
            institutionDao.updateIns(new Object[] {contactTel, regionId, address, url, insByCode.getInsId()});
            return insByCode.getInsId();
          } else {
            logger.error("存在orgcode和单位名称两条记录，发生冲突，请更改被占用的单位名称。");
            throw new ServiceException("存在orgcode和单位名称两条记录，发生冲突，请更改被占用的单位名称。");
          }
        } else {
          if (insByName != null) {
            InsDataFrom df = insDataFromSnsDao.findByInsIdAndToken(insByName.getId(), info.getToken());
            // 单位名称被另一条记录占用，orgcode非空，且不一样
            if (df != null && df.getId().getOrgCode() != null && !df.getId().getOrgCode().equals(info.getOrgCode())) {// 存在单位名称，但单位orgcode不同，返回错误状态
              logger.error("单位名称被另一条记录（orgcode非空，且不一样）占用。");
              throw new ServiceException("单位名称被另一条记录（orgcode非空，且不一样）占用。");
            } else {// 名字相同，且orgcode为空
                    // 更新信息
              institutionDao.updateIns(new Object[] {contactTel, regionId, address, url, insByName.getId()});
              insDataFromSnsDao
                  .save(new InsDataFrom(insByName.getId(), info.getOrgCode(), info.getToken(), new Date()));
              return insByName.getId();
            }

          }

          // 仅有orgcode
          // 仅匹配到orgcode，且无名称相同的记录，更新信息
          /*
           * institutionDao.updateIns(new Object[] { info.getZhName(), info.getEnName(),
           * info.getContactName(), info.getContactTel(), info.getRegionId(),
           * Integer.valueOf(info.getOrgCode().toString()),info.getNature(), info.getAddress(),info.getUrl(),
           * info.getContactEmail(),insByCode.getInsId()});
           */
          logger.error("仅匹配到orgcode，且无名称相同的记录，不考虑单位名称修改的情况，insId=" + insByCode.getInsId() + ",orgCode+token="
              + info.getOrgCode() + info.getToken());
          throw new ServiceException("仅匹配到orgcode，且无名称相同的记录，不考虑单位名称修改的情况");
        }
      }
    } catch (DaoException e) {
      logger.error(e.getMessage() + "同步单位信息不正确，新增或更新单位信息失败", e);
      throw new ServiceException(e.getMessage() + "institution新增或更新单位信息失败", e);
    }
  }

  @Override
  public void addInsPortal(InsInfo info) throws ServiceException {
    if (info.getInsId() == null) {
      logger.error("同步单位信息不正确，insId不能为空，单位：" + info.getZhName());
      throw new ServiceException("同步单位信息不正确，insId不能为空，单位：" + info.getZhName());
    }

    try {
      // 查询现有单位
      InsPortal insPortal = InsPortalDao.get(info.getInsId());
      if (insPortal == null) {// 不存在单位需要增加域名配置

        // 查询新单位的ins_id
        String domain = info.getInsId() + ".cn.scholarmate.com";
        insPortal = new InsPortal();
        insPortal.setInsId(info.getInsId());
        insPortal.setDomain(domain);
        // insPortal.setRolNodeId(10000);
        // insPortal.setSnsNodeId(1);
        insPortal.setDefaultLang(info.getDefaultLang());
        // insPortal.setVersion(info.getVersion());
        insPortal.setSwitchLang(info.getSwitchLang());
        insPortal.setZhTitle(info.getZhName());
        insPortal.setEnTitle(info.getEnName());
      }
      InsPortalDao.saveEntity(insPortal);
    } catch (Exception e) {
      logger.error("同步单位信息不正确，新增或更新单位域名失败", e);
      throw new ServiceException("ins_portal新增或更新单位域名失败", e);
    }

  }

  @Override
  public Long findNewPsnId() throws ServiceException {
    try {
      return personpDao.findNewPsnId();// 查询新人员的psn_id
    } catch (Exception e) {
      logger.error("同步单位信息不正确，查询新人员的psn_id", e);
      throw new ServiceException("查询新人员的psn_id", e);
    }
  }

  @Override
  public Long addPersonSns(InsInfo info) throws ServiceException {
    try {
      Person p = new Person(info.getContactName(), info.getPsnEname(), info.getFirstName(), info.getLastName(),
          info.getZhFirstName(), info.getZhLastName(), info.getSnsAvatar(), info.getContactEmail(), info.getPsnId(),
          new Date(), info.getZhName(), info.getInsId(), info.getRegionId());
      personpDao.save(p);
    } catch (Exception e) {
      logger.error("同步单位信息不正确，增加Person表记录失败", e);
      throw new ServiceException("person(Sns)增加记录失败", e);
    }
    return null;
  }

  @Override
  public Person getPersonById(Long psnId) {
    try {
      if (psnId == null) {

        throw new ServiceException("同步单位信息不正确，获取person表记录，psnId不能为空");
      }
      if (!personpDao.isPersonByPsnId(psnId)) {
        return null;
      } else {
        return personpDao.queryPersonForRegisterByPsnId(psnId);
      }

    } catch (Exception e) {
      logger.error("同步单位信息不正确，人员存在，获取Person表记录失败", e);
      throw new ServiceException("人员存在，person(Sns)获取记录失败", e);
    }
  }

  @Override
  public Long addPsnSid(Long psnId) throws ServiceException {
    Long sid = null;
    try {
      sid = psnSidDao.findSidByPsnId(psnId);
      if (sid == null) {
        // 生成SID
        sid = psnSidDao.getSidSequence();
        PsnSid psnSid = new PsnSid(psnId, sid);
        psnSidDao.save(psnSid);
      }
      return sid;

    } catch (Exception e) {
      logger.error("同步单位信息不正确，增加Psn_sid表记录失败", e);
      throw new ServiceException("Psn_sid(Sns)增加记录失败", e);
    }

  }

  @Override
  public void addSysUserRole(Long psnId, Long roleId, Long insId) throws ServiceException {

    if (psnId == null || insId == null || roleId == null) {

      throw new ServiceException("同步单位信息不正确，psnId、insId和roleId不能为空");
    }

    try {
      InsRole insRole = insRoleDao.getUserRole(psnId, insId, roleId);// 查询是否有人员与单位的角色
      if (insRole == null) {

        insRoleDao.save(new InsRole(psnId, insId, roleId));

      }
    } catch (Exception e) {
      logger.error("同步单位信息不正确，增加人员与单位的角色失败", e);
      throw new ServiceException("sys_user_role增加人员与单位的角色失败,insId=" + insId, e);
    }

  }

  @Override
  public void addPsnIns(InsInfo info) throws ServiceException {

    if (info.getPsnId() == null || info.getInsId() == null) {

      throw new ServiceException("同步单位信息不正确，psnId和insId不能为空，单位：" + info.getZhName());
    }

    try {
      boolean hasPsnIns = psnInsDao.findPsnIsIns(info.getPsnId(), info.getInsId());// 查询是否有人员与单位的关系
      if (!hasPsnIns) {

        psnInsDao.save(new PsnInsSns(info.getPsnId(), info.getInsId(), 0L, 1));

      }
    } catch (DaoException e) {
      logger.error("同步单位信息不正确， 增加人员与单位的关系失败，单位：" + info.getZhName(), e);
      throw new ServiceException("psn_ins增加人员与单位的关系失败", e);
    }
  }

  @Override
  public void deleteSns(Long psnId, Long insId) {
    try {
      personpDao.delete(psnId);
      insRoleDao.removeRole(psnId);
      psnInsDao.delete(new PsnInsPk(psnId, insId));
    } catch (Exception e) {
      logger.error("同步单位信息不正确，清除Sns数据失败,psnId=" + psnId, e);
    }
  }

  @Override
  public InsReg addInsReg(Long insId, Long prvId, Long cyId, Long disId) throws ServiceException {
    if (insId == null) {

      throw new ServiceException("同步单位信息不正确，insId不能为空");
    }

    try {
      InsReg ins = insRegionSnsDao.get(insId);// 查询是否有人员与单位的角色
      if (ins == null) {
        ins = new InsReg();
        ins.setInsId(insId);
        ins.setPrvId(prvId);
        ins.setCyId(cyId);
        ins.setDisId(disId);
      } else {
        if (ins.getCyId() == null || ins.getCyId() == 0) {
          ins.setCyId(cyId);
        }
        if (ins.getPrvId() == null || ins.getPrvId() == 0) {
          ins.setPrvId(prvId);
        }
        ins.setDisId(disId);
      }
      insRegionSnsDao.save(ins);
      return ins;
    } catch (Exception e) {
      logger.error("同步单位信息不正确，高校所在地表记录失败", e);
      throw new ServiceException("INS_REGION增加失败,insId=" + insId, e);
    }

  }

}
