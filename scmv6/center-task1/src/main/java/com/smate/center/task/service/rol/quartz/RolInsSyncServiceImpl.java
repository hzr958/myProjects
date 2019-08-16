package com.smate.center.task.service.rol.quartz;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.rol.quartz.InsDataFromRolDao;
import com.smate.center.task.dao.rol.quartz.InsRegionDao;
import com.smate.center.task.dao.rol.quartz.InsStatusDao;
import com.smate.center.task.dao.rol.quartz.InstitutionRolDao;
import com.smate.center.task.exception.DaoException;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.bpo.InsInfo;
import com.smate.center.task.model.common.InsDataFrom;
import com.smate.center.task.model.common.InsDataFromId;
import com.smate.center.task.model.rol.quartz.InsRegion;
import com.smate.center.task.model.rol.quartz.InsStatus;
import com.smate.center.task.model.rol.quartz.InstitutionRol;
import com.smate.center.task.single.dao.rol.psn.RolPsnInsDao;
import com.smate.center.task.single.model.rol.psn.RolPsnIns;
import com.smate.center.task.single.model.rol.psn.RolPsnInsPk;
import com.smate.core.base.utils.dao.security.role.SieInsPortalDao;
import com.smate.core.base.utils.dao.security.role.SieInsRoleDao;
import com.smate.core.base.utils.model.rol.SieInsPortal;
import com.smate.core.base.utils.model.rol.SieInsRole;

/**
 * 同步单位信息 rol
 * 
 * @author hd
 *
 */
@Service("rolInsSyncService")
@Transactional(rollbackFor = Exception.class)
public class RolInsSyncServiceImpl implements RolInsSyncService {
  Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private InstitutionRolDao institutionRolDao;
  @Autowired
  private SieInsPortalDao insPortalRolDao;
  @Autowired
  private SieInsRoleDao insRoleSieDao;
  @Autowired
  private RolPsnInsDao psnInsRolDao;
  @Autowired
  private InsRegionDao insRegionDao;
  @Autowired
  private InsDataFromRolDao insDataFromRolDao;
  @Autowired
  private InsStatusDao insStatusDao;

  @Override
  public Long addIns(InsInfo info) throws ServiceException {
    if (!NumberUtils.isNumber(info.getOrgCode().toString()) || info.getToken() == null) {
      throw new ServiceException("同步单位信息不正确，orgcode=" + info.getOrgCode() + " token=" + info.getToken());
    }
    Long orgCode = info.getOrgCode();
    try {
      // 通过orgcode和token，查询现有单位id
      InsDataFrom insByCode = insDataFromRolDao.get(new InsDataFromId(orgCode, info.getToken()));
      // 通过单位名称，查询现有单位
      InstitutionRol insByName = institutionRolDao.findInsByName(info.getZhName());
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
        InstitutionRol ins = new InstitutionRol(info);
        // 判断是否来自isis系统的数据
        if (info.getToken().equalsIgnoreCase("2bcca485")) {
          ins.setIsisOrgCode(Integer.valueOf(info.getOrgCode().toString()));
        }
        institutionRolDao.save(ins);
        insDataFromRolDao.save(new InsDataFrom(info.getInsId(), info.getOrgCode(), info.getToken(), new Date()));
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
        InsRegion r = this.addInsReg(insByName.getId(), info.getPrvId(), info.getCyId(), info.getDisId());
        // 从ins_region取值更新，取值顺序：市->省，即优先取“市”，若“市”无值（为空或等于0）则取“省”更新）
        if (r.getCyId() != null && r.getCyId() > 0) {
          regionId = r.getCyId();
        } else {
          regionId = r.getPrvId();
        }
        if (insByCode != null && insByName != null) {
          // 比较ins_id是否相同，相同更新，不同报异常，名字被占用
          if (insByCode.getInsId().equals(insByName.getId())) {// 同一条记录，直接更新

            institutionRolDao.updateIns(new Object[] {contactTel, regionId, address, url, insByCode.getInsId()});
            return insByCode.getInsId();
          } else {
            logger.error("存在orgcode和单位名称两条记录，发生冲突，请更改被占用的单位名称。");
            throw new ServiceException("存在orgcode和单位名称两条记录，发生冲突，请更改被占用的单位名称。");
          }
        } else {
          if (insByName != null) {
            InsDataFrom df = insDataFromRolDao.findByInsIdAndToken(insByName.getId(), info.getToken());
            // 单位名称被另一条记录占用，orgcode非空，且不一样
            if (df != null && df.getId().getOrgCode() != null && !df.getId().getOrgCode().equals(info.getOrgCode())) {// 存在单位名称，但单位orgcode不同，返回错误状态
              logger.error("单位名称被另一条记录（orgcode非空，且不一样）占用。");
              throw new ServiceException("单位名称被另一条记录（orgcode非空，且不一样）占用。");
            } else {// 名字相同，且orgcode为空
              // 更新信息
              institutionRolDao.updateIns(new Object[] {contactTel, regionId, address, url, insByName.getId()});
              insDataFromRolDao
                  .save(new InsDataFrom(insByName.getId(), info.getOrgCode(), info.getToken(), new Date()));
              return insByName.getId();
            }

          }

          // 仅有orgcode
          // 仅匹配到orgcode，且无名称相同的记录，更新信息
          /*
           * institutionRolDao.updateIns(new Object[] { info.getZhName(), info.getEnName(),
           * info.getContactName(), info.getContactTel(), info.getRegionId(),
           * Integer.valueOf(info.getOrgCode().toString()), info.getNature(),
           * info.getAddress(),info.getUrl(),info.getContactEmail(),insByCode.getInsId()});
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
      SieInsPortal insPortal = insPortalRolDao.get(info.getInsId());
      if (insPortal == null) {// 不存在单位需要增加域名配置

        // 查询新单位的ins_id
        String domain = info.getInsId() + ".cn.scholarmate.com";
        insPortal = new SieInsPortal();
        insPortal.setInsId(info.getInsId());
        insPortal.setDomain(domain);
        insPortal.setRolNodeId(10000);
        insPortal.setSnsNodeId(1);
        insPortal.setVersion(info.getVersion());
        insPortal.setDefaultLang(info.getDefaultLang());
        insPortal.setSwitchLang(info.getSwitchLang());
        insPortal.setZhTitle(info.getZhName());
        insPortal.setEnTitle(info.getEnName());
      }

      insPortalRolDao.saveEntity(insPortal);
    } catch (Exception e) {
      logger.error("同步单位信息不正确，新增或更新单位域名失败", e);
      throw new ServiceException("ins_portal新增或更新单位域名失败", e);
    }

  }

  @Override
  public void addPsnIns(InsInfo info) throws ServiceException {

    if (info.getPsnId() == null || info.getInsId() == null) {

      throw new ServiceException("同步单位信息不正确，psnId和insId不能为空，单位：" + info.getZhName());
    }

    try {
      boolean hasPsnIns = psnInsRolDao.findPsnIsIns(info.getPsnId(), info.getInsId());// 查询是否有人员与单位的关系
      if (!hasPsnIns) {

        psnInsRolDao.save(new RolPsnIns(new RolPsnInsPk(info.getPsnId(), info.getInsId()), info.getContactName(),
            info.getPsnEname(), info.getContactEmail(), info.getFirstName(), info.getLastName(), info.getContactTel(),
            info.getRolAvatar(), 1, 1, info.getCyId(), info.getPrvId(), info.getSid()));

      }
    } catch (DaoException e) {
      logger.error("同步单位信息不正确， 增加人员与单位的关系失败，单位：" + info.getZhName(), e);
      throw new ServiceException("psn_ins增加人员与单位的关系失败", e);
    }
  }

  @Override
  public void addSieRole(Long psnId, Long insId) throws ServiceException {

    if (psnId == null || insId == null) {

      throw new ServiceException("同步单位信息不正确，psnId、insId不能为空");
    }
    try {
      // 查询是否有人员与单位的角色
      Boolean HasRo = insRoleSieDao.isHasRo(insId);
      if (!HasRo) {
        // 单位管理员(RO)
        this.addSysUserRole(psnId, 2L, insId);
        // 系统管理员(Admin)
        this.addSysUserRole(psnId, 11L, insId);
      }
      // 单位个人用户(M)
      this.addSysUserRole(psnId, 3L, insId);

    } catch (Exception e) {
      logger.error("同步单位信息不正确，增加人员与单位的角色失败", e);
      throw new ServiceException("sys_user_role增加人员与单位的角色失败,insId=" + insId, e);
    }
  }

  @Override
  public void addSysUserRole(Long psnId, Long roleId, Long insId) throws ServiceException {

    if (psnId == null || insId == null || roleId == null) {

      throw new ServiceException("同步单位信息不正确，psnId、insId和roleId不能为空");
    }

    try {
      // 查询是否有人员与单位的角色
      SieInsRole insRole = insRoleSieDao.getUserRole(psnId, insId, roleId);

      if (insRole == null) {

        insRoleSieDao.save(new SieInsRole(psnId, insId, roleId));

      }


    } catch (Exception e) {
      logger.error("同步单位信息不正确，增加人员与单位的角色失败", e);
      throw new ServiceException("sys_user_role增加人员与单位的角色失败,insId=" + insId + ",roleId=" + roleId, e);
    }

  }

  @Override
  public void deleteRol(Long psnId, Long insId) throws ServiceException {
    try {
      insRoleSieDao.removeInsRole(psnId, insId);
      psnInsRolDao.delete(new RolPsnInsPk(psnId, insId));
    } catch (Exception e) {
      logger.error("同步单位信息不正确，清除Rol数据失败,psnId=" + psnId + "  insId=" + insId, e);
    }
  }

  @Override
  public InsRegion addInsReg(Long insId, Long prvId, Long cyId, Long disId) throws ServiceException {
    if (insId == null) {

      throw new ServiceException("同步单位信息不正确，insId不能为空");
    }

    try {
      InsRegion ins = insRegionDao.get(insId);
      if (ins == null) {
        ins = new InsRegion();
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
      insRegionDao.save(ins);
      return ins;
    } catch (Exception e) {
      logger.error("同步单位信息不正确，高校所在地表记录失败", e);
      throw new ServiceException("INS_REGION增加失败,insId=" + insId, e);
    }

  }

  @Override
  public void addInsStatus(Long insId) throws ServiceException {
    if (insId == null) {

      throw new ServiceException("同步单位信息不正确，insId不能为空");
    }

    try {
      InsStatus ins = insStatusDao.get(insId);
      if (ins == null) {
        insStatusDao.save(new InsStatus(insId, 0L, 0L));

      }
    } catch (Exception e) {
      logger.error("同步单位信息不正确，INS_STATUS记录失败", e);
      throw new ServiceException("INS_STATUS增加失败,insId=" + insId, e);
    }

  }
}

